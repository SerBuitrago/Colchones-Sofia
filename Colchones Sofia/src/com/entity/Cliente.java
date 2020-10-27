package com.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.List;

/**
 * Implementation Cliente. 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@NamedQuery(name="Cliente.findAll", query="SELECT c FROM Cliente c")
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int documento;

	private boolean estado;
	private BigInteger puntos;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_actualizacion")
	private Date fechaActualizacion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_registro")
	private Date fechaRegistro;

	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@OneToOne
	@JoinColumn(name="documento")
	private Persona persona;

	@ManyToOne
	@JoinColumn(name="usuario")
	private Usuario usuario;

	@OneToMany(mappedBy="cliente")
	private List<Venta> ventas;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public Cliente() {
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "Cliente [documento=" + documento + ", estado=" + estado + ", puntos=" + puntos + ", persona=" + persona
				+ "]";
	}	
	
	public Venta addVenta(Venta venta) {
		getVentas().add(venta);
		venta.setCliente(this);
		return venta;
	}
	
	public Venta removeVenta(Venta venta) {
		getVentas().remove(venta);
		venta.setCliente(null);
		return venta;
	}
	
	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public int getDocumento() {
		return this.documento;
	}

	public void setDocumento(int documento) {
		this.documento = documento;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Date getFechaActualizacion() {
		return this.fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public BigInteger getPuntos() {
		return this.puntos;
	}

	public void setPuntos(BigInteger puntos) {
		this.puntos = puntos;
	}

	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Venta> getVentas() {
		return this.ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}
}