package com.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.List;


/**
 * The persistent class for the venta database table.
 * 
 */
@Entity
@NamedQuery(name="Venta.findAll", query="SELECT v FROM Venta v")
public class Venta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="costo_envio")
	private BigInteger costoEnvio;

	private boolean estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_registro")
	private Date fechaRegistro;

	private BigInteger total;

	//bi-directional many-to-one association to DetalleCompraVenta
	@OneToMany(mappedBy="ventaBean")
	private List<DetalleCompraVenta> detalleCompraVentas;

	//bi-directional many-to-one association to Garantia
	@OneToMany(mappedBy="ventaBean")
	private List<Garantia> garantias;

	//bi-directional many-to-one association to ClienteCuenta
	@ManyToOne
	@JoinColumn(name="cuenta_cliente")
	private ClienteCuenta clienteCuenta;

	//bi-directional many-to-one association to MetodoPago
	@ManyToOne
	@JoinColumn(name="metodo_pago")
	private MetodoPago metodoPagoBean;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="cliente")
	private Usuario usuario1;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="vendedor")
	private Usuario usuario2;

	public Venta() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigInteger getCostoEnvio() {
		return this.costoEnvio;
	}

	public void setCostoEnvio(BigInteger costoEnvio) {
		this.costoEnvio = costoEnvio;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public BigInteger getTotal() {
		return this.total;
	}

	public void setTotal(BigInteger total) {
		this.total = total;
	}

	public List<DetalleCompraVenta> getDetalleCompraVentas() {
		return this.detalleCompraVentas;
	}

	public void setDetalleCompraVentas(List<DetalleCompraVenta> detalleCompraVentas) {
		this.detalleCompraVentas = detalleCompraVentas;
	}

	public DetalleCompraVenta addDetalleCompraVenta(DetalleCompraVenta detalleCompraVenta) {
		getDetalleCompraVentas().add(detalleCompraVenta);
		detalleCompraVenta.setVentaBean(this);

		return detalleCompraVenta;
	}

	public DetalleCompraVenta removeDetalleCompraVenta(DetalleCompraVenta detalleCompraVenta) {
		getDetalleCompraVentas().remove(detalleCompraVenta);
		detalleCompraVenta.setVentaBean(null);

		return detalleCompraVenta;
	}

	public List<Garantia> getGarantias() {
		return this.garantias;
	}

	public void setGarantias(List<Garantia> garantias) {
		this.garantias = garantias;
	}

	public Garantia addGarantia(Garantia garantia) {
		getGarantias().add(garantia);
		garantia.setVentaBean(this);

		return garantia;
	}

	public Garantia removeGarantia(Garantia garantia) {
		getGarantias().remove(garantia);
		garantia.setVentaBean(null);

		return garantia;
	}

	public ClienteCuenta getClienteCuenta() {
		return this.clienteCuenta;
	}

	public void setClienteCuenta(ClienteCuenta clienteCuenta) {
		this.clienteCuenta = clienteCuenta;
	}

	public MetodoPago getMetodoPagoBean() {
		return this.metodoPagoBean;
	}

	public void setMetodoPagoBean(MetodoPago metodoPagoBean) {
		this.metodoPagoBean = metodoPagoBean;
	}

	public Usuario getUsuario1() {
		return this.usuario1;
	}

	public void setUsuario1(Usuario usuario1) {
		this.usuario1 = usuario1;
	}

	public Usuario getUsuario2() {
		return this.usuario2;
	}

	public void setUsuario2(Usuario usuario2) {
		this.usuario2 = usuario2;
	}

}