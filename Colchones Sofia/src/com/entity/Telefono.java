package com.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Implementation Telefono.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@NamedQuery(name = "Telefono.findAll", query = "SELECT t FROM Telefono t")
public class Telefono implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String telefono;

	private String descripcion;
	private boolean estado;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public Telefono() {
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "Telefono [telefono=" + telefono + ", descripcion=" + descripcion + ", estado=" + estado + "]";
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
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
}