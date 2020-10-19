package com.bean.session;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;

import com.dao.*;
import com.entity.*;
import com.entity.other.Convertidor;
import com.util.*;

/**
 * Implementation SesionBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "usuario")
@SessionScoped
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
			this.usuario = this.sesion.getLogeado();
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
	@SuppressWarnings("deprecation")
	public void modificar() {
		this.mensaje = validator(this.usuario);
		if (mensaje == null) {
			Fecha fecha = new Fecha();
			PersonaDao dao = new PersonaDao();
			Persona persona = this.usuario.getPersona();
			persona.setDocumento(this.usuario.getDocumento());
			persona.setNombre(persona.getNombre().toUpperCase());
			persona.setApellido(persona.getApellido().toUpperCase());
			persona.setTelefono(Convertidor.telefono(persona.getTelefono()));
			persona.setFechaActualizada(new Date(fecha.fecha()));
			dao.update(persona);
			UsuarioDao uDao = new UsuarioDao();
			if(this.image.getImage()!= null) {
				this.usuario.setFoto(this.image.getImage());
			}
			this.usuario.setPersona(persona);
			this.obtenerUsuario(); 
			uDao.update(this.usuario);
			this.sesion.setLogeado(this.usuario);
			this.mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes", "Se ha actualizado el usuario.");
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
	public FacesMessage validator(Usuario usuario) {
		this.mensaje = null;
		if (usuario != null) {
			if (usuario.getDocumento() > 0) {
				Persona persona = usuario.getPersona();
				if (persona.getTipoDocumento() != null && persona.getTipoDocumento().getNombre() != null
						&& persona.getTipoDocumento().getNombre().length() > 0) {
					if (persona.getNombre() != null && persona.getNombre().length() > 0) {
						if (persona.getApellido() != null && persona.getApellido().length() > 0) {
							if (persona.getEmail() != null && persona.getEmail().length() > 0) {
								if (usuario.getEmailConfirmacion() != null
										&& usuario.getEmailConfirmacion().length() > 0) {
									if (persona.getDireccion() != null && persona.getDireccion().length() > 0) {
										if (persona.getTelefono() != null && persona.getTelefono().length() > 0) {
											if (persona.getFechaActualizada() != null) {
												if (persona.getGenero() != null && persona.getGenero().length() > 0) {
													return null;
												} else {
													this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
															"El campo genero es obligatorio.");
												}
											} else {
												this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
														"El campo fecha nacimiento es obligatorio.");
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
											"El campo email confirmación es obligatorio.");
								}
							} else {
								this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
										"El campo email es obligatorio.");
							}
						} else {
							this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
									"El campo apellido es obligatorio.");
						}
					} else {
						this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
								"El campo documento es obligatorio.");
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
