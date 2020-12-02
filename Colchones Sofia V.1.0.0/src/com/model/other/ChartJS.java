package com.model.other;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Implementation ChartJS. 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class ChartJS implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private int cantidad;
	private BigInteger total;
	
	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public ChartJS() {
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "Cantidad [nombre=" + nombre + ", cantidad=" + cantidad + "]";
	}

	///////////////////////////////////////////////////////
	// Getter y Setters 
	///////////////////////////////////////////////////////
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BigInteger getTotal() {
		return total;
	}

	public void setTotal(BigInteger total) {
		this.total = total;
	}
}
