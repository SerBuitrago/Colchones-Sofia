package com.bean.view;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

import com.bean.session.ImageBean;
import com.bean.session.SessionBean;
import com.dao.PersonaDao;
import com.dao.RolDao;
import com.dao.UsuarioDao;
import com.entity.*;
import com.util.Face;
import com.util.Fecha;

/**
 * Implementation CategoriaBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "asistente")
@ViewScoped
public class AsistenteBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private FacesMessage message;

	private String estado;
	private int index;
	private boolean insert;
	private boolean search;
	private boolean update;
	private boolean remove;
	private boolean hidden;
	private boolean error;
	private boolean active;

	private Usuario usuario;
	
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
	// Builders
	///////////////////////////////////////////////////////
	public AsistenteBean() {
	}
	
	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
	initAsistente();
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
	* Metodo que inicializa el asistente.
	*/
	public void initAsistente() {
	this.usuario = new Usuario();
	this.usuario.setPersona(new Persona());
	this.usuario.getPersona().setTipoDocumento(new TipoDocumento());
	this.usuario.setRol(new Rol ());
	this.usuario.getRol().setNombre("Asistente Administrivo");
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
	// Metodo
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer un asistente por su indice en la tabla.
	 */
	public void asistente() {
		this.message = null;
		this.error = true;
		this.index = -1;
		String documento = Face.get("documento-asistente");
		String tipo_documento = Face.get("tipo-documento-asistente");
		if (documento != null && documento.length() > 0 && tipo_documento != null && tipo_documento.length() > 0) {
			this.index = index(Integer.parseInt(documento), tipo_documento);
			if (this.index >= 0 && this.table.getAsistente() != null && this.index < this.table.getAsistente().size()) {
				this.usuario = this.table.getAsistente().get(this.index);
				if (this.usuario != null) {
					this.error = false;
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"El asistente no se encuentra registrado.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El asistente no se encuentra registrado.");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se ha seleccionado ningun asistente.");
		}
	}

	
	/**
	 * Metodo que permite buscar en una lista el indice de un asistente.
	 * 
	 * @param documento representa el documento asistente.
	 * @param tipo      representa el tipo documento asistente.
	 * @return representa el indice del asistente en la lista.
	 */
	public int index(int documento, String tipo) {
		int aux = 0;
		for (Usuario u : this.table.getAsistente()) {
			if (u.getDocumento() == documento) {
				if (u.getPersona().getTipoDocumento().getNombre().equals(tipo)) {
					return aux;
				}
			}
			aux++;
		}
		return -1;
	}
	
	/**
	 * Metodo que permite filtrar los datos de una persona por su documento.
	 */
	public void filtrar() {
		this.search = false;
		this.error = true;
		this.active = false;
		if (this.usuario != null && this.usuario.getDocumento() > 0) {
			int documento = this.usuario.getDocumento();
			String tipo_documento = this.usuario.getPersona().getTipoDocumento().getNombre();
			UsuarioDao dao = new UsuarioDao();
			this.usuario = dao.find(this.usuario.getDocumento());
			if (this.usuario == null) {
				PersonaDao pDao = new PersonaDao();
				this.usuario = new Usuario();
				this.usuario.setDocumento(documento);
				this.usuario.setPersona(pDao.find(documento));
				if (this.usuario.getPersona() != null) {
					if (this.usuario.getPersona().getTipoDocumento().getNombre().equals(tipo_documento)) {
						message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
								"Se han encontrado los datos de la persona este documento " + documento + ".");
						this.search = true;
						this.error = false;
						this.active = true;
					} else {
						message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
								"No se han encontrado la persona con este documento " + documento + ".");
					}
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
							"No se han encontrado los datos de la persona con este documento " + documento + ".");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"Ya esta registrado un asistente con ese documento " + documento + ".");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo documento es obligatorio.");
		}

		if (this.error) {
			initAsistente();
		}
		FacesContext.getCurrentInstance().addMessage(null, message);
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
	 * Metodo que desactiva los datos del vendedor.
	 */
	public void desactivar() {
		if (this.search) {
			this.initAsistente();
			this.search = false;
			this.error = false;
		}
	}
	
	///////////////////////////////////////////////////////
	// CRUD
	///////////////////////////////////////////////////////
	/**
	* Metodo que registra un asistente.
	*/
	public void registrar() {
		FacesContext.getCurrentInstance().addMessage(null, faceRegistrarAsistente());
	}
	
	/**
	 * Metodo que permite actualizar un asistente.
	 */
	public void actualizar() {
		FacesContext.getCurrentInstance().addMessage(null, faceActualizarAsistente());
	}

	/**
	 * Metodo que permite eliminar un asistente.
	 */
	public void eliminar() {
		FacesContext.getCurrentInstance().addMessage(null, faceEliminarAsistente());
	}

	/**
	 * Metodo que cambia el valor del estado del asistente.
	 */
	@SuppressWarnings("deprecation")
	public void estado() {
		this.update = false;
		this.asistente();
		if (!this.error && this.index >= 0) {
			UsuarioDao dao = new UsuarioDao();
			boolean estado = usuario.getEstado();
			estado = (estado) ? false : true;
			this.usuario.setEstado(estado);
			Fecha fecha = new Fecha();  
			this.usuario.getPersona().setFechaActualizada(new Date(fecha.fecha()));         //REVISAR 
			this.table.getAsistente().set(this.index, this.usuario);
			this.update = true;
			dao.update(this.usuario);          
			this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"Se ha cambiado el estado al asistente con documento " + usuario.getDocumento() + " a estado"
							+ ((estado) ? " Activo." : " Bloqueado."));
			// Init
			initTable();
			this.index = -1;
			this.initAsistente();
		}
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}
	
///////////////////////////////////////////////////////
// Validator
///////////////////////////////////////////////////////
	/**
	 * Metodo que valida los datos del asistente.
	 * 
	 * @param u Representa el usuario.
	 */
	public FacesMessage validar(Usuario u) {
		this.message = null;
		this.error = true;
		if (u != null && u.getDocumento() > 0) {
				if (u.getPersona() != null) {
					Persona p = u.getPersona();
					if (p.getNombre() != null && p.getNombre().length() > 0) {
						if (p.getApellido() != null && p.getApellido().length() > 0) {
							if (p.getDireccion() != null && p.getDireccion().length() > 0) {
								if (p.getEmail() != null && p.getEmail().length() > 0) {
									if (p.getTelefono() != null && p.getTelefono().length() > 0) {
										if (p.getNacimiento() != null) {
											if (p.getGenero() != null && p.getGenero().length() > 0) {
												this.error = false;
												return null;
											} else {
												message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
														"El sexo es obligatorio.");
											}
										} else {
											message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
													"La fecha nacimiento es obligatoria.");
										}
									} else {
										message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
												"El telefono es obligatorio.");
									}
								} else {
									message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
											"El email es obligatorio.");
								}
							} else {
								message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
										"La dirección es obligatorio.");
							}
						} else {
							message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El apellido es obligatorio.");
						}
					} else {
						message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El nombre es obligatorio.");
					}
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"Los datos personales son obligatorios.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El documento del asistente es obligatorio.");
			}
		return message;   //??
	}
	
///////////////////////////////////////////////////////
// Statu
///////////////////////////////////////////////////////
	/**
	 * Metodo que obtiene el estato del asistente a registrar.
	 */
	public void statuRegistrar() {
		if (this.update) {
			this.initAsistente();
			this.update = false;
		}
		this.initDialogForm(1);
		this.hidden = false;
		this.estado = "Registrar";
	}

	/**
	 * Metodo que obtiene el estato del asistente a actualizar.
	 */
	public void statuActualizar() {
		this.update = true;
		this.hidden = true;
		initAsistente();
		this.message = null;
		this.error = true;
		this.asistente();
		if (!this.error && this.index >= 0) {
			this.estado = "Actualizar";
			if (this.usuario.getDocumento() > 0) {
				this.error = false;
				this.initDialogForm(1);
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"El documento " + this.usuario.getDocumento() + " del asistente no es valido.");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No se ha obtenido el documento asistente.");
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
	// FACE
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite insertar el asistente.
	 * 
	 * @return representa el mensaje obtenido.
	 */
	@SuppressWarnings({ "deprecation" })
	private FacesMessage faceRegistrarAsistente() {
		this.message = validar(this.usuario);
		this.insert = false;
		if (message == null) {
			this.error = true;
			PersonaDao dao = new PersonaDao();
			Persona p = dao.find(this.usuario.getDocumento());	
			System.out.println(p);
			System.out.println(this.usuario);
			if (p != null) {
				this.search = true;
				this.update = true;
				Fecha fecha = new Fecha();
				if (image.getImage() != null) {
					p.setFoto(image.getImage());
				}
				p.setFechaActualizada(new Date(fecha.fecha()));
				p.setApellido(p.getApellido().toUpperCase());
				p.setNombre(p.getNombre().toUpperCase());
				dao.update(p);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha actualizado los datos de la persona con documento " + p.getDocumento() + "."));
			} else {
				p = this.usuario.getPersona();
				p.setApellido(p.getApellido().toUpperCase());
				p.setNombre(p.getNombre().toUpperCase());
				p.setDocumento(this.usuario.getDocumento());
				if (image.getImage() != null) {
					p.setFoto(image.getImage());
				}
				Fecha fecha = new Fecha();
				p.setFechaRegistro(new Date(fecha.fecha()));
				dao.insert(p);
				p = dao.find(this.usuario.getDocumento());
				if (p != null) {
					this.insert = true;
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							"Se ha registrado la persona con documento " + p.getDocumento() + "."));
				}
			}
			this.image.setImage(null);
			this.insert = false;
			this.error = true;
			Persona w = p;
			p = new Persona();
			registrarAsistente(w);
		}
		return message;
	}
	
	public void registrarAsistente(Persona w) {
		if (w != null) {
			System.out.println(this.usuario.getClave());
			Usuario asistente = new Usuario();
			asistente.setPersona(w);
			asistente.setDocumento(w.getDocumento());
			asistente.setEstado(true);
			asistente.setClave(this.usuario.getClave());
			RolDao rdao = new RolDao();
			asistente.setRol(rdao.find("Asistente Administrativo"));
			Fecha fecha = new Fecha();
			asistente.setFechaRegistro(new Date(fecha.fecha())); 
			this.initAsistente();
			registrarUsuario(asistente);
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No se ha registrado el asistente.");
		}
	}
	
	public void registrarUsuario(Usuario usu) {
		UsuarioDao udao = new UsuarioDao();
		udao.insert(usu);                                              
		this.insert = true;
		this.active = false;
		this.search = false;
		this.error = false;
		this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
				"Se ha registrado el asistente con documento " + usu.getDocumento() + ".");
		this.table.initAsistente();
		usu = new Usuario();
		
	}
	
	/**
	 * Metodo que permite actualizar un asistente.
	 * 
	 * @return representa el mensage generado.
	 */
	@SuppressWarnings("deprecation")

	private FacesMessage faceActualizarAsistente() {
		this.message = validar(this.usuario);
		this.update = false;
		if (!this.error && this.index >= 0) {
			this.error = true;
			this.usuario = table.getAsistente().get(this.index);
			if (this.usuario != null) {
				UsuarioDao dao = new UsuarioDao();
				Fecha fecha = new Fecha();
				this.usuario.getPersona().setFechaActualizada(new Date(fecha.fecha()));                         
				//this.usuario.setUsuario(this.sesion.getLogeado());                                            
				this.usuario.getPersona().setApellido(this.usuario.getPersona().getApellido().toUpperCase());
				this.usuario.getPersona().setNombre(this.usuario.getPersona().getNombre().toUpperCase());
				if (image.getImage() != null) {
					this.usuario.getPersona().setFoto(this.image.getImage());
				}
				this.table.getAsistente().set(this.index, this.usuario);
				dao.update(this.usuario);
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha actualizado el asistente con documento " + this.usuario.getPersona().getDocumento() + ".");
				this.update = true;
				this.error = false;
				this.image.setImage(null);
				initAsistente();
				this.index = -1;
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No existe ningun asistente con ese documento " + this.usuario.getDocumento() + ".");
			}
		}
		return message;
	}
	
	/**
	 * Metodo que permite eliminar un asistente.
	 * 
	 * @return representa el mensage generado.
	 */
	private FacesMessage faceEliminarAsistente() {
		this.remove = false;
		this.asistente();
		if (!this.error && this.index >= 0) {
			this.error = true;
			if (this.usuario.getVentas() != null && this.usuario.getVentas().size() == 0) {
				UsuarioDao dao = new UsuarioDao();
				dao.delete(this.usuario);
				this.table.getAsistente().remove(this.index);
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha eliminado el asistente con documento " + usuario.getDocumento() + ".");
				this.remove = true;
				this.error = false;
				initAsistente();
				this.index = -1;
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No se ha podido eliminar al asistente con documento " + this.usuario.getDocumento()
								+ ",esta asociado a " + this.usuario.getVentas().size() + " ventas.");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El asistente no fue encontrado.");
		}

		return message;
	}
	
	

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
}
