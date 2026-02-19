use std::{fs, path::PathBuf};

#[cfg(unix)]
use std::os::unix::fs::PermissionsExt;

#[derive(Clone, Debug)]
pub struct YtDlp {
    pub yt_dlp_binary: PathBuf,
}

impl YtDlp {
    pub fn new(yt_dlp_binary: PathBuf) -> Result<Self, String> {
        if !yt_dlp_binary.exists() {
            return Err("yt-dlp binary not found".to_string());
        }

        #[cfg(unix)]
        {
            let mut perms = fs::metadata(&yt_dlp_binary)
                .map_err(|e| format!("Error getting metadata: {}", e))?
                .permissions();

            perms.set_mode(0o755);

            fs::set_permissions(&yt_dlp_binary, perms)
                .map_err(|e| format!("Error setting permissions: {}", e))?;
        }

        #[cfg(windows)]
        {
            let mut perms = fs::metadata(&yt_dlp_binary)
                .map_err(|e| format!("Error getting metadata: {}", e))?
                .permissions();

            perms.set_readonly(false);

            fs::set_permissions(&yt_dlp_binary, perms)
                .map_err(|e| format!("Error setting permissions: {}", e))?;
        }

        Ok(Self { yt_dlp_binary })
    }
}
