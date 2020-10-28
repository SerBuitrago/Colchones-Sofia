package com.bean.view;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.bean.session.*;
import com.dao.*;
import com.entity.*;
import com.util.*;

/**
 * Implementation DataTableBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "cliente")
@ViewScoped
public class ClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private FacesMessage message;

	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean sesion;

	@ManagedProperty("#{table}")
	private DataTableBean table;

	///////////////////////////////////////////////////////
	// Buider
	///////////////////////////////////////////////////////
	public ClienteBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite cambiar el estado de un cliente.
	 */
	@SuppressWarnings("deprecation")
	public void estado() {
		int id = Integer.parseInt(Face.get("id-cliente"));
		this.message = null;
		if (id > 0) {
			int index = indexCliente(id);
			if (index >= 0) {
				Fecha fecha = new Fecha();
				Cliente aux = this.table.getCliente().get(index);
				boolean estado = aux.getEstado();
				estado = (estado ? false : true);
				aux.setEstado(estado);
				aux.setFechaActualizacion(new Date(fecha.fecha()));
				aux.setUsuario(this.sesion.getLogeado());
				ClienteDao dao = new ClienteDao();
				dao.update(aux);

				this.getTable().getCliente().set(index, aux);
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "succes",
						"Se ha cambiado el estado del cliente con documento " + aux.getDocumento() + ".");
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn",
						"No se ha encontrado ningun cliente con documento " + id + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error",
					"El documento " + id + " del cliente no es valido.");
		}
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}

	///////////////////////////////////////////////////////
	// Index
	///////////////////////////////////////////////////////
	/**
	 * Metodo que obtine el index del cliente en la lista.
	 * 
	 * @param documento representa el documento del cliente.
	 * @return el index del cliente.
	 */
	public int indexCliente(int documento) {
		if (this.table.getCliente() != null && this.table.getCliente().size() > 0) {
			int contador = 0;
			for (Cliente c : this.table.getCliente()) {
				if (c.getDocumento() == documento) {
					return contador;
				}
				contador++;
			}
		}
		return -1;
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////
	public FacesMessage getMessage() {
		return message;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
