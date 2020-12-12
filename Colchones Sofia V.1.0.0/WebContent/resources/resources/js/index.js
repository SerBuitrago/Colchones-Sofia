
/*******************************************************************************
 * MENU
 ******************************************************************************/
const btnDepartamentos = document.getElementById('btn-departamentos'),
btnCerrarMenu = document.getElementById('btn-menu-cerrar'),
grid = document.getElementById('grid'),
contenedorEnlacesNav = document.querySelector('#menu .contenedor-enlaces-nav'),
contenedorSubCategorias = document.querySelector('#grid .contenedor-subcategorias'),
esDispositivoMovil = () => window.innerWidth <= 800;

btnDepartamentos.addEventListener('mouseover', () => {
	if(!esDispositivoMovil()){
		grid.classList.add('activo');
	}
});

grid.addEventListener('mouseleave', () => {
	if(!esDispositivoMovil()){
		grid.classList.remove('activo');
	}
});

document.querySelectorAll('#menu .categorias a').forEach((elemento) => {
	elemento.addEventListener('mouseenter', (e) => {
		if(!esDispositivoMovil()){
			document.querySelectorAll('#menu .subcategoria').forEach((categoria) => {
				categoria.classList.remove('activo');
				if(categoria.dataset.categoria == e.target.dataset.categoria){
					categoria.classList.add('activo');
				}
			});
		};
	});
});

//EventListeners para dispositivo movil.
document.querySelector('#btn-menu-barras').addEventListener('click', (e) => {
	e.preventDefault();
	if(contenedorEnlacesNav.classList.contains('activo')){
		contenedorEnlacesNav.classList.remove('activo');
		document.querySelector('body').style.overflow = 'visible';
	} else {
		contenedorEnlacesNav.classList.add('activo');
		document.querySelector('body').style.overflow = 'hidden';
	}
});

//Click en boton de todos los departamentos (Para version movil).
btnDepartamentos.addEventListener('click', (e) => {
	e.preventDefault();
	grid.classList.add('activo');
	btnCerrarMenu.classList.add('activo');
});

//Boton de regresar en el menu de categorias
document.querySelector('#grid .categorias .btn-regresar').addEventListener('click', (e) => {
	e.preventDefault();
	grid.classList.remove('activo');
	btnCerrarMenu.classList.remove('activo');
});

document.querySelectorAll('#menu .categorias a').forEach((elemento) => {
elemento.addEventListener('click', (e) => {
	if(esDispositivoMovil()){
		contenedorSubCategorias.classList.add('activo');
		document.querySelectorAll('#menu .subcategoria').forEach((categoria) => {
			categoria.classList.remove('activo');
			if(categoria.dataset.categoria == e.target.dataset.categoria){
				categoria.classList.add('activo');
			}
		});
	}
});
});

//Boton de regresar en el menu de categorias
document.querySelectorAll('#grid .contenedor-subcategorias .btn-regresar').forEach((boton) => {
boton.addEventListener('click', (e) => {
	e.preventDefault();
	contenedorSubCategorias.classList.remove('activo');
});
});

btnCerrarMenu.addEventListener('click', (e)=> {
	e.preventDefault();
	document.querySelectorAll('#menu .activo').forEach((elemento) => {
		elemento.classList.remove('activo');
	});
	document.querySelector('body').style.overflow = 'visible';
});

/*******************************************************************************
 * KNOW
 ******************************************************************************/
var link = $('.com__nav-link');
var linkParent = link.parent('li');
var section = $('.com__section');
var sectionContent = section.find('*');

var switchTab = function() {
	var p = $(this).parent('li');
	var i  = p.index();
	var s = section.eq(i);
	var c = s.find('*');
	
	section.removeClass('active');
	sectionContent.removeAttr('style');
	s.addClass('active');
	
	c.css({
		transform: 'none',
		opacity: 1
	});
	
	linkParent.removeClass('active');
	p.addClass('active');
	
	return false;
};

link.on('click', switchTab);

function activeFirst() {
	section.first().addClass('active');
	section.first().find('*').css({
		transform: 'none',
		opacity: 1
	});
	linkParent.first().addClass('active');
}

activeFirst();
