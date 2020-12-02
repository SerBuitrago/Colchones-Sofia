package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;


/**
 * Implementation Categoria.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@Table(name="detalle_producto")
@NamedQuery(name="DetalleProducto.findAll", query="SELECT d FROM DetalleProducto d")
public class DetalleProducto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String color;
	private String descripcion;
	private BigInteger descuento;
	private String dimension;
	private int stock;
	private byte[] foto;

	@Column(name="precio_compra")
	private BigInteger precioCompra;

	@Column(name="precio_venta")
	private BigInteger precioVenta;

	@Column(name="stock_minimo")
	private int stockMinimo;

	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@OneToMany(mappedBy="detalleProducto")
	private List<DetalleCompraVenta> detalleCompraVentas;
	
	@ManyToOne
	@JoinColumn(name="producto")
	private Producto productoBean;

	@ManyToMany(mappedBy="detalleProductos")
	private List<Proveedor> proveedors;
	
	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public DetalleProducto() {
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "DetalleProducto [id=" + id + ", color=" + color + ", dimension=" + dimension + ", precioCompra="
				+ precioCompra + ", precioVenta=" + precioVenta + ", stock=" + stock + ", stockMinimo=" + stockMinimo
				+ "]";
	}
	
	public void addStock(int stock, int stock_minimo) {
		this.stock = stock;
		this.stockMinimo = stock_minimo;
	}
	
	public DetalleCompraVenta addDetalleCompraVenta(DetalleCompraVenta detalleCompraVenta) {
		getDetalleCompraVentas().add(detalleCompraVenta);
		detalleCompraVenta.setDetalleProducto(this);

		return detalleCompraVenta;
	}

	public DetalleCompraVenta removeDetalleCompraVenta(DetalleCompraVenta detalleCompraVenta) {
		getDetalleCompraVentas().remove(detalleCompraVenta);
		detalleCompraVenta.setDetalleProducto(null);

		return detalleCompraVenta;
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

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
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

	public String getDimension() {
		return this.dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public byte[] getFoto() {
		return this.foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public BigInteger getPrecioCompra() {
		return this.precioCompra;
	}

	public void setPrecioCompra(BigInteger precioCompra) {
		this.precioCompra = precioCompra;
	}

	public BigInteger getPrecioVenta() {
		return this.precioVenta;
	}

	public void setPrecioVenta(BigInteger precioVenta) {
		this.precioVenta = precioVenta;
	}

	public int getStock() {
		return this.stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getStockMinimo() {
		return this.stockMinimo;
	}

	public void setStockMinimo(int stockMinimo) {
		this.stockMinimo = stockMinimo;
	}

	public List<DetalleCompraVenta> getDetalleCompraVentas() {
		return this.detalleCompraVentas;
	}

	public void setDetalleCompraVentas(List<DetalleCompraVenta> detalleCompraVentas) {
		this.detalleCompraVentas = detalleCompraVentas;
	}

	public Producto getProductoBean() {
		return this.productoBean;
	}

	public void setProductoBean(Producto productoBean) {
		this.productoBean = productoBean;
	}

	public List<Proveedor> getProveedors() {
		return this.proveedors;
	}

	public void setProveedors(List<Proveedor> proveedors) {
		this.proveedors = proveedors;
	}
}