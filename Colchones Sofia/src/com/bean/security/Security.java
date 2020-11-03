package com.bean.security;

import java.io.*;
import java.util.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;


import com.bean.session.SessionBean;
import com.dao.*;
import com.entity.*;
import com.util.Fecha;

/**
 * Implementation Security.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "security") 
@RequestScoped
public class Security implements Serializable {
	private static final long serialVersionUID = 1L;

	private FacesContext context;
	private ExternalContext external;
	private String url;
	
	///////////////////////////////////////////////////////
	// ManagedBean
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean session;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public Security() {
		super();
	}
	
	///////////////////////////////////////////////////////
	// Method Generic
	///////////////////////////////////////////////////////
	/**
	 * Metodo que inicializa el contexto.
	 */
	public void init() {
		this.context = FacesContext.getCurrentInstance();
		this.external = context.getExternalContext();
		this.url = this.external.getRequestContextPath();
	}
	
	/**
	 * Metodo que direcciona al inicio del rol.
	 */
	public void direct() {
		String path = session.getPath();
		String url= this.url + "/faces/pages/" + path + "/index.xhtml";
		try {
			this.external.redirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	///////////////////////////////////////////////////////
	// Method Roles
	///////////////////////////////////////////////////////
	/**
	 * Metodo que verifica si el usuario logeado tiene acceso a la vista.
	 * @param permit representa el acceso a la vista.
	 */
	public void check(String permit) {
		this.session();
		if (permit != null && permit.length() > 0) { 
			Usuario aux = this.session.getLogeado();
			if (aux != null) {
				Rol rol = aux.getRolBean();
				if (rol != null) {
					if (!permit.equals(rol.getRol())) {
						direct();
					}
				}
			}
		}
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que verifica si alguien inicio sesión.
	 */
	public void session() {
		init();
		Usuario aux = this.session.getLogeado();
		if (aux == null) {
			try {
				external.redirect(this.url + "/faces/login.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Metodo que verifica si alguien inicio sesión.
	 */
	public void login() {
		init();
		if (this.session != null) {
			Usuario aux = this.session.getLogeado();
			if (aux != null) {
				direct();
			}
		}
	}

	/**
	 * Metodo que permite cerrar la sesión.
	 */
	@SuppressWarnings("deprecation")
	public String close() {
		Usuario aux = this.session.getLogeado();
		if (aux != null) {
			UsuarioDao dao = new UsuarioDao();
			Fecha fecha = new Fecha();
			aux.setSesion(false);
			aux.setFechaUltimaSesion(new Date(fecha.fecha())); 
			dao.update(aux);
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			session = new SessionBean();
		}
		return "/login?faces-redirect=true";
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public SessionBean getSession() {
		return session;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}

	public FacesContext getContext() {
		return context;
	}

	public void setContext(FacesContext context) {
		this.context = context;
	}

	public ExternalContext getExternal() {
		return external;
	}

	public void setExternal(ExternalContext external) {
		this.external = external;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
