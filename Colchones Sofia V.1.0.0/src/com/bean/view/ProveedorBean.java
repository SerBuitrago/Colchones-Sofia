package com.bean.view;

import java.io.Serializable;
import java.util.ArrayList;
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
 * 
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

	private Persona persona;

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
		this.id ="";
		this.persona = null;
		this.proveedor = new Proveedor();
		this.proveedor.setPersona(new Persona());
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
	 * Metodo que activa el dialogo de productos de un proveedor en especifico.
	 */
	public void productos() {
		PrimeFaces current = PrimeFaces.current();
		this.proveedor();
		if (proveedor != null) {
			current.executeScript("PF('sofia-dialog-product-update').show();");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No has seleccionado ningun proveedor."));
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
				ProveedorController dao = new ProveedorController();
				this.proveedor = dao.find(id);
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

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite buscar en una lista el indice de un proveedor.
	 * 
	 * @param id representa el id del proveedor.
	 * @return representa el indice del proveedor en la lista.
	 */
	public int index(String documento) {
		int aux = 0;
		for (Proveedor p : this.table.getProveedor()) {
			if (Convertidor.equals(p.getDocumento(), documento)) {
				return aux;
			}
			aux++;
		}
		return -1;
	}

	/**
	 * Metodo que permite activar el formulario.
	 */
	public void activar() {
		if (!this.active) {
			this.active = true;
		} else {
			this.active = false;
		}
	}

	/**
	 * Metodo que desactiva los datos del proveedor.
	 */
	public void desactivar() {
		if (this.search) {
			this.initProveedor();
			this.search = false;
			this.error = false;
		}
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
	 * Metodo que permite actualizar un proveedor.
	 */
	public void actualizar() {
		this.message = faceActualizarProveedor();
		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * Metodo que permite eliminar un proveedor.
	 */
	public void eliminar() {
		FacesContext.getCurrentInstance().addMessage(null, faceEliminarProveedor());
	}

	/**
	 * Metodo que agrega todos los productos seleccionado a un proveedor.
	 */
	public void addProductos() {
		PrimeFaces current = PrimeFaces.current();
		this.insert = false;
		this.select = null;
		this.message = null;
		if (this.getProveedor() != null && Convertidor.isCadena(this.getProveedor().getDocumento())) {
			if (this.seleccionadas != null && this.seleccionadas.size() > 0) {
				this.select = new String[this.seleccionadas.size()];
				int index = 0;
				for (DetalleProducto p : this.seleccionadas) {
					this.select[index] = String.valueOf(p.getId());
					index++;
				}
				FacesContext.getCurrentInstance().addMessage(null, faceRegistrarProductos(this.proveedor));
				this.table.initProveedor();
				this.initProveedor();
				this.id = "";
				this.error = false;
				this.seleccionadas = new ArrayList<DetalleProducto>();
				current.executeScript("PF('sofia-dialog-product-update').hide();");
				return;
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No has seleccionado ningun producto.");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No has seleccionado ningun proveedor");
		}
		current.executeScript("PF('sofia-dialog-product-update').hide();");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	/**
	 * Metodo que cambia el valor del estado del proveedor.
	 */
	public void estado() {
		this.update = false;
		String id = Face.get("id-proveedor");
		if (Convertidor.isCadena(id)) {
			ProveedorController dao = new ProveedorController();
			Proveedor aux = dao.find(id);
			if (aux != null) {
				boolean estado = (!aux.getEstado());
				aux.setEstado(estado);
				dao.update(aux);
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha cambiado el estado al proveedor con documento " + aux.getDocumento() + " a estado"
								+ ((estado) ? " Activo." : " Bloqueado."));
				this.table.initProveedor();
				this.update = true;
				initTable();
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No existe ningun proveedor con el ID " + id + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No ID del proveedor no es valido.");
		}

		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * Metodo que permite eliminar un detalle producto de un proveedor.
	 */
	public void eliminarDetalleProducto() {
		this.message = null;
		String id_proveedor = Face.get("id-proveedor");
		String id_detalle = Face.get("id-detalle-producto");
		if (Convertidor.isCadena(id_proveedor) && Convertidor.isCadena(id_detalle)) {
			if (Convertidor.isNumber(id_detalle)) {
				ProveedorController dao = new ProveedorController();
				Proveedor proveedor = dao.find(id_proveedor);
				if (proveedor != null) {
					DetalleProductoController dpDao = new DetalleProductoController();
					DetalleProducto producto = dpDao.find(Integer.parseInt(id_detalle));
					if (producto != null) {
						dao.eliminarDetalleProductoProveedor(proveedor, producto);
						this.table.initProveedor();
						this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
								"Se le ha eliminado al proveedor con documento " + id_proveedor
										+ " el detalle de product con ID " + id_detalle + ".");
						this.table.initProveedor();
					} else {
						this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
								"No existe ningun detalle producto con ID " + id_detalle + ".");
					}
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"No existe ningun proveedor con documento " + id_proveedor + ".");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
						"El detalle producto con ID " + id_detalle + " debe contener pueros caracteres numericos.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"El proveedor o detalle de producto no son validos.");
		}
		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	///////////////////////////////////////////////////////
	// Validator
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
	 * Metodo que permite verificar si existe un proveedor ya tiene asociado un
	 * producto.
	 * 
	 * @param pp representa el proveedor.
	 * @param p  representa el producto.
	 * @return true si lo tiene asociado false si no.
	 */
	public boolean existe(List<DetalleProducto> pp, DetalleProducto p) {
		if (pp != null && p != null && p.getId() > 0) {
			for (DetalleProducto aux : pp) {
				if (aux.getId() == p.getId()) {
					return true;
				}
			}
		}
		return false;
	}

	///////////////////////////////////////////////////////
	// Statu
	///////////////////////////////////////////////////////
	/**
	 * Metodo que obtiene el estato del proveedor a registrar.
	 */
	public void statuRegistrar() {
		if (this.update) {
			this.initProveedor();
			this.update = false;
		}
		this.initDialogForm(1);
		this.hidden = false;
		this.estado = "Registrar";
	}

	/**
	 * Metodo que obtiene el estato del proveedor a actualizar.
	 */
	public void statuActualizar() {
		this.update = true;
		this.hidden = true;
		this.initProveedor();
		this.message = null;
		this.error = true;
		this.id = Face.get("id-proveedor");
		if (Convertidor.isCadena(id)) {
			ProveedorController dao = new ProveedorController();
			this.proveedor = dao.find(id);
			this.id = this.proveedor.getDocumento();
			
			if (this.proveedor != null) {
				
				this.estado = "Actualizar";
				
				this.persona = new Persona();
				this.persona.setNombre(this.proveedor.getPersona().getNombre());
				this.persona.setTelefono(Convertidor.telefono(this.proveedor.getPersona().getTelefono()));
				this.persona.setEmail(this.proveedor.getPersona().getEmail());
				
				if (this.id != null && persona != null) {
					this.error = false;
					this.initDialogForm(1);
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"El ID " + this.id + " del proveedor no es valido.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No se ha obtenido el ID proveedor.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El ID del proveedor no es valido.");
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
	// Renderizar
	///////////////////////////////////////////////////////
	/**
	 * Metodo que consulta todos los productos.
	 * 
	 * @return una lista con todos los productos.
	 */
	public List<DetalleProducto> getProductos() {
		if (this.proveedor != null) {
			DetalleProductoController dao = new DetalleProductoController();
			List<DetalleProducto> categorias = dao.list(); 
			this.productos = new ArrayList<DetalleProducto>();
			List<DetalleProducto> ppp = this.proveedor.getDetalleProductos();
			if (ppp != null) {
				for (DetalleProducto c : categorias) {
					if (c.getProductoBean().getEstado()) {
						if (!existe(ppp, c)) {
							this.productos.add(c);
						}
					}
				}
			}
			return productos;
		}
		return new ArrayList<DetalleProducto>();
	}

	///////////////////////////////////////////////////////
	// Method Face
	///////////////////////////////////////////////////////

	/**
	 * Metodo que permite actualizar un proveedor.
	 * 
	 * @return representa el mensage generado.
	 */

	private FacesMessage faceActualizarProveedor() {
		this.message = validar(this.proveedor, sesion.getLogeado());
		this.update = false;
		if (!this.error) {
			this.error = true;
			
			ProveedorController dao = new ProveedorController();
			PersonaController pDao = new PersonaController();
			Persona a = this.persona;

			if (this.proveedor != null) {

				this.proveedor.getPersona().setNombre(this.proveedor.getPersona().getNombre().toUpperCase());
				this.proveedor.getPersona()
						.setTelefono(Convertidor.telefono(this.proveedor.getPersona().getTelefono()));
				this.proveedor.getPersona().setEmail(this.proveedor.getPersona().getEmail());

				if (a != null) {
					boolean seguir = false;
					Persona aux;
					if (!Convertidor.equals(this.proveedor.getPersona().getEmail(), a.getEmail())) {
						aux = new Persona();
						aux.setEmail(this.proveedor.getPersona().getEmail());
						aux.setTelefono(null);
						aux.setNombre(null);
						seguir = dao.registrar(aux);
					} else if (!Convertidor.equals(this.proveedor.getPersona().getNombre(), a.getNombre())) {
						aux = new Persona();
						aux.setEmail(null);
						aux.setTelefono(null);
						aux.setNombre(this.proveedor.getPersona().getNombre());
						seguir = dao.registrar(aux);
					} else if (!Convertidor.equals(this.proveedor.getPersona().getTelefono(), a.getTelefono())) {
						aux = new Persona();
						aux.setEmail(null);
						aux.setTelefono(this.proveedor.getPersona().getTelefono());
						aux.setNombre(null);
						seguir = dao.registrar(aux);
					}

					if (!seguir) {
						if (this.image.getImage() != null) {
							this.proveedor.getPersona().setFoto(this.image.getImage());
						}

						dao.update(this.proveedor);
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"", "Se ha actualizado el proveedor con ID." + this.id + "."));

						pDao.update(this.proveedor.getPersona());

						message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
								"Se ha actualizado la persona con documento " + this.proveedor.getDocumento() + ".");

						this.table.initProveedor();
						this.update = true;
						this.error = false;
						this.image.setImage(null);
						this.initProveedor();
						this.index = -1;
					} else {
						this.update = true;
						this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
								"El telefono,nombre o email del proveedor ya se encuentra registrado.");
					}
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"No existe ninguna persona asosiada al proveedor con el ID." + this.id + ".");
				}

			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
						"No existe ningun proveedor con ese ID." + this.id + ".");
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
				ProveedorController dao = new ProveedorController();
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
	 * 
	 * @return
	 */
	@SuppressWarnings({ "deprecation" })
	private FacesMessage faceRegistrarProveedor() {

		if (this.proveedor != null && this.id != null && this.proveedor.getDocumento() != null
				&& !this.id.contentEquals(this.proveedor.getDocumento())) {
			this.proveedor.setDocumento(this.id);
		} else if (this.proveedor.getDocumento() == null) {
			this.proveedor.setDocumento(this.id);
		}

		this.message = validar(this.proveedor, sesion.getLogeado());
		this.insert = false;
		if (message == null) {
			this.error = true;
			ProveedorController pDao = new ProveedorController();
			Proveedor aux2 = pDao.find(this.proveedor.getDocumento());
			if (aux2 == null) {
				Persona aux = new Persona();
				aux.setNombre(this.proveedor.getPersona().getNombre().toUpperCase());
				aux.setTelefono(Convertidor.telefono(this.proveedor.getPersona().getTelefono()));
				aux.setEmail(this.proveedor.getPersona().getEmail());
				if (!pDao.registrar(aux)) {
					PersonaController dao = new PersonaController();
					aux = dao.find(this.id);
					this.error = true;
					if (aux == null) {
						aux = new Persona();
						aux.setDocumento(this.proveedor.getDocumento());
						aux.setNombre(this.proveedor.getPersona().getNombre().toUpperCase());
						aux.setTelefono(Convertidor.telefono(this.proveedor.getPersona().getTelefono()));
						aux.setEmail(this.proveedor.getPersona().getEmail());
						aux.setDireccion(this.proveedor.getPersona().getDireccion());

						if (this.image.getImage() != null) {
							aux.setFoto(this.image.getImage());
						}

						this.error = false;
						dao.insert(aux);
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"", "Se registro la persona con documento " + aux.getDocumento() + "."));
					} else {
						aux.setNombre(this.proveedor.getPersona().getNombre().toUpperCase());
						aux.setTelefono(Convertidor.telefono(this.proveedor.getPersona().getTelefono()));
						aux.setEmail(this.proveedor.getPersona().getEmail());
						aux.setDireccion(this.proveedor.getPersona().getDireccion());

						if (this.image.getImage() != null) {
							aux.setFoto(this.image.getImage());
						}

						this.error = false;
						dao.update(aux);
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"", "Se actualizado la persona con documento " + aux.getDocumento() + "."));
					}
					if (!this.error) {
						pDao = new ProveedorController();
						this.proveedor.setPersona(aux);
						Fecha fecha = new Fecha();
						this.proveedor.setEstado(true);
						this.proveedor.setFechaCreacion(new Date(fecha.fecha()));
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
						this.id = "";
						this.initProveedor();
					} else {
						this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
								"Ocurrio un error al procesar la persona.");
					}

				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"El telefono,nombre o email del proveedor ya se encuentra registrado.");
				}

			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"Ya existe un proveedor con ese documento " + this.id + ".");
			}
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
			ProveedorController dao = new ProveedorController();
			if (this.proveedor != null) {
				if (this.proveedor.getCompras() != null && this.proveedor.getCompras().size() == 0) {
					if (this.productos != null) {
						if (this.proveedor.getDetalleProductos().size() == 0) {
							dao.delete(this.proveedor);
							message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
									"Se ha eliminado el proveedor documento " + proveedor.getDocumento() + ".");
							this.remove = true;
							this.table.initProveedor();
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

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}
}
