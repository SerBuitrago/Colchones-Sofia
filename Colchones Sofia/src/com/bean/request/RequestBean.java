package com.bean.request;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.dao.*;
import com.entity.*;
import com.entity.other.ChartJS;
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
	// Renderizar
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer el presupuesto de ventas realizadas.
	 * 
	 * @return representa la cantidad obtenida.
	 */
	public BigInteger getPresupuesto_ventas() {
		VentaDao dao = new VentaDao();
		Fecha fecha= new Fecha();
		ChartJS aux= dao.ventasMensual(fecha.mesActualCadena(), String.valueOf(fecha.anioActual()));
		presupuesto_ventas = BigInteger.ZERO;
		if(aux!= null) {
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
		CompraDao dao = new CompraDao();
		List<Compra> ventas = dao.list();
		this.presupuesto_ventas = this.presupuesto_ventas.add(BigInteger.valueOf(0));
		for (Compra compra : ventas) {
			this.presupuesto_ventas = this.presupuesto_ventas.add(compra.getTotal());
		}
		return presupuesto_compras;
	}

	/**
	 * Metodo que permite conocer la cantidad de clientes registrados.
	 * 
	 * @return representa la cantidad obtenida.
	 */
	public int getCantidad_clientes() {
		ClienteDao dao = new ClienteDao();
		this.cantidad_clientes = dao.findByFieldList("estado", true).size();
		return cantidad_clientes;
	}

	/**
	 * Metodo que permite conocer la cantidad de proveedores registrados.
	 * 
	 * @return representa la cantidad obtenida.
	 */
	public int getCantidad_proveedores() {
		ProveedorDao dao = new ProveedorDao();
		this.cantidad_proveedores = dao.findByFieldList("estado", true).size();
		return cantidad_proveedores;
	}

	/**
	 * Metodo que permite conocer la cantidad de cajeros registrados.
	 * 
	 * @return representa la cantidad obtenida.
	 */
	public int getCantidad_asistente_administrativo() {
		UsuarioDao dao = new UsuarioDao();
		List<Usuario> usuarios = dao.findByFieldList("estado", true);
		this.cantidad_asistente_administrativo = 0;
		for (Usuario u : usuarios) {
			if (u.getRol().getNombre().equals("Asistente Administrativo")) {
				cantidad_asistente_administrativo++;
			}
		}
		return cantidad_asistente_administrativo;
	}

	/**
	 * Metodo que permite conocer la cantidad de vendedores registrados.
	 * 
	 * @return representa la cantidad obtenida.
	 */
	public int getCantidad_vendedores() {
		VendedorDao dao = new VendedorDao();
		this.cantidad_vendedores = dao.findByFieldList("estado", true).size();
		return cantidad_vendedores;
	}

	/**
	 * Metodo que permite conocer la cantidad de productos registrados.
	 * 
	 * @return representa la cantidad obtenida.
	 */
	public int getCantidad_productos() {
		ProductoDao dao = new ProductoDao();
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
