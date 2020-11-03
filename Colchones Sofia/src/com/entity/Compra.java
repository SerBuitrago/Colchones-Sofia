package com.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.List;


/**
 * The persistent class for the compra database table.
 * 
 */
@Entity
@NamedQuery(name="Compra.findAll", query="SELECT c FROM Compra c")
public class Compra implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String descripcion;

	private boolean estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;

	private BigInteger total;

	//bi-directional many-to-one association to Proveedor
	@ManyToOne
	@JoinColumn(name="proveedor")
	private Proveedor proveedorBean;

	//bi-directional many-to-one association to DetalleCompraVenta
	@OneToMany(mappedBy="compraBean")
	private List<DetalleCompraVenta> detalleCompraVentas;

	public Compra() {
	}

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

}