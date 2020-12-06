package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;


/**
 * Implementation DetalleCompraVenta.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@Table(name="detalle_compra_venta")
@NamedQuery(name="DetalleCompraVenta.findAll", query="SELECT d FROM DetalleCompraVenta d")
public class DetalleCompraVenta implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private int cantidad;
	private String descripcion;
	private BigInteger descuento;
	private byte garantia;
	private BigInteger precio;
	private BigInteger subtotal;
	
	@Column(name="	subtotal_sin_iva")
	private BigInteger subtotalSinIva;
	
	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@ManyToOne
	@JoinColumn(name="compra")
	private Compra compraBean;

	@ManyToOne
	@JoinColumn(name="producto")
	private DetalleProducto detalleProducto;

	@ManyToOne
	@JoinColumn(name="venta")
	private Venta ventaBean;
	
	@OneToMany(mappedBy="detalleCompraVenta")
	private List<Garantia> garantias;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public DetalleCompraVenta() {
	}
	
	public DetalleCompraVenta(int id, int cantidad, String descripcion, BigInteger descuento, byte garantia,
			BigInteger precio, BigInteger subtotal, Compra compraBean, DetalleProducto detalleProducto,
			List<Garantia> garantias) {
		this.id = id;
		this.cantidad = cantidad;
		this.descripcion = descripcion;
		this.descuento = descuento;
		this.garantia = garantia;
		this.precio = precio;
		this.subtotal = subtotal;
		this.compraBean = compraBean;
		this.detalleProducto = detalleProducto;
		this.garantias = garantias;
	}
	
	

	public DetalleCompraVenta(int id, int cantidad, String descripcion, BigInteger descuento, byte garantia,
			BigInteger precio, BigInteger subtotal, DetalleProducto detalleProducto, Venta ventaBean) {
		this.id = id;
		this.cantidad = cantidad;
		this.descripcion = descripcion;
		this.descuento = descuento;
		this.garantia = garantia;
		this.precio = precio;
		this.subtotal = subtotal;
		this.detalleProducto = detalleProducto;
		this.ventaBean = ventaBean;
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	public Garantia addGarantia(Garantia garantia) {
		getGarantias().add(garantia);
		garantia.setDetalleCompraVenta(this);

		return garantia;
	}

	public Garantia removeGarantia(Garantia garantia) {
		getGarantias().remove(garantia);
		garantia.setDetalleCompraVenta(null);

		return garantia;
	}
	
	@Override
	public String toString() {
		return "DetalleCompraVenta [id=" + id + ", cantidad=" + cantidad + ", descripcion=" + descripcion
				+ ", descuento=" + descuento + ", garantia=" + garantia + ", precio=" + precio + ", subtotal="
				+ subtotal + "]";
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

	public List<Garantia> getGarantias() {
		return garantias;
	}

	public void setGarantias(List<Garantia> garantias) {
		this.garantias = garantias;
	}

	public BigInteger getSubtotalSinIva() {
		return subtotalSinIva;
	}

	public void setSubtotalSinIva(BigInteger subtotalSinIva) {
		this.subtotalSinIva = subtotalSinIva;
	}
}