import { low_brush, low_chest, low_pot, low_trial } from './assets';

export interface Loottable {
	displayName: string;
	subtitle?: string;
	version: number;
	img: string;
	contents: string[];
	mode: 'percentage' | 'weight'
	group: 'chests/*' | 'pots/*' | 'trials/*' | 'archaeology/*';
	defaultLoottableWeight?: number;
	ignorePercentage?: number;
}

export const groups = {
	'chests/*': low_chest,
	'pots/*': low_pot,
	'trials/*': low_trial,
	'archaeology/*': low_brush,
};

export const lootTables : Loottable[] = [
	{
		displayName: 'Trial chambers',
		img: 'loottable_icons/Trial_Chambers.webp',
		version: 34,
		contents: [
			'chests/trial_chambers/supply',
			'chests/trial_chambers/intersection_barrel',
			'chests/trial_chambers/intersection',
			'chests/trial_chambers/entrance',
			'chests/trial_chambers/corridor',
		],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'Trial chambers',
		subtitle: 'Decorated pots',
		img: 'loottable_icons/Trial_Chambers.webp',
		version: 34,
		contents: [
			'pots/trial_chambers/corridor',
		],
		mode: 'weight',
		group: 'pots/*',
		defaultLoottableWeight: 351,
	},
	{
		displayName: 'Vault',
		img: 'loottable_icons/Vault.webp',
		version: 34,
		contents: [
			'chests/trial_chambers/reward',
		],
		mode: 'weight',
		group: 'trials/*',
		defaultLoottableWeight: 56,
	},
	{
		displayName: 'Vault',
		subtitle: 'Ominous',
		img: 'loottable_icons/Vault_ominous.webp',
		version: 34,
		contents: [
			'chests/trial_chambers/reward_ominous',
		],
		mode: 'weight',
		group: 'trials/*',
		defaultLoottableWeight: 14,
	},
	{
		displayName: 'Trial Spawner',
		img: 'loottable_icons/Trial_Spawner.webp',
		version: 34,
		contents: [
			'spawners/trial_chamber/consumables',
		],
		mode: 'weight',
		group: 'trials/*',
		defaultLoottableWeight: 26,
	},
	{
		displayName: 'Trial Spawner',
		subtitle: 'Ominous',
		img: 'loottable_icons/Trial_Spawner_ominous.webp',
		version: 34,
		contents: [
			'spawners/ominous/trial_chamber/consumables',
		],
		mode: 'weight',
		group: 'trials/*',
		defaultLoottableWeight: 110,
	},
	{
		displayName: 'Ancient City',
		img: 'loottable_icons/Ancient_City.webp',
		version: 9,
		contents: ['chests/ancient_city', 'chests/ancient_city_ice_box'],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'Bastion Remnant',
		img: 'loottable_icons/Bastion_Remnant.webp',
		version: 5,
		contents: [
			'chests/bastion_bridge',
			'chests/bastion_hoglin_treasure',
			'chests/bastion_other',
			'chests/bastion_treasure'
		],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'Desert Pyramid',
		img: 'loottable_icons/Desert_pyramid.webp',
		version: 4,
		contents: ['chests/desert_pyramid'],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'Desert pyramid',
		subtitle: 'Archaeology',
		img: 'loottable_icons/Desert_pyramid.webp',
		version: 15,
		contents: ['archaeology/desert_pyramid'],
		mode: 'weight',
		group: 'archaeology/*',
		defaultLoottableWeight: 8,
	},
	{
		displayName: 'Dungeon',
		img: 'loottable_icons/Dungeon.webp',
		version: 4,
		contents: ['chests/simple_dungeon'],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'End City',
		img: 'loottable_icons/End_City.webp',
		version: 4,
		contents: ['chests/end_city_treasure'],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'Igloo',
		img: 'loottable_icons/Igloo.webp',
		version: 4,
		contents: ['chests/igloo_chest'],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'Jungle Temple',
		img: 'loottable_icons/Jungle_pyramid.webp',
		version: 4,
		contents: ['chests/jungle_temple'],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'Nether Fortress',
		img: 'loottable_icons/Nether_Fortress.webp',
		version: 4,
		contents: ['chests/nether_bridge'],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'Ruined Portal',
		img: 'loottable_icons/Nether_portal.webp',
		version: 5,
		contents: ['chests/ruined_portal'],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'Pillager Outpost',
		img: 'loottable_icons/Pillager_outpost.webp',
		version: 4,
		contents: ['chests/pillager_outpost'],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'Shipwreck',
		img: 'loottable_icons/Shipwreck.webp',
		version: 4,
		contents: ['chests/shipwreck_map', 'chests/shipwreck_supply', 'chests/shipwreck_treasure'],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'Stronghold',
		img: 'loottable_icons/Stronghold.webp',
		version: 4,
		contents: ['chests/stronghold_corridor', 'chests/stronghold_crossing'],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'Ocean Ruins',
		img: 'loottable_icons/Ocean_ruins.webp',
		version: 4,
		contents: ['chests/underwater_ruin_big', 'chests/underwater_ruin_small'],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'Ocean ruins',
		subtitle: 'Archaeology',
		img: 'loottable_icons/Ocean_ruins.webp',
		version: 15,
		contents: ['archaeology/ocean_ruin_cold', 'archaeology/ocean_ruin_warm'],
		mode: 'weight',
		group: 'archaeology/*',
		defaultLoottableWeight: 15,
	},
	{
		displayName: 'Village',
		img: 'loottable_icons/Village.webp',
		version: 4,
		contents: [
			'chests/village/village_plains_house',
			'chests/village/village_savanna_house',
			'chests/village/village_desert_house',
			'chests/village/village_snowy_house',
			'chests/village/village_taiga_house'
		],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'Woodland Mansion',
		img: 'loottable_icons/Woodland_Mansion.webp',
		version: 4,
		contents: ['chests/woodland_mansion'],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'Mineshaft',
		img: 'loottable_icons/Mineshaft.webp',
		version: 4,
		contents: ['chests/abandoned_mineshaft'],
		mode: 'percentage',
		group: 'chests/*',
	},
	{
		displayName: 'Trail ruins',
		subtitle: 'Common',
		img: 'loottable_icons/Trail_Ruins.webp',
		version: 15,
		contents: ['archaeology/trail_ruins_common'],
		mode: 'weight',
		group: 'archaeology/*',
		defaultLoottableWeight: 45,
	},
	{
		displayName: 'Trail ruins',
		subtitle: 'Rare',
		img: 'loottable_icons/Trail_Ruins.webp',
		version: 15,
		contents: ['archaeology/trail_ruins_rare'],
		mode: 'weight',
		group: 'archaeology/*',
		defaultLoottableWeight: 12,
	},
	{
		displayName: 'Desert well',
		img: 'loottable_icons/Desert_well.webp',
		version: 15,
		contents: ['archaeology/desert_well'],
		mode: 'weight',
		group: 'archaeology/*',
		defaultLoottableWeight: 8,
	}
];

export const versions = new Map([
	[4, '1.14 - 1.14.4'],
	[5, '1.15 - 1.16.1'],
	[6, '1.16.2 - 1.16.5'],
	[7, '1.17 - 1.17.1'],
	[8, '1.18 - 1.18.2'],
	[9, '1.19 - 1.19.2'],
	[12, '1.19.3'],
	[13, '1.19.4'],
	[15, '1.20 - 1.20.1'],
	[18, '1.20.2'],
	[22, '1.20.3 - 1.20.4'],
	[32, '1.20.5 - 1.20.6'],
	[34, '1.21 - 1.21.1'],
	[43, '1.21.2 - 1.21.3'],
]);