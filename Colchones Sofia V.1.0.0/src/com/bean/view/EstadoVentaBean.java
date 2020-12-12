package com.bean.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import com.controller.*;
import com.model.*;
import com.model.other.*;

/**
 * 
 * Implementation EstadoVentaBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */

@ManagedBean(name = "estadoVenta")
@ViewScoped
public class EstadoVentaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private EstadoVenta estadoVenta;

	private String id;
	private FacesMessage mensage;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public EstadoVentaBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		initEstadoVenta();

	}

	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * Metodo que inicializa el estadoVenta.
	 */
	public void initEstadoVenta() {
		this.id = "";
		this.estadoVenta = null;
		this.estadoVenta = new EstadoVenta();

	}

	///////////////////////////////////////////////////////
	// Method EstadiVenta
	///////////////////////////////////////////////////////
	/**
	 * 
	 */
	public void consultarEstadoVenta() {
		this.mensage = null;
		if (Convertidor.isCadena(this.id)) {
			EstadoVentaController dao = new EstadoVentaController();
			Venta venta = new Venta();
			VentaController controller = new VentaController();
			int id = Integer.parseInt(this.id);
			venta = controller.find(id);
			if (venta != null) {
				this.estadoVenta = dao.findByField("ventaEstadoVenta", venta);
				if (this.estadoVenta != null) {
					this.mensage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
							"Se han obtenido el ID " + this.estadoVenta.getVenta() + "del estado de la venta.");
				} else {
					this.mensage = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"Esta venta no tiene ningun estado.");
				}
			} else {

				this.mensage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Warn",
						"No existe ninguna venta con ese id.");
			}
		} else {
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El campo ID es obligatorio.");
		}

		if (this.mensage != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensage);
		}
	}

	/**
	 * 
	 */
	public void limpiarFiltroEstadoVenta() {
		if (this.estadoVenta != null && this.estadoVenta.getVentaEstadoVenta() != null
				&& this.estadoVenta.getVentaEstadoVenta().getDetalleCompraVentas().size() > 0) {
			this.initEstadoVenta();
			this.id = null;

			this.mensage = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"Se ha limpiado el filtro de estado venta.");
		} else {
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No has filtrado ningun estado venta.");
		}

		if (this.mensage != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensage);
		}
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////

	public EstadoVenta getEstadoVenta() {
		return estadoVenta;
	}

	public FacesMessage getMensage() {
		return mensage;
	}

	public void setMensage(FacesMessage mensage) {
		this.mensage = mensage;
	}

	public void setEstadoVenta(EstadoVenta estadoVenta) {
		this.estadoVenta = estadoVenta;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
