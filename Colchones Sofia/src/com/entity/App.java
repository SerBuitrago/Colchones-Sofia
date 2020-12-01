package com.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

	private List<Email> email;
	private List<Telefono> telefono;
	private Global global;
	private String aaa;
	private List<Carrusel> carrousel;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public App() {
		this(null, null, null);
	}

	public App(List<Email> email, List<Telefono> telefono, Global global) {
		super();
		this.email = email;
		this.telefono = telefono;
		this.global = global;
		this.carrousel = new ArrayList<Carrusel>();
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////
	public List<Email> getEmail() {
		return email;
	}

	public void setEmail(List<Email> email) {
		this.email = email;
	}

	public List<Telefono> getTelefono() {
		return telefono;
	}

	public void setTelefono(List<Telefono> telefono) {
		this.telefono = telefono;
	}

	public Global getGlobal() {
		return global;
	}

	public void setGlobal(Global global) {
		this.global = global;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Carrusel> getCarrousel() {
		return carrousel;
	}

	public void setCarrousel(List<Carrusel> carrousel) {
		this.carrousel = carrousel;
	}
}
