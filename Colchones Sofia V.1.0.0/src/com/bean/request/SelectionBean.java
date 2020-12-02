package com.bean.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import com.controller.*;
import com.model.*;

/**
 * Implementation SelectionBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "selection")
@RequestScoped
public class SelectionBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<SelectItem> roles;
	private List<SelectItem> detalles_productos;
	private List<SelectItem> documentos;
	private List<SelectItem> personas;
	private List<SelectItem> detalles_ventas;
	private List<SelectItem> categorias;
	private List<SelectItem> categoria;
	private List<SelectItem> tipos_pagos;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public SelectionBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		this.roles = null;
		this.documentos = null;
		this.personas = null;
		this.detalles_productos = null;
		this.detalles_ventas = null;
		this.categorias = null;
		this.tipos_pagos = null;
		this.categoria = null;
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite traer todos los tipos de roles de la empresa.
	 * 
	 * @return una lista con los roles de la empresa.
	 */
	public List<SelectItem> getRoles() {
		this.roles = new ArrayList<SelectItem>();
		RolController dao = new RolController();
		List<Rol> roles = dao.rolesLogin();
		SelectItemGroup s = new SelectItemGroup("No se encontro ningun tipo usuario.");
		if (roles != null && roles.size() > 0) {
			s = new SelectItemGroup("Tipo Usuario");
			SelectItem[] items = new SelectItem[roles.size()];
			for (int i = 0; i < roles.size(); i++) {
				items[i] = new SelectItem(String.valueOf(roles.get(i).getRol()), String.valueOf(roles.get(i).getRol()));
			}
			s.setSelectItems(items);
		}
		this.roles.add(s);
		return this.roles;
	}

	/**
	 * Metodo que permita traer todas las categorias.
	 * 
	 * @return una lista con las categorias y sus productos.
	 */
	public List<SelectItem> getCategorias() {
		this.categorias = new ArrayList<SelectItem>();
		CategoriaController dao = new CategoriaController();
		List<Categoria> list = dao.findByFieldList("estado", true);
		for (Categoria categoria : list) {
			ProductoController pDao = new ProductoController();
			categoria.setProductos(pDao.findByFieldList("categoriaBean", categoria));
			for (Producto p : categoria.getProductos()) {
				if (p != null) {
					SelectItemGroup select = new SelectItemGroup(categoria.getNombre());
					DetalleProductoController dpDao = new DetalleProductoController();
					List<DetalleProducto> dpList = dpDao.findByFieldList("productoBean", p);
					SelectItem[] items = new SelectItem[dpList.size()];
					int i = 0;
					for (DetalleProducto dp : dpList) {
						items[i] = new SelectItem(String.valueOf(dp.getId()),
								String.valueOf(p.getNombre()) + " - ID Detalle: " + String.valueOf(dp.getId()));
						i++;
					}
					select.setSelectItems(items);
					this.categorias.add(select);
				}
			}
		}
		return categorias;
	}

	/**
	 * Metodo que permita traer todas las categorias.
	 * 
	 * @return una lista con las categorias y sus productos.
	 */
	public List<SelectItem> getDetalles_productos() {
		this.detalles_productos = new ArrayList<SelectItem>();
		CategoriaController dao = new CategoriaController();
		List<Categoria> list = dao.findByFieldList("estado", true);
		for (Categoria categoria : list) {
			ProductoController pDao = new ProductoController();
			categoria.setProductos(pDao.findByFieldList("categoriaBean", categoria));
			for (Producto p : categoria.getProductos()) {
				if (p != null) {
					SelectItemGroup select = new SelectItemGroup(categoria.getNombre());
					DetalleProductoController dpDao = new DetalleProductoController();
					List<DetalleProducto> dpList = dpDao.findByFieldList("productoBean", p);
					SelectItem[] items = new SelectItem[dpList.size()];
					int i = 0;
					for (DetalleProducto dp : dpList) {
						items[i] = new SelectItem(String.valueOf(dp.getId()),
								p.getNombre() + " - ID Detalle: " + String.valueOf(dp.getId()));
						i++;
					}
					select.setSelectItems(items);
					this.detalles_productos.add(select);
				}
			}
		}
		return detalles_productos;
	}

	/**
	 * Metodo que permita traer todas los tipos documentos.
	 * 
	 * @return una lista con los tipos documentos.
	 */
	public List<SelectItem> getDocumentos() {
		TipoDocumentoController dao = new TipoDocumentoController();
		List<TipoDocumento> p = dao.findByFieldList("estado", true);
		SelectItemGroup g1 = new SelectItemGroup("Tipo Documento");
		SelectItem[] items = new SelectItem[p.size()];
		for (int i = 0; i < p.size(); i++) {
			TipoDocumento tipo = p.get(i);
			items[i] = new SelectItem(tipo.getTipoDocumento(), tipo.getTipoDocumento());
		}
		this.documentos = new ArrayList<SelectItem>();
		g1.setSelectItems(items);
		this.documentos.add(g1);
		return documentos;
	}

	/**
	 * Metodo que permita traer todas las personas registradas en el sistema.
	 * 
	 * @return una lista con las personas.
	 */
	public List<SelectItem> getPersonas() {
		this.personas = new ArrayList<SelectItem>();
		PersonaController dao = new PersonaController();
		List<Persona> p = dao.list();
		SelectItemGroup g1 = new SelectItemGroup("Personas");
		SelectItem[] items = new SelectItem[p.size()];
		for (int i = 0; i < p.size(); i++) {
			Persona persona = p.get(i);
			items[i] = new SelectItem(persona.getDocumento(), persona.getDocumento() + " - " + persona.getNombre());
		}
		g1.setSelectItems(items);
		this.personas.add(g1);
		return personas;
	}

	/**
	 * Metodo que permita traer todas los detalles de ventas activos.
	 * 
	 * @return una lista con los detalles de ventas.
	 */
	public List<SelectItem> getDetalles_ventas() {
		this.detalles_ventas = new ArrayList<SelectItem>();
		VentaController dao = new VentaController();
		List<Venta> list = dao.findByFieldList("estado", true);
		for (Venta v : list) {
			SelectItemGroup g1 = new SelectItemGroup("Venta ID " + v.getId());
			if (v.getDetalleCompraVentas() != null) {
				SelectItem[] items = new SelectItem[v.getDetalleCompraVentas().size()];
				int index = 0;
				for (DetalleCompraVenta dp : v.getDetalleCompraVentas()) {
					items[index] = new SelectItem(String.valueOf(dp.getId()), String.valueOf(dp.getId())
							+ " - ID Detalle Producto: " + String.valueOf(dp.getDetalleProducto().getId()));
					index++;
				}
				g1.setSelectItems(items);
			}
			this.detalles_ventas.add(g1);
		}
		return detalles_ventas;
	}

	/**
	 * Metodo que permita traer todos los tipos de pagos activos.
	 * 
	 * @return una lista con los tipos pagos.
	 */
	public List<SelectItem> getTipos_pagos() {
		this.tipos_pagos = new ArrayList<SelectItem>();
		TipoPagoController dao = new TipoPagoController();
		List<TipoPago> list = dao.findByFieldList("estado", true);
		int i = 0;
		for (TipoPago tipo : list) {
			List<MetodoPago> metodos = tipo.getMetodoPagos();
			SelectItemGroup select = new SelectItemGroup(tipo.getNombre());
			SelectItem[] items = new SelectItem[metodos.size()];
			i = 0;
			for (MetodoPago metodo : metodos) {
				items[i] = new SelectItem(String.valueOf(metodo.getBanco()), metodo.getBanco());
				i++;
			}
			select.setSelectItems(items);
			this.tipos_pagos.add(select);
		}
		return tipos_pagos;
	}

	/**
	 * Metodo que permita traer todas las categorias.
	 * 
	 * @return una lista con las categorias.
	 */
	public List<SelectItem> getCategoria() {
		this.categoria = new ArrayList<SelectItem>();
		CategoriaController dao = new CategoriaController();
		List<Categoria> list = dao.findByFieldList("estado", true);
		SelectItemGroup select = new SelectItemGroup("Categoria");
		int i = 0;
		SelectItem[] items = new SelectItem[list.size()];
		for (Categoria categoria : list) {
			items[i] = new SelectItem(categoria.getId(), categoria.getNombre());
			i++;
		}
		select.setSelectItems(items);
		this.categoria.add(select);
		return categoria;
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setRoles(List<SelectItem> roles) {
		this.roles = roles;
	}

	public void setDetalles_productos(List<SelectItem> detalles_productos) {
		this.detalles_productos = detalles_productos;
	}

	public void setDocumentos(List<SelectItem> documentos) {
		this.documentos = documentos;
	}

	public void setPersonas(List<SelectItem> personas) {
		this.personas = personas;
	}

	public void setDetalles_ventas(List<SelectItem> detalles_ventas) {
		this.detalles_ventas = detalles_ventas;
	}

	public void setCategorias(List<SelectItem> categorias) {
		this.categorias = categorias;
	}

	public void setTipos_pagos(List<SelectItem> tipos_pagos) {
		this.tipos_pagos = tipos_pagos;
	}

	public void setCategoria(List<SelectItem> categoria) {
		this.categoria = categoria;
	}
}
