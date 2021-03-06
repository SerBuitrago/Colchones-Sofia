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

import com.bean.session.*;
import com.controller.*;
import com.model.*;
import com.model.other.*;
import com.util.*;

/**
 * Implementation AsistenteBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "asistente")
@ViewScoped
public class AsistenteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private FacesMessage message;

	private final String rol = "ASISTENTE ADMINISTRATIVO";

	private String estado;
	private int index;

	private boolean insert;
	private boolean search;
	private boolean update;
	private boolean remove;
	private boolean hidden;
	private boolean error;
	private boolean active;
	private boolean filtro_persona;

	private Usuario usuario;
	private String documento;
	private Usuario validar;

	private String clave;
	private Persona persona;

	///////////////////////////////////////////////////////
	// ManagedBean
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
		this.estado = "";
		this.index = -1;
		this.update = false;
		this.error = false;
		this.hidden = false;
		this.insert = false;
		this.remove = false;
		this.search = false;
		this.active = false;
		initAsistente();
	}

	/**
	 * Metodo que inicializa el asistente.
	 */
	public void initAsistente() {
		this.persona = null;
		this.clave = null;
		this.documento = null;
		this.validar = null;
		this.filtro_persona = false;
		this.usuario = new Usuario();
		this.usuario.setPersona(new Persona());
		this.usuario.getPersona().setTipoDocumentoBean(new TipoDocumento());
		this.usuario.setRolBean(new Rol());
		this.usuario.getRolBean().setRol("ASISTENTE ADMINISTRATIVO");
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
	// METODOS
	///////////////////////////////////////////////////////
	/**
	 * Metodo que genara KEY.
	 * 
	 * @return representa la KEY.
	 */
	public int generarKEY() {
		UsuarioController vDao = new UsuarioController();
		Usuario aux = vDao.ultimoAdd();
		if (vDao.ultimoAdd() != null)
			return Integer.parseInt(aux.getId()) + 1;
		return 1;
	}

	/**
	 * Metodo que permite conocer un asistente por su indice en la tabla.
	 */
	public void asistente() {
		this.message = null;
		this.error = true;
		this.index = -1;
		String documento = Face.get("documento-asistente");
		String tipo_documento = Face.get("tipo-documento-asistente");
		if (Convertidor.isCadena(documento) && Convertidor.isCadena(tipo_documento)) {
			this.index = index(documento, tipo_documento);
			if (this.index >= 0 && this.table.getAsistente() != null && this.index < this.table.getAsistente().size()) {
				this.usuario = asistente(this.table.getAsistente().get(this.index));
				this.clave = this.usuario.getClave();
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
	 * Metodo que permite buscar en una lista el indice de un usuario.
	 * 
	 * @param documento representa el documento usuario.
	 * @param tipo      representa el tipo documento usuario.
	 * @return representa el indice del asistente en la lista.
	 */
	public int index(String documento, String tipo) {
		if (Convertidor.isCadena(documento) && Convertidor.isCadena(tipo)) {
			int aux = 0;
			for (Usuario v : this.table.getAsistente()) {
				if (v.getPersona().getDocumento().equals(documento)) {
					if (v.getPersona().getTipoDocumentoBean().getTipoDocumento().equals(tipo)) {
						return aux;
					}
				}
				aux++;
			}
		}
		return -1;
	}

	/**
	 * Metodo que permite filtrar los datos de una persona por su documento.
	 */
	public void filtrar() {
		this.filtro_persona = false;
		this.search = false;
		this.error = true;
		this.active = false;
		this.validar = null;
		if (this.usuario != null && Convertidor.isCadena(this.usuario.getPersona().getDocumento())
				&& this.usuario.getPersona() != null && this.usuario.getPersona().getTipoDocumentoBean() != null
				&& Convertidor.isCadena(this.usuario.getPersona().getTipoDocumentoBean().getTipoDocumento())) {

			String documento = this.usuario.getPersona().getDocumento();
			String tipo_documento = this.usuario.getPersona().getTipoDocumentoBean().getTipoDocumento();

			UsuarioController dao = new UsuarioController();
			Usuario aux = dao.find(this.usuario.getPersona().getDocumento());

			if (aux == null) {
				PersonaController pDao = new PersonaController();
				this.usuario = new Usuario();
				this.usuario.setPersona(pDao.find(documento));
				if (this.usuario.getPersona() != null) {
					if (this.usuario.getPersona().getTipoDocumentoBean().getTipoDocumento().equals(tipo_documento)) {
						message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
								"Se han encontrado los datos de la persona este documento " + documento + ".");
						this.search = true;
						this.error = false;
						this.active = true;
						this.filtro_persona = true;
						this.validar = asistente(this.usuario);
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
		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	/**
	 * 
	 * @param v
	 * @return
	 */
	public Usuario asistente(Usuario v) {
		Usuario u = null;
		if (v != null && v.getPersona() != null && Convertidor.isCadena(v.getPersona().getDocumento())) {
			u = new Usuario();
			u.setId(v.getId());
			u.setPersona(new Persona());
			u.getPersona().setTipoDocumentoBean(new TipoDocumento());
			u.getPersona().setDocumento(v.getPersona().getDocumento());
			u.setEstado(v.getEstado());
			u.setFechaCreacion(v.getFechaCreacion());
			u.setPersona(v.getPersona());
			u.setRolBean(new Rol());
			u.getRolBean().setRol("ASISTENTE ADMINISTRATIVO");
		}
		return u;
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
	 * Metodo que desactiva los datos del usuario.
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
	 * Metodo que registra un usuario.
	 */
	public void registrar() {
		this.message = faceRegistrarAsistente();
		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * Metodo que permite actualizar un usuario.
	 */
	public void actualizar() {
		this.message = faceActualizarAsistente();
		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * Metodo que permite eliminar un usuario.
	 */
	public void eliminar() {
		this.message = faceEliminarAsistente();
		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * Metodo que cambia el valor del estado del usuario.
	 */
	public void estado() {
		this.update = false;
		String documento = Face.get("documento-asistente");
		if (Convertidor.isCadena(documento)) {
			UsuarioController dao = new UsuarioController();
			Usuario aux = dao.usuarioRol(rol, documento);
			if (aux != null) {
				boolean estado = (!aux.getEstado());
				aux.setEstado(estado);
				dao.update(aux); 
				this.table.initAsistente();
				this.update = true;
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha cambiado el estado al asistente con documento " + aux.getPersona().getDocumento()
								+ " a estado" + ((estado) ? " Activo." : " Bloqueado."));
				initTable();
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No existe ningun " + this.rol.toLowerCase() + " con documento " + documento + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"El documento del " + this.rol.toLowerCase() + " no se recibio.");
		}
		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	///////////////////////////////////////////////////////
	// Validator
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite validar los campos del asistente.
	 * 
	 * @param v representa el asistente.
	 * @param u representa el usuario logeado.
	 * @return el error obtenido.
	 */
	public FacesMessage validar(Usuario v, Usuario u) {
		this.message = null;
		this.error = true;
		if (u != null && Convertidor.isCadena(u.getPersona().getDocumento())) {
			if (v != null && Convertidor.isCadena(v.getPersona().getDocumento())) {
				if (v.getPersona() != null) {
					Persona p = v.getPersona();
					if (Convertidor.isCadena(p.getNombre())) {
						if (!Convertidor.containsNumber(p.getNombre())) {
							if (Convertidor.isCadena(p.getApellido())) {
								if (!Convertidor.containsNumber(p.getApellido())) {
									if (Convertidor.isCadena(p.getDireccion())) {
										if (Convertidor.isCadena(p.getEmail())) {
											if (Convertidor.isCadena(p.getTelefono())) {
												String numero = Convertidor.telefono(p.getTelefono());
												if (Convertidor.isNumber(numero)) {
													if (p.getFechaNacimiento() != null) {
														if (Convertidor.isCadena(p.getGenero())) {
															this.error = false;
															return null;
														} else {
															message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
																	"El campo genero es obligatorio.");
														}
													} else {
														message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
																"El campo fecha nacimiento es obligatoria.");
													}
												} else {
													message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
															"El campo telefono solo debe contener caracteres numericos.");
												}
											} else {
												message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
														"El campo telefono es obligatorio.");
											}
										} else {
											message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
													"El campo email es obligatorio.");
										}
									} else {
										message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
												"El campo dirección es obligatorio.");
									}
								} else {
									message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
											"El campo apellido no debe caracteres numericos.");
								}
							} else {
								message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
										"El campo apellido es obligatorio.");
							}
						} else {
							message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
									"El campo nombre no debe caracteres numericso.");
						}
					} else {
						message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo nombre es obligatorio.");
					}
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"Los datos personales son obligatorios.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo documento es obligatorio.");
			}

		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No te has logeado.");
		}
		return message;
	}

	///////////////////////////////////////////////////////
	// Statu
	///////////////////////////////////////////////////////
	/**
	 * Metodo que obtiene el estato del asistente a registrar.
	 */
	public void statuRegistrar() {
		System.out.println();
		this.estado = "Registrar";
		if (this.update) {
			this.initAsistente();
			this.update = false;
		}
		this.initDialogForm(1);
		this.hidden = false;
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

		String documento = Face.get("documento-asistente");
		if (Convertidor.isCadena(documento)) {
			UsuarioController dao = new UsuarioController();
			Usuario aux = dao.usuarioRol(this.rol, documento);
			if (aux != null) {
				this.usuario = aux;
				this.persona = new Persona();

				this.clave = this.usuario.getClave();
				this.persona.setEmail(this.usuario.getPersona().getEmail());
				this.persona.setTelefono(this.usuario.getPersona().getTelefono());

				if (Convertidor.isCadena(this.usuario.getPersona().getDocumento())) {
					this.estado = "Actualizar";
					this.error = false;
					this.documento = this.getUsuario().getPersona().getDocumento();
					this.initDialogForm(1);
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
							"El documento del " + this.rol.toLowerCase() + " no es valido.");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No existe ningun " + this.rol.toLowerCase() + " con el documento " + documento + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"El documento del " + this.rol.toLowerCase() + " no se recibio.");
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
	 * Metodo que permite insertar el usuario.
	 * 
	 * @return representa el mensaje obtenido.
	 */
	@SuppressWarnings({ "deprecation" })
	private FacesMessage faceRegistrarAsistente() {
		this.message = validar(this.usuario, sesion.getLogeado());
		this.insert = false;
		if (message == null) {
			this.error = true;
			PersonaController dao = new PersonaController();
			Persona p = dao.find(this.usuario.getPersona().getDocumento());

			if (p != null) {
				this.search = true;
				this.update = true;

				if (image.getImage() != null) {
					p.setFoto(image.getImage());
				}

				p.setApellido(this.usuario.getPersona().getApellido().toUpperCase());
				p.setNombre(this.usuario.getPersona().getNombre().toUpperCase());
				p.setEmail(this.usuario.getPersona().getEmail());
				p.setTelefono(Convertidor.telefono(this.usuario.getPersona().getTelefono()));
				System.out.println(p.getTelefono());
				p.setDireccion(this.usuario.getPersona().getDireccion());
				p.setGenero(this.usuario.getPersona().getGenero());

				boolean error = false;
				if (this.filtro_persona && this.validar != null) {
					ProveedorController pDao = new ProveedorController();
					if (!this.usuario.getPersona().getEmail().contentEquals(this.validar.getPersona().getEmail())
							|| !this.usuario.getPersona().getTelefono()
									.contentEquals(this.validar.getPersona().getTelefono())) {
						error = pDao.registrar(this.usuario.getPersona().getEmail(),
								this.usuario.getPersona().getTelefono());
					}
				}
				if (!error) {
					System.out.println(p.getTelefono());
					dao.update(p);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							"Se ha actualizado los datos de la persona con documento " + p.getDocumento() + "."));
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El email o telefono ya esta en uso."));
				}
				this.error = error;

			} else {
				p = this.usuario.getPersona();
				p.setTelefono(Convertidor.telefono(p.getTelefono())); 
				p.setApellido(p.getApellido().toUpperCase());
				p.setNombre(p.getNombre().toUpperCase());

				boolean error = false;
				ProveedorController pDao = new ProveedorController();

				if (this.validar != null) {
					this.validar.getPersona().setTelefono(Convertidor.telefono(this.validar.getPersona().getTelefono()));
					if (!Convertidor.equals(p.getEmail(), this.validar.getPersona().getEmail())) {
						error = pDao.registrar(this.usuario.getPersona().getEmail(), null);
					}else if(!Convertidor.equals(p.getTelefono(), this.validar.getPersona().getTelefono())) {
						error = pDao.registrar(null,this.usuario.getPersona().getTelefono());
					}
				} else {
					error = pDao.registrar(p.getEmail(), p.getTelefono());
				}

				if (!error) {
					if (image.getImage() != null) {
						p.setFoto(image.getImage());
					}
					dao.insert(p);
					this.table.initAsistente();
					p = dao.find(this.usuario.getPersona().getDocumento());
					if (p != null) {
						this.insert = true;
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"", "Se ha registrado la persona con documento " + p.getDocumento() + "."));
					}
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El email o telefono ya esta en uso."));
				}
				this.error = error;
			}

			this.image.setImage(null);

			this.insert = false;

			if (p != null && !this.error) {
				UsuarioController vDao = new UsuarioController();
				Usuario aux = vDao.usuarioRol(this.rol, p.getDocumento());
				if (aux == null) {
					this.error = true;
					String documento = p.getDocumento();
					p = new Persona();
					p.setDocumento(documento);
					this.usuario.setPersona(p);
					this.usuario.setEstado(true);
					Fecha fecha = new Fecha();
					this.usuario.setFechaCreacion(new Date(fecha.fecha()));
					this.usuario.setRolBean(new Rol());
					this.usuario.getRolBean().setRol(this.rol);
					this.usuario.setId(String.valueOf(generarKEY()));

					vDao.insert(this.usuario);

					this.insert = true;
					this.active = false;
					this.search = false;
					this.error = false;
					this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							"Se ha registrado el " + this.rol.toLowerCase() + " con documento "
									+ this.usuario.getPersona().getDocumento() + ".");
					this.table.setRenderizar_asistente(0);
					this.table.initAsistente();
					this.initAsistente();
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El usuario con documento "
							+ p.getDocumento() + ", ya esta registrado como " + this.rol.toLowerCase() + ".");
				}

			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No se ha registrado el usuario.");
			}
		}
		return message;
	}

	/**
	 * Metodo que permite actualizar un usuario.
	 * 
	 * @return representa el mensage generado.
	 */
	private FacesMessage faceActualizarAsistente() {
		this.message = validar(this.usuario, sesion.getLogeado());
		this.update = false;
		if (!this.error) {
			this.error = true;
			if (this.usuario != null) {
				UsuarioController dao = new UsuarioController();

				this.usuario.getPersona().setTelefono(Convertidor.telefono(this.usuario.getPersona().getTelefono()));
				Persona a = this.persona;

				System.out.println("USUARIO : " + this.usuario);
				System.out.println("Persona: " + a);

				if (a != null) {
					boolean seguir = false;
					ProveedorController pDao = new ProveedorController();
					if (!Convertidor.equals(this.usuario.getPersona().getEmail(), a.getEmail())) {
						seguir = pDao.registrar(this.usuario.getPersona().getEmail(), null);
					} else if (!Convertidor.equals(this.usuario.getPersona().getTelefono(), a.getTelefono())) {
						seguir = pDao.registrar(null, this.usuario.getPersona().getTelefono());
					}
					if (!seguir) {
						if (!Convertidor.isCadena(this.usuario.getClave()) && Convertidor.isCadena(this.clave)) {
							this.usuario.setClave(this.clave);
						}
						this.usuario.getPersona().setApellido(this.usuario.getPersona().getApellido().toUpperCase());
						this.usuario.getPersona().setNombre(this.usuario.getPersona().getNombre().toUpperCase());
						if (image.getImage() != null) {
							this.usuario.getPersona().setFoto(this.image.getImage());
						}
						dao.update(this.usuario);
						PersonaController sdao = new PersonaController();
						sdao.update(this.usuario.getPersona());
						this.table.initAsistente();
						message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
								"Se ha actualizado el " + this.rol.toLowerCase() + " con documento "
										+ this.usuario.getPersona().getDocumento() + ".");
						this.update = true;
						this.error = false;
						this.image.setImage(null);
						initAsistente();
						this.index = -1;
					} else {
						message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
								"El email o telefono ya esta en uso.");
					}
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"No se ha podido obtener el " + this.rol.toLowerCase() + ".");
				}

			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No existe ningun asistente con ese documento " + this.usuario.getPersona().getDocumento()
								+ ".");
			}
		}
		return message;
	}

	/**
	 * Metodo que permite eliminar un usuario.
	 * 
	 * @return representa el mensage generado.
	 */
	private FacesMessage faceEliminarAsistente() {
		this.remove = false;
		String documento = Face.get("documento-asistente");
		if (Convertidor.isCadena(documento)) {
			UsuarioController dao = new UsuarioController();
			Usuario aux = dao.usuarioRol(this.rol, documento);
			if (aux != null) {
				dao.delete(aux);
				this.table.initAsistente();
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Se ha eliminado el "
						+ this.rol.toLowerCase() + " con documento " + aux.getPersona().getDocumento() + ".");
				this.remove = true;
				this.error = false;
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No existe ningun " + this.rol.toLowerCase() + " con el documento " + documento + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"El documento del " + this.rol.toLowerCase() + " no se recibio.");
		}
		return message;
	}

	///////////////////////////////////////////////////////
	// Getter and Setter
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

	public boolean isFiltro_persona() {
		return filtro_persona;
	}

	public void setFiltro_persona(boolean filtro_persona) {
		this.filtro_persona = filtro_persona;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getValidar() {
		return validar;
	}

	public void setValidar(Usuario validar) {
		this.validar = validar;
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

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public String getRol() {
		return rol;
	}
}