use std::path::PathBuf;

use yt_dlp::{fetcher::deps::Libraries, Youtube};

static mut YOUTUBE_DL: Option<Youtube> = None;

pub async fn get_yt_dlp(base_directory: PathBuf) -> Result<Youtube, String> {
    if unsafe { YOUTUBE_DL.is_some() } {
        return Ok(unsafe { YOUTUBE_DL.clone().unwrap() });
    }

    let lib_dir = base_directory.join("lib");
    let output_dir = base_directory.join("output");

    let dlp_binding = lib_dir.join("yt-dlp");
    let ffmpeg_binding = lib_dir.join("ffmpeg");

    let youtube = if !dlp_binding.exists() || !ffmpeg_binding.exists() {
        match Youtube::with_new_binaries(lib_dir, output_dir).await {
            Ok(youtube) => youtube,
            Err(e) => return Err(e.to_string()),
        }
    } else {
        let libraries = Libraries::new(dlp_binding, ffmpeg_binding);
        let youtube = match Youtube::new(libraries, output_dir) {
            Ok(youtube) => youtube,
            Err(e) => return Err(e.to_string()),
        };

        if let Err(e) = youtube.update_downloader().await {
            return Err(e.to_string());
        }

        youtube
    };

    unsafe {
        YOUTUBE_DL = Some(youtube.clone());
    }

    Ok(youtube)
}