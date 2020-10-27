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
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import com.entity.*;
import com.util.*;
import com.dao.*;

/**
 * Implementation ComprarBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "comprar")
@SessionScoped
public class ComprarBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private FacesMessage message;

	private boolean hidden;

	private Compra compra;
	private ProveedorProducto producto;
	private DetalleProducto detalleProducto;
	private DetalleCompra detalleCompra;

	private List<SelectItem> productos;
	private List<DetalleCompra> tabla; //
	private List<DetalleProducto> detalles;

	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean sesion;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public ComprarBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		this.compra = new Compra();
		this.compra.setTotal(BigInteger.ZERO);
		initCompra();
		this.tabla = new ArrayList<DetalleCompra>();
	}

	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * Metodo que inicializa la compra.
	 */
	public void initCompra() {
		this.producto = new ProveedorProducto();
		this.producto.setProducto(new Producto());
		this.producto.setProveedor(new Proveedor());
		this.detalleCompra = new DetalleCompra();
		this.detalleProducto = new DetalleProducto();
		this.detalleProducto.setProducto(new Producto());
		this.hidden = false;
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que trae todos los productos d un proveedor especifico.
	 */
	public void productosProveedor() {
		this.productos = new ArrayList<SelectItem>();
		if (this.producto != null && this.producto.getProveedor() != null
				&& this.producto.getProveedor().getIdProveedor() > 0) {
			ProveedorProductoDao dao = new ProveedorProductoDao();
			List<Categoria> categorias = dao.listCategoria(this.producto.getProveedor().getIdProveedor());
			for (Categoria categoria : categorias) {
				SelectItemGroup s = new SelectItemGroup(categoria.getNombre());
				SelectItem[] items = new SelectItem[categoria.getProductos().size()];
				int index = 0;
				for (Producto pp : categoria.getProductos()) {
					items[index] = new SelectItem(String.valueOf(pp.getIdProducto()), String.valueOf(pp.getNombre()));
					index++;
				}
				s.setSelectItems(items);
				this.productos.add(s);
			}
		} else {
			SelectItemGroup s = new SelectItemGroup("Proveedor no encontrado.");
			this.productos.add(s);
		}
	}

	/**
	 * Metodo que trae todos los detalles del producto.
	 */
	public void detallesProducto() {
		this.detalles = new ArrayList<DetalleProducto>();
		if (this.producto != null && this.producto.getProducto() != null
				&& this.producto.getProducto().getIdProducto() > 0) {
			DetalleProductoDao dao = new DetalleProductoDao();
			this.detalles = dao.findByFieldList("producto", this.producto.getProducto());
			if (this.detalles.size() == 0) {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "El producto con ID "
						+ this.producto.getProducto().getIdProducto() + " no tiene asosiado ningun detalle.");
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Se han filtrado todos los detalles del producto con ID "
								+ this.producto.getProducto().getIdProducto() + ".");
				FacesContext.getCurrentInstance().addMessage(null, this.message);
				return;
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"No has seleccionado ningun producto.");
		}
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}

	/**
	 * Metodo que permite actualizar el detalle producto.
	 */
	public void addDetalleProducto() {
		String aux = Face.get("index-detalle-producto");
		if (aux != null && aux.length() > 0) {
			if (this.detalles != null && this.detalles.size() > 0) {
				int index = Integer.parseInt(aux);
				if (index >= 0 && index < this.detalles.size()) {
					this.detalleProducto = this.detalles.get(index);
					this.hidden = true;
					this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
							"Se ha agregado el detalle del producto con ID " + this.detalleProducto.getId() + ".");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"No se han listado los detalles del producto.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Se ha presentado un error al agregar el detalle del producto.");
		}
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}

	/**
	 * Metodo que permite agregar un producto a la tabla de compra.
	 */
	public void addProducto() {
		validar();
		if (message == null) {
			validarDetalleProducto();
			if (message == null) {
				validarDetalleCompra();
				if (this.tabla == null) {
					this.tabla = new ArrayList<DetalleCompra>();
				}
				if (message == null) {
					this.tabla.add(this.detalleCompra);
					this.total();
					this.initCompra();
					this.productos = new ArrayList<SelectItem>();
					this.detalles = new ArrayList<DetalleProducto>();
					this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se ha agregado la compra.");
				}
			}
		}
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}

	/**
	 * Metodo que calcula el total a pagar.
	 */
	public void total() {
		if (this.tabla != null) {
			BigInteger total = BigInteger.ZERO;
			for (DetalleCompra d : this.tabla) {
				int cantidad = d.getCantidad();
				BigInteger aux = d.getPrecio().multiply(BigInteger.valueOf(cantidad));
				total = total.add(aux);
			}
			this.compra.setTotal(total);
		}
	}

	/**
	 * Metodo que retorna el subtotal.
	 * 
	 * @param cantidad representa la cantidad.
	 * @param precio   representa el precio.
	 * @return representa el subtotal.
	 */
	public BigInteger subTotal(int cantidad, BigInteger precio) {
		return precio.multiply(BigInteger.valueOf(cantidad));
	}

	/**
	 * Metodo que recarga la tabla.
	 */
	public void recargar() {
		this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se ha recargado la vista.");
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}

	/**
	 * Metodo que elimina la tabla.
	 */
	public void eliminar() {
		this.initCompra();
		this.productos = new ArrayList<SelectItem>();
		this.tabla = new ArrayList<DetalleCompra>();
		this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
				"Se ha eliminado todos los registros de compra.");
		FacesContext.getCurrentInstance().addMessage(null, this.message);

	}

	/**
	 * Metodo que elimina una compra indicada.
	 */
	public void eliminarCompra() {
		String aux = Face.get("eliminar-compra-indice");
		if (aux != null && aux.length() > 0) {
			int index = Integer.parseInt(aux);
			if (index >= 0 && index < this.tabla.size()) {
				this.tabla.remove(index);
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se ha eliminado la compra.");
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"El identificador de la compra no es valido.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Se ha presentado un error al eliminar una compra.");
		}
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}

	///////////////////////////////////////////////////////
	// Validator
	///////////////////////////////////////////////////////
	/**
	 * Metodo que valida la parte del proveedor y producto.
	 */
	public void validar() {
		this.message = null;
		if (this.producto != null) {
			Proveedor proveedor = this.producto.getProveedor();
			Producto producto = this.producto.getProducto();
			if (proveedor != null && proveedor.getIdProveedor() > 0) {
				if (producto != null && producto.getIdProducto() > 0) {
					ProveedorDao dao = new ProveedorDao();
					proveedor = dao.find(proveedor.getIdProveedor());
					if (proveedor != null) {
						if (proveedor.getEstado()) {
							this.producto.setProveedor(proveedor);
							for (ProveedorProducto pp : this.producto.getProveedor().getProveedorProductos()) {
								if (pp.getProducto() != null
										&& pp.getProducto().getIdProducto() == producto.getIdProducto()) {
									if (pp.getProducto().getEstado()) {
										this.producto.setProducto(pp.getProducto());
									} else {
										this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Warn",
												"El producto con ID " + pp.getProducto().getIdProducto()
														+ " esta bloqueado.");
									}
									return;
								}
							}
							if (this.message == null) {
								this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Warn",
										"El producto no esta registrado.");
							}
						} else {
							this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Warn",
									"El proveedor con ID " + proveedor.getIdProveedor() + " esta bloqueado.");
						}
					} else {
						this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Warn", "El proveedor con ID "
								+ this.producto.getProveedor().getIdProveedor() + " no esta registrado.");
					}
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"No has seleccionado ningun producto.");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"No has seleccionado ningun proveedor.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "No se ha podido agregar el producto.");
		}
	}

	/**
	 * Metodo que valida el campo de detalle de producto.
	 */
	public void validarDetalleProducto() {
		this.message = null;
		if (this.detalleProducto != null) {
			if (this.detalleProducto.getColor() != null && this.detalleProducto.getColor().length() > 0) {
				if (this.detalleProducto.getDimension() != null && this.detalleProducto.getDimension().length() > 0) {
					if (!this.detalleProducto.getPrecioVenta().equals(BigInteger.ZERO)) {
						return;
					} else {
						this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
								"Falto por llenar el campo precio de venta del producto.");
					}
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"Falto por llenar el campo dimensión del producto.");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"Falto por llenar el campo color del producto.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
					"Los campos de detalle de producto son obligatorios.");
		}
		this.producto.setProveedor(new Proveedor());
		this.producto.setProducto(new Producto());
		this.productos = new ArrayList<SelectItem>();
	}

	/**
	 * Metodo que permite validar el los campos de detalle de compra.
	 */
	public void validarDetalleCompra() {
		this.message = null;
		if (this.detalleCompra != null) {
			if (this.detalleCompra.getCantidad() > 0) {
				if (this.detalleCompra != null && this.detalleCompra.getPrecio() != null
						&& !this.detalleCompra.getPrecio().equals(BigInteger.ZERO)) {
					this.detalleProducto.setPrecioCosto(this.detalleCompra.getPrecio());
					this.detalleCompra.setProveedor(this.producto.getProveedor());
					this.detalleCompra.setCompra(this.compra);
					this.detalleProducto.setProducto(this.producto.getProducto());
					this.detalleCompra.setDetalleProducto(this.detalleProducto);
					return;
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"El campo el precio de compra es obligatorio.");
				}
			} else if (this.detalleCompra.getCantidad() == 0) {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"El campo cantidad de producto debe ser positivo.");
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"El campo cantidad no puede ser negativa.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
					"Los campos de detalle de compra son obligatorios.");
		}
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

	public Compra getCompra() {
		return compra;
	}

	public List<DetalleCompra> getTabla() {
		return tabla;
	}

	public void setTabla(List<DetalleCompra> tabla) {
		this.tabla = tabla;
	}

	public List<SelectItem> getProductos() {
		return productos;
	}

	public DetalleProducto getDetalleProducto() {
		return detalleProducto;
	}

	public void setDetalleProducto(DetalleProducto detalleProducto) {
		this.detalleProducto = detalleProducto;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public List<DetalleProducto> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleProducto> detalles) {
		this.detalles = detalles;
	}

	public DetalleCompra getDetalleCompra() {
		return detalleCompra;
	}

	public void setDetalleCompra(DetalleCompra detalleCompra) {
		this.detalleCompra = detalleCompra;
	}

	public ProveedorProducto getProducto() {
		return producto;
	}

	public void setProducto(ProveedorProducto producto) {
		this.producto = producto;
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
}
