#[derive(serde::Serialize, serde::Deserialize, Clone)]
pub struct User {
    kind: String,
    avatar_url: String,
    city: Option<String>,
    comments_count: Option<i32>,
    country_code: Option<String>,
    created_at: Option<String>,
    description: Option<String>,
    followers_count: Option<i32>,
    followings_count: Option<i32>,
    first_name: Option<String>,
    full_name: Option<String>,
    groups_count: Option<i32>,
    id: i32,
    last_name: Option<String>,
    permalink_url: String,
    uri: String,
    username: String,
}

#[derive(serde::Serialize, serde::Deserialize, Clone)]
pub struct TranscodingFormat {
    protocol: String,
    mime_type: String,
}

#[derive(serde::Serialize, serde::Deserialize, Clone)]
pub struct Transcoding {
    pub url: Option<String>,
    preset: String,
    snipped: bool,
    pub format: Option<TranscodingFormat>,
}

#[derive(serde::Serialize, serde::Deserialize, Clone)]
pub struct Media {
    pub transcodings: Vec<Transcoding>,
}

#[derive(serde::Serialize, serde::Deserialize, Clone)]
pub struct TrackInfo {
    kind: String,
    monetization_model: String,
    pub id: i32,
    policy: String,
    comment_count: Option<i32>,
    full_duration: Option<i32>,
    pub downloadable: Option<bool>,
    created_at: Option<String>,
    description: Option<String>,
    pub media: Option<Media>,
    pub title: Option<String>,
    publisher_metadata: Option<serde_json::Value>,
    duration: Option<i32>,
    has_downloads_left: Option<bool>,
    artwork_url: Option<String>,
    public: Option<bool>,
    streamable: Option<bool>,
    tag_list: Option<String>,
    genre: Option<String>,
    reposts_count: Option<i32>,
    label_name: Option<String>,
    state: Option<String>,
    last_modified: Option<String>,
    commentable: Option<bool>,
    uri: Option<String>,
    download_count: Option<i32>,
    likes_count: Option<i32>,
    display_date: Option<String>,
    user_id: Option<i32>,
    waveform_url: Option<String>,
    permalink: Option<String>,
    permalink_url: Option<String>,
    user: Option<User>,
    playback_count: Option<i32>,
}

#[derive(serde::Serialize, serde::Deserialize)]
pub struct SetInfo {
    duration: i32,
    permalink_url: String,
    reposts_count: i32,
    genre: String,
    permalink: String,
    purchase_url: Option<String>,
    description: Option<String>,
    uri: String,
    label_name: Option<String>,
    tag_list: String,
    set_type: String,
    public: bool,
    track_count: i32,
    user_id: i32,
    last_modified: String,
    license: String,
    pub tracks: Vec<TrackInfo>,
    pub id: i32,
    release_date: Option<String>,
    display_date: String,
    sharing: String,
    pub secret_token: Option<String>,
    created_at: String,
    likes_count: i32,
    kind: String,
    purchase_title: Option<String>,
    managed_by_feeds: bool,
    artwork_url: Option<String>,
    is_album: bool,
    user: User,
    published_at: String,
    embeddable_by: String,
}