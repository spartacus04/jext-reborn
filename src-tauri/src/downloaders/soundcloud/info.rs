use std::io::Write;

use crate::downloaders::soundcloud::structs::TrackInfo;

use super::structs::SetInfo;

pub fn extract_id_from_personalized_track_url(url: String) -> Option<String> {
    if !url.contains("https://soundcloud.com/discover/sets/personalized-tracks::") {
        return None;
    }

    let split = url.split(":");
    let split_vec = split.collect::<Vec<&str>>();

    if split_vec.len() < 5 {
        return None;
    }

    Some(split_vec[4].to_string())
}

pub async fn get_track_info_by_id(id: i32, client_id: String) -> Option<TrackInfo> {
    let url = format!("https://api-v2.soundcloud.com/tracks?ids={}&client_id={}", id, client_id);

    let response = match reqwest::get(&url).await {
        Ok(response) => response,
        Err(e) => {
            println!("Error getting track info: {}", e);
            return None;
        }
    };

    let data = match response.json::<TrackInfo>().await {
        Ok(data) => data,
        Err(e) => {
            println!("Error parsing track info: {}", e);
            return None;
        }
    };

    Some(data)
}

pub async fn get_track_info_by_id_pl(id: Vec<i32>, client_id: &String, playlist_id: i32, playlist_secret_token: String) -> Option<Vec<TrackInfo>> {
    let ids = id.iter().map(|i| i.to_string()).collect::<Vec<String>>().join(",");
    let url = format!("https://api-v2.soundcloud.com/tracks?ids={}&client_id={}&playlistId={}&playlistSecretToken={}", ids, client_id, playlist_id, playlist_secret_token);

    let response = match reqwest::get(&url).await {
        Ok(response) => response,
        Err(e) => {
            println!("Error getting track info: {}", e);
            return None;
        }
    };

    let text = match response.text().await {
        Ok(text) => text,
        Err(e) => {
            println!("Error parsing track info: {}", e);
            return None;
        }
    };

    // save to /home/andreas/Downloads/track_info.json
    let mut file = std::fs::File::create("/home/andrea/Downloads/track_info.json").unwrap();
    file.write(text.as_bytes()).unwrap();

    // decode json
    let data: Vec<TrackInfo> = match serde_json::from_str(&text) {
        Ok(data) => data,
        Err(e) => {
            println!("Error parsing track info: {}", e);
            return None;
        }
    };

    Some(data)

    // let data = match response.json::<Vec<TrackInfo>>().await {
    //     Ok(data) => data,
    //     Err(e) => {
    //         println!("Error parsing track info: {}", e);
    //         return None;
    //     }
    // };

    // Some(data)
}

pub async fn get_track_info_base(url: String, client_id: String) -> Result<TrackInfo, String> {
    let url = format!("https://api-v2.soundcloud.com/resolve?url={}&client_id={}", url, client_id);

    let response = match reqwest::get(&url).await {
        Ok(response) => response,
        Err(e) => return Err(format!("Error getting track info: {}", e)),
    };

    let data = match response.json::<TrackInfo>().await {
        Ok(data) => data,
        Err(e) => return Err(format!("Error parsing track info: {}", e)),
    };

    Ok(data)
}

pub async fn get_set_info_base(url: String, client_id: String) -> Result<SetInfo, String> {
    let url = format!("https://api-v2.soundcloud.com/resolve?url={}&client_id={}", url, client_id);

    let response = match reqwest::get(&url).await {
        Ok(response) => response,
        Err(e) => return Err(format!("Error getting track info: {}", e)),
    };

    let text = match response.text().await {
        Ok(text) => text,
        Err(e) => return Err(format!("Error parsing track info: {}", e)),
    };

    // save to /home/andreas/Downloads/set_info.json
    let mut file = std::fs::File::create("/home/andrea/Downloads/set_info.json").unwrap();

    file.write(text.as_bytes()).unwrap();

    // decode json
    let data: SetInfo = match serde_json::from_str(&text) {
        Ok(data) => data,
        Err(e) => return Err(format!("Error parsing track info: {}", e)),
    };

    Ok(data)

    // let data = match response.json::<SetInfo>().await {
    //     Ok(data) => data,
    //     Err(e) => return Err(format!("Error parsing track info: {}", e)),
    // };

    // Ok(data)
}

pub fn sort_tracks(mut tracks: Vec<TrackInfo>, temp: Vec<i32>) -> Vec<TrackInfo> {
    for i in 0..temp.len() {
        if tracks[i].id != temp[i] {
            for j in 0..tracks.len() {
                if tracks[j].id == temp[i] {
                    let temp = tracks[i].clone();
                    tracks[i] = tracks[j].clone();
                    tracks[j] = temp;
                }
            }
        }
    }

    tracks
}