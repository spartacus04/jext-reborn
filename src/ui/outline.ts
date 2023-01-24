export const outline = (e : HTMLElement, { width = '1px', color = 'white' } : {width ?: string, color ?: string} = { width: '1px', color: 'white' }) => {
	const mouseEnter = () => {
		e.style.border = `${width} solid ${color}`;
	};

	const mouseLeave = () => {
		e.style.border = `${width} solid transparent`;
	};

	e.addEventListener('mouseenter', mouseEnter);
	e.addEventListener('mouseleave', mouseLeave);

	mouseLeave();

	return {
		destroy() {
			e.removeEventListener('mouseenter', mouseEnter);
			e.removeEventListener('mouseleave', mouseLeave);
		},
	};
};