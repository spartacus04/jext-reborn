export const hoversrc = (e : HTMLImageElement, params: { src: string, hover: string }) => {
	const { src, hover } = params;

	const mouseEnter = () => {
		e.src = hover;
	};

	const mouseLeave = () => {
		e.src = src;
	};

	mouseLeave();

	e.addEventListener('mouseenter', mouseEnter);
	e.addEventListener('mouseleave', mouseLeave);
};