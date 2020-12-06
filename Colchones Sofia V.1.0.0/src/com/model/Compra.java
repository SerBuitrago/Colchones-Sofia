package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.List;

/**
 * Implementation Compra.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@Table(name = "compra")
@NamedQuery(name="Compra.findAll", query="SELECT c FROM Compra c")
public class Compra implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String descripcion;
	private boolean estado;
	private BigInteger total;
	private String iva;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;
	
	@Column(name="total_sin_iva")
	private BigInteger totalSinIva;
	
	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@ManyToOne
	@JoinColumn(name="proveedor")
	private Proveedor proveedorBean;
	
	@ManyToOne
	@JoinColumn(name="usuario") 
	private Usuario usuario;

	@OneToMany(mappedBy="compraBean")
	private List<DetalleCompraVenta> detalleCompraVentas;
	
	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public Compra() {
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "Compra [id=" + id + ", estado=" + estado + ", total=" + total + "]";
	}
	
	public DetalleCompraVenta addDetalleCompraVenta(DetalleCompraVenta detalleCompraVenta) {
		getDetalleCompraVentas().add(detalleCompraVenta);
		detalleCompraVenta.setCompraBean(this);
		return detalleCompraVenta;
	}

	public DetalleCompraVenta removeDetalleCompraVenta(DetalleCompraVenta detalleCompraVenta) {
		getDetalleCompraVentas().remove(detalleCompraVenta);
		detalleCompraVenta.setCompraBean(null);
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

	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public BigInteger getTotal() {
		return this.total;
	}

	public void setTotal(BigInteger total) {
		this.total = total;
	}

	public Proveedor getProveedorBean() {
		return this.proveedorBean;
	}

	public void setProveedorBean(Proveedor proveedorBean) {
		this.proveedorBean = proveedorBean;
	}

	public List<DetalleCompraVenta> getDetalleCompraVentas() {
		return this.detalleCompraVentas;
	}

	public void setDetalleCompraVentas(List<DetalleCompraVenta> detalleCompraVentas) {
		this.detalleCompraVentas = detalleCompraVentas;
	}

	public String getIva() {
		return iva;
	}

	public void setIva(String iva) {
		this.iva = iva;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public BigInteger getTotalSinIva() {
		return totalSinIva;
	}

	public void setTotalSinIva(BigInteger totalSinIva) {
		this.totalSinIva = totalSinIva;
	}
}