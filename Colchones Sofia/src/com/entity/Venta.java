package com.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * Implementation Venta. 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@NamedQuery(name="Venta.findAll", query="SELECT v FROM Venta v")
public class Venta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_venta")
	private int idVenta;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_actualizacion")
	private Date fechaActualizacion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_registro")
	private Date fechaRegistro;

	private BigInteger total;

	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@OneToMany(mappedBy="venta")
	private List<DetalleVenta> detalleVentas;

	@OneToMany(mappedBy="venta")
	private List<DevolucionGarantia> devolucionGarantias;

	@OneToMany(mappedBy="venta")
	private List<EstadoVenta> estadoVentas;

	@OneToMany(mappedBy="venta")
	private List<HistorialPresupuesto> historialPresupuestos;

	@ManyToOne
	@JoinColumn(name="id_cliente")
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name="id_pago")
	private TipoPago tipoPago;

	@ManyToOne
	@JoinColumn(name="id_vendedor")
	private Vendedor vendedor;

	@ManyToOne
	@JoinColumn(name="usuario")
	private Usuario usuario;

	
	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public Venta() {
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "Venta [idVenta=" + idVenta + ", total=" + total + ", cliente=" + cliente + ", vendedor=" + vendedor
				+ "]";
	}
	
	public DetalleVenta addDetalleVenta(DetalleVenta detalleVenta) {
		getDetalleVentas().add(detalleVenta);
		detalleVenta.setVenta(this);
		return detalleVenta;
	}

	public DetalleVenta removeDetalleVenta(DetalleVenta detalleVenta) {
		getDetalleVentas().remove(detalleVenta);
		detalleVenta.setVenta(null);
		return detalleVenta;
	}
	
	public DevolucionGarantia addDevolucionGarantia(DevolucionGarantia devolucionGarantia) {
		getDevolucionGarantias().add(devolucionGarantia);
		devolucionGarantia.setVenta(this);
		return devolucionGarantia;
	}

	public DevolucionGarantia removeDevolucionGarantia(DevolucionGarantia devolucionGarantia) {
		getDevolucionGarantias().remove(devolucionGarantia);
		devolucionGarantia.setVenta(null);
		return devolucionGarantia;
	}
	
	public HistorialPresupuesto addHistorialPresupuesto(HistorialPresupuesto historialPresupuesto) {
		getHistorialPresupuestos().add(historialPresupuesto);
		historialPresupuesto.setVenta(this);
		return historialPresupuesto;
	}

	public HistorialPresupuesto removeHistorialPresupuesto(HistorialPresupuesto historialPresupuesto) {
		getHistorialPresupuestos().remove(historialPresupuesto);
		historialPresupuesto.setVenta(null);
		return historialPresupuesto;
	}
	
	public EstadoVenta addEstadoVenta(EstadoVenta estadoVenta) {
		getEstadoVentas().add(estadoVenta);
		estadoVenta.setVenta(this);
		return estadoVenta;
	}

	public EstadoVenta removeEstadoVenta(EstadoVenta estadoVenta) {
		getEstadoVentas().remove(estadoVenta);
		estadoVenta.setVenta(null);
		return estadoVenta;
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public int getIdVenta() {
		return this.idVenta;
	}

	public void setIdVenta(int idVenta) {
		this.idVenta = idVenta;
	}

	public Date getFechaActualizacion() {
		return this.fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
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

	public List<DetalleVenta> getDetalleVentas() {
		return this.detalleVentas;
	}

	public void setDetalleVentas(List<DetalleVenta> detalleVentas) {
		this.detalleVentas = detalleVentas;
	}

	public List<DevolucionGarantia> getDevolucionGarantias() {
		return this.devolucionGarantias;
	}

	public void setDevolucionGarantias(List<DevolucionGarantia> devolucionGarantias) {
		this.devolucionGarantias = devolucionGarantias;
	}

	public List<EstadoVenta> getEstadoVentas() {
		return this.estadoVentas;
	}

	public void setEstadoVentas(List<EstadoVenta> estadoVentas) {
		this.estadoVentas = estadoVentas;
	}

	public List<HistorialPresupuesto> getHistorialPresupuestos() {
		return this.historialPresupuestos;
	}

	public void setHistorialPresupuestos(List<HistorialPresupuesto> historialPresupuestos) {
		this.historialPresupuestos = historialPresupuestos;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public TipoPago getTipoPago() {
		return this.tipoPago;
	}

	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

	public Vendedor getVendedor() {
		return this.vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}