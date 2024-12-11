use std::{path::PathBuf, process::Command};

use reqwest::header::USER_AGENT;
use tauri::http::HeaderValue;

use super::platform::{Architecture, Platform};
use std::os::unix::fs::PermissionsExt;

#[derive(Clone, Debug)]
pub struct YtDlp {
    pub yt_dlp_binary: PathBuf,
}

impl YtDlp {
    pub async fn new(base_directory: PathBuf) -> Result<Self, String> {
        let lib_dir = base_directory.join("lib");

        let yt_dlp_binary = lib_dir.join("yt-dlp");

        if !yt_dlp_binary.exists() || !is_yt_dlp_updated(&yt_dlp_binary).await.unwrap() {
            println!("Downloading yt-dlp binary...");
            match download_yt_dlp(&yt_dlp_binary).await {
                Ok(_) => println!("yt-dlp binary downloaded"),
                Err(e) => return Err(e),
            }
        }

        Ok(Self {
            yt_dlp_binary,
        })
    }
}

pub async fn is_yt_dlp_updated(binary_path: &PathBuf) -> Result<bool, String> {
    let output = match Command::new(binary_path)
        .arg("--version")
        .output() {
        Ok(output) => output,
        Err(e) => return Err(format!("Error getting yt-dlp version: {}", e)),
    };

    let local_version = match String::from_utf8(output.stdout) {
        Ok(version) => version,
        Err(e) => return Err(format!("Error getting yt-dlp version: {}", e)),
    };

    let data = match get_yt_dlp_release_data().await {
        Ok(data) => data,
        Err(e) => return Err(e),
    };

    let latest_version = match data.get("tag_name").and_then(|v| v.as_str()) {
        Some(version) => version,
        None => return Err("No version found".to_string()),
    };

    Ok(local_version.trim() == latest_version)
}

pub async fn download_yt_dlp(binary_path: &PathBuf) -> Result<(), String> {
    let platform = Platform::detect();
    let arch = Architecture::detect();

    let data = match get_yt_dlp_release_data().await {
        Ok(data) => data,
        Err(e) => return Err(e),
    };

    let assets = match data.get("assets").and_then(|a| a.as_array()) {
        Some(assets) => assets,
        None => return Err("No assets found".to_string()),
    };

    let asset = assets.iter().find(|asset| {
        let name = asset.get("name").and_then(|n| n.as_str()).unwrap_or("");

        match (platform.clone(), arch.clone()) {
            (Platform::Windows, Architecture::X64) => {
                name.contains("yt-dlp.exe")
            }
            (Platform::Windows, Architecture::X86) => {
                name.contains("yt-dlp_x86.exe")
            }

            (Platform::Linux, Architecture::X64) => {
                name.contains("yt-dlp_linux")
            }
            (Platform::Linux, Architecture::Armv7l) => {
                name.contains("yt-dlp_linux_armv7l")
            }
            (Platform::Linux, Architecture::Aarch64) => {
                name.contains("yt-dlp_linux_aarch64")
            }

            (Platform::Mac, _) => name.contains("yt-dlp_macos"),

            _ => false,
        }
    });

    if asset.is_none() {
        return Err("No asset found for this platform".to_string());
    }

    let asset = asset.unwrap();
    let download_url = asset.get("browser_download_url").and_then(|u| u.as_str()).unwrap_or("");

    let client = reqwest::Client::new();

    let response = match client.get(download_url).send().await {
        Ok(response) => response,
        Err(e) => return Err(format!("Error downloading yt-dlp: {}", e)),
    };

    let bytes = match response.bytes().await {
        Ok(bytes) => bytes,
        Err(e) => return Err(format!("Error downloading yt-dlp: {}", e)),
    };

    std::fs::write(binary_path, bytes).unwrap();

    let mut perms = std::fs::metadata(binary_path)
        .map_err(|e| format!("Error getting metadata: {}", e))?
        .permissions();

    perms.set_mode(0o755);

    std::fs::set_permissions(binary_path, perms)
        .map_err(|e| format!("Error setting permissions: {}", e))?;

    Ok(())
}

pub async fn get_yt_dlp_release_data() -> Result<serde_json::Value, String> {
    let url = "https://api.github.com/repos/yt-dlp/yt-dlp/releases/latest";

    let client = reqwest::Client::new();
    let response = match client
        .get(url)
        .header(USER_AGENT, HeaderValue::from_static("rust-reqwest"))
        .send().await {
        Ok(response) => response,
        Err(e) => return Err(format!("Error getting yt-dlp release: {}", e)),
    };

    let data = match response.json::<serde_json::Value>().await {
        Ok(data) => data,
        Err(e) => return Err(format!("Error parsing yt-dlp release: {}", e)),
    };

    Ok(data)
}