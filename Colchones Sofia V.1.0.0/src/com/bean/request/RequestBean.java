package com.bean.request;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.controller.*;
import com.model.other.*;
import com.util.Fecha;

/**
 * Implementation RequestBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "requests")
@RequestScoped
public class RequestBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigInteger presupuesto_ventas;
	private BigInteger presupuesto_compras;

	private int cantidad_clientes;
	private int cantidad_proveedores;
	private int cantidad_asistente_administrativo;
	private int cantidad_vendedores;
	private int cantidad_productos;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public RequestBean() {

	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		this.presupuesto_compras = new BigInteger("0");
		this.presupuesto_ventas = new BigInteger("0");
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * 
	 * @return
	 */
	public List<ChartJS> colores(){
		DetalleProductoController dao = new DetalleProductoController();
		return dao.colores();
	}

	///////////////////////////////////////////////////////
	// Renderizar
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer el presupuesto de ventas realizadas.
	 * 
	 * @return representa la cantidad obtenida.
	 */
	public BigInteger getPresupuesto_ventas() {
		VentaController dao = new VentaController();
		Fecha fecha = new Fecha();
		ChartJS aux = dao.ventasMensual(fecha.mesActualCadena(), String.valueOf(fecha.anioActual()));
		presupuesto_ventas = BigInteger.ZERO;
		if (aux != null) {
			presupuesto_ventas = aux.getTotal();
		}
		return presupuesto_ventas;
	}

	/**
	 * Metodo que permite conocer el presupuesto de compras realizadas.
	 * 
	 * @return representa la cantidad obtenida.
	 */
	public BigInteger getPresupuesto_compras() {
		CompraController dao = new CompraController();
		Fecha fecha = new Fecha();
		ChartJS aux = dao.comprasMensual(fecha.mesActualCadena(), String.valueOf(fecha.anioActual()));
		this.presupuesto_compras = BigInteger.ZERO;
		if (aux != null) {
			this.presupuesto_compras = aux.getTotal();
		}
		return presupuesto_compras;
	}

	/**
	 * Metodo que permite conocer la cantidad de clientes registrados.
	 * 
	 * @return representa la cantidad obtenida.
	 */
	public int getCantidad_clientes() {
		UsuarioController dao = new UsuarioController();
		this.cantidad_clientes = dao.countRol("CLIENTE", (byte) 0).size();
		return this.cantidad_clientes;
	}

	/**
	 * Metodo que permite conocer la cantidad de proveedores registrados.
	 * 
	 * @return representa la cantidad obtenida.
	 */
	public int getCantidad_proveedores() {
		ProveedorController dao = new ProveedorController();
		this.cantidad_proveedores = dao.findByFieldList("estado", true).size();
		return cantidad_proveedores;
	}

	/**
	 * Metodo que permite conocer la cantidad de cajeros registrados.
	 * 
	 * @return representa la cantidad obtenida.
	 */
	public int getCantidad_asistente_administrativo() {
		UsuarioController dao = new UsuarioController();
		this.cantidad_asistente_administrativo = dao.countRol("ASISTENTE ADMINISTRATIVO", (byte) 0).size();
		return this.cantidad_asistente_administrativo;
	}

	/**
	 * Metodo que permite conocer la cantidad de vendedores registrados.
	 * 
	 * @return representa la cantidad obtenida.
	 */
	public int getCantidad_vendedores() {
		UsuarioController dao = new UsuarioController();
		this.cantidad_vendedores = dao.countRol("VENDEDOR", (byte) 0).size();
		return cantidad_vendedores;
	}

	/**
	 * Metodo que permite conocer la cantidad de productos registrados.
	 * 
	 * @return representa la cantidad obtenida.
	 */
	public int getCantidad_productos() {
		ProductoController dao = new ProductoController();
		this.cantidad_productos = dao.findByFieldList("estado", true).size();
		return cantidad_productos;
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public void setPresupuesto_ventas(BigInteger presupuesto_ventas) {
		this.presupuesto_ventas = presupuesto_ventas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setPresupuesto_compras(BigInteger presupuesto_compras) {
		this.presupuesto_compras = presupuesto_compras;
	}

	public void setCantidad_clientes(int cantidad_clientes) {
		this.cantidad_clientes = cantidad_clientes;
	}

	public void setCantidad_proveedores(int cantidad_proveedores) {
		this.cantidad_proveedores = cantidad_proveedores;
	}

	public void setCantidad_asistente_administrativo(int cantidad_asistente_administrativo) {
		this.cantidad_asistente_administrativo = cantidad_asistente_administrativo;
	}

	public void setCantidad_vendedores(int cantidad_vendedores) {
		this.cantidad_vendedores = cantidad_vendedores;
	}

	public void setCantidad_productos(int cantidad_productos) {
		this.cantidad_productos = cantidad_productos;
	}
}
