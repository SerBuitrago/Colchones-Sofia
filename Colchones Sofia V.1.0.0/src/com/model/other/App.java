package com.model.other;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.model.*;

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
	private String mision;
	private String vision;
	private String quienesSomos;

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
		this.mision = "What is Lorem Ipsum?\r\n" + 
				"Lorem Ipsum is simply dummy text of the printing and typesetting industry. "
				+ "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,"
				+ " when an unknown printer took a galley of type and scrambled it to make a type specimen book."
				+ " It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially"
				+ " unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages,"
				+ " and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
		this.vision = "What is Lorem Ipsum?\r\n" + 
				"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy"
				+ " text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It "
				+ "has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was "
				+ "popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing "
				+ "software like Aldus PageMaker including versions of Lorem Ipsum.";
		this.quienesSomos ="Why do we use it?\r\n" + 
				"It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using "
				+ "Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like"
				+ " readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem "
				+ "ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose "
				+ "(injected humour and the like).";
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

	public String getMision() {
		return mision;
	}

	public void setMision(String mision) {
		this.mision = mision;
	}

	public String getVision() {
		return vision;
	}

	public void setVision(String vision) {
		this.vision = vision;
	}

	public String getQuienesSomos() {
		return quienesSomos;
	}

	public void setQuienesSomos(String quienesSomos) {
		this.quienesSomos = quienesSomos;
	}
}
