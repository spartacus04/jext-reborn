use base64::{engine::general_purpose, Engine};
use m3u8_rs::{parse_playlist, Playlist};

use super::structs::Transcoding;

pub async fn from_download_link(id: i32, client_id: String) -> Result<String, String> {
    let url = format!("https://api-v2.soundcloud.com/tracks/{}/download?client_id={}", id, client_id);

    let response = match reqwest::get(&url).await {
        Ok(response) => response,
        Err(e) => return Err(format!("Error getting download link: {}", e)),
    };

    let data = match response.bytes().await {
        Ok(data) => data,
        Err(e) => return Err(format!("Error parsing download link: {}", e)),
    };

    return Ok(general_purpose::STANDARD.encode(data));
}

pub async fn from_media_obj(media: Transcoding, client_id: String) -> Result<String, String> {
    let url = media.url.clone();

    if media.url.is_none() || media.format.is_none() {
        return Err("Invalid media object provided".to_string());
    }

    return Ok(from_url_base(url.unwrap(), client_id).await.unwrap());
}

async fn from_url_base(url: String, client_id: String) -> Result<String, String> {
    let media_url = match get_media_url(url.clone(), client_id).await {
        Ok(media_url) => media_url,
        Err(e) => return Err(e),
    };

    if url.contains("/progressive") {
        match get_progressive_stream(media_url).await {
            Ok(stream) => return Ok(stream),
            Err(e) => return Err(e),
        }
    }

    match get_hls_stream(media_url).await {
        Ok(stream) => return Ok(stream),
        Err(e) => return Err(e),
    }
}

async fn get_media_url(url: String, client_id: String) -> Result<String, String> {
    let url = format!("{}?client_id={}", url, client_id);

    let client = reqwest::Client::builder()
        .user_agent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36")
        .build()
        .unwrap();

    let response = match client.get(&url).send().await {
        Ok(response) => response,
        Err(e) => return Err(format!("Error getting media URL: {}", e)),
    };

    let data = match response.json::<serde_json::Value>().await {
        Ok(data) => data,
        Err(e) => return Err(format!("Error parsing media URL: {}", e)),
    };

    if !data["url"].is_string() {
        return Err(format!("Invalid response from Soundcloud. Check if the URL provided is correct: {}", url));
    }

    return Ok(data["url"].as_str().unwrap().to_string());
}

async fn get_progressive_stream(url: String) -> Result<String, String> {
    let response = match reqwest::get(&url).await {
        Ok(response) => response,
        Err(e) => return Err(format!("Error getting progressive stream: {}", e)),
    };

    let data = match response.bytes().await {
        Ok(data) => data,
        Err(e) => return Err(format!("Error parsing progressive stream: {}", e)),
    };

    return Ok(general_purpose::STANDARD.encode(data));
}

async fn get_hls_stream(url: String) -> Result<String, String> {
    let response = match reqwest::get(&url).await {
        Ok(response) => response,
        Err(e) => return Err(format!("Error getting HLS stream: {}", e)),
    };

    if !response.status().is_success() {
        return Err(format!("Error getting HLS stream: {}", response.status()));
    }

    let content = match response.bytes().await {
        Ok(content) => content,
        Err(e) => return Err(format!("Error parsing HLS stream: {}", e)),
    };

    match parse_playlist(&content) {
        Ok((_, pl)) => {
            let mut bytes = Vec::new();
            match pl {
                Playlist::MasterPlaylist(pl) => {
                    if let Err(e) = pl.write_to(&mut bytes) {
                        return Err(format!("Error writing to byte array: {}", e));
                    }
                }
                Playlist::MediaPlaylist(pl) => {
                    if let Err(e) = pl.write_to(&mut bytes) {
                        return Err(format!("Error writing to byte array: {}", e));
                    }
                }
            };
            
            return Ok(general_purpose::STANDARD.encode(bytes))
        },
        Err(e) => return Err(format!("Parsing error: {}", e)),
    }
}