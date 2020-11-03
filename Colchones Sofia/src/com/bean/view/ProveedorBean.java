package com.bean.view;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

import com.bean.session.*;
import com.dao.*;
import com.entity.*;
import com.entity.other.*;
import com.util.*;

/**
 * Implementation ProveedorBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "proveedor")
@ViewScoped
public class ProveedorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Proveedor proveedor;
	private String id;
	private FacesMessage message;

	private String estado;
	private int index;

	private String[] select;

	private List<DetalleProducto> seleccionadas;
	private List<DetalleProducto> productos;

	private boolean insert;
	private boolean search;
	private boolean update;
	private boolean remove;
	private boolean hidden;
	private boolean error;
	private boolean active;

	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean sesion;

	@ManagedProperty("#{image}")
	private ImageBean image;

	@ManagedProperty("#{table}")
	private DataTableBean table;

	@PostConstruct
	public void init() {
		initProveedor();
		this.estado = "";
		this.index = -1;
		this.update = false;
		this.error = false;
		this.hidden = false;
		this.insert = false;
		this.remove = false;
		this.search = false;
		this.active = false;
	}

	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * Metodo que inicializa el proveedor.
	 */
	public void initProveedor() {
		this.proveedor = new Proveedor();
		this.select = null;
	}
	
	/**
	 * Metodo que limpia el filtro de la tabla.
	 */
	public void initTable() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('sofia-table-update').clearFilters());");
	}

	/**
	 * Metodo que cambia el estado a dialogo de formulario.
	 * 
	 * @param estado representa el estado.
	 */
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
	 * Metodo que permite conocer un proveedor por su indice en la tabla.
	 */
	public void proveedor() {
		this.message = null;
		this.error = true;
		this.index = -1;
		String id = Face.get("id-proveedor");
		if (Convertidor.isCadena(id)) {
			this.index = index(id);
			if (this.table.getProveedor() != null && this.index >= 0 && this.index < this.table.getProveedor().size()) {
				this.proveedor = this.table.getProveedor().get(this.index);
				if (this.proveedor != null) {
					this.error = false;
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"El proveedor no se encuentra registrado.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El proveedor no se encuentra registrado.");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se ha seleccionado ningun proveedor.");
		}
	}

	/**
	 * Metodo que permite buscar en una lista el indice de un proveedor.
	 * 
	 * @param id representa el id del proveedor.
	 * @return representa el indice del proveedor en la lista.
	 */
	public int index(String documento) {
		int aux = 0;
		for (Proveedor p : this.table.getProveedor()) {
			if (p.getDocumento() == id) {
				return aux;
			}
			aux++;
		}
		return -1;
	}

	///////////////////////////////////////////////////////
	// CRUD
	///////////////////////////////////////////////////////
	/**
	 * Metodo que registra un proveedor.
	 */
	public void registrar() {
		FacesContext.getCurrentInstance().addMessage(null, faceRegistrarProveedor());
	}

	/**
	 * Metodo que permite eliminar un proveedor.
	 */
	public void eliminar() {
		FacesContext.getCurrentInstance().addMessage(null, faceEliminarProveedor());
	}
	
	/**
	 * Metodo que cambia el valor del estado del proveedor.
	 */
	public void estado() {
		this.update = false;
		this.proveedor();
		if (!this.error && this.index >= 0) {
			ProveedorDao dao = new ProveedorDao();
			boolean estado = proveedor.getEstado();
			estado = (estado) ? false : true;
			this.proveedor.setEstado(estado);
			this.table.getProveedor().set(this.index, this.proveedor);
			this.update = true;
			dao.update(this.proveedor);
			this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"Se ha cambiado el estado al proveedor con documento " + proveedor.getDocumento() + " a estado"
							+ ((estado) ? " Activo." : " Bloqueado."));
			// Init
			initTable();
			this.index = -1;
			this.initProveedor();
		}
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////

	/**
	 * 
	 * @param p
	 * @param u
	 * @return
	 */
	public FacesMessage validar(Proveedor p, Usuario u) {
		this.message = null;
		this.error = true;
		if (u != null && Convertidor.isCadena(u.getPersona().getDocumento())) {
			if (p != null && Convertidor.isCadena(p.getDocumento())) {
				if (Convertidor.isCadena(p.getPersona().getNombre())) {
					if (Convertidor.isCadena(p.getPersona().getTelefono())) {
						if (Convertidor.isCadena(p.getPersona().getDireccion())) {
							this.error = false;
							return null;
						} else {
							message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
									"El campo dirección es obligatorio.");
						}
					} else {
						message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo telefono es obligatorio.");
					}
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo nombre es obligatorio.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El documento proveedor no es valido.");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No te has logeado.");
		}
		return message;
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings({ "deprecation" })
	private FacesMessage faceRegistrarProveedor() {
		if (this.proveedor != null && this.id != this.proveedor.getDocumento()) {
			this.proveedor.setDocumento(this.id);
		}
		this.message = validar(this.proveedor, sesion.getLogeado());
		this.insert = false;
		if (message == null) {
			this.error = true;
			ProveedorDao pDao = new ProveedorDao();
			Proveedor aux = pDao.find(this.proveedor.getDocumento());
			if (aux == null) {
				this.proveedor.getPersona().setNombre(this.proveedor.getPersona().getNombre().toUpperCase());
				this.proveedor.getPersona()
						.setTelefono(Convertidor.telefono(this.proveedor.getPersona().getTelefono()));
				Fecha fecha = new Fecha();
				this.proveedor.setEstado(true);
				this.proveedor.setFechaCreacion(new Date(fecha.fecha()));

				if (this.image.getImage() != null) {
					this.proveedor.getPersona().setFoto(this.image.getImage());
				}

				// INSERT
				pDao.insert(this.proveedor);
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha registrado el proveedor con ID " + this.proveedor.getDocumento() + ".");
				FacesContext.getCurrentInstance().addMessage(null, this.message);
				this.message = faceRegistrarProductos(this.proveedor);
				// RESET
				this.image.setImage(null);
				this.insert = true;
				this.active = false;
				this.search = false;
				this.error = false;
				this.table.initProveedor();
				this.initProveedor();
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"Ya existe un proveedor con ese documento " + this.id + ".");
			}
		}
		return message;
	}

	/**
	 * Metodo que permite registrar los productos de un proveedor.
	 * 
	 * @param p represenat el proveedor.
	 * @return representa el mensage generado.
	 */
	private FacesMessage faceRegistrarProductos(Proveedor p) {
		this.message = null;
		if (p != null && Convertidor.isCadena(p.getDocumento())) {
			if (select != null && select.length > 0) {
				ProveedorDao dao = new ProveedorDao();
				int j = 0;
				for (String aux : select) {
					if (dao.insertProveedorProducto(p.getPersona().getDocumento(), aux)) {
						j++;
					}
				}
				if (j == select.length) {
					message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							"Todos los productos del proveedor con ID." + p.getPersona().getDocumento()
									+ ",fueron registrados (" + j + "/" + select.length + ",productos).");
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
							"Faltaron algunos productos del proveedor con ID." + p.getPersona().getDocumento()
									+ ", por registrar (" + j + "/" + select.length + ",productos).");
				}
				this.select = null;
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No has seleccionado ningun producto.");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Los campos del proveedor son obligatorios.");
		}
		return message;
	}

	/**
	 * Metodo que permite eliminar el proveedor.
	 * 
	 * @return representa el mensage generado.
	 */
	private FacesMessage faceEliminarProveedor() {
		this.remove = false;
		this.proveedor();
		if (!this.error && this.index >= 0) {
			ProveedorDao dao = new ProveedorDao();
			if (this.proveedor != null) {
				if (this.proveedor.getCompras() != null && this.proveedor.getCompras().size() == 0) {
					if (this.productos != null) {
						if (this.proveedor.getDetalleProductos().size() == 0) {
							dao.delete(this.proveedor);
							this.table.getProveedor().remove(this.index);
							message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
									"Se ha eliminado el proveedor documento " + proveedor.getDocumento() + ".");
							this.remove = true;
							this.error = false;
							initProveedor();
							this.index = -1;
						} else {
							message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
									"No se ha eliminado el proveedor con documento " + proveedor.getDocumento()
											+ ",primero tienes que eliminar los productos que el tiene asociado que son "
											+ this.proveedor.getDetalleProductos().size() + " productos.");
						}
					} else {
						message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
								"Se ha presentado un error a listar los productos del proveedor con ID." + this.id
										+ " vuelva a intentarlo.");
					}
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"No se ha podido eliminar al proveedor con el documento " + this.proveedor.getDocumento()
									+ " porque esta asociado a " + this.proveedor.getCompras().size() + " compras.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No existe ningun proveedor con ese documento " + this.id + ".");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					"El documento " + this.id + " del proveedor no es valido.");
		}

		return message;
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FacesMessage getMessage() {
		return message;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
	}

	public String[] getSelect() {
		return select;
	}

	public void setSelect(String[] select) {
		this.select = select;
	}

	public List<DetalleProducto> getSeleccionadas() {
		return seleccionadas;
	}

	public void setSeleccionadas(List<DetalleProducto> seleccionadas) {
		this.seleccionadas = seleccionadas;
	}

	public List<DetalleProducto> getProductos() {
		return productos;
	}

	public void setProductos(List<DetalleProducto> productos) {
		this.productos = productos;
	}

	public boolean isInsert() {
		return insert;
	}

	public void setInsert(boolean insert) {
		this.insert = insert;
	}

	public boolean isSearch() {
		return search;
	}

	public void setSearch(boolean search) {
		this.search = search;
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

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public ImageBean getImage() {
		return image;
	}

	public void setImage(ImageBean image) {
		this.image = image;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public DataTableBean getTable() {
		return table;
	}

	public void setTable(DataTableBean table) {
		this.table = table;
	}
}
