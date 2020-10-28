package com.bean.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.bean.session.*;
import com.dao.*;
import com.entity.*;
import com.util.Face;

/**
 * Implementation CategoriaBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "presupuesto")
@ViewScoped
public class HistorialPresupuestoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private FacesMessage message;

	private List<HistorialPresupuesto> historial_presupuesto;

	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean sesion;

	///////////////////////////////////////////////////////
	// Buider
	///////////////////////////////////////////////////////
	public HistorialPresupuestoBean() {
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
	 * Metodo que permite modificar el estado de un historial de presupuesto.
	 */
	public void modificarEstado() {
		this.message = null;
		int id = Integer.parseInt(Face.get("id-presupuesto"));
		if (id > 0) {
			int index = indexPresupuesto(id);
			if (index >= 0) {
				HistorialPresupuesto aux = this.historial_presupuesto.get(index);
				boolean estado = aux.getEstado();
				estado = (estado) ? false : true;
				aux.setEstado(estado);
				HistorialPresupuestoDao dao = new HistorialPresupuestoDao();
				dao.update(aux);
				this.historial_presupuesto.set(index, aux);
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "succes",
						"Se ha cambiado el estado del historial de presupuesto con ID " + aux.getId() + ".");
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn",
						"No se ha encontrado ningun historial de presupuesto con ID " + id + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error",
					"El ID " + id + " del historial de presupuesto no es valido.");
		}
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}

	///////////////////////////////////////////////////////
	// Index
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer el index de un historial de presupuesto en la
	 * lista.
	 * 
	 * @param id representa el id del historial de presupuesto
	 * @return el index encontrado.
	 */
	public int indexPresupuesto(int id) {
		if (this.historial_presupuesto != null && this.historial_presupuesto.size() > 0) {
			int contador = 0;
			for (HistorialPresupuesto p : this.historial_presupuesto) {
				if (p.getId() == id) {
					return contador;
				}
				contador++;
			}
		}
		return -1;
	}

	///////////////////////////////////////////////////////
	// Renderizar
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite consultar todos los historiales de venta.
	 * 
	 * @return representa los historiales de venta.
	 */
	public List<HistorialPresupuesto> getHistorial_presupuesto() {
		HistorialPresupuestoDao dao = new HistorialPresupuestoDao();
		List<HistorialPresupuesto> aux = dao.list();
		this.historial_presupuesto = new ArrayList<HistorialPresupuesto>();
		for (int i = (aux.size() - 1); i >= 0; i--) {
			this.historial_presupuesto.add(aux.get(i));
		}
		return historial_presupuesto;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setHistorial_presupuesto(List<HistorialPresupuesto> historial_presupuesto) {
		this.historial_presupuesto = historial_presupuesto;
	}
}
