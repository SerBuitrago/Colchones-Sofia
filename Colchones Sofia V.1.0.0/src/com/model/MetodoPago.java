package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * Implementation MetodoPago.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@Table(name="metodo_pago")
@NamedQuery(name="MetodoPago.findAll", query="SELECT m FROM MetodoPago m")
public class MetodoPago implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String banco;

	private boolean estado;
	private String foto;

	@Column(name="numero_cuenta")
	private String numeroCuenta;

	@Column(name="tipo_cuenta")
	private String tipoCuenta;

	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@ManyToOne
	@JoinColumn(name="tipo_pago")
	private TipoPago tipoPagoBean;

	@OneToMany(mappedBy="metodoPagoBean")
	private List<Venta> ventas;
	
	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public MetodoPago() {
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "MetodoPago [banco=" + banco + ", estado=" + estado + ", foto=" + foto + ", numeroCuenta=" + numeroCuenta
				+ ", tipoCuenta=" + tipoCuenta + "]";
	}
	
	public Venta addVenta(Venta venta) {
		getVentas().add(venta);
		venta.setMetodoPagoBean(this);

		return venta;
	}

	public Venta removeVenta(Venta venta) {
		getVentas().remove(venta);
		venta.setMetodoPagoBean(null);

		return venta;
	}
	
	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public String getBanco() {
		return this.banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getFoto() {
		return this.foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getNumeroCuenta() {
		return this.numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getTipoCuenta() {
		return this.tipoCuenta;
	}

	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	public TipoPago getTipoPagoBean() {
		return this.tipoPagoBean;
	}

	public void setTipoPagoBean(TipoPago tipoPagoBean) {
		this.tipoPagoBean = tipoPagoBean;
	}

	public List<Venta> getVentas() {
		return this.ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}
}