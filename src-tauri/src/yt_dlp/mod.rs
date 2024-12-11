use std::path::PathBuf;

use tauri::{AppHandle, Manager};

mod client;
mod platform;
mod download;

static mut YT_DLP: Option<client::YtDlp> = None;

pub async fn get_yt_dlp(path: PathBuf) -> Result<client::YtDlp, String> {
    if unsafe { YT_DLP.is_some() } {
        return Ok(unsafe { YT_DLP.clone().unwrap() });
    }

    let youtube = client::YtDlp::new(path).await.unwrap();

    unsafe {
        YT_DLP = Some(youtube.clone());
    }

    Ok(youtube)
}

#[tauri::command(async)]
pub async fn yt_dlp_get_info(url: String, handle: AppHandle) -> Result<serde_json::Value, String> {
    let youtube = get_yt_dlp(handle.path().app_cache_dir().unwrap()).await?;

    let info = youtube.get_info(url).await?;

    Ok(info)
}

#[tauri::command(async)]
pub async fn yt_dlp_get_playlist_info(url: String, handle: AppHandle) -> Result<serde_json::Value, String> {
    let youtube = get_yt_dlp(handle.path().app_cache_dir().unwrap()).await?;

    let info = youtube.get_playlist_info(url).await?;

    Ok(info)
}

#[tauri::command(async)]
pub async fn yt_dlp_download(url: String, handle: AppHandle) -> Result<String, String> {
    let youtube = get_yt_dlp(handle.path().app_cache_dir().unwrap()).await?;

    let base64 = youtube.download_audio(handle, url).await?;

    Ok(base64)
}
