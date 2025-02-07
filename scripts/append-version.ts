import fs from 'node:fs';

const text = fs.readFileSync('src-tauri/Cargo.toml', 'utf8');

const version = process.argv[2];

const newText = text.replace(/version = "(.*)"/, `version = "$1-${version}"`);

fs.writeFileSync('src-tauri/Cargo.toml', newText);

console.log(`Set commit version to ${version}`);