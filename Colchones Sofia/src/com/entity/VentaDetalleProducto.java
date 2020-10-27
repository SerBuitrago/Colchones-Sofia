package com.entity;

import java.io.Serializable;

/**
 * Implementation VentaDetalleProducto.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class VentaDetalleProducto implements Serializable {

	private static final long serialVersionUID = 1L;

	private DetalleProducto detalle;
	private int cantidad;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public VentaDetalleProducto() {
		super();
	}

	public VentaDetalleProducto(DetalleProducto detalle, int cantidad) {
		super();
		this.detalle = detalle;
		this.cantidad = cantidad;
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "VentaDetalleProducto [detalle=" + detalle + ", cantidad=" + cantidad + "]";
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public DetalleProducto getDetalle() {
		return detalle;
	}

	public void setDetalle(DetalleProducto detalle) {
		this.detalle = detalle;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
