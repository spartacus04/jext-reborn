export const versions = new Map([
    [4, '1.14 - 1.14.4'],
    [5, '1.15 - 1.16.1'],
    [6, '1.16.2-1.16.5'],
    [7, '1.17 - 1.17.1'],
    [8, '1.18 - 1.18.2'],
    [9, '1.19 - 1.19.2'],
]);

export const dungeons : dungeon[] = [
    {
        source: 'chests/ancient_city,chests/ancient_city_ice_box',
        img: 'loottable_icons/Ancient_City.webp',
        name: 'Ancient City',
        minVersion: 9,
    },
    {
        source: 'chests/bastion_bridge,chests/bastion_hoglin_treasure,chests/bastion_other,chests/bastion_treasure',
        img: 'loottable_icons/Bastion_Remnant.webp',
        name: 'Bastion Remnant',
        minVersion: 5,
    },
    {
        source: 'chests/desert_pyramid',
        img: 'loottable_icons/Desert_pyramid.webp',
        name: 'Desert Pyramid',
        minVersion: 4,
    },
    {
        source: 'chests/simple_dungeon',
        img: 'loottable_icons/Dungeon.webp',
        name: 'Dungeon',
        minVersion: 4,
    },
    {
        source: 'chests/end_city_treasure',
        img: 'loottable_icons/End_City.webp',
        name: 'End City',
        minVersion: 4,
    },
    {
        source: 'chests/igloo_chest',
        img: 'loottable_icons/Igloo.webp',
        name: 'Igloo',
        minVersion: 4,
    },
    {
        source: 'chests/jungle_temple',
        img: 'loottable_icons/Jungle_pyramid.webp',
        name: 'Jungle Temple',
        minVersion: 4,
    },
    {
        source: 'chests/nether_bridge',
        img: 'loottable_icons/Nether_Fortress.webp',
        name: 'Nether Fortress',
        minVersion: 4,
    },
    {
        source: 'chests/ruined_portal',
        img: 'loottable_icons/Nether_portal.webp',
        name: 'Ruined Portal',
        minVersion: 5,
    },
    {
        source: 'chests/pillager_outpost',
        img: 'loottable_icons/Pillager_outpost.webp',
        name: 'Pillager Outpost',
        minVersion: 4,
    },
    {
        source: 'chests/shipwreck_map,chests/shipwreck_supply,chests/shipwreck_treasure',
        img: 'loottable_icons/Shipwreck.webp',
        name: 'Shipwreck',
        minVersion: 4,
    },
    {
        source: 'chests/stronghold_corridor,chests/stronghold_crossing',
        img: 'loottable_icons/Stronghold.webp',
        name: 'Stronghold',
        minVersion: 4,
    },
    {
        source: 'chests/underwater_ruin_big,chests/underwater_ruin_small',
        img: 'loottable_icons/Underwater_ruin.webp',
        name: 'Underwater Ruins',
        minVersion: 4,
    },
    {
        source: 'chests/village/village_plains_house,chests/village/village_savanna_house,chests/village/village_desert_house,chests/village/village_snowy_house,chests/village/village_taiga_house',
        img: 'loottable_icons/Village.webp',
        name: 'Village',
        minVersion: 4,
    },
    {
        source: 'chests/woodland_mansion',
        img: 'loottable_icons/Woodland_Mansion.webp',
        name: 'Woodland Mansion',
        minVersion: 4,
    },
    {
        source: 'chests/abandoned_mineshaft',
        img: 'loottable_icons/Mineshaft.webp',
        name: 'Mineshaft',
        minVersion: 4,
    },
];

interface dungeon {
	source: string,
	img: string,
	name: string,
	minVersion: number
}

export interface songData {
	uploadedFile: File,
	oggFile : Blob,
	monoFile : Blob,
	isMono: boolean,
	name: string,
	author: string,
	lores: string,
	texture: Blob,
	namespace: string,
	creeperDrop: boolean,
	lootTables: string[]
}

export interface Disc {
	title: string;
	author: string;
	'disc-namespace': string;
	'model-data': number;
	'creeper-drop': boolean;
	lores: string[];
	'loot-tables'?: string[]
}