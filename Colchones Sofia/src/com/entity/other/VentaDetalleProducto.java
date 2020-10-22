package com.entity.other;

import java.io.Serializable;
import java.math.BigInteger;

import com.entity.DetalleProducto;

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
	private BigInteger cantidad;
	private double stock;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public VentaDetalleProducto() {
		super();
	}

	public VentaDetalleProducto(DetalleProducto detalle, BigInteger cantidad) {
		super();
		this.detalle = detalle;
		this.cantidad = cantidad;
		this.stock = 0;
		if(this.detalle!= null) {
			this.stock= this.detalle.getStock();
		}
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

	public double getStock() {
		return stock;
	}

	public void setStock(double stock) {
		this.stock = stock;
	}

	public BigInteger getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigInteger cantidad) {
		this.cantidad = cantidad;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
