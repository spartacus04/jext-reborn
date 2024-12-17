import { Hono } from "hono";
import { isLoggedIn } from "../utils";
import * as fs from 'node:fs';

export let config: { lang: string; "jukebox-behaviour": string; "jukebox-gui-style": string; "jukebox-gui-size": number; "disable-music-overlap": boolean; "jukebox-range": number; "disc-loottables-limit": any; "fragment-loottables-limit": any; "force-resource-pack": boolean; "check-for-updates": boolean; "allow-metrics": boolean; "enable-resource-pack-host": boolean; "web-interface-port": number; "override-web-interface-base-url": string; "web-interface-api-enabled": boolean; "web-interface-password": string; };

if(fs.existsSync('./config.json')) {
    config = JSON.parse(fs.readFileSync('./config.json', 'utf-8'));
} else {
    config = {
        lang: "auto",
        "jukebox-behaviour": "vanilla",
        "jukebox-gui-style": "scroll-vertical",
        "jukebox-gui-size": 96,
        "disable-music-overlap": true,
        "jukebox-range": 64,
        "disc-loottables-limit": {},
        "fragment-loottables-limit": {},
        "force-resource-pack": false,
        "check-for-updates": true,
        "allow-metrics": true,
        "enable-resource-pack-host": true,
        "web-interface-port": 9871,
        "override-web-interface-base-url": "",
        "web-interface-api-enabled": true,
        "web-interface-password": ""
    };
}

const configData = {
    "lang": {
        "name": "Language mode",
        "description": "If set to auto, the plugin will use the player's locale; if set to silent, the plugin won't output any messages; if set to custom the plugin will use the language specified in the custom language file; If set to a locale, the plugin will use that locale.",
        "default": "auto",
        "enumValues": ["auto", "custom", "silent", "af_za", "ar_sa", "ca_es", "cs_cz", "da_dk", "de_de", "el_gr", "en_us", "es_es", "fi_fi", "fr_fr", "he_il", "hu_hu", "it_it", "ja_jp", "ko_kr", "nl_nl", "no_no", "pl_pl", "pt_br", "pt_pt", "ro_ro", "ru_ru", "sr_sp", "sv_se", "tr_tr", "uk_ua", "vi_vn", "zh_cn", "zh_tw"],
    },
    "jukebox-behaviour": {
        "name": "Jukebox behaviour",
        "description": "If set to vanilla, the plugin will use the vanilla jukebox behaviour; if set to gui, the plugin will use the custom GUI.",
        "default": "vanilla",
        "enumValues": ["vanilla", "gui"],
    },
    "jukebox-gui-style": {
        "name": "Jukebox GUI style",
        "description": "Determines the style of the jukebox GUI.",
        "default": "scroll-vertical",
        "enumValues": ["scroll-vertical", "scroll-horizontal", "page-vertical", "page-horizontal"],
    },
    "jukebox-gui-size": {
        "name": "Jukebox GUI size",
        "description": "Sets the maximum amount of items that can be added to a jukebox GUI.",
        "default": 96,
    },
    "disable-music-overlap": {
        "name": "Disable music overlap",
        "description": "If set to true, the plugin will not play music if there is already music playing.",
        "default": true,
    },
    "jukebox-range": {
        "name": "Jukebox range",
        "description": "Sets the range for sound playback, if set to values <= 0 it'll play for everyone on the server. Only works if the audio is mono",
        "default": 64,
    },
    "disc-loottables-limit": {
        "name": "Disc loot tables limit",
        "description": "Sets the maximum amount of discs that can be found in chests, the default amount is 2.",
        "default": {},
        "enumValues": "chests/*",
    },
    "fragment-loottables-limit": {
        "name": "Disc fragments loot tables limit",
        "description": "Sets the maximum amount of disc fragments that can be found in chests, the default amount is 3.",
        "default": {},
        "enumValues": "chests/*",
    },
    "force-resource-pack": {
        "name": "Force resource pack",
        "description": "If set to true, the plugin will force players to use the resource pack. If the player declines, they will be kicked.",
        "default": false,
    },
    "check-for-updates": {
        "name": "Check for updates",
        "description": "If set to true, the plugin will check for updates on startup.",
        "default": true,
    },
    "allow-metrics": {
        "name": "Allow metrics",
        "description": "If set to true, the plugin will send metrics to bStats. Please consider enabling this, as it helps me improve the plugin.",
        "default": true,
    },
    "enable-resource-pack-host": {
        "name": "Enable resource pack host",
        "description": "If set to true, the plugin will host the resource pack and automatically send it to players.",
        "default": true,
    },
    "web-interface-port": {
        "name": "Web interface port",
        "description": "The port the web interface api & resource pack will be hosted on.",
        "default": 9871,
    },
    "override-web-interface-base-url": {
        "name": "Web interface base url",
        "description": "If set to something it'll use the custom base url for both the web interface api & resource pack host",
        "default": "",
    },
    "web-interface-api-enabled": {
        "name": "Web interface api enabled",
        "description": "If set to true, the plugin will have a REST api running to edit the discs settings and the config.",
        "default": true,
    },
    "web-interface-password": {
        "name": "Web interface password",
        "description": "The password required to access the web interface api.",
        "default": "",
    },
};

const configTypes = {
    lang: ["auto", "custom", "silent", "af_za", "ar_sa", "ca_es", "cs_cz", "da_dk", "de_de", "el_gr", "en_us", "es_es", "fi_fi", "fr_fr", "he_il", "hu_hu", "it_it", "ja_jp", "ko_kr", "nl_nl", "no_no", "pl_pl", "pt_br", "pt_pt", "ro_ro", "ru_ru", "sr_sp", "sv_se", "tr_tr", "uk_ua", "vi_vn", "zh_cn", "zh_tw"],
    "jukebox-behaviour": ["vanilla", "gui"],
    "jukebox-gui-style": ["scroll-vertical", "scroll-horizontal", "page-vertical", "page-horizontal"],
    "jukebox-gui-size": "number",
    "disable-music-overlap": "boolean",
    "jukebox-range": "number",
    "disc-loottables-limit": "object",
    "fragment-loottables-limit": "object",
    "force-resource-pack": "boolean",
    "check-for-updates": "boolean",
    "allow-metrics": "boolean",
    "enable-resource-pack-host": "boolean",
    "web-interface-port": "number",
    "override-web-interface-base-url": "string",
    "web-interface-api-enabled": "boolean",
    "web-interface-password": "string"
};

interface ConfigNode {
    name: string;
    id: string;
    description: string;
    value: any;
    default: any;
    enumValues?: string[] | string;
}

const configRouter = new Hono();

configRouter.get('/config/apply', async (c) => {
    return c.text("Please use POST method");
});

configRouter.post('/config/apply', async (c) => {
    if(!isLoggedIn(c)) {
        c.status(401);
        return c.text("401 Unauthorized");
    }

    const json = await c.req.json();

    const isConfigValid = () => {
        for (const key in json) {
            const type = (configTypes as any)[key];

            if(Array.isArray(type)) {
                if(!type.includes(json[key])) {
                    return false;
                }
            }

            if(type === "number") {
                if(isNaN(json[key])) {
                    return false;
                }
            }

            if(type === "boolean") {
                if(json[key] !== true && json[key] !== false) {
                    return false;
                }
            }

            if(type === "object") {
                if(typeof json[key] !== "object") {
                    return false;
                }
            }

            if(type === "string") {
                if(typeof json[key] !== "string") {
                    return false;
                }
            }
        }

        return true;
    }

    if(isConfigValid()) {
        for (const key in json) {
            if((config as any)[key] !== undefined) {
                (config as any)[key] = json[key];
            }
        }

        fs.writeFileSync('./config.json', JSON.stringify(config, null, 4));

        return c.text("", 200);
    } else {
        return c.text("400 Bad Request", 400);
    }
});

configRouter.get('/config/read', async (c) => {
    if(!isLoggedIn(c)) {
        c.status(401);
        return c.text("401 Unauthorized");
    }

    const configNodes: ConfigNode[] = [];

    for (const key in config) {
        const node = {
            name: (configData as any)[key].name,
            id: key,
            description: (configData as any)[key].description,
            value: (config as any)[key],
            default: (configData as any)[key].default,
            enumValues: (configData as any)[key].enumValues,
        };

        configNodes.push(node);
    }

    return c.json(configNodes, 200);
});

export default configRouter;