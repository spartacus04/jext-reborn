mod ffmpeg;
mod yt_dlp;

// I've never used rust, if you are a rust developer, don't judge me, I'm just trying to make it work

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    tauri::Builder::default()
        .plugin(tauri_plugin_clipboard_manager::init())
        .plugin(tauri_plugin_os::init())
        .plugin(tauri_plugin_notification::init())
        .plugin(tauri_plugin_http::init())
        .plugin(tauri_plugin_shell::init())
        .plugin(tauri_plugin_fs::init())
        .plugin(tauri_plugin_dialog::init())
        .setup(|app| {
            if cfg!(debug_assertions) {
                app.handle().plugin(
                    tauri_plugin_log::Builder::default()
                        .level(log::LevelFilter::Info)
                        .build(),
                )?;
            }
            Ok(())
        })
        .invoke_handler(tauri::generate_handler![
            ffmpeg::try_download_ffmpeg,
            ffmpeg::run_ffmpeg,
            ffmpeg::run_ffprobe,
            ffmpeg::extract_ffmpeg_cover,
            yt_dlp::yt_dlp_get_info,
            yt_dlp::yt_dlp_get_playlist_info,
            yt_dlp::yt_dlp_download,
        ])
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
