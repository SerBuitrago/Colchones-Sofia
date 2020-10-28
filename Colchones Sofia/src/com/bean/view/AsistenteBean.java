package com.bean.view;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.entity.*;

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
	
	private Usuario usuario;
	private String h1;

	public void mensajes() {
		message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
				"Se ha presionado");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void mensajes2() {
		message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
				"Se ha presionado 2");
		this.h1 ="TEXTO AGREGADO";
		FacesContext.getCurrentInstance().addMessage(null, message);
	}    
	
	
	  
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String getH1() {
		return h1;
	}

	public void setH1(String h1) {
		this.h1 = h1;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public FacesMessage getMessage() {
		return message;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
	}
}
