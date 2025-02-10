import fs from 'node:fs';
import child_process from 'node:child_process';
import { json } from '@sveltejs/kit';

export const prerender = true;

const executeCommand = async (command: string) : Promise<string> => {
    return new Promise((resolve, reject) => {
        child_process.exec(command, (error, stdout) => {
            if (error) {
                reject(error);
            }
            resolve(stdout.trim());
        });
    });
}

export const GET = async () => {
    const webuiVersion = JSON.parse(fs.readFileSync('package.json', 'utf8')).version
    const desktopVersion = JSON.parse(fs.readFileSync('src-tauri/tauri.conf.json', 'utf8')).version
    const commit = await executeCommand('git rev-parse HEAD');
    const date = await executeCommand('git --no-pager log -1 --pretty="format:%cd" --date="format:%Y-%m-%d %H:%M:%S"');

    return json({
        webuiVersion,
        desktopVersion,
        commit,
        date: new Date(date).toLocaleString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric', hour12: true })
    })
}