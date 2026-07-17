import { writable } from 'svelte/store';
import { base } from '$app/paths';

export const templatesStore = writable<any[]>([]);

export const loadBuiltInTemplates = async () => {
	console.log('loadBuiltInTemplates: Starting load process with base path:', base);
	let templateNames = ['music_disc_13', 'music_disc_lava_chicken', 'music_disc_creator_music_box', 'music_disc_precipice', 'music_disc_relic', 'music_disc_tears'];
	
	try {
		console.log('loadBuiltInTemplates: Fetching templates/index.json');
		let res = await fetch(`${base}/templates/index.json`);
		if (!res.ok) {
			console.log('loadBuiltInTemplates: base fetch failed, trying relative ./templates/index.json');
			res = await fetch(`./templates/index.json`);
		}
		if (!res.ok) {
			console.log('loadBuiltInTemplates: relative fetch failed, trying absolute /templates/index.json');
			res = await fetch(`/templates/index.json`);
		}
		if (res.ok) {
			templateNames = await res.json();
			console.log('loadBuiltInTemplates: Successfully loaded templates list:', templateNames);
		} else {
			console.warn('loadBuiltInTemplates: Failed to fetch templates index from all paths. Status:', res.status);
		}
	} catch (err) {
		console.warn('loadBuiltInTemplates: Failed to load templates/index.json, falling back to default list:', err);
	}

	const loaded: any[] = [];
	for (const name of templateNames) {
		try {
			console.log(`loadBuiltInTemplates: Fetching template details for ${name}`);
			let res = await fetch(`${base}/templates/${name}.json`);
			if (!res.ok) {
				res = await fetch(`./templates/${name}.json`);
			}
			if (!res.ok) {
				res = await fetch(`/templates/${name}.json`);
			}
			if (res.ok) {
				const json = await res.json();
				loaded.push(json);
				console.log(`loadBuiltInTemplates: Successfully loaded template ${name}`);
			} else {
				console.error(`loadBuiltInTemplates: Failed to load template ${name}: HTTP ${res.status}`);
			}
		} catch (err) {
			console.error(`loadBuiltInTemplates: Failed to load template ${name}:`, err);
		}
	}
	templatesStore.set(loaded);
	console.log('loadBuiltInTemplates: Finished loading templates. Store size:', loaded.length);
};
