import fs from 'node:fs';
import { json } from '@sveltejs/kit';

export const GET = async () => {
    const packFileNames = fs.readdirSync('community-packs');

    const packs = packFileNames.filter(pack => pack != 'schema.json').map(pack => {
        const packData = fs.readFileSync(`community-packs/${pack}`, 'utf-8');
        return JSON.parse(packData);
    })

    return json(packs);
}