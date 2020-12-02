package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * Implementation Garantia.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@Table(name = "garantia")
@NamedQuery(name = "Garantia.findAll", query = "SELECT g FROM Garantia g")
public class Garantia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private boolean estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_creacion")
	private Date fechaCreacion;

	private String motivo;
	
	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@ManyToOne
	@JoinColumn(name = "venta")
	private DetalleCompraVenta detalleCompraVenta;
	
	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public Garantia() {
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "Garantia [id=" + id + ", estado=" + estado + ", fechaCreacion=" + fechaCreacion + ", motivo=" + motivo
				+ "]";
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getMotivo() {
		return this.motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public DetalleCompraVenta getDetalleCompraVenta() {
		return this.detalleCompraVenta;
	}

	public void setDetalleCompraVenta(DetalleCompraVenta detalleCompraVenta) {
		this.detalleCompraVenta = detalleCompraVenta;
	}
}