package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * Implementation ClienteCuenta.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@Table(name="cliente_cuenta")
@NamedQuery(name="ClienteCuenta.findAll", query="SELECT c FROM ClienteCuenta c")
public class ClienteCuenta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="numero_cuenta")
	private String numeroCuenta;

	private String banco;

	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@ManyToOne
	@JoinColumn(name="cliente")
	private Usuario usuario;

	@OneToMany(mappedBy="clienteCuenta")
	private List<Venta> ventas;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public ClienteCuenta() {
	}
	
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "ClienteCuenta [numeroCuenta=" + numeroCuenta + ", banco=" + banco + ", usuario=" + usuario + "]";
	}

	public Venta addVenta(Venta venta) {
		getVentas().add(venta);
		venta.setClienteCuenta(this);

		return venta;
	}

	public Venta removeVenta(Venta venta) {
		getVentas().remove(venta);
		venta.setClienteCuenta(null);

		return venta;
	}
	
	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public String getNumeroCuenta() {
		return this.numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getBanco() {
		return this.banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Venta> getVentas() {
		return this.ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}
}