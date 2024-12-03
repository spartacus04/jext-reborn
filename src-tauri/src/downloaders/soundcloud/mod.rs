use std::borrow::Borrow;

use regex::Regex;
use url::Url;

mod info;
mod client;
mod structs;
mod download;

#[tauri::command(async)]
pub fn sc_is_valid_url(url: String) -> bool {
    let regexp = Regex::new(r"^https?:\/\/(soundcloud\.com)\/(.*)$").unwrap();
    let mobile_url_regex = Regex::new(r"^https?:\/\/(m\.soundcloud\.com)\/(.*)$").unwrap();
    let firebase_url_regex = Regex::new(r"^https?:\/\/(soundcloud\.app\.goo\.gl)\/(.*)$").unwrap();

    let mut success = false;
    if mobile_url_regex.is_match(&url) {
        success = regexp.is_match(&url);
    }

    if !success && firebase_url_regex.is_match(&url) {
        success = regexp.is_match(&url);
    }

    if !success && regexp.is_match(&url) {
        success = true;
    }

    return success
}

#[tauri::command(async)]
pub fn sc_is_playlist_url(url: String) -> bool {
    let url = url.clone();

    if !sc_is_valid_url(url.clone()) {
        return false;
    }

    match Url::parse(&url) {
        Ok(url) => {
            return url.path().contains("/sets/")
        }
        Err(_) => {
            return false
        }
    }
}

#[tauri::command(async)]
pub async fn sc_get_info(url: String) -> Result<structs::TrackInfo, String> {
    let client_id = match client::get_client_id().await {
        Ok(client_id) => client_id,
        Err(e) => return Err(e),
    };
    
    let data = if url.contains("https://soundcloud.com/discover/sets/personalized-tracks::") {
        let id_string = info::extract_id_from_personalized_track_url(url.clone());
        if id_string.is_none() {
            return Err(format!("Could not parse track ID from given URL: {}", url));
        }

        let id = id_string.unwrap().parse::<i32>().unwrap();
        let track_info = info::get_track_info_by_id(id, client_id).await;
        if track_info.is_none() {
            return Err(format!("Could not find track with ID: {}", id));
        }

        track_info.unwrap()
    } else {
        match info::get_track_info_base(url, client_id).await {
            Ok(data) => data,
            Err(e) => return Err(e),
        }
    };

    if data.media.is_none() {
        return Err("The given URL does not link to a Soundcloud track".to_string());
    }

    return Ok(data)
}

#[tauri::command(async)]
pub async fn sc_get_set_info(url: String) -> Result<structs::SetInfo, String> {
    let client_id = match client::get_client_id().await {
        Ok(client_id) => client_id,
        Err(e) => return Err(e),
    };

    let mut set_info = match info::get_set_info_base(url, client_id.clone()).await {
        Ok(data) => data,
        Err(e) => return Err(e),
    };

    let temp = set_info.tracks.clone().iter().map(|track| track.id).collect::<Vec<i32>>();
    let playlist_id = set_info.id;
    let playlist_secret_token = set_info.secret_token.clone();
    let incomplete_tracks = set_info.tracks.iter().filter(|track| track.title.is_none()).collect::<Vec<&structs::TrackInfo>>();

    if incomplete_tracks.len() == 0 {
        return Ok(set_info);
    }

    let complete_tracks = set_info.tracks.iter().filter(|track| track.title.is_some()).collect::<Vec<&structs::TrackInfo>>();

    let ids = incomplete_tracks.iter().map(|t| t.id).collect::<Vec<i32>>();

    if ids.len() > 50 {
        let mut split_ids = Vec::new();
        for _ in 0..(ids.len() / 50) {
            split_ids.push(Vec::new());
        }

        for x in 0..ids.len() {
            let i = x / 50;
            split_ids[i].push(ids[x]);
        }

        let mut promises = Vec::new();
        for ids in split_ids {
            promises.push(info::get_track_info_by_id_pl(ids, client_id.borrow(), playlist_id, playlist_secret_token.clone().unwrap()));
        }

        let info = futures::future::join_all(promises).await;

        let mut temp_tracks = Vec::new();
        for i in complete_tracks {
            temp_tracks.push(i.clone());
        }

        for i in info {
            let i = i.unwrap();
            for j in i {
                temp_tracks.push(j);
            }
        }

        set_info.tracks = info::sort_tracks(set_info.tracks.clone(), temp);

        return Ok(set_info);
    }

    let info = info::get_track_info_by_id_pl(ids, client_id.borrow(), playlist_id, playlist_secret_token.clone().unwrap_or("".to_string())).await;

    let mut temp_tracks = Vec::new();
    for i in complete_tracks {
        temp_tracks.push(i.clone());
    }

    if let Some(i) = info {
        for j in i {
            temp_tracks.push(j);
        }
    }

    set_info.tracks = info::sort_tracks(set_info.tracks.clone(), temp);

    return Ok(set_info)
}

#[tauri::command(async)]
pub async fn sc_download(url: String) -> Result<String, String> {
    let client_id = match client::get_client_id().await {
        Ok(client_id) => client_id,
        Err(e) => return Err(e),
    };

    let info = match info::get_track_info_base(url.clone(), client_id.clone()).await {
        Ok(info) => info,
        Err(e) => return Err(e),
    };

    if info.downloadable.unwrap_or(false) {
        match download::from_download_link(info.id, client_id.clone()).await {
            Ok(download_link) => return Ok(download_link),
            Err(e) => { return Err(e); }
        }
    }

    let media = info.media.clone().
        unwrap_or(structs::Media {
            transcodings: vec![]
        });

    if media.transcodings.len() == 0 {
        return Err("No transcodings found".to_string());
    }

    let transcoding = media.transcodings[0].clone();

    match download::from_media_obj(transcoding, client_id.clone()).await {
        Ok(media) => return Ok(media),
        Err(e) => return Err(e),
    }
}