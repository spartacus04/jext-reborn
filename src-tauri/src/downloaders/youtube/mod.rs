use std::io::Read;

use base64::{engine::general_purpose, Engine};
use regex::Regex;
use rusty_ytdl::{search::Playlist, Video, VideoInfo};
use tauri::{AppHandle, Manager};

mod bin;

#[tauri::command(async)]
pub fn yt_is_valid_url(url: String) -> bool {
    let regexp = Regex::new(r#"(youtu.*be.*)\/(watch\?v=|embed\/|v|shorts|)(.*?((?=[&#?])|$))"#).unwrap();

    return regexp.is_match(&url);
}

#[tauri::command(async)]
pub fn yt_is_playlist_url(url: String) -> bool {
    let regexp = Regex::new(r#"(youtu.*be.*)/playlist\?list=(.*?((\?=[&#?])|$))"#).unwrap();

    return regexp.is_match(&url);
}

#[tauri::command(async)]
pub async fn yt_get_info(url: String) -> Result<VideoInfo, String> {
    let video = match Video::new(url) {
        Ok(video) => video,
        Err(e) => return Err(e.to_string()),
    };

    match video.get_info().await {
        Ok(info) => return Ok(info),
        Err(e) => return Err(e.to_string()),
    };
}

#[tauri::command(async)]
pub async fn yt_get_playlist_info(url: String) -> Result<Playlist, String> {
    let playlist = match Playlist::get(url, None).await {
        Ok(video) => video,
        Err(e) => return Err(e.to_string()),
    };

    return Ok(playlist);
}

#[tauri::command(async)]
pub async fn yt_download(url: String, handle: AppHandle) -> Result<String, String> {
    let ytdl = match bin::get_yt_dlp(handle.path().app_cache_dir().unwrap()).await {
        Ok(ytdl) => ytdl,
        Err(e) => return Err(e),
    };

    let file_name = format!("{}.mp3", general_purpose::STANDARD.encode(url.as_bytes()));


    let output = match ytdl.download_audio_stream_from_url(url, file_name.as_str()).await {
        Ok(output) => output,
        Err(e) => return Err(e.to_string()),
    };

    let mut file = std::fs::File::open(output).unwrap();
    let mut buffer = Vec::new();
    file.read_to_end(&mut buffer).unwrap();

    return Ok(general_purpose::STANDARD.encode(buffer.as_slice()));
}