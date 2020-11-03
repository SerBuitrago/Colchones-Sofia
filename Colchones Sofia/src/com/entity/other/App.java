package com.entity.other;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.entity.*;

/**
 * Implementation App.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class App implements Serializable {
	private static final long serialVersionUID = 1L;

	private Empresa empresa;
	private List<EmpresaInformacion> email;
	private List<EmpresaInformacion> telefono;
	private List<EmpresaInformacion> carrousel;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public App() {
		this(null, null, null);
	}

	public App(List<EmpresaInformacion> email, List<EmpresaInformacion> telefono, Empresa empresa) {
		this(email, telefono, empresa, new ArrayList<EmpresaInformacion>());
	}

	public App(List<EmpresaInformacion> email, List<EmpresaInformacion> telefono, Empresa empresa,
			List<EmpresaInformacion> carrousel) {
		super();
		this.email = email;
		this.telefono = telefono;
		this.empresa = empresa;
		this.carrousel = carrousel;
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<EmpresaInformacion> getEmail() {
		return email;
	}

	public void setEmail(List<EmpresaInformacion> email) {
		this.email = email;
	}

	public List<EmpresaInformacion> getTelefono() {
		return telefono;
	}

	public void setTelefono(List<EmpresaInformacion> telefono) {
		this.telefono = telefono;
	}

	public List<EmpresaInformacion> getCarrousel() {
		return carrousel;
	}

	public void setCarrousel(List<EmpresaInformacion> carrousel) {
		this.carrousel = carrousel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
