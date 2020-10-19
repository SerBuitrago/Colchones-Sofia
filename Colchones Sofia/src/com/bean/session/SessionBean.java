package com.bean.session;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.entity.*;
import com.entity.other.*;
import com.dao.*;
import com.util.Fecha;

/**
 * Implementation SesionBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "sesion")
@SessionScoped
public class SessionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private FacesMessage mensaje;   
   
	private int intentos;
	private String path;
	private String inicio, fin;
	private int esperar;   
	
	private Validar usuario;
	private Usuario logeado;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public SessionBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		this.mensaje = null;
		this.inicio = null;
		this.fin = null;
		this.usuario = new Validar();
		this.intentos = 3;
		this.esperar = 5;
	} 

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite verificar los datos del usuario a logear.
	 * 
	 * @return representa el estado obtenido.
	 */
	@SuppressWarnings("deprecation")
	public String logearse() {
		this.mensaje = null;
		Fecha fecha = new Fecha();
		if (this.logeado == null) {
			if (this.intentos > 0) {
				PersonaDao dao = new PersonaDao();   
				Persona persona = dao.findByField("email", this.usuario.getEmail());
				if (persona != null) {
					UsuarioDao uDao = new UsuarioDao();
					Usuario usuario = uDao.findByField("documento", persona.getDocumento());
					if (usuario != null) {
						if (usuario.getRol().getNombre().contentEquals(this.usuario.getTipo())) {
							if (usuario.getEstado()) {
								String clave = usuario.getClave();
								if (clave.contentEquals(this.usuario.getClave())) {
									this.logeado = usuario;
									fecha = new Fecha();
									logeado.setSession(true);
									logeado.setFechaSesion(new Date(fecha.fecha()));
									uDao.update(logeado);
									this.mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
											"Usuario Logeado.");
									this.usuario = new Validar();
									return rol(this.logeado.getRol().getNombre());

								} else {
									this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
											"Contraseña Incorrecta.");
								}
							} else {
								this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
										"El usuario ha sido dehabilitado.");
							}
						} else {
							this.mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "Datos Incorrectos.");
						}
						if (this.intentos == 3) {
							fecha = new Fecha();
							fecha.setFormat(new SimpleDateFormat("yyyy-MM-dd h:mm", Locale.US));
							this.inicio = fecha.fecha();
							this.fin = null;
							Date aux;
							try {
								aux = fecha.fecha("yyyy-MM-dd h:mm", inicio);
								fecha.setFormat(new SimpleDateFormat("yyyy-MM-dd h:mm", Locale.US));
								this.fin = fecha.getFormat().format(fecha.sumarMinutos(aux, this.esperar));
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						this.intentos--;
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Warn", "Tiene " + intentos + " intentos."));
					} else {
						this.mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "REGISTRASE",
								"Usuario No existe.");
					}
				} else {
					this.mensaje = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal", "Usuario No existe.");
				}
			} else {
				fecha = new Fecha();
				this.usuario = new Validar();
				fecha.setFormat(new SimpleDateFormat("yyyy-MM-dd h:mm", Locale.US));
				this.inicio = fecha.fecha();
				if (this.inicio != null && this.fin != null) {
					try {
						fecha = new Fecha();
						fecha.restarHoras(this.inicio, this.fin);
						if (fecha.getMinutos() <= 0) {
							this.intentos = 3;
							this.mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
									"Ya puedes intentar de nuevo.");
						} else {
							this.mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Faltan "
									+ fecha.getMinutos() + " minutos para volver a intentarlo a las " + fin + ".");
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					this.mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"Has superado el numero de intentos tienes que esperar " + this.esperar + " minutos.");
				}
			}
		} else {
			this.intentos = 3;
			this.usuario = new Validar();
			return rol(this.logeado.getRol().getNombre());
		}
		FacesContext.getCurrentInstance().addMessage(null, this.mensaje);
		return "login";
	}

	/**
	 * Metodo que permite recuperar la contraseña.
	 */
	public String recuperar() {
		FacesMessage message = null;
		if (logeado == null) {
			PersonaDao pDao = new PersonaDao();
			Persona persona = pDao.findByField("email", this.usuario.getEmail());
			if (persona != null) {
				UsuarioDao dao = new UsuarioDao();
				Usuario usuario = dao.findByField("documento", persona.getDocumento());
				if (usuario != null) {
					message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							"Se te ha enviado un correo con la clave.");
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El usuario no esta registrado.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "REGISTRASE", "Usuario No existe.");
			}
		} else {
			this.usuario = new Validar();
			return rol(this.logeado.getRol().getNombre());
		}
		FacesContext.getCurrentInstance().addMessage(null, message);
		return "login";
	}
	
	///////////////////////////////////////////////////////
	// Direction
	///////////////////////////////////////////////////////

	/**
	 * Metodo que tiene las rutas del login.
	 * 
	 * @param rol representa el rol.
	 * @return retorna la ruta.
	 */
	public String rol(String rol) {
		this.path = rol.toLowerCase();
		return path;
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////
	public FacesMessage getMensaje() {
		return mensaje;
	}

	public void setMensaje(FacesMessage mensaje) {
		this.mensaje = mensaje;
	}

	public Validar getUsuario() {
		return usuario;
	}

	public void setUsuario(Validar usuario) {
		this.usuario = usuario;
	}

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public String getFin() {
		return fin;
	}

	public void setFin(String fin) {
		this.fin = fin;
	}

	public int getEsperar() {
		return esperar;
	}

	public void setEsperar(int esperar) {
		this.esperar = esperar;
	}

	public Usuario getLogeado() {
		return logeado;
	}

	public void setLogeado(Usuario logeado) {
		this.logeado = logeado;
	}

	public int getIntentos() {
		return intentos;
	}

	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
