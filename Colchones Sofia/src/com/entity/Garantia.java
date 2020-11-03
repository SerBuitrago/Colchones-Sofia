package com.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the garantia database table.
 * 
 */
@Entity
@NamedQuery(name="Garantia.findAll", query="SELECT g FROM Garantia g")
public class Garantia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private boolean estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;

	private String motivo;

	//bi-directional many-to-one association to Venta
	@ManyToOne
	@JoinColumn(name="venta")
	private Venta ventaBean;

	public Garantia() {
	}

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

	public Venta getVentaBean() {
		return this.ventaBean;
	}

	public void setVentaBean(Venta ventaBean) {
		this.ventaBean = ventaBean;
	}

}