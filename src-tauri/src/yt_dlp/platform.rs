#[derive(Clone, Debug)]
pub enum Platform {
    Windows,
    Linux,
    Mac,
    Unknown,
}

#[derive(Clone, Debug)]
pub enum Architecture {
    X64,
    X86,
    Armv7l,
    Aarch64,
    Unknown,
}

impl Platform {
    pub fn detect() -> Self {
        let os = std::env::consts::OS;

        match os {
            "windows" => Platform::Windows,
            "linux" => Platform::Linux,
            "macos" => Platform::Mac,
            _ => Platform::Unknown,
        }
    }
}

impl Architecture {
    pub fn detect() -> Self {
        let arch = std::env::consts::ARCH;

        match arch {
            "x86_64" => Architecture::X64,
            "x86" => Architecture::X86,
            "armv7l" => Architecture::Armv7l,
            "aarch64" => Architecture::Aarch64,
            _ => Architecture::Unknown,
        }
    }
}
