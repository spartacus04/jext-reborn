use base64::{
    engine::general_purpose::{self, STANDARD},
    Engine,
};
use ffmpeg_sidecar::event::FfmpegEvent;
use serde::Serialize;
use std::{
    fs,
    io::{Read, Write},
    path::{Path, PathBuf},
    process::Command,
};
use tauri::{Emitter, Manager};
use tauri_plugin_dialog::{DialogExt, MessageDialogButtons};

fn target_triple() -> Option<&'static str> {
    let os = std::env::consts::OS;
    let arch = std::env::consts::ARCH;

    match (os, arch) {
        ("linux", "x86_64") => Some("x86_64-unknown-linux-gnu"),
        ("linux", "aarch64") => Some("aarch64-unknown-linux-gnu"),
        ("macos", "aarch64") => Some("aarch64-apple-darwin"),
        ("macos", "x86_64") => Some("x86_64-apple-darwin"),
        ("windows", "x86_64") => Some("x86_64-pc-windows-msvc"),
        _ => None,
    }
}

fn resolve_sidecar_path(resource_dir: &Path, name: &str) -> PathBuf {
    let resource_base = resource_dir.join("bin");
    let manifest_base = Path::new(env!("CARGO_MANIFEST_DIR")).join("bin");

    for base in [resource_base, manifest_base] {
        let base_path = base.join(name);
        if base_path.exists() {
            return base_path;
        }

        if let Some(triple) = target_triple() {
            let candidate = base.join(format!("{name}-{triple}"));
            if candidate.exists() {
                return candidate;
            }
        }
    }

    resource_dir.join("bin").join(name)
}

fn resolve_ffmpeg_paths(app_handle: &tauri::AppHandle) -> Result<(PathBuf, PathBuf), String> {
    let os = std::env::consts::OS;
    let arch = std::env::consts::ARCH;

    let (ffmpeg_name, ffprobe_name) = match (os, arch) {
        ("linux", "x86_64") => ("ffmpeg-linux-x64", "ffprobe-linux-x64"),
        ("linux", "aarch64") => ("ffmpeg-linux-arm64", "ffprobe-linux-arm64"),
        ("macos", _) => ("ffmpeg-darwin-arm64", "ffprobe-darwin-arm64"),
        ("windows", _) => ("ffmpeg-win32-x64", "ffprobe-win32-x64"),
        _ => {
            return Err(format!(
                "Unsupported platform/arch for ffmpeg: {os}/{arch}"
            ))
        }
    };

    let resource_dir = app_handle
        .path()
        .resource_dir()
        .map_err(|e| format!("Error resolving resource dir: {e}"))?;

    Ok((
        resolve_sidecar_path(&resource_dir, ffmpeg_name),
        resolve_sidecar_path(&resource_dir, ffprobe_name),
    ))
}

#[tauri::command]
pub fn try_download_ffmpeg(app: tauri::AppHandle) -> Result<bool, String> {
    let (ffmpeg_path, ffprobe_path) = resolve_ffmpeg_paths(&app)?;

    if ffmpeg_path.exists() && ffprobe_path.exists() {
        return Ok(true);
    }

    app.dialog()
        .message("Required ffmpeg binaries are missing. Exit the app?")
        .title("Error")
        .buttons(MessageDialogButtons::YesNo)
        .show(move |result| {
            if result {
                app.exit(1);
            }
        });

    Err("Required ffmpeg binaries are missing".to_string())
}

#[tauri::command(async)]
pub fn run_ffmpeg(
    input: String,
    args: Vec<String>,
    app_handle: tauri::AppHandle,
) -> Result<String, String> {
    let binary = STANDARD.decode(input).unwrap();

    let (ffmpeg_path, _) = resolve_ffmpeg_paths(&app_handle)?;
    if !ffmpeg_path.exists() {
        return Err("Ffmpeg binary not found".to_string());
    }

    // create a temporary file with the input data

    let cache_binding = app_handle.path().app_cache_dir().unwrap();

    if let Err(e) = fs::create_dir_all(&cache_binding) {
        return Err(format!("Error creating cache directory: {}", e));
    }

    let input_binding = cache_binding.join("temp_file");
    let input_file = input_binding.to_str().unwrap();

    let output_binding = cache_binding.join("output_file.ogg");
    let output_file = output_binding.to_str().unwrap();

    if fs::metadata(input_file).is_ok() {
        if let Err(e) = fs::remove_file(input_file) {
            return Err(format!("Error removing temporary file: {}", e));
        }
    }

    if fs::metadata(output_file).is_ok() {
        if let Err(e) = fs::remove_file(output_file) {
            return Err(format!("Error removing output file: {}", e));
        }
    }

    let mut temp_file = fs::File::create(input_file).unwrap();

    if let Err(e) = temp_file.write_all(&binary) {
        return Err(format!("Error writing to temporary file: {}", e));
    }

    // run the ffmpeg command with the args and the temporary file
    let mut ffmpeg = ffmpeg_sidecar::command::FfmpegCommand::new_with_path(ffmpeg_path)
        .input(input_file)
        .args(args)
        .output(output_file)
        .spawn()
        .unwrap();

    ffmpeg.iter().unwrap().for_each(|e| match e {
        FfmpegEvent::Log(_level, msg) => {
            println!("[ffmpeg] {msg}");

            if !msg.contains("time=") || !msg.contains("speed=") {
                return;
            }

            let time = msg.split("time=").collect::<Vec<&str>>()[1]
                .split(" ")
                .collect::<Vec<&str>>()[0];

            if time.starts_with("-") {
                return;
            }

            app_handle.emit("ffmpeg-progress", time).unwrap();
        }
        _ => {}
    });

    let ffmpeg_result = ffmpeg.wait();

    match ffmpeg_result {
        Ok(exit) => {
            if !exit.success() {
                return Err(format!("Error running ffmpeg: {:?}", exit.code()));
            }
        }
        Err(e) => {
            return Err(format!("Error running ffmpeg: {}", e));
        }
    }

    // read the temporary file and return the data

    let mut output_file = fs::File::open(output_file).unwrap();
    let mut output_data = vec![];

    if let Err(e) = output_file.read_to_end(&mut output_data) {
        return Err(format!("Error reading output file: {}", e));
    }

    return Ok(general_purpose::STANDARD.encode(output_data));
}

#[tauri::command(async)]
pub async fn run_ffprobe(args: Vec<String>, app_handle: tauri::AppHandle) -> Result<String, String> {
    let (_, ffprobe_path) = resolve_ffmpeg_paths(&app_handle)?;
    if !ffprobe_path.exists() {
        return Err("Ffprobe binary not found".to_string());
    }

    let output = Command::new(ffprobe_path)
        .args(args)
        .output()
        .map_err(|e| format!("Error running ffprobe: {e}"))?;

    if !output.status.success() {
        return Err(String::from_utf8_lossy(&output.stderr).to_string());
    }

    Ok(String::from_utf8_lossy(&output.stdout).to_string())
}

#[derive(Serialize)]
pub struct CoverImagePayload {
    mime: String,
    data: String,
}

fn detect_image_mime(data: &[u8]) -> &'static str {
    if data.starts_with(b"\x89PNG\r\n\x1a\n") {
        return "image/png";
    }

    if data.starts_with(&[0xff, 0xd8, 0xff]) {
        return "image/jpeg";
    }

    if data.starts_with(b"GIF87a") || data.starts_with(b"GIF89a") {
        return "image/gif";
    }

    "application/octet-stream"
}

#[tauri::command(async)]
pub async fn extract_ffmpeg_cover(
    path: String,
    app_handle: tauri::AppHandle,
) -> Result<CoverImagePayload, String> {
    let (ffmpeg_path, _) = resolve_ffmpeg_paths(&app_handle)?;
    if !ffmpeg_path.exists() {
        return Err("Ffmpeg binary not found".to_string());
    }

    let output = Command::new(ffmpeg_path)
        .args([
            "-v",
            "error",
            "-i",
            &path,
            "-map",
            "0:v:0",
            "-c",
            "copy",
            "-f",
            "image2pipe",
            "-",
        ])
        .output()
        .map_err(|e| format!("Error extracting cover: {e}"))?;

    if !output.status.success() {
        return Err(String::from_utf8_lossy(&output.stderr).to_string());
    }

    let mime = detect_image_mime(&output.stdout).to_string();
    let data = general_purpose::STANDARD.encode(output.stdout);

    Ok(CoverImagePayload { mime, data })
}
