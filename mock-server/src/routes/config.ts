import { Router } from 'express';

const router = Router();

export interface ConfigNode<T> {
	name: string,
	id: string,
	description: string,
	value: T,
	defaultValue: T,
	enumValues?: string[]
}

export interface ConfigNodeApply<T> {
	id: string,
	value: T
}

let config : ConfigNode<boolean|number|string|{[key : string] : boolean}>[] = [
	{
		name: 'Language',
		id: 'lang',
		description: 'Can be set to auto, custom, silent or a custom locale. If set to auto the plugin will display itself to the player in his language locale (for example if someone is playing in italian, the plugin messages are going to be in italian for him). The console locale is always set to en_us. If set to custom the plugin will generate a language file in his config, which can be edited by the user to make custom messages. These custom language is applied to all players regardless of their locale (except for console). If set to silent the plugin won\'t display any messages in chat. If set to a custom locale each player will see the plugin in that language regardless of their locale.',
		value: 'auto',
		defaultValue: 'auto',
		enumValues: ['auto', 'custom', 'silent', 'other'],
	},
	{
		name: 'Force resource pack',
		id: 'force-resource-pack',
		description: 'If set to true the plugin will kick the player if they reject the server resource pack.',
		value: true,
		defaultValue: true,
	},
	{
		name: 'Ignore failed download',
		id: 'ignore-failed-download',
		description: 'If set to true the plugin will kick the player if the resource pack download fails.',
		value: false,
		defaultValue: false,
	},
	{
		name: 'Allow music overlapping',
		id: 'allow-music-overlapping',
		description: 'If set to true and a player tries to play a record near another jukebox that\'s already playing custom music, the second jukebox won\'t play any music.',
		value: false,
		defaultValue: false,
	},
	{
		name: 'Jukebox behaviour',
		id: 'jukebox-behaviour',
		description: 'When set to gui or legacy-gui it will add a custom jukebox gui when clicking on a jukebox. To add/remove a disc use the left click, to play/stop a disc use the right click.',
		value: 'vanilla',
		defaultValue: 'vanilla',
		enumValues: ['vanilla', 'gui', 'legacy-gui'],
	},
	{
		name: 'Allow metrics',
		id: 'allow-metrics',
		description: 'If set to true anonymous stats about your server will be sent to the bstats api. Please don\'t disable this, as watching how many people use the plugin helps me stay motivated.',
		value: false,
		defaultValue: true,
	},
];

router.get('/api/config', (_, res) => {
	res.status(200).json(config);
});

router.post('/api/config', (req, res) => {
	const aconfig = req.body as ConfigNodeApply<boolean|number|string|{[key : string] : boolean}>[];

	for(const node of aconfig) {
		const index = config.findIndex(n => n.id === node.id);

		if(index === -1) {
			continue;
		}

		config[index].value = node.value;
	}

	res.status(200).send({
		message: 'ok',
	});
});

export default router;