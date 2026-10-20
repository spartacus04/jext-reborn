use std::path::{Path, PathBuf};
use std::sync::OnceLock;

use tauri::{AppHandle, Manager};

mod client;
mod download;

static YT_DLP: OnceLock<client::YtDlp> = OnceLock::new();

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
        let candidates = [base.join(name), base.join(format!("{name}.exe"))];

        let candidates = target_triple().map_or(candidates.to_vec(), |triple| {
            [
                base.join(format!("{name}-{triple}")),
                base.join(format!("{name}-{triple}.exe")),
            ]
            .into_iter()
            .chain(candidates)
            .collect()
        });

        for candidate in candidates {
            if candidate.exists() {
                return candidate;
            }
        }
    }

    resource_dir.join("bin").join(name)
}

fn resolve_yt_dlp_path(handle: &AppHandle) -> Result<PathBuf, String> {
    let os = std::env::consts::OS;
    let arch = std::env::consts::ARCH;

    let yt_dlp_name = match (os, arch) {
        ("linux", _) => "yt-dlp-linux",
        ("macos", "aarch64") => "yt-dlp-macos-aarch64",
        ("macos", "x86_64") => "yt-dlp-macos-x86_64",
        ("windows", _) => "yt-dlp-windows",
        _ => return Err(format!("Unsupported platform/arch for yt-dlp: {os}/{arch}")),
    };

    let resource_dir = handle
        .path()
        .resource_dir()
        .map_err(|e| format!("Error resolving resource dir: {e}"))?;

    Ok(resolve_sidecar_path(&resource_dir, yt_dlp_name))
}

pub fn get_yt_dlp(handle: &AppHandle) -> Result<client::YtDlp, String> {
    if let Some(youtube) = YT_DLP.get() {
        return Ok(youtube.clone());
    }

    let yt_dlp_path = resolve_yt_dlp_path(handle)?;
    let youtube = client::YtDlp::new(yt_dlp_path)?;
    let _ = YT_DLP.set(youtube.clone());

    Ok(YT_DLP.get().cloned().unwrap_or(youtube))
}

#[tauri::command(async)]
pub async fn yt_dlp_get_info(url: String, handle: AppHandle) -> Result<serde_json::Value, String> {
    let youtube = get_yt_dlp(&handle)?;

    let info = youtube.get_info(url).await?;

    Ok(info)
}

#[tauri::command(async)]
pub async fn yt_dlp_get_playlist_info(
    url: String,
    handle: AppHandle,
) -> Result<serde_json::Value, String> {
    let youtube = get_yt_dlp(&handle)?;

    let info = youtube.get_playlist_info(url).await?;

    Ok(info)
}

#[tauri::command(async)]
pub async fn yt_dlp_download(url: String, handle: AppHandle) -> Result<String, String> {
    let youtube = get_yt_dlp(&handle)?;

    let base64 = youtube.download_audio(handle, url).await?;

    Ok(base64)
}
