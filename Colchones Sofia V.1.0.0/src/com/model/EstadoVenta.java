package com.model;

import java.io.Serializable;

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
	@Column (name="venta")
	private int id_venta;
	
	private String descripcion;
	
	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@ManyToOne
	@JoinColumn (name="id")
	private Venta venta;

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
		return "EstadoVenta [id_venta=" + id_venta + ", descripcion=" + descripcion + "]";
	}
	
	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public int getId_venta() {
		return id_venta;
	}
	public void setId_venta(int id_venta) {
		this.id_venta = id_venta;
	}
	public Venta getVenta() {
		return venta;
	}
	public void setVenta(Venta venta) {
		this.venta = venta;
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
