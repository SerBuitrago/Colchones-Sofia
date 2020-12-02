package com.bean.view;

import java.io.*;
import java.util.Date;

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
 * Implementation SesionBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "producto")
@ViewScoped
public class ProductoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private FacesMessage message;
	
	private Producto producto;
	private Producto modificar;

	private String estado;
	private String idProducto;

	private boolean error;
	private boolean insert;
	private boolean update;
	private boolean remove;
	private boolean hidden;

	@ManagedProperty("#{file}")
	private ImageBean file;

	@ManagedProperty("#{table}")
	private DataTableBean table;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public ProductoBean() {
	}

	@PostConstruct
	public void init() {
		this.producto = new Producto();
		this.modificar = new Producto();
		this.estado = null;
		this.insert = false;
		this.remove = false;
		this.hidden = false;
		this.update = false;

		this.producto.setCategoriaBean(new Categoria());

	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////

	public void initProducto() {
		this.producto = new Producto();
		this.producto.setCategoriaBean(new Categoria());
	}

	public void initTable() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('sofia-table-update').clearFilters());");
	}

	public void initDialogForm(int estado) {
		PrimeFaces current = PrimeFaces.current();
		switch (estado) {
		case 1:
			current.executeScript("PF('sofia-dialog-update').show();");
			break;
		case 2:
			current.executeScript("PF('sofia-dialog-update').hide();");
			break;
		default:
			break;
		}
	}

	/**
	 * Metodo que registra un producto.
	 * 
	 * @return retorna el mensage obtenido.
	 */
	@SuppressWarnings("deprecation")
	public FacesMessage registrarProducto() {
		this.insert = false;
		FacesMessage message = null;
		if (Convertidor.isCadena(this.producto.getId())) {	
			if (Convertidor.isCadena(this.producto.getNombre())) {
				if (Convertidor.isCadena(this.producto.getDescripcion())) {
					if (this.producto.getStock() > 0) {
						if (this.producto.getStockMinimo() > 0) {
							if (this.producto.getGarantia() >= 0) {
								CategoriaController cDao = new CategoriaController();
								Categoria aux = cDao.find(this.producto.getCategoriaBean().getId());
								ProductoController pD = new ProductoController();
								Producto aux1 = pD.find(this.producto.getId());
								this.producto.setCategoriaBean(aux);
								if (aux1 == null) {
									Fecha fecha = new Fecha();
									this.producto.setFechaCreacion(new Date(fecha.fecha()));
									this.producto.setEstado(true);
									pD.insert(this.producto);
									this.insert = true;
									this.producto = new Producto();
									this.producto.setCategoriaBean(new Categoria());
									this.table.initProducto();
									message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
											"Se ha registrado el producto.");
								} else {
									message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
											"El producto ya existe..");
								}
							} else {
								message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
										"El campo Garantia es obligatorio..");
							}
						} else {
							message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
									"El campo Stock minimo es obligatorio.");
						}
					} else {
						message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
								"El campo Stock es obligatorio.");
					}

				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"El campo Descripcion minimo es obligatorio.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "El campo Nombre es obligatorio.");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "El campo Id es obligatorio.");
		}

		return message;
	}

	public void registrarP() {
		FacesMessage message = registrarProducto();
		if (message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);

		}
	}

	/**
	 * Metodo que actualiza un producto.
	 */
	@SuppressWarnings("deprecation")
	public FacesMessage actualizar() {
		this.update = false;
		this.modificar= this.producto;
		FacesMessage message = null;
		if (Convertidor.isCadena(this.modificar.getId())) {
			if (Convertidor.isCadena(this.modificar.getNombre())) {
				if (Convertidor.isCadena(this.modificar.getDescripcion())) {
					if (this.modificar.getStock() >= 0) {
						if (this.modificar.getStockMinimo() >= 0) {
							if (this.modificar.getGarantia() > 0) {
								CategoriaController cDao = new CategoriaController();
								Categoria aux = cDao.find(this.modificar.getCategoriaBean().getId());

								this.modificar.setCategoriaBean(aux);
								ProductoController pD= new ProductoController();

									Fecha fecha = new Fecha();
									this.modificar.setFechaCreacion(new Date(fecha.fecha()));
									this.modificar.setEstado(true);
									pD.update(this.modificar);
									this.update = true;
									this.modificar = new Producto();
									this.modificar.setCategoriaBean(new Categoria());

									message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
											"Se ha Actualizado el producto.");
								

							} else {
								message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
										"El campo Garantia es obligatorio..");
							}
						} else {
							message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
									"El campo Stock minimo es obligatorio.");
						}
					} else {
						message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
								"El campo Stock es obligatorio.");
					}

				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"El campo Descripcion minimo es obligatorio.");
				}
			}

			else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "El campo Nombre es obligatorio.");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "El campo Id es obligatorio.");
		}

		return message;
	}

	/**
	 * Metodo que permite eliminar un producto.
	 * 
	 * @return
	 */
	public FacesMessage eliminarProducto() {
		this.remove = false;
		FacesMessage message = null;
		String id = Face.get("id-producto");
		if(Convertidor.isCadena(id)) {
			ProductoController dao = new ProductoController();
			Producto producto = dao.find(id); 
			if (producto != null) {
				if(producto.getStock() == 0) {
					if (producto.getDetalleProductos() != null) {
						if (producto.getDetalleProductos().size() == 0) {
							dao.delete(producto);
							this.remove = true;
							message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
									"Se ha eliminado el producto ID. " + producto.getId() + ".");
						} else {
							message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
									"No se ha eliminado el producto ID. " + producto.getId()
											+ ", primero tienes que eliminar los productos que el tiene asociado que son "
											+ producto.getDetalleProductos().size() + " producto.");
						}
					} else {
						message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
								"Se ha presentado un error vuelva a intentarlo.");
					}
				}else {
					message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
							"No se puede eliminar el producto todavia tiene stock disponible.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"No se encontro ningun prroducto con ese ID.");
			}
		}else {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El ID producto no es valido.");
		}
		return message;
	}

	/**
	 * 
	 * @param id
	 */
	public void deleteProducto() {
		FacesContext.getCurrentInstance().addMessage(null, eliminarProducto());
	}

	/**
	 * 
	 */
	public void statuActualizar() {
		this.update = true;
		this.hidden = true;
		this.initProducto();
		this.message = null;
		this.error = true;
		String idProducto = Face.get("id-producto");
		
		if (Convertidor.isCadena(idProducto)) {
			ProductoController dao = new ProductoController();
			this.producto = dao.find(idProducto);
			this.idProducto = this.producto.getId();
			
			if (this.producto != null) {
			this.modificar=this.producto;//
			this.estado = "Actualizar";
			
			this.initDialogForm(1);
			
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No se ha obtenido el ID producto.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El ID del producto no es valido.");
		}

		if (message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	/**
	 * 
	 */
	public void statuRegistrar() {
		this.hidden = false;
		this.modificar = new Producto();
		this.estado = "Registrar";
		this.initDialogForm(1);
	}

	/**
	 * 
	 */
	public void statu() {
		switch (estado) {
		case "Actualizar":
			this.actualizarProducto();
			break;
		case "Registrar":
			this.registrarP();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 */
	public void actualizarProducto() {
		FacesMessage message = actualizar();
		if (this.update) {
			this.table.producto();
		}
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	/**
	 * Metodo que registra un producto.
	 */
	public void agregarProducto() {
		this.producto = this.modificar;
		FacesMessage message = registrarProducto();
		if (this.insert) {

			this.modificar = new Producto();
		}
		FacesContext.getCurrentInstance().addMessage(null, message);

	}
	
	public void estado() {
		this.update = false;
		String id = Face.get("id-producto");
		if (Convertidor.isCadena(id)) {
			ProductoController dao = new ProductoController();
			Producto aux = dao.find(id);
			if (aux != null) {
				boolean estado = (!aux.getEstado());
				aux.setEstado(estado);
				dao.update(aux);
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha cambiado el estado al producto con ID " + aux.getId() + " a estado"
								+ ((estado) ? " Activo." : " Bloqueado."));
				this.table.initProducto();
				this.update = true;
				initTable();
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No existe ningun producto con el ID " + id + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No ID del producto no es valido.");
		}

		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}


	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Producto getModificar() {
		return modificar;
	}

	public void setModificar(Producto modificar) {
		this.modificar = modificar;
	}

	public String getEstado() {
		return estado;
	}

	public FacesMessage getMessage() {
		return message;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	

	public String getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
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

	public ImageBean getFile() {
		return file;
	}

	public void setFile(ImageBean file) {
		this.file = file;
	}

	public DataTableBean getTable() {
		return table;
	}

	public void setTable(DataTableBean table) {
		this.table = table;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
