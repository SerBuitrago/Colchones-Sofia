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
public class CatalogoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private FacesMessage message;

	private List<DetalleProducto> detalles_productos;
	private List<DetalleProducto> promocion;
	private List<Producto> productos;

	private List<DetalleProducto> detalles_productos_inicio;
	private List<DetalleProducto> detalles_productos_inicio_copia;
	private List<DetalleProducto> filtrar;
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
		this.detalles_productos_inicio_copia = null;
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
						if (Operacion.mayorZero(dp.getDescuento())) {
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
	 * 
	 * @param id representa el id del detalle producto.
	 * @return el resultado encontrado.
	 */
	public DetalleProducto consultarDetalleProducto(int id) {
		if (this.detalles_productos != null && this.detalles_productos.size() > 0 && id > 0) {
			for (DetalleProducto dp : this.detalles_productos) {
				if (dp.getId() == id) {
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
	/**
	 * 
	 */
	public void ocultarFiltro() {
		this.mostrar_resultados = false;
		this.filtrar = new ArrayList<DetalleProducto>();
	}
	
	/**
	 * 
	 */
	public void filtrar() {
		this.message = null;
		ocultarFiltro();
		if (Convertidor.isCadena(this.txt_filtrar)) {
			if (!Convertidor.containsNumber(this.txt_filtrar)) {
				this.filtrar = buscar(this.txt_filtrar);
				if (this.filtrar != null && this.filtrar.size() > 0) {
					this.mostrar_resultados = true;
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se encontro ningun resultado.");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No puede ingresar valores numerico.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No has ingresado nigun valor a filtrar.");
		}

		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	/**
	 * 
	 */
	public void filtrarPrecio() {
		this.message = null;
		if(this.a >= 0 && this.b >= 0) {
			if(this.a<=this.b) {
				this.detalles_productos_inicio_copia = this.detalles_productos_inicio;
				this.detalles_productos_inicio = new ArrayList<DetalleProducto>();
				BigInteger a_ = new BigInteger(String.valueOf(a));
				BigInteger b_ = new BigInteger(String.valueOf(b));
				for (DetalleProducto dp : this.detalles_productos) {
					int c = a_.compareTo(dp.getPrecioVenta());
					int d = b_.compareTo(dp.getPrecioVenta());
					if(c <= 0 && d >= 0) {
						this.detalles_productos_inicio.add(dp);
					}
				}
				if(this.detalles_productos_inicio.size() > 0) {
					this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Se han encontrado algunos resultados en ese rango.");
				}else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se encontro ningun resultado en ese rango.");
				}
				
			}else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El rango a debe ser menor b.");
			}
		}else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Los rangos debe ser mayor a cero");
		}
		if(this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
	}

	/**
	 * 
	 * @param txt
	 * @return
	 */
	public List<DetalleProducto> buscar(String txt) {
		List<DetalleProducto> aux = new ArrayList<DetalleProducto>();
		if (Convertidor.isCadena(txt)) {
			for (DetalleProducto dp : this.detalles_productos) {
				boolean entro = false;
				if (dp.getProductoBean() != null) {
					if (dp.getProductoBean().getCategoriaBean() != null) {
						if (dp.getProductoBean().getCategoriaBean().getNombre().contains(txt.toLowerCase())
								|| dp.getProductoBean().getCategoriaBean().getNombre().contains(txt.toUpperCase())) {
							entro = true;
						}

					}
					if (dp.getProductoBean() != null) {
						if (dp.getProductoBean().getNombre().contains(txt.toLowerCase())
								|| dp.getProductoBean().getNombre().contains(txt.toUpperCase())) {
							entro = true;
						}
					}
				}

				if (entro) {
					aux.add(dp);
				}
			}
		}
		return aux;
	}

	///////////////////////////////////////////////////////
	// Method Operation
	///////////////////////////////////////////////////////
	public BigInteger resta(BigInteger a, BigInteger b) {
		if (Operacion.mayorIgualZero(a) && Operacion.mayorIgualZero(b)) {
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

	public List<DetalleProducto> getFiltrar() {
		return filtrar;
	}

	public void setFiltrar(List<DetalleProducto> filtrar) {
		this.filtrar = filtrar;
	}

	public List<DetalleProducto> getDetalles_productos_inicio_copia() {
		return detalles_productos_inicio_copia;
	}

	public void setDetalles_productos_inicio_copia(List<DetalleProducto> detalles_productos_inicio_copia) {
		this.detalles_productos_inicio_copia = detalles_productos_inicio_copia;
	}
}
