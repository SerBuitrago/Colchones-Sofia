package com.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Implementation Submenu.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@Table(name = "submenu")
@NamedQuery(name="Submenu.findAll", query="SELECT s FROM Submenu s")
public class Submenu implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String accion;
	private String data;
	private boolean estado;
	private byte[] foto;
	private String icono;
	private String nombre;
	private String postaccion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;

	@Column(name="pie_pagina")
	private boolean piePagina;
	
	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@ManyToMany(mappedBy="submenus")
	private List<Rol> rols; 

	@ManyToOne
	@JoinColumn(name="menu")
	private Menu menuBean;

	@ManyToOne
	@JoinColumn(name="item")
	private Submenu submenu;

	@OneToMany(mappedBy="submenu")
	private List<Submenu> submenus;
	
	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public Submenu() {
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "Submenu [id=" + id + ", accion=" + accion + ", data=" + data + ", estado=" + estado + ", fechaCreacion="
				+ fechaCreacion + ", foto=" + Arrays.toString(foto) + ", icono=" + icono + ", nombre=" + nombre
				+ ", piePagina=" + piePagina + ", postaccion=" + postaccion + "]";
	}
	
	public Submenu addSubmenus(Submenu submenus) {
		getSubmenus().add(submenus);
		submenus.setSubmenu(this);
		return submenus;
	}

	public Submenu removeSubmenus(Submenu submenus) {
		getSubmenus().remove(submenus);
		submenus.setSubmenu(null);
		return submenus;
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

	public String getAccion() {
		return this.accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public byte[] getFoto() {
		return this.foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String getIcono() {
		return this.icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean getPiePagina() {
		return this.piePagina;
	}

	public void setPiePagina(boolean piePagina) {
		this.piePagina = piePagina;
	}

	public String getPostaccion() {
		return this.postaccion;
	}

	public void setPostaccion(String postaccion) {
		this.postaccion = postaccion;
	}

	public List<Rol> getRols() {
		return this.rols;
	}

	public void setRols(List<Rol> rols) {
		this.rols = rols;
	}

	public Menu getMenuBean() {
		return this.menuBean;
	}

	public void setMenuBean(Menu menuBean) {
		this.menuBean = menuBean;
	}

	public Submenu getSubmenu() {
		return this.submenu;
	}

	public void setSubmenu(Submenu submenu) {
		this.submenu = submenu;
	}

	public List<Submenu> getSubmenus() {
		return this.submenus;
	}

	public void setSubmenus(List<Submenu> submenus) {
		this.submenus = submenus;
	}
}