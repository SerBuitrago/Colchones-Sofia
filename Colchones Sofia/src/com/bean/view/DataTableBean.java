package com.bean.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.entity.*;
import com.dao.*;

/**
 * Implementation DataTableBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "table")
@ViewScoped
public class DataTableBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Proveedor> proveedor;
	private List<Proveedor> filter_proveedor;
	private int renderizar_proveedor;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public DataTableBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		this.renderizar_proveedor = 0;
	}
	
	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * 
	 */
	public void initProveedor() {
		this.renderizar_proveedor = 0;
		this.getProveedor();
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que obtine todos los proveedores.
	 * 
	 * @return una lista con todos los proveedores.
	 */
	public List<Proveedor> proveedores() {
		ProveedorDao dao = new ProveedorDao();
		return dao.list();
	}

	///////////////////////////////////////////////////////
	// Renderizar
	///////////////////////////////////////////////////////
	/**
	 * Renderizando la tabla proveedor.
	 * 
	 * @return una lista nueva.
	 */
	public List<Proveedor> getProveedor() {
		if (this.renderizar_proveedor == 0) {
			this.proveedor = proveedores();
			this.renderizar_proveedor = 1;
		}
		return proveedor;
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////
	public List<Proveedor> getFilter_proveedor() {
		return filter_proveedor;
	}

	public void setFilter_proveedor(List<Proveedor> filter_proveedor) {
		this.filter_proveedor = filter_proveedor;
	}

	public int getRenderizar_proveedor() {
		return renderizar_proveedor;
	}

	public void setRenderizar_proveedor(int renderizar_proveedor) {
		this.renderizar_proveedor = renderizar_proveedor;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setProveedor(List<Proveedor> proveedor) {
		this.proveedor = proveedor;
	}
}