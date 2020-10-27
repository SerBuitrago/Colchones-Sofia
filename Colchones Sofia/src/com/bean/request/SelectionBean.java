package com.bean.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import com.dao.*;
import com.entity.*;

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
	private List<SelectItem> documentos;
	private List<SelectItem> proveedores;
	private List<SelectItem> categorias;
	private List<SelectItem> personas;
	private List<SelectItem> tipos_pagos;
	private List<SelectItem> vendedores;

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
		this.proveedores = null;
		this.categorias = null;
		this.personas = null;
		this.tipos_pagos = null;
		this.vendedores = null;
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
		SelectItemGroup s = new SelectItemGroup("Tipo Usuario");
		RolDao dao = new RolDao();
		List<Rol> roles = dao.findByFieldList("estado", true);
		SelectItem[] items = new SelectItem[roles.size()];
		for (int i = 0; i < roles.size(); i++) {
			items[i] = new SelectItem(String.valueOf(roles.get(i).getNombre()),
					String.valueOf(roles.get(i).getNombre()));
		}
		s.setSelectItems(items);
		this.roles.add(s);
		return this.roles;
	}

	/**
	 * Metodo que permite traer todos los tipos de documentos de la empresa.
	 * 
	 * @return una lista con todos los tipod de documentos.
	 */
	public List<SelectItem> getDocumentos() {
		TipoDocumentoDao dao = new TipoDocumentoDao();
		List<TipoDocumento> p = dao.findByFieldList("estado", true);
		SelectItemGroup g1 = new SelectItemGroup("Tipo Documento");
		SelectItem[] items = new SelectItem[p.size()];
		for (int i = 0; i < p.size(); i++) {
			TipoDocumento tipo = p.get(i);
			items[i] = new SelectItem(String.valueOf(tipo.getNombre()), String.valueOf(tipo.getNombre()));
		}
		this.documentos = new ArrayList<SelectItem>();
		g1.setSelectItems(items);
		this.documentos.add(g1);
		return documentos;
	}

	/**
	 * Metodo que permita traer todas las categorias.
	 * 
	 * @return una lista con las categorias y sus productos.
	 */
	public List<SelectItem> getCategorias() {
		this.categorias = new ArrayList<SelectItem>();
		CategoriaDao dao = new CategoriaDao();
		List<Categoria> list = dao.findByFieldList("estado", true);
		for (Categoria categoria : list) {
			SelectItemGroup select = new SelectItemGroup(categoria.getNombre());
			SelectItem[] items = new SelectItem[categoria.getProductos().size()];
			int i = 0;
			for (Producto p : categoria.getProductos()) {
				items[i] = new SelectItem(String.valueOf(p.getIdProducto()), String.valueOf(p.getNombre()));
				i++;
			}
			select.setSelectItems(items);
			this.categorias.add(select);
		}
		return categorias;
	}

	/**
	 * Metodo que permita traer todos los proveedores.
	 * 
	 * @return una lista con los proveedores y sus productos.
	 */
	public List<SelectItem> getProveedores() {
		this.proveedores = new ArrayList<SelectItem>();
		ProveedorDao dao = new ProveedorDao();
		List<Proveedor> p = dao.findByFieldList("estado", true);
		SelectItemGroup g1 = new SelectItemGroup("Proveedores");
		SelectItem[] items = new SelectItem[p.size()];
		for (int i = 0; i < p.size(); i++) {
			Proveedor proveedor = p.get(i);
			items[i] = new SelectItem(String.valueOf(proveedor.getIdProveedor()),
					String.valueOf(proveedor.getNombre()));
		}
		g1.setSelectItems(items);
		this.proveedores.add(g1);
		return proveedores;
	}

	/**
	 * Metodo que permita traer todas las personas registradas en el sistema.
	 * 
	 * @return una lista con las personas.
	 */
	public List<SelectItem> getPersonas() {
		this.personas = new ArrayList<SelectItem>();
		PersonaDao dao = new PersonaDao();
		List<Persona> p = dao.list();
		SelectItemGroup g1 = new SelectItemGroup("Personas");
		SelectItem[] items = new SelectItem[p.size()];
		for (int i = 0; i < p.size(); i++) {
			Persona persona = p.get(i);
			items[i] = new SelectItem(String.valueOf(persona.getDocumento()),
					String.valueOf(persona.getDocumento()) + " - " + String.valueOf(persona.getNombre()));
		}
		g1.setSelectItems(items);
		this.personas.add(g1);
		return personas;
	}

	/**
	 * Metodo que permita traer todos los tipos de pagos registrados en el sistema.
	 * 
	 * @return una lista con los tipos de pagos.
	 */
	public List<SelectItem> getTipos_pagos() {
		this.tipos_pagos = new ArrayList<SelectItem>();
		TipoPagoDao dao = new TipoPagoDao();
		List<TipoPago> list = dao.findByFieldList("estado", true);
		SelectItemGroup g1 = new SelectItemGroup("Tipos Pagos");
		SelectItem[] items = new SelectItem[list.size()];
		for (int i = 0; i < list.size(); i++) {
			TipoPago tipo = list.get(i);
			items[i] = new SelectItem(tipo.getNombre(), tipo.getNombre());
		}
		g1.setSelectItems(items);
		this.tipos_pagos.add(g1);
		return tipos_pagos;
	}

	/**
	 * Metodo que permita traer todos los vendedores registrados en el sistema y
	 * este activos.
	 * 
	 * @return una lista con los vendedores.
	 */
	public List<SelectItem> getVendedores() {
		this.vendedores = new ArrayList<SelectItem>();
		VendedorDao dao = new VendedorDao();
		List<Vendedor> list = dao.findByFieldList("estado", true);
		SelectItemGroup g1 = new SelectItemGroup("Vendedores");
		SelectItem[] items = new SelectItem[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Vendedor tipo = list.get(i);
			items[i] = new SelectItem(String.valueOf(tipo.getDocumento()), String.valueOf(tipo.getDocumento() + " - "
					+ tipo.getPersona().getNombre() + " " + tipo.getPersona().getApellido()));
		}
		g1.setSelectItems(items);
		this.vendedores.add(g1);
		return vendedores;
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setCategorias(List<SelectItem> categorias) {
		this.categorias = categorias;
	}

	public void setRoles(List<SelectItem> roles) {
		this.roles = roles;
	}

	public void setDocumentos(List<SelectItem> documentos) {
		this.documentos = documentos;
	}

	public void setProveedores(List<SelectItem> proveedores) {
		this.proveedores = proveedores;
	}

	public void setPersonas(List<SelectItem> personas) {
		this.personas = personas;
	}

	public void setTipos_pagos(List<SelectItem> tipos_pagos) {
		this.tipos_pagos = tipos_pagos;
	}

	public void setVendedores(List<SelectItem> vendedores) {
		this.vendedores = vendedores;
	}
}
