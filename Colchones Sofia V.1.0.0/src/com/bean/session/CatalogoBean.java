package com.bean.session;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.controller.*;
import com.model.*;
import com.model.other.*;

/**
 * Implementation CatalogoBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "catalogo")
@SessionScoped
public class CatalogoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private FacesMessage message;
	
	private List<DetalleProducto> detalles_productos;
	private List<DetalleProducto> promocion;
	private List<Producto> productos;
	
	private List<DetalleProducto> detalles_productos_inicio;
	private String txt_filtrar;
	private boolean mostrar_resultados;
	
	private int a, b;
	
	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public CatalogoBean() {	
		this.detalles_productos = null;
		this.productos = null;
	}
	
	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		initProducto();
		this.mostrar_resultados = false;
	}
	
	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite inicializar los productos.
	 */
	public void initProducto() {
		this.detalles_productos = new ArrayList<DetalleProducto>();
		this.productos = new ArrayList<Producto>();
		this.promocion = new ArrayList<DetalleProducto>();
		ProductoController dao = new ProductoController();
		List<Producto> list = dao.findByFieldList("estado", true);
		for (Producto p : list) {
			if (p.getStock() > 0) {
				this.productos.add(p);
				DetalleProductoController dpDao = new DetalleProductoController();
				List<DetalleProducto> list2 = dpDao.findByFieldList("productoBean", p);
				for (DetalleProducto dp : list2) { 
					if (dp.getStock() > 0) {
						detalles_productos.add(dp); 
						if(Operacion.mayorZero(dp.getDescuento())) {
							this.promocion.add(dp);
						}
						
					}
				}
			}
		}
		this.detalles_productos_inicio = this.detalles_productos;
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite buscar un detalle de producto en la lista.
	 * @param id representa el id del detalle producto.
	 * @return el resultado encontrado.
	 */
	public DetalleProducto consultarDetalleProducto(int id) {
		if(this.detalles_productos != null  && this.detalles_productos.size() > 0 && id > 0) {
			for(DetalleProducto dp : this.detalles_productos) {
				if(dp.getId() == id) {
					return dp;
				}
			}
		}
		return null;
	}
	
	///////////////////////////////////////////////////////
	// Method Promotion
	///////////////////////////////////////////////////////
	/**
	 * 
	 */
	public void recalcularPromocion() {
		DetalleProductoController dp = new DetalleProductoController();
		this.promocion = dp.promocion();
	}
	
	
	///////////////////////////////////////////////////////
	// Method Filter
	///////////////////////////////////////////////////////
	public void filtrar() {
		this.message = null;
		this.mostrar_resultados= false;
		this.detalles_productos_inicio = new ArrayList<DetalleProducto>();
		if(Convertidor.isCadena(this.txt_filtrar)) {
			if(!Convertidor.containsNumber(this.txt_filtrar)) {
				// LOGICA
				if(this.detalles_productos_inicio != null && this.detalles_productos_inicio.size() > 0) {
					this.mostrar_resultados= true;
				}else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se encontro ningun resultado.");
				}
			}else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No puede ingresar valores numerico.");	
			}
		}else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No has ingresado nigun valor a filtrar.");
		}

		
		if(this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}	
	}
	
	///////////////////////////////////////////////////////
	// Method Operation
	///////////////////////////////////////////////////////
	public BigInteger resta(BigInteger a, BigInteger b) {
		if(Operacion.mayorIgualZero(a) && Operacion.mayorIgualZero(b)) {
			return a.subtract(b);
		}
		return BigInteger.ZERO;
	}
	
	
	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTxt_filtrar() {
		return txt_filtrar;
	}

	public void setTxt_filtrar(String txt_filtrar) {
		this.txt_filtrar = txt_filtrar;
	}

	public List<DetalleProducto> getDetalles_productos() {
		return detalles_productos;
	}
	
	public void setDetalles_productos(List<DetalleProducto> detalles_productos) {
		this.detalles_productos = detalles_productos;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public boolean isMostrar_resultados() {
		return mostrar_resultados;
	}

	public void setMostrar_resultados(boolean mostrar_resultados) {
		this.mostrar_resultados = mostrar_resultados;
	}

	public List<DetalleProducto> getDetalles_productos_inicio() {
		return detalles_productos_inicio;
	}

	public void setDetalles_productos_inicio(List<DetalleProducto> detalles_productos_inicio) {
		this.detalles_productos_inicio = detalles_productos_inicio;
	}

	public FacesMessage getMessage() {
		return message;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public List<DetalleProducto> getPromocion() {
		return promocion;
	}

	public void setPromocion(List<DetalleProducto> promocion) {
		this.promocion = promocion;
	}
}
