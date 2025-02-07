import fs from 'node:fs';
import { $ } from 'bun';

const text = fs.readFileSync('src-tauri/Cargo.toml', 'utf8');

const hash = (await $`git rev-parse --short HEAD`).stdout.toString().trim();

const newText = text.replace(/version = "(.*)"/, `version = "$1-${hash}"`);

fs.writeFileSync('src-tauri/Cargo.toml', newText);

console.log(`Set commit version to ${hash}`);