package com.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
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
@NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int documento;

	private String clave;
	private boolean estado;
	private boolean session;

	@Column(name = "email_confirmacion")
	private String emailConfirmacion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_registro")
	private Date fechaRegistro;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_sesion")
	private Date fechaSesion;

	@Lob
	private byte[] foto;

	@Column(name = "ultima_clave")
	private String ultimaClave;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ultima_session")
	private Date ultimaSession;

	///////////////////////////////////////////////////////
	// Map
	///////////////////////////////////////////////////////
	@OneToMany(mappedBy = "usuario")
	private List<Categoria> categorias;

	@OneToMany(mappedBy = "usuario")
	private List<Cliente> clientes;

	@OneToMany(mappedBy = "usuario")
	private List<Compra> compras;

	@OneToMany(mappedBy = "usuario")
	private List<DevolucionGarantia> devolucionGarantias;

	@OneToMany(mappedBy = "usuario")
	private List<EstadoVenta> estadoVentas;

	@OneToMany(mappedBy = "usuario")
	private List<Producto> productos;

	@OneToMany(mappedBy = "usuario")
	private List<Proveedor> proveedors;

	@OneToMany(mappedBy = "usuario")
	private List<ProveedorProducto> proveedorProductos;

	@OneToOne
	@JoinColumn(name = "documento")
	private Persona persona;

	@ManyToOne
	@JoinColumn(name = "rol")
	private Rol rol;

	@OneToMany(mappedBy = "usuario")
	private List<Vendedor> vendedors;

	@OneToMany(mappedBy = "usuario")
	private List<Venta> ventas;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public Usuario() {
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public String toString() {
		return "Usuario [documento=" + documento + ", estado=" + estado + ", session=" + session + "]";
	}

	public Categoria addCategoria(Categoria categoria) {
		getCategorias().add(categoria);
		categoria.setUsuario(this);
		return categoria;
	}

	public Categoria removeCategoria(Categoria categoria) {
		getCategorias().remove(categoria);
		categoria.setUsuario(null);
		return categoria;
	}

	public DevolucionGarantia addDevolucionGarantia(DevolucionGarantia devolucionGarantia) {
		getDevolucionGarantias().add(devolucionGarantia);
		devolucionGarantia.setUsuario(this);
		return devolucionGarantia;
	}

	public DevolucionGarantia removeDevolucionGarantia(DevolucionGarantia devolucionGarantia) {
		getDevolucionGarantias().remove(devolucionGarantia);
		devolucionGarantia.setUsuario(null);
		return devolucionGarantia;
	}

	public Cliente addCliente(Cliente cliente) {
		getClientes().add(cliente);
		cliente.setUsuario(this);
		return cliente;
	}

	public Cliente removeCliente(Cliente cliente) {
		getClientes().remove(cliente);
		cliente.setUsuario(null);
		return cliente;
	}

	public EstadoVenta addEstadoVenta(EstadoVenta estadoVenta) {
		getEstadoVentas().add(estadoVenta);
		estadoVenta.setUsuario(this);
		return estadoVenta;
	}

	public EstadoVenta removeEstadoVenta(EstadoVenta estadoVenta) {
		getEstadoVentas().remove(estadoVenta);
		estadoVenta.setUsuario(null);
		return estadoVenta;
	}

	public Compra addCompra(Compra compra) {
		getCompras().add(compra);
		compra.setUsuario(this);
		return compra;
	}

	public Compra removeCompra(Compra compra) {
		getCompras().remove(compra);
		compra.setUsuario(null);
		return compra;
	}

	public Producto addProducto(Producto producto) {
		getProductos().add(producto);
		producto.setUsuario(this);
		return producto;
	}

	public Producto removeProducto(Producto producto) {
		getProductos().remove(producto);
		producto.setUsuario(null);
		return producto;
	}

	public Proveedor addProveedor(Proveedor proveedor) {
		getProveedors().add(proveedor);
		proveedor.setUsuario(this);
		return proveedor;
	}

	public Proveedor removeProveedor(Proveedor proveedor) {
		getProveedors().remove(proveedor);
		proveedor.setUsuario(null);
		return proveedor;
	}

	public ProveedorProducto addProveedorProducto(ProveedorProducto proveedorProducto) {
		getProveedorProductos().add(proveedorProducto);
		proveedorProducto.setUsuario(this);
		return proveedorProducto;
	}

	public ProveedorProducto removeProveedorProducto(ProveedorProducto proveedorProducto) {
		getProveedorProductos().remove(proveedorProducto);
		proveedorProducto.setUsuario(null);
		return proveedorProducto;
	}

	public Vendedor addVendedor(Vendedor vendedor) {
		getVendedors().add(vendedor);
		vendedor.setUsuario(this);
		return vendedor;
	}

	public Vendedor removeVendedor(Vendedor vendedor) {
		getVendedors().remove(vendedor);
		vendedor.setUsuario(null);
		return vendedor;
	}

	public Venta addVenta(Venta venta) {
		getVentas().add(venta);
		venta.setUsuario(this);
		return venta;
	}

	public Venta removeVenta(Venta venta) {
		getVentas().remove(venta);
		venta.setUsuario(null);
		return venta;
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public int getDocumento() {
		return this.documento;
	}

	public void setDocumento(int documento) {
		this.documento = documento;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getEmailConfirmacion() {
		return this.emailConfirmacion;
	}

	public void setEmailConfirmacion(String emailConfirmacion) {
		this.emailConfirmacion = emailConfirmacion;
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

	public Date getFechaSesion() {
		return this.fechaSesion;
	}

	public void setFechaSesion(Date fechaSesion) {
		this.fechaSesion = fechaSesion;
	}

	public byte[] getFoto() {
		return this.foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public boolean getSession() {
		return this.session;
	}

	public void setSession(boolean session) {
		this.session = session;
	}

	public String getUltimaClave() {
		return this.ultimaClave;
	}

	public void setUltimaClave(String ultimaClave) {
		this.ultimaClave = ultimaClave;
	}

	public Date getUltimaSession() {
		return this.ultimaSession;
	}

	public void setUltimaSession(Date ultimaSession) {
		this.ultimaSession = ultimaSession;
	}

	public List<Categoria> getCategorias() {
		return this.categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public List<Cliente> getClientes() {
		return this.clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<Compra> getCompras() {
		return this.compras;
	}

	public void setCompras(List<Compra> compras) {
		this.compras = compras;
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

	public List<Producto> getProductos() {
		return this.productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public List<Proveedor> getProveedors() {
		return this.proveedors;
	}

	public void setProveedors(List<Proveedor> proveedors) {
		this.proveedors = proveedors;
	}

	public List<ProveedorProducto> getProveedorProductos() {
		return this.proveedorProductos;
	}

	public void setProveedorProductos(List<ProveedorProducto> proveedorProductos) {
		this.proveedorProductos = proveedorProductos;
	}

	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Rol getRol() {
		return this.rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public List<Vendedor> getVendedors() {
		return this.vendedors;
	}

	public void setVendedors(List<Vendedor> vendedors) {
		this.vendedors = vendedors;
	}

	public List<Venta> getVentas() {
		return this.ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}
	
}