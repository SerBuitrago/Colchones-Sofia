package com.bean.view;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import javax.annotation.PostConstruct;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;

import com.bean.session.*;
import com.controller.*;
import com.model.*;
import com.model.other.*;

/**
 * Implementation SesionBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "usuario")
@ViewScoped
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private FacesMessage mensaje;

	private boolean cambiar_imagen;
	private boolean selecciono_imagen;

	private Usuario usuario;

	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean sesion;

	@ManagedProperty("#{image}")
	private ImageBean image;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public UsuarioBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		this.mensaje = null;
		this.usuario = null;
		this.cambiar_imagen = false;
		this.selecciono_imagen = false;
		this.obtenerUsuario();
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite intercambiar la información del usuario logeado en el
	 * usuario a editar.
	 */
	public void obtenerUsuario() {
		this.usuario = null;
		this.cambiar_imagen = false;
		if (this.sesion != null && this.sesion.getLogeado() != null) {
			UsuarioController dao = new UsuarioController();
			this.usuario = dao.find(this.sesion.getLogeado().getId());
			this.image.setImage(null);
			this.image.setPerfil(new DefaultStreamedContent());
		}
	}

	/**
	 * Metodo que permite cambiar la imagen.
	 */
	@SuppressWarnings("deprecation")
	public void uploadImage(FileUploadEvent event) {
		this.image.setImage(null);
		this.cambiar_imagen = false;
		this.image.setPerfil(null);
		this.mensaje = this.image.uploadImage(event.getFile());

		if (this.mensaje != null && !this.image.isError()) {
			this.cambiar_imagen = true;
			this.image.setPerfil(new DefaultStreamedContent(new ByteArrayInputStream(this.image.getImage())));
		}
		if (this.image.getPerfil() == null) {
			this.cambiar_imagen = false;
		}
		if (mensaje != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensaje);
		}
	}

	///////////////////////////////////////////////////////
	// CRUD
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite modificar un usuario.
	 */
	public void modificar() {
		this.mensaje = validator(this.usuario);
		if (mensaje == null) {
			PersonaController dao = new PersonaController();
			Persona persona = this.usuario.getPersona();
			persona.setTelefono(Convertidor.telefono(persona.getTelefono()));
			ProveedorController pDao = new ProveedorController();
			boolean error = false;

			System.out.println(this.sesion.getLogeado().getPersona());
			System.out.println(persona);

			if (!Convertidor.equals(this.sesion.getLogeado().getPersona().getEmail(), persona.getEmail())) {
				error = pDao.registrar(persona.getEmail(), null);
			} else if (!Convertidor.equals(this.sesion.getLogeado().getPersona().getTelefono(),
					persona.getTelefono())) {
				error = pDao.registrar(null, persona.getTelefono());
			}

			if (!error) {
				persona.setNombre(persona.getNombre().toUpperCase());
				persona.setApellido(persona.getApellido().toUpperCase());

				if (this.image.getImage() != null) {
					persona.setFoto(this.image.getImage());
				}
				dao.update(persona);

				UsuarioController uDao = new UsuarioController();
				this.usuario.setPersona(persona);
				this.obtenerUsuario();
				uDao.update(this.usuario);
				this.sesion.setLogeado(this.usuario);
				this.mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes", "Se ha actualizado el usuario.");
			} else {
				this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El telefono o email ya esta en uso.");
			}
		}
		FacesContext.getCurrentInstance().addMessage(null, this.mensaje);
	}

	///////////////////////////////////////////////////////
	// Validator
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite validar los datos de un usuario.
	 * 
	 * @param usuario representa el usuario.
	 * @return representa el error obtenido.
	 */

	///////////////////////////////////////////////////////
	// Validator
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite validar los datos de un usuario.
	 * 
	 * @param usuario representa el usuario.
	 * @return representa el error obtenido.
	 */
	public FacesMessage validator(Usuario usuario) {
		this.mensaje = null;
		if (usuario != null) {
			if (usuario.getPersona().getDocumento() != null) {
				Persona persona = usuario.getPersona();
				if (persona.getTipoDocumentoBean() != null && persona.getTipoDocumentoBean().getTipoDocumento() != null
						&& persona.getTipoDocumentoBean().getTipoDocumento().length() > 0) {
					if (persona.getNombre() != null && persona.getNombre().length() > 0) {
						if (!Convertidor.containsNumber(usuario.getPersona().getNombre())) {
							if (Convertidor.isCadena(usuario.getPersona().getApellido())) {
								if (!Convertidor.containsNumber(usuario.getPersona().getApellido())) {
									if (persona.getEmail() != null && persona.getEmail().length() > 0) {
										if (Convertidor.isCadena(persona.getDireccion())) {
											if (Convertidor.isCadena(persona.getTelefono())) {
												String numero = Convertidor.telefono(persona.getTelefono());
												if (Convertidor.isNumber(numero)) {
													if (Convertidor.isCadena(persona.getGenero())) {
														return null;
													} else {
														this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN,
																"Warn", "El campo genero es obligatorio.");
													}
												} else {
													this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
															"El campo telefono solo puede tener caracteres numericos.");
												}
											} else {
												this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
														"El campo telefono es obligatorio.");
											}
										} else {
											this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
													"El campo dirección es obligatorio.");
										}
									} else {
										this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
												"El campo email es obligatorio.");
									}
								} else {
									this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
											"El campo apellido no debe tener caracteres numericos.");
								}
							} else {
								this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
										"El campo apellido es obligatorio.");
							}
						} else {
							this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
									"El campo nombre no debe tener caracteres numericos.");
						}
					} else {
						this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
								"El campo nombre es obligatorio.");
					}
				} else {
					this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"El campo tipo documento es obligatorio.");
				}
			} else {
				this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"El campo documento es obligatorio.");
			}
		} else {
			this.mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No te has logeado.");
		}
		return mensaje;
	}

	///////////////////////////////////////////////////////
	// Renderizar
	///////////////////////////////////////////////////////

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////
	public FacesMessage getMensaje() {
		return mensaje;
	}

	public void setMensaje(FacesMessage mensaje) {
		this.mensaje = mensaje;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public SessionBean getSesion() {
		return sesion;
	}

	public void setSesion(SessionBean sesion) {
		this.sesion = sesion;
	}

	public boolean isCambiar_imagen() {
		return cambiar_imagen;
	}

	public void setCambiar_imagen(boolean cambiar_imagen) {
		this.cambiar_imagen = cambiar_imagen;
	}

	public ImageBean getImage() {
		return image;
	}

	public void setImage(ImageBean image) {
		this.image = image;
	}

	public boolean isSelecciono_imagen() {
		return selecciono_imagen;
	}

	public void setSelecciono_imagen(boolean selecciono_imagen) {
		this.selecciono_imagen = selecciono_imagen;
	}
}
