package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.List;

/**
 * Implementation Venta.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@Table(name = "venta") 
@NamedQuery(name="Venta.findAll", query="SELECT v FROM Venta v")
public class Venta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;
	
	private boolean estado;
	private BigInteger total;
	private String iva;

	@Column(name="costo_envio")
	private BigInteger costoEnvio;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@OneToMany(mappedBy="ventaBean")
	private List<DetalleCompraVenta> detalleCompraVentas;
	
	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@ManyToOne
	@JoinColumn(name="cuenta_cliente")
	private ClienteCuenta clienteCuenta;

	@ManyToOne
	@JoinColumn(name="metodo_pago")
	private MetodoPago metodoPagoBean;

	@ManyToOne
	@JoinColumn(name="cliente")
	private Usuario usuario1;

	@ManyToOne
	@JoinColumn(name="vendedor")
	private Usuario usuario2;
	
	@ManyToOne
	@JoinColumn(name="usuario")
	private Usuario usuario3;
	
	@OneToMany(mappedBy="ventaEstadoVenta")
	private List<EstadoVenta> estados;
	
	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public Venta() {
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "Venta [id=" + id + ", estado=" + estado + ", total=" + total + "]";
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
	

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
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

	public String getIva() {
		return iva;
	}

	public void setIva(String iva) {
		this.iva = iva;
	}

	public Usuario getUsuario3() {
		return usuario3;
	}

	public void setUsuario3(Usuario usuario3) {
		this.usuario3 = usuario3;
	}

	public List<EstadoVenta> getEstados() {
		return estados;
	}

	public void setEstados(List<EstadoVenta> estados) {
		this.estados = estados;
	}
}