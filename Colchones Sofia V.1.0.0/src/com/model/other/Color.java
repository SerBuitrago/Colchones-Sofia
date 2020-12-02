package com.model.other;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation Color.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class Color implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<String> colores;
	private List<String> bordes;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public Color() {
		this.color();
		this.bordes();
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que inicializa los colores a usar.
	 */
	public void color() {
		this.colores = new ArrayList<String>();
		this.colores.add("rgba(255, 99, 132, 0.2)");
		this.colores.add("rgba(255, 159, 64, 0.2)");
		this.colores.add("rgba(255, 205, 86, 0.2)");
		this.colores.add("rgba(75, 192, 192, 0.2)");
		this.colores.add("rgba(54, 162, 235, 0.2)");
		this.colores.add("rgba(153, 102, 255, 0.2)");
		this.colores.add("rgba(201, 203, 207, 0.2)");
		this.colores.add("rgba(199, 149, 149, 0.2)");
	}

	/**
	 * Metodo que inicializar los bordes a usar.
	 */
	public void bordes() {
		this.bordes = new ArrayList<String>();
		this.bordes.add("rgb(255, 99, 132)");
		this.bordes.add("rgb(255, 159, 64)");
		this.bordes.add("rgb(255, 205, 86)");
		this.bordes.add("rgb(75, 192, 192)");
		this.bordes.add("rgb(54, 162, 235)");
		this.bordes.add("rgb(153, 102, 255)");
		this.bordes.add("rgb(201, 203, 207)");
		this.bordes.add("rgb(199, 149, 149)");
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////
	public List<String> getColores() {
		return colores;
	}

	public void setColores(List<String> colores) {
		this.colores = colores;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<String> getBordes() {
		return bordes;
	}

	public void setBordes(List<String> bordes) {
		this.bordes = bordes;
	}
}
