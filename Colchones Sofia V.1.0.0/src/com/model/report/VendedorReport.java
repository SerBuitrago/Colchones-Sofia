package com.model.report;

import java.io.Serializable;

public class VendedorReport implements Serializable{

	private static final long serialVersionUID = 9081527902211899995L;
	
	private String documento;
	private String nombre;
	private String fecha;
	private int ventas;
	
	 public VendedorReport() {
	}
	
	@Override
	public String toString() {
		return "VendedorReport [documento=" + documento + ", nombre=" + nombre + ", fecha=" + fecha + ", ventas="
				+ ventas + "]";
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public int getVentas() {
		return ventas;
	}
	public void setVentas(int ventas) {
		this.ventas = ventas;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
