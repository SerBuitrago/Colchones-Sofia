package com.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Implementation HistorialPresupuesto.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@Table(name = "historial_presupuesto")
@NamedQuery(name = "HistorialPresupuesto.findAll", query = "SELECT h FROM HistorialPresupuesto h")
public class HistorialPresupuesto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String descripcion;

	private boolean estado;

	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@ManyToOne
	@JoinColumn(name = "compra")
	private Compra compra;

	@ManyToOne
	@JoinColumn(name = "venta")
	private Venta venta;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public HistorialPresupuesto() {
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "HistorialPresupuesto [id=" + id + ", descripcion=" + descripcion + ", estado=" + estado + ", compra="
				+ compra + ", venta=" + venta + "]";
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

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Compra getCompra() {
		return this.compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public Venta getVenta() {
		return this.venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}
}