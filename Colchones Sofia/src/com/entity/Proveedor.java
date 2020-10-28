package com.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Implementation Proveedor.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@Entity
@NamedQuery(name = "Proveedor.findAll", query = "SELECT p FROM Proveedor p")
public class Proveedor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_proveedor")
	private int idProveedor;
	
	@Column(name = "nombre")
	private String nombre;
	@Column(name = "telefono")
	private String telefono;
	private String direccion;
	private boolean estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_actualizacion")
	private Date fechaActualizacion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_registro")
	private Date fechaRegistro;

	@Lob
	private byte[] foto;

	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@OneToMany(mappedBy = "proveedor")
	private List<DetalleCompra> detalleCompras;

	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;

	@OneToMany(mappedBy = "proveedor")
	private List<ProveedorProducto> proveedorProductos;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public Proveedor() {
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "Proveedor [idProveedor=" + idProveedor + ", nombre=" + nombre + ", estado=" + estado + ", usuario="
				+ usuario + "]";
	}

	public DetalleCompra addDetalleCompra(DetalleCompra detalleCompra) {
		getDetalleCompras().add(detalleCompra);
		detalleCompra.setProveedor(this);
		return detalleCompra;
	}

	public DetalleCompra removeDetalleCompra(DetalleCompra detalleCompra) {
		getDetalleCompras().remove(detalleCompra);
		detalleCompra.setProveedor(null);
		return detalleCompra;
	}

	public ProveedorProducto addProveedorProducto(ProveedorProducto proveedorProducto) {
		getProveedorProductos().add(proveedorProducto);
		proveedorProducto.setProveedor(this);
		return proveedorProducto;
	}

	public ProveedorProducto removeProveedorProducto(ProveedorProducto proveedorProducto) {
		getProveedorProductos().remove(proveedorProducto);
		proveedorProducto.setProveedor(null);
		return proveedorProducto;
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////

	public int getIdProveedor() {
		return this.idProveedor;
	}

	public void setIdProveedor(int idProveedor) {
		this.idProveedor = idProveedor;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
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

	public byte[] getFoto() {
		return this.foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public List<DetalleCompra> getDetalleCompras() {
		return this.detalleCompras;
	}

	public void setDetalleCompras(List<DetalleCompra> detalleCompras) {
		this.detalleCompras = detalleCompras;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<ProveedorProducto> getProveedorProductos() {
		return this.proveedorProductos;
	}

	public void setProveedorProductos(List<ProveedorProducto> proveedorProductos) {
		this.proveedorProductos = proveedorProductos;
	}
}