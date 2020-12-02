package com.bean.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.util.Fecha;
import com.bean.session.*;
import com.controller.*;
import com.model.*;

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

	private List<Usuario> usuarios_logeados;
	private List<Usuario> filter_usuarios_logeados;
	private int renderizar_usuarios_logeados;

	private List<Categoria> categoria;
	private List<Categoria> filter_categoria;
	private int renderizar_categoria;

	private List<Usuario> vendedores;
	private List<Usuario> vendedores_filtrar;
	private int renderizar_vendedores;

	private List<Usuario> asistente;
	private List<Usuario> filter_asistente;
	private int renderizar_asistente;

	private List<Compra> comprar;
	private List<Compra> filter_comprar;
	private int renderizar_comprar;

	private List<Venta> ventas;
	private List<Venta> filter_ventas;
	private int renderizar_ventas;
	
	private List<Venta> venta_mensual;
	private List<Venta> filter_venta_mensual;
	private int renderizar_venta_mensual;

	private List<Usuario> clientes;
	private List<Usuario> filter_cliente;
	private int renderizar_clientes;
	
	List<Garantia> garantia;
	private List<Garantia> filter_garantia;
	private int renderizar_garantia;
	
	private List<Producto> producto;
	private List<Producto> filter_producto;
	private int renderizar_producto;
	
	///////////////////////////////////////////////////////
	// ManagedBean
	///////////////////////////////////////////////////////
	@ManagedProperty("#{venta}")
	private VentaBean venta;
	
	@ManagedProperty("#{compra}")
	private CompraBean compra;

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
		this.renderizar_usuarios_logeados = 0;
		this.renderizar_categoria = 0;
		this.renderizar_vendedores = 0;
		this.renderizar_asistente = 0;
		this.renderizar_comprar = 0;
		this.renderizar_clientes = 0;
		this.renderizar_ventas = 0;
		this.renderizar_garantia = 0;
		this.renderizar_venta_mensual = 0;
		this.renderizar_producto = 0;
	}

	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite inicializar los proveedores.
	 */
	public void initProveedor() {
		this.renderizar_proveedor = 0;
		this.getProveedor();
	}

	/**
	 * Metodo que inicializa los valores de la tabla usuarios.
	 */
	public void initUsuariosLogeados() {
		UsuarioController dao = new UsuarioController();
		this.usuarios_logeados = dao.usuariosPermisosLogin();
	}

	/**
	 * Metodo que inicializa los valores de la tabla categoria.
	 */
	public void initCategoria() {
		List<Categoria> aux = categorias();
		this.categoria = new ArrayList<Categoria>();
		for (Categoria c : aux) {
			ProductoController dao = new ProductoController();
			c.setProductos(dao.findByFieldList("categoriaBean", c));
			this.categoria.add(c);
		}
	}

	/**
	 * Metodo que permite inicializar los vendedores con sus ventas.
	 */
	public void initVendedor() {
		List<Usuario> aux = vendedores();
		this.vendedores = new ArrayList<Usuario>();
		for (Usuario v : aux) {
			VentaController dao = new VentaController();
			v.setVentas1(dao.findByFieldList("usuario2", v));
			this.vendedores.add(v);
		}
	}

	/**
	 * Metodo que permite inicializar los asistentes.
	 */
	public void initAsistente() {
		UsuarioController dao = new UsuarioController();
		List<Usuario> list = dao.roles("ASISTENTE ADMINISTRATIVO");
		this.asistente = new ArrayList<Usuario>();
		for (Usuario u : list) {
			PersonaController pDao = new PersonaController();
			u.setPersona(pDao.find(u.getPersona().getDocumento()));
			this.asistente.add(u);
		}
	}

	/**
	 * Metodo que permite inicializar los clientes.
	 */
	public void initCliente() {
		this.clientes = clientes();
	}

	/**
	 * Metodo que permite inicializar las compras.
	 */
	public void initCompra() {
		CompraController dao = new CompraController();
		this.comprar = dao.list();
	}

	/**
	 * Metodo que permite inicializar las ventas.
	 */
	public void initVenta() {
		VentaController dao = new VentaController();
		this.ventas = dao.list();
	}
	
	/**
	 * Metodo que permite conocer las ventas mensual.
	 */
	public void initVentaMensual() {
		VentaController dao= new VentaController();
		Fecha fecha = new Fecha();
		List<Venta> aux = dao.ventaMensual(fecha.mesActualCadena(), String.valueOf(fecha.anioActual()));
		this.venta_mensual = new ArrayList<Venta>();
		for(Venta v: aux) {
			this.venta_mensual.add(v);
		}
	}
	
	/**
	 * Metodo que permite inicializar las garantias.
	 */
	public void initGarantia() {
		this.garantia = garantias();
	}
	
	/**
	 * Metodo que permite inicializar los productos.
	 */
	public void initProducto() {
		this.renderizar_producto = 0;
		this.getProducto();
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
		ProveedorController dao = new ProveedorController();
		return dao.list();
	}

	/**
	 * Metodo que lista todas las categorias.
	 * 
	 * @return una lista con las categorias.
	 */
	public List<Categoria> categorias() {
		CategoriaController dao = new CategoriaController();
		return dao.list();
	}

	/**
	 * Metodo que lista todas los vendedores.
	 * 
	 * @return una lista con los vendedores.
	 */
	public List<Usuario> vendedores() {
		UsuarioController dao = new UsuarioController();
		return dao.roles("VENDEDOR");
	}

	/**
	 * Metodo que lista todas los clientes.
	 * 
	 * @return una lista con los clientes.
	 */
	public List<Usuario> clientes() {
		UsuarioController dao = new UsuarioController();
		return dao.roles("CLIENTE");
	}
	
	/**
	 * Metodo que obtine todos los proveedores.
	 * 
	 * @return una lista con todos los proveedores.
	 */
	public List<Garantia> garantias() {
		GarantiaController dao = new GarantiaController();
		return dao.list();
	}
	
	/**
	 * Metodo que obtine todos los productos.
	 * 
	 * @return una lista con todos los productos.
	 */
	
	public List<Producto> producto() {
		ProductoController dao = new ProductoController();
		return dao.list();
	}
	
	
	///////////////////////////////////////////////////////
	// Statu
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite cambiar estado venta.
	 */
	public void estadoVenta() {
		this.venta.estadoVenta();
		FacesMessage message = venta.getMessage();
		if(message != null) {
			initVenta();
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	/**
	 * Metodo que permite cambiar estado venta.
	 */
	public void estadoCompra() {
		this.compra.estadoCompra();
		FacesMessage message = compra.getMessage();
		if(message != null) {
			initCompra();
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
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
	 * Renderizando la tabla vendedores.
	 * 
	 * @return una lista nueva.
	 */
	public List<Usuario> getVendedores() {
		if (renderizar_vendedores == 0) {
			initVendedor();
			this.renderizar_vendedores = 1;

		}
		return vendedores;
	}

	/**
	 * Renderizando la tabla asistente.
	 * 
	 * @return una lista nueva.
	 */
	public List<Usuario> getAsistente() {
		if (this.renderizar_asistente == 0) {
			initAsistente();
			this.renderizar_asistente = 1;
		}
		return asistente;
	}

	/**
	 * Renderizando la tabla compra.
	 * 
	 * @return una lista nueva.
	 */
	public List<Compra> getComprar() {
		if (this.renderizar_comprar == 0) {
			initCompra();
			this.renderizar_comprar = 1;
		}
		return comprar;
	}

	/**
	 * Renderizando la tabla compra.
	 * 
	 * @return una lista nueva.
	 */
	public List<Venta> getVentas() {
		if (this.renderizar_ventas == 0) {
			initVenta();
			this.renderizar_ventas = 1;
		}
		return ventas;
	}
	
	/**
	 * Renderizando la tabla venta mensual.
	 * 
	 * @return una lista nueva.
	 */
	public List<Venta> getVenta_mensual() {
		if(this.renderizar_venta_mensual == 0) {
			initVentaMensual();
			this.renderizar_venta_mensual = 1;
		}
		return venta_mensual;
	}

	/**
	 * Renderizando la tabla cliente.
	 * 
	 * @return una lista nueva.
	 */
	public List<Usuario> getClientes() {
		if (renderizar_clientes == 0) {
			initCliente();
			this.renderizar_clientes = 1;
		}
		return clientes;
	}
	
	/**
	 * Renderizando la tabla garantia.
	 * 
	 * @return una lista nueva.
	 */
	public List<Garantia> getGarantia() {
		if (this.renderizar_garantia == 0) {
			initGarantia();
			this.renderizar_garantia = 1;
		}
		return garantia;
	}
	
	/**
	 * Renderizando la tabla producto.
	 * 
	 * @return una lista nueva.
	 */
	public List<Producto> getProducto() {
		if (this.renderizar_producto == 0) {
			this.producto = producto();
			this.renderizar_producto = 1;
		}
		return producto;
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

	public List<Usuario> getFilter_usuarios_logeados() {
		return filter_usuarios_logeados;
	}

	public void setFilter_usuarios_logeados(List<Usuario> filter_usuarios_logeados) {
		this.filter_usuarios_logeados = filter_usuarios_logeados;
	}

	public int getRenderizar_usuarios_logeados() {
		return renderizar_usuarios_logeados;
	}

	public void setRenderizar_usuarios_logeados(int renderizar_usuarios_logeados) {
		this.renderizar_usuarios_logeados = renderizar_usuarios_logeados;
	}

	public void setUsuarios_logeados(List<Usuario> usuarios_logeados) {
		this.usuarios_logeados = usuarios_logeados;
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

	public void setCategoria(List<Categoria> categoria) {
		this.categoria = categoria;
	}

	public List<Usuario> getVendedores_filtrar() {
		return vendedores_filtrar;
	}

	public void setVendedores_filtrar(List<Usuario> vendedores_filtrar) {
		this.vendedores_filtrar = vendedores_filtrar;
	}

	public int getRenderizar_vendedores() {
		return renderizar_vendedores;
	}

	public void setRenderizar_vendedores(int renderizar_vendedores) {
		this.renderizar_vendedores = renderizar_vendedores;
	}

	public void setVendedores(List<Usuario> vendedores) {
		this.vendedores = vendedores;
	}

	public List<Usuario> getFilter_asistente() {
		return filter_asistente;
	}

	public void setFilter_asistente(List<Usuario> filter_asistente) {
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

	public void setComprar(List<Compra> comprar) {
		this.comprar = comprar;
	}

	public List<Compra> getFilter_comprar() {
		return filter_comprar;
	}

	public void setFilter_comprar(List<Compra> filter_comprar) {
		this.filter_comprar = filter_comprar;
	}

	public int getRenderizar_comprar() {
		return renderizar_comprar;
	}

	public void setRenderizar_comprar(int renderizar_comprar) {
		this.renderizar_comprar = renderizar_comprar;
	}

	public List<Venta> getFilter_ventas() {
		return filter_ventas;
	}

	public void setFilter_ventas(List<Venta> filter_ventas) {
		this.filter_ventas = filter_ventas;
	}

	public int getRenderizar_ventas() {
		return renderizar_ventas;
	}

	public void setRenderizar_ventas(int renderizar_ventas) {
		this.renderizar_ventas = renderizar_ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}

	public List<Usuario> getFilter_cliente() {
		return filter_cliente;
	}

	public void setFilter_cliente(List<Usuario> filter_cliente) {
		this.filter_cliente = filter_cliente;
	}

	public int getRenderizar_clientes() {
		return renderizar_clientes;
	}

	public void setRenderizar_clientes(int renderizar_clientes) {
		this.renderizar_clientes = renderizar_clientes;
	}

	public void setClientes(List<Usuario> clientes) {
		this.clientes = clientes;
	}

	public List<Garantia> getFilter_garantia() {
		return filter_garantia;
	}

	public void setFilter_garantia(List<Garantia> filter_garantia) {
		this.filter_garantia = filter_garantia;
	}

	public int getRenderizar_garantia() {
		return renderizar_garantia;
	}

	public void setRenderizar_garantia(int renderizar_garantia) {
		this.renderizar_garantia = renderizar_garantia;
	}

	public void setGarantia(List<Garantia> garantia) {
		this.garantia = garantia;
	}

	public VentaBean getVenta() {
		return venta;
	}

	public void setVenta(VentaBean venta) {
		this.venta = venta;
	}

	public void setVenta_mensual(List<Venta> venta_mensual) {
		this.venta_mensual = venta_mensual;
	}

	public List<Venta> getFilter_venta_mensual() {
		return filter_venta_mensual;
	}

	public void setFilter_venta_mensual(List<Venta> filter_venta_mensual) {
		this.filter_venta_mensual = filter_venta_mensual;
	}

	public int getRenderizar_venta_mensual() {
		return renderizar_venta_mensual;
	}

	public void setRenderizar_venta_mensual(int renderizar_venta_mensual) {
		this.renderizar_venta_mensual = renderizar_venta_mensual;
	}

	public CompraBean getCompra() {
		return compra;
	}

	public void setCompra(CompraBean compra) {
		this.compra = compra;
	}

	public List<Producto> getFilter_producto() {
		return filter_producto;
	}

	public void setFilter_producto(List<Producto> filter_producto) {
		this.filter_producto = filter_producto;
	}

	public int getRenderizar_producto() {
		return renderizar_producto;
	}

	public void setRenderizar_producto(int renderizar_producto) {
		this.renderizar_producto = renderizar_producto;
	}

	public void setProducto(List<Producto> producto) {
		this.producto = producto;
	}	
}