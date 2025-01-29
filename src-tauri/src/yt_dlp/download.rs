use std::process::Stdio;

use super::client::YtDlp;
use base64::{engine::general_purpose, Engine};
use serde_json::Value;
use tauri::{Emitter, Manager};
use tokio::{
    io::{AsyncBufReadExt, BufReader},
    process::Command,
};

impl YtDlp {
    pub async fn get_info(&self, url: String) -> Result<serde_json::Value, String> {
        let output = Command::new(&self.yt_dlp_binary)
            .arg("--no-progress")
            .arg("--dump-single-json")
            .arg("--no-playlist")
            .arg(url)
            .output()
            .await
            .map_err(|e| e.to_string())?;

        if !output.status.success() {
            return Err(String::from_utf8_lossy(&output.stderr).to_string());
        }

        let stdout = String::from_utf8_lossy(&output.stdout);
        let json: Value = serde_json::from_str(&stdout).map_err(|e| e.to_string())?;

        Ok(json)
    }

    pub async fn get_playlist_info(&self, url: String) -> Result<serde_json::Value, String> {
        let output = Command::new(&self.yt_dlp_binary)
            .arg("--no-progress")
            .arg("--dump-single-json")
            .arg("--flat-playlist")
            .arg("--no-playlist")
            .arg(url)
            .output()
            .await
            .map_err(|e| e.to_string())?;

        if !output.status.success() {
            return Err(String::from_utf8_lossy(&output.stderr).to_string());
        }

        let stdout = String::from_utf8_lossy(&output.stdout);
        let json: Value = serde_json::from_str(&stdout).map_err(|e| e.to_string())?;

        Ok(json)
    }

    pub async fn download_audio(
        &self,
        app: tauri::AppHandle,
        url: String,
    ) -> Result<String, String> {
        let output_path = format!(
            "{}/{}.temp",
            app.path().app_cache_dir().unwrap().to_str().unwrap(),
            general_purpose::STANDARD.encode(url.clone().as_bytes())
        );

        let mut output = match Command::new(&self.yt_dlp_binary)
            .arg("--newline")
            .arg("--no-playlist")
            .arg("-x")
            .arg(url.clone())
            .arg("--output")
            .arg(output_path.clone())
            .stdout(Stdio::piped())
            .spawn()
        {
            Ok(output) => output,
            Err(e) => return Err(e.to_string()),
        };

        let stdout = output.stdout.take().expect("Failed to get stdout");

        let reader = BufReader::new(stdout);
        let mut lines = reader.lines();

        let mut destination = output_path;

        while let Ok(Some(line)) = lines.next_line().await {
            if line.contains("Destination: ") {
                destination = line
                    .replace("[ExtractAudio] Destination: ", "")
                    .trim()
                    .to_string();
            }
            app.emit("yt-dlp-download-progress", line).unwrap();
        }

        output
            .wait_with_output()
            .await
            .expect("Failed to wait for command");

        tokio::time::sleep(tokio::time::Duration::from_secs(1)).await;

        let binary = tokio::fs::read(destination.clone())
            .await
            .map_err(|e| e.to_string())?;

        tokio::fs::remove_file(destination)
            .await
            .map_err(|e| e.to_string())?;

        Ok(general_purpose::STANDARD.encode(binary))
    }
}
