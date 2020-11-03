package com.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the detalle_compra_venta database table.
 * 
 */
@Entity
@Table(name="detalle_compra_venta")
@NamedQuery(name="DetalleCompraVenta.findAll", query="SELECT d FROM DetalleCompraVenta d")
public class DetalleCompraVenta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private int cantidad;

	private String descripcion;

	private BigInteger descuento;

	private byte garantia;

	private BigInteger precio;

	private BigInteger subtotal;

	//bi-directional many-to-one association to Compra
	@ManyToOne
	@JoinColumn(name="compra")
	private Compra compraBean;

	//bi-directional many-to-one association to DetalleProducto
	@ManyToOne
	@JoinColumn(name="producto")
	private DetalleProducto detalleProducto;

	//bi-directional many-to-one association to Venta
	@ManyToOne
	@JoinColumn(name="venta")
	private Venta ventaBean;

	public DetalleCompraVenta() {
	}

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

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigInteger getDescuento() {
		return this.descuento;
	}

	public void setDescuento(BigInteger descuento) {
		this.descuento = descuento;
	}

	public byte getGarantia() {
		return this.garantia;
	}

	public void setGarantia(byte garantia) {
		this.garantia = garantia;
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

	public Compra getCompraBean() {
		return this.compraBean;
	}

	public void setCompraBean(Compra compraBean) {
		this.compraBean = compraBean;
	}

	public DetalleProducto getDetalleProducto() {
		return this.detalleProducto;
	}

	public void setDetalleProducto(DetalleProducto detalleProducto) {
		this.detalleProducto = detalleProducto;
	}

	public Venta getVentaBean() {
		return this.ventaBean;
	}

	public void setVentaBean(Venta ventaBean) {
		this.ventaBean = ventaBean;
	}

}