interface Loottables {
	[key: string]: {
		[key: string]: {
			version: number;
			img: string;
			contents: string[];
			mode: 'percentage' | 'weight'
		};
	};
}

export const loottables: Loottables = {
	'chests/*': {
		'Ancient City': {
			version: 9,
			img: 'loottable_icons/Ancient_City.webp',
			contents: ['chests/ancient_city', 'chests/ancient_city_ice_box'],
			mode: 'percentage',
		},
		'Bastion Remnant': {
			version: 5,
			img: 'loottable_icons/Bastion_Remnant.webp',
			contents: [
				'chests/bastion_bridge',
				'chests/bastion_hoglin_treasure',
				'chests/bastion_other',
				'chests/bastion_treasure'
			],
			mode: 'percentage',
		},
		'Desert Pyramid': {
			version: 4,
			img: 'loottable_icons/Desert_pyramid.webp',
			contents: ['chests/desert_pyramid'],
			mode: 'percentage',
		},
		Dungeon: {
			version: 4,
			img: 'loottable_icons/Dungeon.webp',
			contents: ['chests/simple_dungeon'],
			mode: 'percentage',
		},
		'End City': {
			version: 4,
			img: 'loottable_icons/End_City.webp',
			contents: ['chests/end_city_treasure'],
			mode: 'percentage',
		},
		Igloo: {
			version: 4,
			img: 'loottable_icons/Igloo.webp',
			contents: ['chests/igloo_chest'],
			mode: 'percentage',
		},
		'Jungle Temple': {
			version: 4,
			img: 'loottable_icons/Jungle_pyramid.webp',
			contents: ['chests/jungle_temple'],
			mode: 'percentage',
		},
		'Nether Fortress': {
			version: 4,
			img: 'loottable_icons/Nether_Fortress.webp',
			contents: ['chests/nether_bridge'],
			mode: 'percentage',
		},
		'Ruined Portal': {
			version: 5,
			img: 'loottable_icons/Nether_portal.webp',
			contents: ['chests/ruined_portal'],
			mode: 'percentage',
		},
		'Pillager Outpost': {
			version: 4,
			img: 'loottable_icons/Pillager_outpost.webp',
			contents: ['chests/pillager_outpost'],
			mode: 'percentage',
		},
		Shipwreck: {
			version: 4,
			img: 'loottable_icons/Shipwreck.webp',
			contents: ['chests/shipwreck_map', 'chests/shipwreck_supply', 'chests/shipwreck_treasure'],
			mode: 'percentage',
		},
		Stronghold: {
			version: 4,
			img: 'loottable_icons/Stronghold.webp',
			contents: ['chests/stronghold_corridor', 'chests/stronghold_crossing'],
			mode: 'percentage',
		},
		'Underwater Ruins': {
			version: 4,
			img: 'loottable_icons/Underwater_ruin.webp',
			contents: ['chests/underwater_ruin_big', 'chests/underwater_ruin_small'],
			mode: 'percentage',
		},
		Village: {
			version: 4,
			img: 'loottable_icons/Village.webp',
			contents: [
				'chests/village/village_plains_house',
				'chests/village/village_savanna_house',
				'chests/village/village_desert_house',
				'chests/village/village_snowy_house',
				'chests/village/village_taiga_house'
			],
			mode: 'percentage',
		},
		'Woodland Mansion': {
			version: 4,
			img: 'loottable_icons/Woodland_Mansion.webp',
			contents: ['chests/woodland_mansion'],
			mode: 'percentage',
		},
		Mineshaft: {
			version: 4,
			img: 'loottable_icons/Mineshaft.webp',
			contents: ['chests/abandoned_mineshaft'],
			mode: 'percentage',
		}
	},
	'archaeology/*': {
		'Trail ruins': {
			version: 15,
			img: 'loottable_icons/Trail_Ruins.webp',
			contents: ['archaeology/trail_ruins_rare', 'archaeology/trail_ruins_common'],
			mode: 'weight'
		},
		'Ocean ruins': {
			version: 15,
			img: 'loottable_icons/Ocean_ruins.webp',
			contents: ['archaeology/ocean_ruin_cold', 'archaeology/ocean_ruin_warm'],
			mode: 'weight'
		},
		'Desert pyramid': {
			version: 15,
			img: 'loottable_icons/Desert_pyramid.webp',
			contents: ['archaeology/desert_pyramid'],
			mode: 'weight'
		},
		'Desert well': {
			version: 15,
			img: 'loottable_icons/Desert_well.webp',
			contents: ['archaeology/desert_well'],
			mode: 'weight'
		}
	},
};
