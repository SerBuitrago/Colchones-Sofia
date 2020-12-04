package com.bean.view;

import java.io.Serializable;
import java.math.BigInteger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

import com.bean.session.*;
import com.controller.*;
import com.model.*;
import com.model.other.*;
import com.util.*;

/**
 * Implementation DataTableBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "detalle_producto")
@ViewScoped
public class DetalleProductoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private FacesMessage message;

	private DetalleProducto detalle_producto;
	private DetalleProducto modificar;

	private String estado;
	private int idDetalle;

	private int stock_sumar;
	private boolean error;
	private boolean insert;
	private boolean update;
	private boolean remove;
	private boolean hidden;

	private boolean filtrar_producto;

	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean sesion;

	@ManagedProperty("#{image}")
	private ImageBean image;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public DetalleProductoBean() {

	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		initDetalleProducto();
		this.modificar = new DetalleProducto();
		this.insert = false;
		this.remove = false;
		this.hidden = false;
		this.update = false;
	}

	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * 
	 */
	public void initDetalleProducto() {
		this.detalle_producto = new DetalleProducto();
		this.detalle_producto.setId(generarID());
		this.stock_sumar = 0;
		this.detalle_producto.setDescuento(BigInteger.ZERO);
		this.detalle_producto.setPrecioVenta(BigInteger.ZERO);
		this.detalle_producto.setPrecioCompra(BigInteger.ZERO);
		initProducto();
	}

	/**
	 * Metodo que permite inicializar el producto.
	 */
	public void initProducto() {
		this.filtrar_producto = false;
		if (detalle_producto != null) {
			this.detalle_producto.setProductoBean(new Producto());
			this.detalle_producto.getProductoBean().setCategoriaBean(new Categoria());
		}
	}

	///////////////////////////////////////////////////////
	// Method Product
	///////////////////////////////////////////////////////
	/**
	 * Metodo que filtrar un producto
	 */
	public void filtrarProduco() {
		this.filtrar_producto = false;
		this.message = null;
		if (detalle_producto != null && this.detalle_producto.getProductoBean() != null
				&& Convertidor.isCadena(this.detalle_producto.getProductoBean().getId())) {
			ProductoController dao = new ProductoController();
			Producto aux = dao.find(this.detalle_producto.getProductoBean().getId());
			if (aux != null) {
				this.detalle_producto.setProductoBean(aux);
				this.filtrar_producto = true;
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Se ha filtrado el producto con el ID "
						+ this.detalle_producto.getProductoBean().getId() + ".");
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No existe ningun producto con ese ID "
						+ this.detalle_producto.getProductoBean().getId() + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No has ingresado el ID del producto.");
		}

		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	/**
	 * 
	 */
	public void limpiarDetalleProducto() {
		this.filtrar_producto = false;
		initDetalleProducto();
	}

	/**
	 * 
	 */
	public void aumentarCantidad() {
		FacesMessage aux = null;
		if (this.detalle_producto != null && this.detalle_producto.getProductoBean() != null
				&& Convertidor.isCadena(this.detalle_producto.getProductoBean().getId())) {
			ProductoController dao = new ProductoController();
			int sumar = this.detalle_producto.getProductoBean().getStock() + this.detalle_producto.getStock();
			this.detalle_producto.getProductoBean().setStock(sumar);
			dao.update(this.detalle_producto.getProductoBean());
			aux = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"Se ha actualiazo la cantidad del stock del porducto con ID "
							+ this.detalle_producto.getProductoBean().getId() + ".");
		} else {
			aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se ha actualizado la cantidad del producto.");
		}
		if (aux != null) {
			FacesContext.getCurrentInstance().addMessage(null, aux);
		}
	}

	///////////////////////////////////////////////////////
	// Method Detail
	///////////////////////////////////////////////////////
	/**
	 * 
	 * @return
	 */
	public int generarID() {
		DetalleProductoController dao = new DetalleProductoController();
		DetalleProducto dp = dao.addUltimo();
		if (dp != null) {
			return dp.getId() + 1;
		}
		return 1;
	}

	/**
	 * 
	 */
	public void registrar() {
		this.message = this.validarCampos();
		if (this.message == null) {
			DetalleProductoController dao = new DetalleProductoController();
			DetalleProducto aux = dao.existe(this.detalle_producto.getColor(), this.detalle_producto.getDimension());
			if (aux == null) {
				if (this.image.getImage() != null) {
					this.detalle_producto.setFoto(this.image.getImage());
					this.image.setImage(null);
				}
				dao.insert(this.detalle_producto);
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Se ha registrado el detalle venta.");

			} else {
				int suma = this.detalle_producto.getStock() + aux.getStock();
				aux.setStock(suma);

				if (this.image.getImage() != null) {
					this.detalle_producto.setFoto(this.image.getImage());
					this.image.setImage(null);
					aux.setFoto(this.detalle_producto.getFoto());
				}

				if (this.detalle_producto.getPrecioCompra().compareTo(BigInteger.ZERO) > 0) {
					aux.setPrecioCompra(detalle_producto.getPrecioCompra());
				}
				if (this.detalle_producto.getPrecioVenta().compareTo(BigInteger.ZERO) > 0) {
					aux.setPrecioVenta(detalle_producto.getPrecioVenta());
				}
				if (this.detalle_producto.getStockMinimo() > 0) {
					aux.setStockMinimo(detalle_producto.getStockMinimo());
				}
				if (Convertidor.isCadena(this.detalle_producto.getDescripcion())) {
					aux.setDescripcion(this.detalle_producto.getDescripcion());
				}
				dao.update(aux);
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"El detalle producto ya existia se ha actualizado la información.");
			}
			this.aumentarCantidad();
			this.initDetalleProducto();
		}

		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	/**
	 * Metodo que permite terminar el detalle producto.
	 * 
	 * @return
	 */
	public FacesMessage validarCampos() {
		FacesMessage aux = null;
		if (this.detalle_producto != null && this.detalle_producto.getId() > 0) {
			if (this.detalle_producto.getProductoBean() != null) {
				if (Convertidor.isCadena(this.detalle_producto.getColor())) {
					if (!Convertidor.containsNumber(this.detalle_producto.getColor())) {
						if (Convertidor.isCadena(this.detalle_producto.getDimension())) {
							if (this.detalle_producto.getDescuento() != null) {
								if (this.detalle_producto.getStock() > 0) {
									if (this.detalle_producto.getStockMinimo() >= 0) {
										if (this.detalle_producto.getStock() >= this.detalle_producto
												.getStockMinimo()) {
											if (this.detalle_producto.getDescuento() != null) {
												if (this.detalle_producto.getPrecioVenta() != null) {
													if (this.detalle_producto.getPrecioCompra() != null) {
														if (this.detalle_producto.getDescuento().compareTo(
																this.detalle_producto.getPrecioVenta()) <= 0) {
															if (this.detalle_producto.getPrecioCompra().compareTo(
																	this.detalle_producto.getPrecioVenta()) <= 0) {
															} else {
																aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
																		"El precio de veta no puede ser menor al precio compra.");
															}
														} else {
															aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
																	"El precio de veta no puede ser menor al descuento del producto.");
														}
													} else {
														aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
																"El campo precio compra es obligatorio.");
													}
												} else {
													aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
															"El campo precio venta es obligatorio.");
												}
											} else {
												aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
														"El campo descuento es obligatorio.");
											}
										} else {
											aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
													"El campo stock debe ser mayor o igual a stock minimo.");
										}

									} else {
										aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
												"El campo stock minimo debe ser mayor a cero.");
									}
								} else {
									aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
											"El campo stock debe ser mayor a cero.");
								}
							} else {
								aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
										"El campo descuento es obligatorio.");
							}

						} else {
							aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
									"El campo dimensión es obligatorio.");
						}
					} else {
						aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
								"El campo color no puede contener caracteres numericos.");
					}
				} else {
					aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo color es obligatorio.");
				}
			} else {
				aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo ID del producto es obligatorio.");
			}
		} else {
			aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo ID del detalle producto es obligatorio.");
		}
		return aux;
	}

	/**
	 * 
	 * @param estado
	 */
	public void initDialogForm(int estado) {
		PrimeFaces current = PrimeFaces.current();
		switch (estado) {
		case 1:
			current.executeScript("PF('sofia-dialog-detalle-update').show();");
			break;
		case 2:
			current.executeScript("PF('sofia-dialog-detalle-update').hide();");
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @return
	 */
	public FacesMessage actualizar() {
		this.update = false;
		this.modificar = this.detalle_producto;
		FacesMessage message = validarCampos();
		if (message == null) {
			DetalleProductoController pD = new DetalleProductoController();
			if (this.image.getImage() != null) {
				this.modificar.setFoto(this.image.getImage());
				this.image.setImage(null); 
			}
			pD.update(this.modificar);
			this.update = true;
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Se ha Actualizado el detalle producto con ID "+this.modificar.getId()+".");
			this.modificar = new DetalleProducto();
			this.initDetalleProducto();
		}
		if (message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

		return message;
	}

	/**
	 * 
	 */
	public void statuActualizar() {
		this.update = true;
		this.hidden = true;
		this.initDetalleProducto();
		this.message = null;
		this.error = true;

		String idDetalle_producto = Face.get("id-detalle");
		if (Convertidor.isCadena(idDetalle_producto)) {
			if (Convertidor.isNumber(idDetalle_producto)) {
				int aux = Integer.parseInt(idDetalle_producto);
				DetalleProductoController dao = new DetalleProductoController();
				this.detalle_producto = dao.find(aux);
				this.idDetalle = this.detalle_producto.getId();

				if (this.detalle_producto != null) {
					this.modificar = this.detalle_producto;//
					this.estado = "Actualizar";

					this.initDialogForm(1);

				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No se ha obtenido el ID del Detalle.");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El ID del Detalle no es valido.");
			}
		}

		if (message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	/**
	 * 
	 */
	public void statu() {
		switch (estado) {
		case "Actualizar":
			this.actualizar();
			break;
		case "Registrar":
			this.registrar();
			break;
		default:
			break;
		}
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public DetalleProducto getDetalle_producto() {
		return detalle_producto;
	}

	public void setDetalle_producto(DetalleProducto detalle_producto) {
		this.detalle_producto = detalle_producto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isFiltrar_producto() {
		return filtrar_producto;
	}

	public void setFiltrar_producto(boolean filtrar_producto) {
		this.filtrar_producto = filtrar_producto;
	}

	public FacesMessage getMessage() {
		return message;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
	}

	public int getStock_sumar() {
		return stock_sumar;
	}

	public void setStock_sumar(int stock_sumar) {
		this.stock_sumar = stock_sumar;
	}

	public SessionBean getSesion() {
		return sesion;
	}

	public void setSesion(SessionBean sesion) {
		this.sesion = sesion;
	}

	public ImageBean getImage() {
		return image;
	}

	public void setImage(ImageBean image) {
		this.image = image;
	}

	public DetalleProducto getModificar() {
		return modificar;
	}

	public void setModificar(DetalleProducto modificar) {
		this.modificar = modificar;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getIdDetalle() {
		return idDetalle;
	}

	public void setIdDetalle(int idDetalle) {
		this.idDetalle = idDetalle;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public boolean isInsert() {
		return insert;
	}

	public void setInsert(boolean insert) {
		this.insert = insert;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public boolean isRemove() {
		return remove;
	}

	public void setRemove(boolean remove) {
		this.remove = remove;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
