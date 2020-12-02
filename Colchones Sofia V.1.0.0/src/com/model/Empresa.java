package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation Empresa.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@Table(name = "empresa")
@NamedQuery(name="Empresa.findAll", query="SELECT e FROM Empresa e")
public class Empresa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String nit;

	private String clave;
	private String descripcion;
	private String direccion;
	private String email;
	private byte[] logo;
	private String nombre;
	private byte[] portada;
	private String iva;
	private BigInteger presupuesto;

	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@ManyToMany
	@JoinTable(
		name="empresa_menu"
		, joinColumns={
			@JoinColumn(name="empresa")
			}
		, inverseJoinColumns={
			@JoinColumn(name="menu")
			}
		)
	private List<Menu> menus;
	
	@OneToMany(mappedBy="empresaBean")
	private List<EmpresaInformacion> empresaInformacions;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public Empresa() {
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	public EmpresaInformacion addEmpresaInformacion(EmpresaInformacion empresaInformacion) {
		getEmpresaInformacions().add(empresaInformacion);
		empresaInformacion.setEmpresaBean(this);

		return empresaInformacion;
	}

	@Override
	public String toString() {
		return "Empresa [nit=" + nit + ", clave=" + clave + ", descripcion=" + descripcion + ", direccion=" + direccion
				+ ", email=" + email + ", logo=" + Arrays.toString(logo) + ", nombre=" + nombre + ", portada="
				+ Arrays.toString(portada) + ", presupuesto=" + presupuesto + "]";
	}

	public EmpresaInformacion removeEmpresaInformacion(EmpresaInformacion empresaInformacion) {
		getEmpresaInformacions().remove(empresaInformacion);
		empresaInformacion.setEmpresaBean(null);

		return empresaInformacion;
	}
	
	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public String getNit() {
		return this.nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getLogo() {
		return this.logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public byte[] getPortada() {
		return this.portada;
	}

	public void setPortada(byte[] portada) {
		this.portada = portada;
	}

	public BigInteger getPresupuesto() {
		return this.presupuesto;
	}

	public void setPresupuesto(BigInteger presupuesto) {
		this.presupuesto = presupuesto;
	}

	public List<Menu> getMenus() {
		return this.menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public List<EmpresaInformacion> getEmpresaInformacions() {
		return this.empresaInformacions;
	}

	public void setEmpresaInformacions(List<EmpresaInformacion> empresaInformacions) {
		this.empresaInformacions = empresaInformacions;
	}

	public String getIva() {
		return iva;
	}

	public void setIva(String iva) {
		this.iva = iva;
	}
}