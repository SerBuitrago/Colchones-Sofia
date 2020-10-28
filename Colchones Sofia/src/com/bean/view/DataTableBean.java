package com.bean.view;

import java.io.Serializable;
import java.util.ArrayList;
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

	private List<Vendedor> vendedor;
	private List<Vendedor> filter_vendedor;
	private int renderizar_vendedor;

	private List<Proveedor> proveedor;
	private List<Proveedor> filter_proveedor;
	private int renderizar_proveedor;

	private List<Categoria> categoria;
	private List<Categoria> filter_categoria;
	private int renderizar_categoria;

	private List<Usuario> usuarios_logeados;
	private List<Categoria> filter_usuarios_logeados;
	private int renderizar_usuarios_logeados;
	
	private List<Usuario> asistente;
	private List<Rol> filter_asistente;
	private int renderizar_asistente;
	


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
		this.renderizar_vendedor = 0;
		this.renderizar_proveedor = 0;
		this.renderizar_categoria = 0;
		this.renderizar_usuarios_logeados = 0;
		this.renderizar_asistente = 0;
	}

	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * Metodo que inicializa los valores de la tabla vendedor.
	 */
	public void initVendedor() {
		this.vendedor = vendedores();
		List<Vendedor> aux = vendedores();
		this.vendedor = new ArrayList<Vendedor>();
		for (Vendedor v : aux) {
			VentaDao dao = new VentaDao();
			v.setVentas(dao.findByFieldList("vendedor", v));
			this.vendedor.add(v);
		}
	}
	
	/**
	 * Metodo que inicializa los valores de la tabla asistente.
	 */
	/**public void initAsistente() {
		this.asistente = asistente();
		List<Usuario> aux = asistente();
		//this.asistente = new ArrayList<Usuario>();
		for (Usuario u : aux) {
			List<Usuario> usu = new ArrayList<Usuario>();
			UsuarioDao dao = new UsuarioDao();
			usu = dao.rolAdd();
			aux.add(u);
		}
	}*/
	
	/**public void initAsistente() {
		this.asistente = asistente();
		List<Usuario> aux = new ArrayList<Usuario>();
		for (Usuario u : this.asistente) {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			UsuarioDao dao = new UsuarioDao();
			usuarios = dao.rolAdd();
			u.setProveedorProductos(usuarios);
			aux.add(u);
		}*/
	
	
	public void initAsistente() {
		
		UsuarioDao dao = new UsuarioDao();
		this.asistente = dao.consultarAsistente();
	}
	
	

	/**
	 * Metodo que inicializa los valores de la tabla proveedor.
	 */
	public void initProveedor() {
		this.proveedor = proveedores();
		List<Proveedor> aux = new ArrayList<Proveedor>();
		for (Proveedor p : this.proveedor) {
			List<ProveedorProducto> productos = new ArrayList<ProveedorProducto>();
			ProveedorProductoDao dao = new ProveedorProductoDao();
			productos = dao.findByFieldList("proveedor", p);
			p.setProveedorProductos(productos);
			aux.add(p);
		}
		this.proveedor = aux;
	}

	/**
	 * Metodo que inicializa los valores de la tabla categoria.
	 */
	public void initCategoria() {
		List<Categoria> aux = categorias();
		this.categoria = new ArrayList<Categoria>();
		for (Categoria c : aux) {
			ProductoDao dao = new ProductoDao();
			c.setProductos(dao.findByFieldList("categoria", c));
			this.categoria.add(c);
		}
	}

	/**
	 * Metodo que inicializa los valores de la tabla usuarios logeados.
	 */
	public void initUsuariosLogeados() {
		UsuarioDao dao = new UsuarioDao();
		this.usuarios_logeados = dao.list();
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que lista todas los vendedores.
	 * 
	 * @return una lista con los vendedores.
	 */
	public List<Vendedor> vendedores() {
		VendedorDao dao = new VendedorDao();
		return dao.list();
	}

	/**
	 * Metodo que obtine todos los proveedores.
	 * 
	 * @return una lista con todos los proveedores.
	 */
	public List<Proveedor> proveedores() {
		ProveedorDao dao = new ProveedorDao();
		return dao.list();
	}

	/**
	 * Metodo que lista todas las categorias.
	 * 
	 * @return una lista con las categorias.
	 */
	public List<Categoria> categorias() {
		CategoriaDao dao = new CategoriaDao();
		return dao.list();
	}
	
	/**public List<Usuario> asistente(){
	UsuarioDao dao = new UsuarioDao();
	return dao.list();
	}*/
	

	///////////////////////////////////////////////////////
	// Renderizar
	///////////////////////////////////////////////////////
	/**
	 * Renderizando la tabla vendedor.
	 * 
	 * @return una lista nueva.
	 */
	public List<Vendedor> getVendedor() {
		if (this.renderizar_vendedor == 0) {
			initVendedor();
			this.renderizar_vendedor = 1;
		}
		return vendedor;
	}

	/**
	 * Renderizando la tabla proveedor.
	 * 
	 * @return una lista nueva.
	 */
	public List<Proveedor> getProveedor() {
		if (this.renderizar_proveedor == 0) {
			initProveedor();
			this.renderizar_proveedor = 1;
		}
		return proveedor;
	}

	/**
	 * Renderizando la tabla categoria.
	 * 
	 * @return una lista nueva.
	 */
	public List<Categoria> getCategoria() {
		if (this.renderizar_categoria == 0) {
			initCategoria();
			this.renderizar_categoria = 1;
		}
		return categoria;
	}

	/**
	 * Renderizando la tabla usuario logeado.
	 * 
	 * @return una lista nueva.
	 */
	public List<Usuario> getUsuarios_logeados() {
		if (this.renderizar_usuarios_logeados == 0) {
			initUsuariosLogeados();
			this.renderizar_usuarios_logeados = 1;
		}
		return usuarios_logeados;
	}
	
	public List<Usuario> getAsistente() {
		if (this.renderizar_asistente == 0) {
			initAsistente();
			this.renderizar_asistente = 1;
		}
		return asistente;
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////
	public List<Vendedor> getFilter_vendedor() {
		return filter_vendedor;
	}

	public void setFilter_vendedor(List<Vendedor> filter_vendedor) {
		this.filter_vendedor = filter_vendedor;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setVendedor(List<Vendedor> vendedor) {
		this.vendedor = vendedor;
	}

	public int getRenderizar_vendedor() {
		return renderizar_vendedor;
	}

	public void setRenderizar_vendedor(int renderizar_vendedor) {
		this.renderizar_vendedor = renderizar_vendedor;
	}

	public void setProveedor(List<Proveedor> proveedor) {
		this.proveedor = proveedor;
	}

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

	public void setCategoria(List<Categoria> categoria) {
		this.categoria = categoria;
	}

	public List<Categoria> getFilter_categoria() {
		return filter_categoria;
	}

	public void setFilter_categoria(List<Categoria> filter_categoria) {
		this.filter_categoria = filter_categoria;
	}

	public int getRenderizar_categoria() {
		return renderizar_categoria;
	}

	public void setRenderizar_categoria(int renderizar_categoria) {
		this.renderizar_categoria = renderizar_categoria;
	}

	public void setUsuarios_logeados(List<Usuario> usuarios_logeados) {
		this.usuarios_logeados = usuarios_logeados;
	}

	public List<Categoria> getFilter_usuarios_logeados() {
		return filter_usuarios_logeados;
	}

	public void setFilter_usuarios_logeados(List<Categoria> filter_usuarios_logeados) {
		this.filter_usuarios_logeados = filter_usuarios_logeados;
	}

	public int getRenderizar_usuarios_logeados() {
		return renderizar_usuarios_logeados;
	}

	public void setRenderizar_usuarios_logeados(int renderizar_usuarios_logeados) {
		this.renderizar_usuarios_logeados = renderizar_usuarios_logeados;
	}

	public List<Rol> getFilter_asistente() {
		return filter_asistente;
	}

	public void setFilter_asistente(List<Rol> filter_asistente) {
		this.filter_asistente = filter_asistente;
	}

	public int getRenderizar_asistente() {
		return renderizar_asistente;
	}

	public void setRenderizar_asistente(int renderizar_asistente) {
		this.renderizar_asistente = renderizar_asistente;
	}

	public void setAsistente(List<Usuario> asistente) {
		this.asistente = asistente;
	}
	
	
}
