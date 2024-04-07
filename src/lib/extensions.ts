if (console.logs === undefined) {
	console.logs = [];

	console.defaultLog = console.log.bind(console);
	console.log = (...args) => {
		console.logs.push({
			type: 'log',
			datetime: Date().toLocaleString(),
			value: Array.from(args)
		});
		console.defaultLog(...args);
	};

	console.defaultError = console.error.bind(console);
	console.error = (...args) => {
		console.logs.push({
			type: 'error',
			datetime: Date().toLocaleString(),
			value: Array.from(args)
		});
		console.defaultError(...args);
	};

	console.defaultWarn = console.warn.bind(console);
	console.warn = (...args) => {
		console.logs.push({
			type: 'warn',
			datetime: Date().toLocaleString(),
			value: Array.from(args)
		});
		console.defaultWarn(...args);
	};

	console.defaultDebug = console.debug.bind(console);
	console.debug = (...args) => {
		console.logs.push({
			type: 'debug',
			datetime: Date().toLocaleString(),
			value: Array.from(args)
		});
		console.defaultDebug(...args);
	};
}

if (Object.mergeDeep === undefined) {
	Object.mergeDeep = (...objects: any[]) => {
		const isObject = (obj: any) => obj && typeof obj == 'object';

		if(Array.isArray(objects[0]) || Array.isArray(objects[1])) {
			return objects.reduce((prev, obj) => {
				if(obj === undefined) return prev;
				return prev.concat(...obj);
			}, []);
		}

		return objects.reduce((prev, obj) => {
			if(obj === undefined) return prev;
			Object.keys(obj).forEach((key) => {
				const pVal = prev[key];
				const oVal = obj[key];

				if (Array.isArray(pVal) && Array.isArray(oVal)) {
					prev[key] = pVal.concat(...oVal);
				} else if (isObject(pVal) && isObject(oVal)) {
					prev[key] = Object.mergeDeep(pVal, oVal);
				} else {
					prev[key] = oVal;
				}
			});

			return prev;
		}, {});
	};
}