// Prevents additional console window on Windows in release, DO NOT REMOVE!!
#![cfg_attr(not(debug_assertions), windows_subsystem = "windows")]

use base64::{engine::general_purpose::{self, STANDARD}, Engine};
use ffmpeg_sidecar::event::{FfmpegEvent, FfmpegProgress};
use tauri::{api::dialog, Manager};
use std::{fs, io::{Read, Write}};

// I've never used rust, if you are a rust developer, don't judge me, I'm just trying to make it work

#[tauri::command]
fn download_ffmpeg(app_handle: tauri::AppHandle) -> bool {
    let result = ffmpeg_sidecar::download::auto_download();

    match result {
        Ok(_) => return true,
        Err(e) => {
            dialog::confirm(app_handle.get_focused_window().as_ref(), "Error downloading ffmpeg. Exit the app?", &e.to_string(), move |value| {
                if value {
                    app_handle.exit(1);
                }
            });
            return false;
        }
    }
}

#[tauri::command]
fn ffmpeg(input: String, args: Vec<String>, app_handle: tauri::AppHandle) -> String {
    let binary = STANDARD.decode(input).unwrap();

    if !ffmpeg_sidecar::command::ffmpeg_is_installed() {
        return String::from("");
    }

    // create a temporary file with the input data

    let cache_binding = app_handle.path_resolver().app_cache_dir().unwrap();

    println!("Cache binding: {:?}", cache_binding);

    let _ = fs::create_dir_all(&cache_binding);

    let input_binding = cache_binding.join("temp_file");
    let input_file_path = input_binding.to_str().unwrap();

    let output_binding = cache_binding.join("output_file.ogg");
    let output_file_path = output_binding.to_str().unwrap();

    let _ = fs::remove_file(input_file_path);
    let _ = fs::remove_file(output_file_path);

    let mut temp_file = fs::File::create(input_file_path).unwrap();

    let file_result = temp_file.write_all(&binary);

    match file_result {
        Ok(_) => (),
        Err(e) => {
            println!("Error creating temporary file: {}", e);
            return String::from("");
        }
    }

    // run the ffmpeg command with the args and the temporary file

    let mut ffmpeg = ffmpeg_sidecar::command::FfmpegCommand::new()
        .input(input_file_path)
        .args(args)
        .output(output_file_path)
        .spawn()
        .unwrap();

    ffmpeg.iter().unwrap()
        .for_each(|e| {
        match e {
            FfmpegEvent::Progress(FfmpegProgress { frame, .. }) =>
            println!("Current frame: {frame}"),
            FfmpegEvent::Log(_level, msg) =>
            println!("[ffmpeg] {msg}"),
            _ => {}
        }
        });

    let ffmpeg_result = ffmpeg.wait();

    match ffmpeg_result {
        Ok(exit) => {
            if !exit.success() {
                println!("Error running ffmpeg: {:?}", exit.code());
                return String::from("");
            }
        },
        Err(e) => {
            println!("Error running ffmpeg: {}", e);
            return String::from("");
        }
    }

    // read the temporary file and return the data

    let mut output_file = fs::File::open(output_file_path).unwrap();
    let mut output_data = vec![];

    let read_result = output_file.read_to_end(&mut output_data);

    match read_result {
        Ok(_) => (),
        Err(e) => {
            println!("Error reading output file: {}", e);
            return String::from("");
        }
    }

    return general_purpose::STANDARD.encode(output_data);
}

fn main() {
    tauri::Builder::default()
        .invoke_handler(tauri::generate_handler![
            download_ffmpeg,
            ffmpeg
        ])
        .run(tauri::generate_context!())
        .expect("error while running tauri application")
}

// add a ffmpeg function that receives a arraybuffer and a string array, and returns a arraybuffer
