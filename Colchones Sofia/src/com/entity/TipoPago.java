package com.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tipo_pago database table.
 * 
 */
@Entity
@Table(name="tipo_pago")
@NamedQuery(name="TipoPago.findAll", query="SELECT t FROM TipoPago t")
public class TipoPago implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String nombre;

	private boolean estado;

	private String foto;

	//bi-directional many-to-one association to MetodoPago
	@OneToMany(mappedBy="tipoPagoBean")
	private List<MetodoPago> metodoPagos;

	public TipoPago() {
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public List<MetodoPago> getMetodoPagos() {
		return this.metodoPagos;
	}

	public void setMetodoPagos(List<MetodoPago> metodoPagos) {
		this.metodoPagos = metodoPagos;
	}

	public MetodoPago addMetodoPago(MetodoPago metodoPago) {
		getMetodoPagos().add(metodoPago);
		metodoPago.setTipoPagoBean(this);

		return metodoPago;
	}

	public MetodoPago removeMetodoPago(MetodoPago metodoPago) {
		getMetodoPagos().remove(metodoPago);
		metodoPago.setTipoPagoBean(null);

		return metodoPago;
	}

}