package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * Implementation TipoPago.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@Table(name="tipo_pago")
@NamedQuery(name="TipoPago.findAll", query="SELECT t FROM TipoPago t")
public class TipoPago implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private String nombre;

	private boolean estado;
	private String foto;
	
	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@OneToMany(mappedBy="tipoPagoBean")
	private List<MetodoPago> metodoPagos;
	
	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public TipoPago() {
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "TipoPago [nombre=" + nombre + ", estado=" + estado + ", foto=" + foto + "]";
	}
	
	public MetodoPago addMetodoPago(MetodoPago metodoPago) {
		getMetodoPagos().add(metodoPago);
		metodoPago.setTipoPagoBean(this);
		return metodoPago;
	}

	public MetodoPago removeMetodoPago(MetodoPago metodoPago) {
		getMetodoPagos().remove(metodoPago);
		metodoPago.setTipoPagoBean(null);
		return metodoPago;
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getFoto() {
		return this.foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public List<MetodoPago> getMetodoPagos() {
		return this.metodoPagos;
	}

	public void setMetodoPagos(List<MetodoPago> metodoPagos) {
		this.metodoPagos = metodoPagos;
	}
}