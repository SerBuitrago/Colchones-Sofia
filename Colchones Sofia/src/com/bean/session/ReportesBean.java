package com.bean.session;

import javax.faces.event.ActionEvent;

import java.io.Serializable;
import java.util.Date;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.dao.*;
import com.entity.*;
import com.entity.other.*;
import com.util.Fecha;

@ManagedBean(name = "reporte")
@SessionScoped
public class ReportesBean implements Serializable {
	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean sesion;

	private static final long serialVersionUID = 1L;

	private FacesMessage mensage;

	private Reporte<Compra> reporte_fecha_compra;

	private Reporte<Proveedor> reporte_fecha_proveedor;
	private List<Proveedor> proveedores;
	private List<Proveedor> proveedor_filter;

	private Date fecha_inicio;
	private String fecha_formato_inicio;
	private Date fecha_fin;
	private String fecha_formato_fin;

	public ReportesBean() {
	}

	@PostConstruct
	public void init() {

	}

	public void consultarProveedor() {
		this.mensage = null;
		if (this.fecha_inicio != null) {
			if (this.fecha_fin != null) {
				ProveedorDao dao = new ProveedorDao();
				Fecha fecha = new Fecha();
				this.fecha_formato_inicio = fecha.darFormato(fecha_inicio, "yyyy/MM/dd HH:mm:ss");
				this.fecha_formato_fin = fecha.darFormato(fecha_fin, "yyyy/MM/dd HH:mm:ss");
				System.out.println(this.fecha_formato_inicio + "   " + this.fecha_formato_fin);
				this.proveedores = dao.consultaProveedor(this.fecha_formato_inicio, this.fecha_formato_fin);
				if (this.proveedores.size() > 0) {
					this.mensage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
							"Se han obtenido " + this.proveedores.size() + " proveedores.");
					this.reporte_fecha_proveedor = new Reporte<Proveedor>(this.proveedores, "/reporteP.jasper",
							sesion.getLogeado());
				} else {
					this.mensage = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"No se ha encontrado ningun proveedor.");
				}

			} else {
				this.mensage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"El campo fecha fin es obligatorio.");
			}
		} else {
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"El campo fecha inicio es obligatorio.");
		}

		if (this.mensage != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensage);
		}
	}
   
	public void exportarPDF(ActionEvent actionEvent) throws Exception {
		this.reporte_fecha_proveedor = new Reporte<Proveedor>(this.proveedores, "/report.jasper", sesion.getLogeado());
		this.reporte_fecha_proveedor.exportarPDF(actionEvent);
		this.reporte_fecha_proveedor.verPDF(actionEvent);

	}

	public Reporte<Compra> getReporte_fecha_compra() {
		return reporte_fecha_compra;
	}

	public void setReporte_fecha_compra(Reporte<Compra> reporte_fecha_compra) {
		this.reporte_fecha_compra = reporte_fecha_compra;
	}

	public Reporte<Proveedor> getReporte_fecha_proveedor() {
		return reporte_fecha_proveedor;
	}

	public void setReporte_fecha_proveedor(Reporte<Proveedor> reporte_fecha_proveedor) {
		this.reporte_fecha_proveedor = reporte_fecha_proveedor;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public Date getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(Date fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	public FacesMessage getMensage() {
		return mensage;
	}

	public void setMensage(FacesMessage mensage) {
		this.mensage = mensage;
	}

	public List<Proveedor> getProveedores() {
		return proveedores;
	}

	public void setProveedores(List<Proveedor> proveedores) {
		this.proveedores = proveedores;
	}

	public List<Proveedor> getProveedor_filter() {
		return proveedor_filter;
	}

	public void setProveedor_filter(List<Proveedor> proveedor_filter) {
		this.proveedor_filter = proveedor_filter;
	}

	public String getFecha_formato_inicio() {
		return fecha_formato_inicio;
	}

	public void setFecha_formato_inicio(String fecha_formato_inicio) {
		this.fecha_formato_inicio = fecha_formato_inicio;
	}

	public String getFecha_formato_fin() {
		return fecha_formato_fin;
	}

	public SessionBean getSesion() {
		return sesion;
	}

	public void setSesion(SessionBean sesion) {
		this.sesion = sesion;
	}

	public void setFecha_formato_fin(String fecha_formato_fin) {
		this.fecha_formato_fin = fecha_formato_fin;
	}
}