use regex::Regex;

static mut CLIENT_ID: Option<String> = None;

pub async fn get_client_id() -> Result<String, String> {
    if unsafe { CLIENT_ID.is_none() } {

        match set_client_id().await {
            Ok(key) => {
                unsafe { CLIENT_ID = Some(key) };
            },
            Err(e) => {
                return Err(e);
            }
        };
    }

    return Ok(unsafe { CLIENT_ID.clone().unwrap() });
}

async fn set_client_id() -> Result<String, String> {
    let response = match reqwest::get("https://soundcloud.com/").await {
        Ok(response) => response,
        Err(e) => return Err(format!("Error fetching SoundCloud website: {}", e)),
    };

    let body = response.text().await.unwrap();

    let res = body.split("<script crossorigin src=\"").collect::<Vec<&str>>();
    let mut urls = vec![];

    for url_a in res {
        let urlreg = Regex::new(r"^https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)$").unwrap();
        let url = url_a.replace("\"></script>", "");
        let res = url.split("\n").collect::<Vec<&str>>()[0];
        if urlreg.is_match(res) {
            urls.push(res.to_string());
        }
    }

    let response2 = match reqwest::get(urls[urls.len() - 1].clone()).await {
        Ok(response) => response,
        Err(e) => return Err(format!("Error fetching SoundCloud website: {}", e)),
    };

    let body2 = response2.text().await.unwrap();

    if body2.contains(",client_id:\"") {
        let thing_a = body2.split(",client_id:\"").collect::<Vec<&str>>();
        let key = thing_a[1].split("\"").collect::<Vec<&str>>()[0];
        return Ok(key.to_string());
    }

    return Err("Unable to fetch a SoundCloud API key! This most likely has happened due to either making too many requests, or the SoundCloud website has changed!".to_string());
}