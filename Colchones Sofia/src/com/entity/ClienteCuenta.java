package com.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the cliente_cuenta database table.
 * 
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

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="cliente")
	private Usuario usuario;

	//bi-directional many-to-one association to Venta
	@OneToMany(mappedBy="clienteCuenta")
	private List<Venta> ventas;

	public ClienteCuenta() {
	}

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

}