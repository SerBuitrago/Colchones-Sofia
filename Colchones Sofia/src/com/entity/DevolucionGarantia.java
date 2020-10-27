package com.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * Implementation DevolucionGarantia.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@Table(name = "devolucion_garantia")
@NamedQuery(name = "DevolucionGarantia.findAll", query = "SELECT d FROM DevolucionGarantia d")
public class DevolucionGarantia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_actualizacion")
	private Date fechaActualizacion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_iregistro")
	private Date fechaIregistro;

	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "id_venta")
	private Venta venta;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public DevolucionGarantia() {
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "DevolucionGarantia [id=" + id + ", estado=" + estado + ", fechaActualizacion=" + fechaActualizacion
				+ ", fechaIregistro=" + fechaIregistro + ", usuario=" + usuario + "]";
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

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaActualizacion() {
		return this.fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public Date getFechaIregistro() {
		return this.fechaIregistro;
	}

	public void setFechaIregistro(Date fechaIregistro) {
		this.fechaIregistro = fechaIregistro;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Venta getVenta() {
		return this.venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}
}