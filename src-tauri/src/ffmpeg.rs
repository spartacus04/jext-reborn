use base64::{
    engine::general_purpose::{self, STANDARD},
    Engine,
};
use ffmpeg_sidecar::event::FfmpegEvent;
use std::{
    fs,
    io::{Read, Write},
};
use tauri::{Emitter, Manager};
use tauri_plugin_dialog::{DialogExt, MessageDialogButtons};

#[tauri::command]
pub fn try_download_ffmpeg(app: tauri::AppHandle) -> Result<bool, String> {
    let result = ffmpeg_sidecar::download::auto_download();

    match result {
        Ok(_) => return Ok(true),
        Err(e) => {
            app.dialog()
                .message("Error downloading Ffmpeg. Exit the app?")
                .title("Error")
                .buttons(MessageDialogButtons::YesNo)
                .show(move |result| {
                    if result {
                        app.exit(1);
                    }
                });

            return Err(format!("Error downloading ffmpeg: {}", e));
        }
    }
}

#[tauri::command(async)]
pub fn run_ffmpeg(
    input: String,
    args: Vec<String>,
    app_handle: tauri::AppHandle,
) -> Result<String, String> {
    let binary = STANDARD.decode(input).unwrap();

    if !ffmpeg_sidecar::command::ffmpeg_is_installed() {
        return Err("Ffmpeg is not installed".to_string());
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

    if let Err(e) = fs::remove_file(input_file) {
        return Err(format!("Error removing temporary file: {}", e));
    }
    if let Err(e) = fs::remove_file(output_file) {
        return Err(format!("Error removing output file: {}", e));
    }

    let mut temp_file = fs::File::create(input_file).unwrap();

    if let Err(e) = temp_file.write_all(&binary) {
        return Err(format!("Error writing to temporary file: {}", e));
    }

    // run the ffmpeg command with the args and the temporary file
    let mut ffmpeg = ffmpeg_sidecar::command::FfmpegCommand::new()
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
