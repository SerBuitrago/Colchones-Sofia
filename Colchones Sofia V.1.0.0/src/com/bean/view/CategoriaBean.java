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
import com.controller.*;
import com.model.*;
import com.model.other.*;
import com.util.*;

/**
 * Implementation CategoriaBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "categoria")
@ViewScoped
public class CategoriaBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private FacesMessage message;

	private String estado;

	private int index;
	private String id;

	private boolean insert;
	private boolean search;
	private boolean update;
	private boolean remove;
	private boolean hidden;
	private boolean error;
	private boolean active;

	private Categoria categoria;

	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean sesion;

	@ManagedProperty("#{table}")
	private DataTableBean table;

	@ManagedProperty("#{image}")
	private ImageBean image;

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		initCategoria();
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
	 * Metodo que inicializa la categoria.
	 */
	public void initCategoria() {
		this.categoria = new Categoria();
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

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer la categoria por su indice en la tabla.
	 */
	public void categoria() {
		this.message = null;
		this.error = true;
		this.index = -1;
		String id = Face.get("id-categoria");
		if (Convertidor.isCadena(id)) {
			this.index = index(id);
			if (this.index >= 0 && this.table.getCategoria() != null && this.index < this.table.getCategoria().size()) {
				this.categoria = this.table.getCategoria().get(this.index);
				if (this.categoria != null) {
					this.error = false;
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"La categoria no se encuentra registrada.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "La categoria no se encuentra registrada.");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se ha seleccionado ninguna categoria.");
		}
	}

	/**
	 * Metodo que permite buscar en una lista el indice de una categoria.
	 * 
	 * @param nombre representa el nombre de la categoria.
	 * @return representa el indice de la categoria en la lista.
	 */
	public int index(String nombre) {
		int aux = 0;
		for (Categoria c : this.table.getCategoria()) {
			if (c.getId().contentEquals(nombre)) {
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
	 * Metodo que registra una categoria.
	 */
	public void registrar() {
		FacesContext.getCurrentInstance().addMessage(null, faceRegistrarCategoria());
	}

	/**
	 * Metodo que permite actualizar una categoria.
	 */
	public void actualizar() {
		FacesContext.getCurrentInstance().addMessage(null, faceActualizarCategoria());
	}

	/**
	 * Metodo que permite eliminar una categoria.
	 */
	public void eliminar() {
		FacesContext.getCurrentInstance().addMessage(null, faceEliminarCategoria());
	}

	/**
	 * Metodo que cambia el valor del estado de la categoria.
	 */
	public void estado() {
		this.update = false;
		this.categoria();
		if (!this.error && this.index >= 0) {
			CategoriaController dao = new CategoriaController();
			boolean estado = categoria.getEstado();
			estado = (estado) ? false : true;
			this.categoria.setEstado(estado);
			this.table.getCategoria().set(this.index, this.categoria);
			this.update = true;
			dao.update(this.categoria);
			this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"Se ha cambiado el estado de la categoria ID " + categoria.getId() + " a estado"
							+ ((estado) ? " Activo." : " Bloqueado."));
			// Init
			initTable();
			this.index = -1;
			this.initCategoria();
		}
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}

	///////////////////////////////////////////////////////
	// Validator
	///////////////////////////////////////////////////////
	/**
	 * Metodo que valida los datos de la categoria.
	 * 
	 * @param c Representa la categoria.
	 * @param u Representa el usuario logeado.
	 */
	public FacesMessage validar(Categoria c, Usuario u) {
		this.message = null;
		this.error = true;
		if (u != null && Convertidor.isCadena(u.getPersona().getDocumento())) {
			if (Convertidor.isCadena(c.getId())) {
				if (Convertidor.isCadena(c.getNombre())) {
					this.error = false;
					return null;
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
							"El campo nombre de la categoria es obligatorio.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
						"El campo ID de la categoria es obligatorio.");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No te has logeado.");
		}
		return this.message;
	}

	///////////////////////////////////////////////////////
	// Statu
	///////////////////////////////////////////////////////
	/**
	 * Metodo que obtiene el estato de la categoria a registrar.
	 */
	public void statuRegistrar() {
		this.id = "";
		if (this.update) {
			this.initCategoria();
			this.update = false;
		}
		this.initDialogForm(1);
		this.hidden = false;
		this.estado = "Registrar";
	}

	/**
	 * Metodo que obtiene el estato de la categoria a actualizar.
	 */
	public void statuActualizar() {
		this.update = true;
		this.hidden = true;
		this.initCategoria();
		this.message = null;
		this.error = true;
		this.categoria();
		if (!this.error && this.index >= 0) {
			this.estado = "Actualizar";
			this.id = this.categoria.getId();
			if (Convertidor.isCadena(id)) {
				this.error = false;
				this.initDialogForm(1);
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"El ID " + this.id + " de la categoria no es valida.");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No se ha obtenido el ID de la categoria.");
		}
		if (message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	/**
	 * Metodo que enviado al metodo correspondiente el estado.
	 */
	public void status() {
		switch (estado) {
		case "Actualizar":
			this.actualizar();
			if (this.update && !this.error) {
				initDialogForm(0);
			} else {
				initDialogForm(1);
			}
			break;
		case "Registrar":
			this.registrar();
			if (this.insert && !this.error) {
				initDialogForm(0);
			} else {
				initDialogForm(1);
			}
			break;
		default:
			break;
		}
	}

	///////////////////////////////////////////////////////
	// Face
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite insertar una categoria.
	 * 
	 * @return representa el mensaje obtenido.
	 */
	@SuppressWarnings({ "deprecation" })
	private FacesMessage faceRegistrarCategoria() {
		if (this.categoria != null && this.id != null && !this.id.equalsIgnoreCase(this.getCategoria().getId())) {
			this.categoria.setId(this.id);
		}
		this.message = validar(this.categoria, sesion.getLogeado());
		this.insert = false;
		if (message == null) {
			this.error = true;
			CategoriaController dao = new CategoriaController();
			Categoria aux = dao.find(this.categoria.getId());
			if (aux == null) {
				this.categoria.setNombre(this.categoria.getNombre().toUpperCase());
				aux = dao.findByField("nombre", this.categoria.getNombre());
				if (aux == null) {
					Fecha fecha = new Fecha();
					this.categoria.setNombre(this.categoria.getNombre().toUpperCase());
					this.categoria.setEstado(true);
					this.categoria.setFechaCreacion(new Date(fecha.fecha()));
					// INSERT
					dao.insert(this.categoria);
					this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							"Se ha registrado la categoria con el nombre " + this.categoria.getNombre() + ".");
					// RESET
					this.image.setImage(null);
					this.insert = true;
					this.active = false;
					this.search = false;
					this.error = false;
					this.table.initCategoria();
					this.initCategoria();
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"Ya existe una categoria con ese nombre " + this.categoria.getNombre() + ".");
				}

			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"Ya existe una categoria con ese ID " + this.id + ".");
			}
		}
		return this.message;
	}

	/**
	 * Metodo que permite actualizar una categoria.
	 * 
	 * @return representa el mensage generado.
	 */
	private FacesMessage faceActualizarCategoria() {
		this.message = validar(this.categoria, sesion.getLogeado());
		this.update = false;
		if (!this.error && this.index >= 0) {
			this.error = true;
			CategoriaController dao = new CategoriaController();
			if (this.categoria != null) {
				this.categoria.setNombre(this.categoria.getNombre().toUpperCase());
				Categoria aux = dao.findByField("nombre", this.categoria.getNombre());
				if (aux == null) {
					dao.update(this.categoria);
					// RESET
					this.table.getCategoria().set(this.index, this.categoria);
					message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							"Se ha actualizado la categoria con el ID " + this.categoria.getId() + ".");
					this.update = true;
					this.error = false;
					this.initCategoria();
					this.index = -1;
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"Ya existe ninguna categoria con ese nombre " + this.categoria.getNombre() + ".");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No existe ninguna categoria con ese nombre " + this.id + ".");
			}
		}
		return this.message;
	}

	/**
	 * Metodo que permite eliminar una categoria.
	 * 
	 * @return representa el mensage generado.
	 */
	private FacesMessage faceEliminarCategoria() {
		this.remove = false;
		this.categoria();
		if (!this.error && this.index >= 0) {
			CategoriaController dao = new CategoriaController();
			if (this.categoria != null) {
				ProductoController pDao = new ProductoController();
				List<Producto> list = pDao.findByFieldList("categoriaBean", this.categoria);
				if (list != null) {
					if (list.size() == 0) {
						dao.delete(this.categoria);
						this.table.getCategoria().remove(this.index);
						message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
								"Se ha eliminado la categoria con ID " + categoria.getId() + ".");
						this.remove = true;
						this.error = false;
						initCategoria();
						this.index = -1;
					} else {
						message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
								"No se ha eliminado la categora con ID " + categoria.getId()
										+ ",primero tienes que eliminar los productos que el tiene asociado que son "
										+ list.size() + " producto.");
					}
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"Se ha presentado un error a listar los productos de la categoria con el ID " + this.id
									+ ",vuelva a intentarlo.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No existe ninguna categoria con ese ID " + this.id + ".");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					"El ID " + this.id + " de la categoria no es valido.");
		}
		return this.message;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public SessionBean getSesion() {
		return sesion;
	}

	public void setSesion(SessionBean sesion) {
		this.sesion = sesion;
	}

	public DataTableBean getTable() {
		return table;
	}

	public void setTable(DataTableBean table) {
		this.table = table;
	}

	public ImageBean getImage() {
		return image;
	}

	public void setImage(ImageBean image) {
		this.image = image;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
