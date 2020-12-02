package com.bean.session;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

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
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{mail}")
	private EmailBean mail;

	@ManagedProperty("#{app}")
	private AppBean app;

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
		if (usuario != null && Convertidor.isCadena(usuario.getClave()) && Convertidor.isCadena(usuario.getEmail())
				&& Convertidor.isCadena(usuario.getTipo())) {
			this.mensaje = null;
			Fecha fecha = new Fecha();
			if (this.logeado == null) {
				if (this.intentos > 0) {
					UsuarioController dao = new UsuarioController();
					Usuario auxUsuario = dao.ingresar(this.usuario.getEmail(), this.usuario.getClave(),
							this.usuario.getTipo());
					if (auxUsuario != null) {
						this.logeado = auxUsuario;
						fecha = new Fecha();
						logeado.setSesion(true);
						logeado.setFechaSesion(new Date(fecha.fecha()));
						dao.update(logeado);
						this.mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Usuario Logeado.");
						this.usuario = new Validar();
						return rol(this.logeado.getRolBean().getRol());

					} else {
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
						this.mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
								"Las credenciales no son validas.");
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
				return rol(this.logeado.getRolBean().getRol());
			}
		} else {
			this.mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Los campos de login son obligatorios.");
		}

		if (this.mensaje != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensaje);
		}
		return "login";
	}

	/**
	 * Metodo que permite recuperar la contraseña.
	 */
	public String recuperar() {
		FacesMessage message = null;
		if (logeado == null) {
			UsuarioController dao = new UsuarioController();
			Usuario usuario = dao.usuario(this.usuario.getEmail());
			if (usuario != null) {
				List<EmpresaInformacion> telefonos = app.getApp().getTelefono();
				String mensaje = mail.formatRecuperar(usuario.getClave(),
						((telefonos != null && telefonos.size() > 0) ? String.valueOf(telefonos.get(0).getTelefono())
								: ""),
						app.getApp().getEmpresa().getDireccion());
				mail.send(usuario.getPersona().getEmail(), "Recuperación Contraseña", mensaje);
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Se te ha enviado un correo con la clave.");
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "REGISTRASE", "Usuario No existe.");
			}
		} else {
			this.usuario = new Validar();
			return rol(this.logeado.getRolBean().getRol());
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

	/**
	 * Metodo que permite direccionar de una vista a otra.
	 * 
	 * @return la vista para donde vamos.
	 */
	public String direccionar() {
		String direecionar = Face.get("direccionar");
		if (Convertidor.isCadena(direecionar)) {
			return direecionar;
		}
		return "";

	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public FacesMessage getMensaje() {
		return mensaje;
	}

	public void setMensaje(FacesMessage mensaje) {
		this.mensaje = mensaje;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Validar getUsuario() {
		return usuario;
	}

	public void setUsuario(Validar usuario) {
		this.usuario = usuario;
	}

	public Usuario getLogeado() {
		return logeado;
	}

	public void setLogeado(Usuario logeado) {
		this.logeado = logeado;
	}

	public int getEsperar() {
		return esperar;
	}

	public void setEsperar(int esperar) {
		this.esperar = esperar;
	}

	public EmailBean getMail() {
		return mail;
	}

	public void setMail(EmailBean mail) {
		this.mail = mail;
	}

	public AppBean getApp() {
		return app;
	}

	public void setApp(AppBean app) {
		this.app = app;
	}
}
