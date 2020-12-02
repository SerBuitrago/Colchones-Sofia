package com.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Implementation EmpresaInformacion.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@Table(name = "empresa_informacion")
@NamedQuery(name = "EmpresaInformacion.findAll", query = "SELECT e FROM EmpresaInformacion e")
public class EmpresaInformacion implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String descripcion;
	private String email;
	private boolean estado;
	private byte[] foto;
	private String telefono;
	
	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@ManyToOne
	@JoinColumn(name = "empresa")
	private Empresa empresaBean;

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "EmpresaInformacion [id=" + id + ", email=" + email + ", estado=" + estado + ", telefono=" + telefono
				+ "]";
	}
	
	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public EmpresaInformacion() {
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public byte[] getFoto() {
		return this.foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Empresa getEmpresaBean() {
		return this.empresaBean;
	}

	public void setEmpresaBean(Empresa empresaBean) {
		this.empresaBean = empresaBean;
	}
}