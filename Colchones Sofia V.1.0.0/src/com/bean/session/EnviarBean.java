package com.bean.session;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "email")
@SessionScoped
public class EnviarBean {
	
	private String tipo;
	private String name;
	private String telefono;
	private String mailAddress;
	private String mensaje;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public EnviarBean() {

	}

	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{mail}")
	private EmailBean email;
	
	///////////////////////////////////////////////////////
	//Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{app}")
	private AppBean app;
	
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * 
	 * @return
	 */
	public String formato() {
		String aux = "<table style='width:100%;border:1px solid black;'>" + "<thead style='display:none;'><tr>" + "<td>Tipo</td>"
				+ "<td>Resultado</td></tr>" + "</thead>"
				+ "<tbody><tr style='padding:16px;background:#f5f5f5;'><th style='padding:16px;font-weight: bold;text-transform uppercase'>Nombre</th><th style='padding:16px;'>"
				+ name + "</th></tr>"
				+ "<tr style='padding:16px;background:#fffff;'><th style='padding:16px;font-weight: bold;text-transform uppercase'>Email</th><th style='padding:16px;'>"
				+ mailAddress + "</th></tr>"
				+ "<tr style='padding:16px;background:#f5f5f5;'><th style='padding:16px;font-weight: bold;text-transform uppercase'>Telefono</th><th style='padding:16px;'>"
				+ telefono + "</th></tr>"
				+ "<tr style='padding:16px;background:#fffff;'><th style='padding:16px;font-weight: bold;text-transform uppercase'>Descripcion</th><th style='padding:16px;'>"
				+ mensaje + "</th></tr>" + "</tbody>" + "</table>";

		return aux;
	}

	/**
	 * 
	 */
	public void sendemail() {
		email.send(app.getApp().getEmpresa().getEmail(), this.tipo, formato());
		if (email.isEstado()) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Su " + tipo + " ha sido enviado satisfactoria."));
			this.tipo=null;
			this.name= null;
			this.telefono= null;
			this.mailAddress= null;
			this.mensaje= null;
		} else
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Su " + tipo + " no ha sido enviada."));

	}
	
	///////////////////////////////////////////////////////
	//  Getter and Setters
	///////////////////////////////////////////////////////
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public EmailBean getEmail() {
		return email;
	}

	public void setEmail(EmailBean email) {
		this.email = email;
	}

	public AppBean getApp() {
		return app;
	}

	public void setApp(AppBean app) {
		this.app = app;
	}
}
