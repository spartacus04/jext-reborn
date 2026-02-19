import fs from 'node:fs';
import path from 'node:path';

const platform = process.platform;
const arch = process.arch;

const ytBase = 'https://github.com/yt-dlp/yt-dlp/releases/latest/download/';
const ffBase = 'https://github.com/eugeneware/ffmpeg-static/releases/latest/download/';

const downloadFile = async (url: string, outputPaths: string[]) => {
    const response = await fetch(url);
    if (!response.ok) {
        throw new Error(`Failed to download file: ${response.statusText}`);
    }

    const buffer = Buffer.from(await response.arrayBuffer());

    for (const outputPath of outputPaths) {
        fs.writeFileSync(outputPath, buffer);

        // Make executable on Unix systems
        if (platform !== 'win32') {
            fs.chmodSync(outputPath, 0o755);
        }
    }
};

const resolveTargetTriple = () => {
    if (platform === 'linux' && arch === 'x64') {
        return 'x86_64-unknown-linux-gnu';
    }

    if (platform === 'linux' && arch === 'arm64') {
        return 'aarch64-unknown-linux-gnu';
    }

    if (platform === 'darwin' && arch === 'arm64') {
        return 'aarch64-apple-darwin';
    }

    if (platform === 'darwin' && arch === 'x64') {
        return 'x86_64-apple-darwin';
    }

    if (platform === 'win32' && arch === 'x64') {
        return 'x86_64-pc-windows-msvc';
    }

    throw new Error(`Unsupported platform/arch: ${platform}/${arch}`);
};

const ytDlpBinaries = [
    {
        url: ytBase + 'yt-dlp_linux',
        name: 'yt-dlp-linux'
    },
    {
        url: ytBase + 'yt-dlp_macos',
        name: 'yt-dlp-macos-aarch64'
    },
    {
        url: ytBase + 'yt-dlp_macos',
        name: 'yt-dlp-macos-x86_64'
    },
    {
        url: ytBase + 'yt-dlp.exe',
        name: 'yt-dlp-windows.exe'
    }
];

const ffmpegBinaries = [
    {
        url: ffBase + 'ffmpeg-linux-x64',
        name: 'ffmpeg-linux-x64'
    },
    {
        url: ffBase + 'ffprobe-linux-x64',
        name: 'ffprobe-linux-x64'
    },
    {
        url: ffBase + 'ffmpeg-linux-arm64',
        name: 'ffmpeg-linux-arm64'
    },
    {
        url: ffBase + 'ffprobe-linux-arm64',
        name: 'ffprobe-linux-arm64'
    },
    {
        url: ffBase + 'ffmpeg-darwin-arm64',
        name: 'ffmpeg-darwin-arm64'
    },
    {
        url: ffBase + 'ffprobe-darwin-arm64',
        name: 'ffprobe-darwin-arm64'
    },
    {
        url: ffBase + 'ffmpeg-win32-x64',
        name: 'ffmpeg-win32-x64'
    },
    {
        url: ffBase + 'ffprobe-win32-x64',
        name: 'ffprobe-win32-x64'
    }
];

const main = async () => {
    const target = resolveTargetTriple();
    const suffix = `-${target}`;

    const outDir = path.resolve('./src-tauri/bin');
    if (!fs.existsSync(outDir)) {
        fs.mkdirSync(outDir);
    }

    const binaries = [...ytDlpBinaries, ...ffmpegBinaries];

    for (const binary of binaries) {
        const basePath = path.join(outDir, binary.name);
        const suffixedPath = path.join(outDir, `${binary.name}${suffix}`);
        const outputPaths = basePath === suffixedPath ? [basePath] : [basePath, suffixedPath];

        await downloadFile(binary.url, outputPaths);
    }

    console.log('All binaries downloaded successfully.');
};

main().catch(console.error);
