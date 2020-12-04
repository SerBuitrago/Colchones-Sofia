package com.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Implementation EmpresaInformacion.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@Table(name = "estado_venta")
@NamedQuery(name = "EstadoVenta.findAll", query = "SELECT e FROM EstadoVenta e")
public class EstadoVenta implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@Column (name="venta",nullable=false)
	private int venta;
	
	private String descripcion;
	
	@Column (name="fecha_creacion")
	private Date fechaCreacion;
	
	private boolean estado;
	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@ManyToOne
	@JoinColumn (name="venta",insertable=false, updatable=false)
	private Venta ventaEstadoVenta;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public EstadoVenta() {
	}
	
	///////////////////////////////////////////////////////
	// Mehod
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "EstadoVenta [id=" + venta + ", descripcion=" + descripcion + "]";
	}
	
	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	
	
	public int getVenta() {
		return venta;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public void setVenta(int venta) {
		this.venta= venta;
	}
	
	public Venta getVentaEstadoVenta() {
		return ventaEstadoVenta;
	}

	public void setVentaEstadoVenta(Venta ventaEstadoVenta) {
		this.ventaEstadoVenta = ventaEstadoVenta;
	}

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}