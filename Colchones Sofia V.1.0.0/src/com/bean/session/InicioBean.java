package com.bean.session;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.controller.DetalleProductoController;
import com.controller.ProductoController;
import com.model.*;
import com.model.other.Convertidor;
import com.util.Face;

/**
 * Implementation AppBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "inicio")
@SessionScoped
public class InicioBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private FacesContext context;
	private ExternalContext external;
	private String url;

	private Producto producto;
	private String producto_seleccionar;
	private List<String>  producto_detalle_producto;
	
	private DetalleProducto detalle_producto;
	
	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public InicioBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		initProducto();
		initDetalleProducto();
	}

	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * Metodo que incializa el producto.
	 */
	public void initProducto() {
		this.producto = new Producto();
		this.producto.setDetalleProductos(new ArrayList<DetalleProducto>());
		this.producto.setCategoriaBean(new Categoria());
	}
	
	/**
	 * Metodo que inicializa el detalle producto.
	 */
	public void initDetalleProducto() {
		this.detalle_producto = new DetalleProducto();
	}

	/**
	 * Metodo que inicializa el contexto.
	 */
	public void initContext() {
		this.context = FacesContext.getCurrentInstance();
		this.external = context.getExternalContext();
		this.url = this.external.getRequestContextPath();
	}

	///////////////////////////////////////////////////////
	// Method View Product
	///////////////////////////////////////////////////////
	/**
	 * Metodo que verifica el id del producto.
	 */
	public void verificarProducto() {
		initContext();
		String direccionar = this.url + "/faces/index.xhtml";
		String aux = Face.get("producto");
		if (Convertidor.isCadena(aux)) {
			if (this.producto != null && Convertidor.isCadena(this.producto.getId())
					&& this.producto.getId().equals(aux)) {
				direccionar = null;
			} else {
				ProductoController dao = new ProductoController();
				this.producto_detalle_producto = null;
				this.producto = dao.find(aux);
				if (this.producto != null) {
					if(this.producto.getEstado() && this.producto.getStock() > 0) {
						direccionar = null;
						this.producto_detalle_producto = new ArrayList<String>(); 
						for(DetalleProducto dp: this.producto.getDetalleProductos()) {
							this.producto_detalle_producto.add(String.valueOf(dp.getId()));
						}
					}
				}
			}
		}

		if (Convertidor.isCadena(direccionar)) {
			try {
				external.redirect(direccionar);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	///////////////////////////////////////////////////////
	// Method View Detail Product
	///////////////////////////////////////////////////////
	/**
	 * Metodo que verifica el id del producto.
	 */
	public void detalleProducto() {
		initContext();
		String direccionar = this.url+"/faces/index.xhtml";
		String aux = Face.get("detalle-producto");
		if (Convertidor.isCadena(aux)) {
			if(Convertidor.isNumber(aux)) {
				int aux2 = Integer.parseInt(aux);
				if(aux2 > 0) {
					if(this.detalle_producto != null &&  this.detalle_producto.getId() == aux2) {
						direccionar = null;
					}else {
						DetalleProductoController dao = new DetalleProductoController();
						this.detalle_producto = dao.find(aux2);
						if (this.detalle_producto != null) {
							if(this.detalle_producto.getStock() > 0) {
								direccionar = null;
							}
						}	
					}
				}
			}
		}
		if (Convertidor.isCadena(direccionar)) {
			try {
				external.redirect(direccionar);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public FacesContext getContext() {
		return context;
	}

	public void setContext(FacesContext context) {
		this.context = context;
	}

	public ExternalContext getExternal() {
		return external;
	}

	public void setExternal(ExternalContext external) {
		this.external = external;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProducto_seleccionar() {
		return producto_seleccionar;
	}

	public void setProducto_seleccionar(String producto_seleccionar) {
		this.producto_seleccionar = producto_seleccionar;
	}

	public List<String> getProducto_detalle_producto() {
		return producto_detalle_producto;
	}

	public void setProducto_detalle_producto(List<String> producto_detalle_producto) {
		this.producto_detalle_producto = producto_detalle_producto;
	}

	public DetalleProducto getDetalle_producto() {
		return detalle_producto;
	}

	public void setDetalle_producto(DetalleProducto detalle_producto) {
		this.detalle_producto = detalle_producto;
	}
}
