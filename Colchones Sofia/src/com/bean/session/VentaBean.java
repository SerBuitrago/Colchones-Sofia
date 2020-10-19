package com.bean.session;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.entity.*;
import com.dao.*;

/**
 * Implementation VentaBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "venta")
@SessionScoped
public class VentaBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private FacesMessage message;

	private boolean hidden;
	private boolean hidden_tablas;

	private Venta venta;
	private DetalleVenta detalle_venta;

	private List<Producto> tabla_productos_seleccionados;
	private List<Producto> tabla_productos;
	private boolean renderizar_tabla_productos;

	private List<DetalleProducto> tabla_detalle_producto;
	private List<DetalleProducto> tabla_detalle_producto_seleccionados;
	private boolean renderizar_tabla_detalle_producto;

	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean sesion;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public VentaBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		this.venta = new Venta();
		this.venta.setTotal(BigInteger.ZERO);
		initCliente();
		initVenta();
	}

	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * Metodo que inicializa la venta.
	 */
	public void initVenta() {
		this.detalle_venta = new DetalleVenta();
		this.hidden = false;
		initRenderizarTablaProductos();
	}

	/**
	 * Metodo que inicializa el cliente.
	 */
	public void initCliente() {
		this.venta.setCliente(new Cliente());
		this.venta.getCliente().setPersona(new Persona());
		this.venta.getCliente().getPersona().setTipoDocumento(new TipoDocumento());
		this.venta.setTipoPago(new TipoPago());
	}

	/**
	 * Metodo que renderizar la tabla productos.
	 */
	public void initRenderizarTablaProductos() {
		this.renderizar_tabla_productos = true;
		this.renderizar_tabla_detalle_producto = false;
		this.hidden_tablas = false;
	}

	/**
	 * Metodo que renderizar la tabla detalle producto.
	 */
	public void initRederizarTablaDetalleProducto() {
		this.renderizar_tabla_productos = false;
		this.renderizar_tabla_detalle_producto = true;
		this.hidden_tablas = true;
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite filtrar la información de un cliente.
	 */
	public void filtrarCliente() {
		this.hidden = false;
		this.message = null;
		if (this.venta != null && this.venta.getCliente() != null && this.venta.getCliente().getDocumento() > 0) {
			PersonaDao dao = new PersonaDao();
			Persona aux = dao.find(this.venta.getCliente().getDocumento());
			if (aux != null) {
				ClienteDao cDao = new ClienteDao();
				Cliente cliente = cDao.find(this.venta.getCliente().getDocumento());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Succes",
						"Se ha filtrado la persona con documento " + this.venta.getCliente().getDocumento() + "."));
				if (cliente != null) {
					this.venta.setCliente(cliente);
					this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
							"Se ha filtrado el cliente con documento " + this.venta.getCliente().getDocumento() + ".");
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"No se ha encontrado ningun cliente con ese documento "
									+ this.venta.getCliente().getDocumento() + ".");
				}
				this.venta.getCliente().setPersona(aux);
				this.hidden = true;
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"No se ha encontrado ninguna persona con ese documento "
								+ this.venta.getCliente().getDocumento() + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "No has seleccionado ningun cliente.");
		}
		if (!this.hidden) {
			initCliente();
		}
		if (message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * 
	 */
	public void addProductosADetalleProductos() {
		if (this.tabla_productos_seleccionados != null && this.tabla_productos_seleccionados.size() > 0) {
			initRederizarTablaDetalleProducto();
			this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succces",
					"Se han filtrado " + this.tabla_productos_seleccionados.size() + " detalles de productos.");
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "No has seleccionado ningun producto.");
		}
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}

	///////////////////////////////////////////////////////
	// Renderizar
	///////////////////////////////////////////////////////
	/**
	 * Metodo que consulta todos los productos.
	 * 
	 * @return una lista con todos los productos.
	 */
	public List<Producto> getTabla_productos() {
		if (renderizar_tabla_productos) {
			CategoriaDao dao = new CategoriaDao();
			List<Categoria> categorias = dao.findByFieldList("estado", true);
			this.tabla_productos = new ArrayList<Producto>();
			for (Categoria c : categorias) {
				ProductoDao pDao = new ProductoDao();
				List<Producto> productos = pDao.findByFieldList("categoria", c);
				for (Producto p : productos) {
					if (p.getStock() > 0) {
						tabla_productos.add(p);
					}
				}
			}
			this.renderizar_tabla_productos = false;
			this.hidden_tablas = false;
		}
		return tabla_productos;
	}

	/**
	 * Metodo que permite filtrar los detalles de productos de los productos
	 * seleccionados.
	 */
	public List<DetalleProducto> getTabla_detalle_producto() {
		this.tabla_detalle_producto = new ArrayList<DetalleProducto>();
		if (this.renderizar_tabla_detalle_producto) {
			if (this.tabla_productos_seleccionados != null && this.tabla_productos_seleccionados.size() > 0) {
				for (Producto p : this.tabla_productos_seleccionados) {
					DetalleProductoDao dao = new DetalleProductoDao();
					if (p.getStock() > 0) {
						List<DetalleProducto> list = dao.findByFieldList("producto", p);
						for (DetalleProducto dp : list) {
							if (dp.getStock() > 0) {
								this.tabla_detalle_producto.add(dp);
							}

						}
					}
				}
			}
			this.renderizar_tabla_detalle_producto = false;
			this.hidden_tablas = true;
		}
		return tabla_detalle_producto;
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public FacesMessage getMessage() {
		return message;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public DetalleVenta getDetalle_venta() {
		return detalle_venta;
	}

	public void setDetalle_venta(DetalleVenta detalle_venta) {
		this.detalle_venta = detalle_venta;
	}

	public SessionBean getSesion() {
		return sesion;
	}

	public void setSesion(SessionBean sesion) {
		this.sesion = sesion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Producto> getTabla_productos_seleccionados() {
		return tabla_productos_seleccionados;
	}

	public void setTabla_productos_seleccionados(List<Producto> tabla_productos_seleccionados) {
		this.tabla_productos_seleccionados = tabla_productos_seleccionados;
	}

	public void setTabla_productos(List<Producto> tabla_productos) {
		this.tabla_productos = tabla_productos;
	}

	public boolean isRenderizar_tabla_productos() {
		return renderizar_tabla_productos;
	}

	public void setRenderizar_tabla_productos(boolean renderizar_tabla_productos) {
		this.renderizar_tabla_productos = renderizar_tabla_productos;
	}

	public void setTabla_detalle_producto(List<DetalleProducto> tabla_detalle_producto) {
		this.tabla_detalle_producto = tabla_detalle_producto;
	}

	public List<DetalleProducto> getTabla_detalle_producto_seleccionados() {
		return tabla_detalle_producto_seleccionados;
	}

	public void setTabla_detalle_producto_seleccionados(List<DetalleProducto> tabla_detalle_producto_seleccionados) {
		this.tabla_detalle_producto_seleccionados = tabla_detalle_producto_seleccionados;
	}

	public boolean isRenderizar_tabla_detalle_producto() {
		return renderizar_tabla_detalle_producto;
	}

	public void setRenderizar_tabla_detalle_producto(boolean renderizar_tabla_detalle_producto) {
		this.renderizar_tabla_detalle_producto = renderizar_tabla_detalle_producto;
	}

	public boolean isHidden_tablas() {
		return hidden_tablas;
	}

	public void setHidden_tablas(boolean hidden_tablas) {
		this.hidden_tablas = hidden_tablas;
	}
}
