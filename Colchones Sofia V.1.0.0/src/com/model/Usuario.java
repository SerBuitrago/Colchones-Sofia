package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.List;

/**
 * Implementation Usuario.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@Table(name = "usuario")
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String clave;
	private boolean estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_sesion")
	private Date fechaSesion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_ultima_sesion")
	private Date fechaUltimaSesion;

	private BigInteger puntos;

	private boolean sesion;

	@Column(name="ultima_clave")
	private String ultimaClave;
	
	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@OneToMany(mappedBy="usuario")
	private List<ClienteCuenta> clienteCuentas;

	@ManyToOne
	@JoinColumn(name="documento")
	private Persona persona;

	@ManyToOne
	@JoinColumn(name="rol")
	private Rol rolBean;
	
	@OneToMany(mappedBy="usuario1")
	private List<Venta> ventas1;

	@OneToMany(mappedBy="usuario2")
	private List<Venta> ventas2; 
	
	@OneToMany(mappedBy="usuario3")
	private List<Venta> ventas3; 
	
	@OneToMany(mappedBy="usuario")
	private List<Compra> compra;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public Usuario() {
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "Usuario [id=" + id + ", estado=" + estado + ", fechaCreacion=" + fechaCreacion + ", persona=" + persona +"]";
	}
	
	public ClienteCuenta addClienteCuenta(ClienteCuenta clienteCuenta) {
		getClienteCuentas().add(clienteCuenta);
		clienteCuenta.setUsuario(this);

		return clienteCuenta;
	}

	public ClienteCuenta removeClienteCuenta(ClienteCuenta clienteCuenta) {
		getClienteCuentas().remove(clienteCuenta);
		clienteCuenta.setUsuario(null);

		return clienteCuenta;
	}
	
	public Venta addVentas1(Venta ventas1) {
		getVentas1().add(ventas1);
		ventas1.setUsuario1(this);

		return ventas1;
	}

	public Venta removeVentas1(Venta ventas1) {
		getVentas1().remove(ventas1);
		ventas1.setUsuario1(null);

		return ventas1;
	}
	
	public Venta addVentas2(Venta ventas2) {
		getVentas2().add(ventas2);
		ventas2.setUsuario2(this);

		return ventas2;
	}

	public Venta removeVentas2(Venta ventas2) {
		getVentas2().remove(ventas2);
		ventas2.setUsuario2(null);

		return ventas2;
	}
	
	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
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

	public Date getFechaSesion() {
		return this.fechaSesion;
	}

	public void setFechaSesion(Date fechaSesion) {
		this.fechaSesion = fechaSesion;
	}

	public Date getFechaUltimaSesion() {
		return this.fechaUltimaSesion;
	}

	public void setFechaUltimaSesion(Date fechaUltimaSesion) {
		this.fechaUltimaSesion = fechaUltimaSesion;
	}

	public BigInteger getPuntos() {
		return this.puntos;
	}

	public void setPuntos(BigInteger puntos) {
		this.puntos = puntos;
	}

	public boolean getSesion() {
		return this.sesion;
	}

	public void setSesion(boolean sesion) {
		this.sesion = sesion;
	}

	public String getUltimaClave() {
		return this.ultimaClave;
	}

	public void setUltimaClave(String ultimaClave) {
		this.ultimaClave = ultimaClave;
	}

	public List<ClienteCuenta> getClienteCuentas() {
		return this.clienteCuentas;
	}

	public void setClienteCuentas(List<ClienteCuenta> clienteCuentas) {
		this.clienteCuentas = clienteCuentas;
	}

	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Rol getRolBean() {
		return this.rolBean;
	}

	public void setRolBean(Rol rolBean) {
		this.rolBean = rolBean;
	}

	public List<Venta> getVentas1() {
		return this.ventas1;
	}

	public void setVentas1(List<Venta> ventas1) {
		this.ventas1 = ventas1;
	}

	public List<Venta> getVentas2() {
		return this.ventas2;
	}

	public void setVentas2(List<Venta> ventas2) {
		this.ventas2 = ventas2;
	}

	public List<Compra> getCompra() {
		return compra;
	}

	public void setCompra(List<Compra> compra) {
		this.compra = compra;
	}

	public List<Venta> getVentas3() {
		return ventas3;
	}

	public void setVentas3(List<Venta> ventas3) {
		this.ventas3 = ventas3;
	}
}