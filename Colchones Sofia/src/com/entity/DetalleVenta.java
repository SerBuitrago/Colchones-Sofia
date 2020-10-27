package com.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;

/**
 * Implementation DetalleVenta.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@Table(name = "detalle_venta")
@NamedQuery(name = "DetalleVenta.findAll", query = "SELECT d FROM DetalleVenta d")
public class DetalleVenta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private int cantidad;
	private BigInteger descuento;
	private BigInteger precio;
	private BigInteger subtotal;

	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@ManyToOne
	@JoinColumn(name = "id_producto")
	private DetalleProducto detalleProducto;

	@ManyToOne
	@JoinColumn(name = "id_venta")
	private Venta venta;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public DetalleVenta() {
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "DetalleVenta [id=" + id + ", cantidad=" + cantidad + ", descuento=" + descuento + ", precio=" + precio
				+ ", subtotal=" + subtotal + "]";
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

	public int getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public BigInteger getDescuento() {
		return this.descuento;
	}

	public void setDescuento(BigInteger descuento) {
		this.descuento = descuento;
	}

	public BigInteger getPrecio() {
		return this.precio;
	}

	public void setPrecio(BigInteger precio) {
		this.precio = precio;
	}

	public BigInteger getSubtotal() {
		return this.subtotal;
	}

	public void setSubtotal(BigInteger subtotal) {
		this.subtotal = subtotal;
	}

	public DetalleProducto getDetalleProducto() {
		return this.detalleProducto;
	}

	public void setDetalleProducto(DetalleProducto detalleProducto) {
		this.detalleProducto = detalleProducto;
	}

	public Venta getVenta() {
		return this.venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}
}