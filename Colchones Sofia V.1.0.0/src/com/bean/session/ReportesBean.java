package com.bean.session;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.controller.CompraController;
import com.controller.ProductoController;
import com.controller.ProveedorController;
import com.controller.UsuarioController;
import com.controller.VentaController;
import com.model.Compra;
import com.model.Producto;
import com.model.Proveedor;
import com.model.Usuario;
import com.model.Venta;
import com.model.other.Reporte;
import com.util.Fecha;

@ManagedBean(name = "reporte")
@SessionScoped
public class ReportesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private FacesMessage mensage;

	private Reporte<Usuario> reporte_fecha_cliente;
	private List<Usuario> clientes;
	private List<Usuario> cliente_filter;

	private Reporte<Usuario> reporte_fecha_vendedor;
	private List<Usuario> vendedores;
	private List<Usuario> vendedor_filter;

	private Reporte<Proveedor> reporte_fecha_proveedor;
	private List<Proveedor> proveedores;
	private List<Proveedor> proveedor_filter;

	private Reporte<Compra> reporte_fecha_compra;
	private List<Compra> compras;
	private List<Compra> compra_filter;

	private Reporte<Venta> reporte_fecha_venta;
	private List<Venta> ventas;
	private List<Venta> venta_filter;

	private Date fecha_inicio;
	private String fecha_formato_inicio;
	private Date fecha_fin;
	private String fecha_formato_fin;

	private Reporte<Producto> reporte_fecha_producto;
	private List<Producto> productos;
	private List<Producto> producto_filter;

	private String path;
	private String formato_fecha;
	
	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean sesion;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public ReportesBean() { 

	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		path = "/resources/report/";
		formato_fecha ="yyyy/MM/dd HH:mm:ss";
	}

	///////////////////////////////////////////////////////
	// Method Provider
	///////////////////////////////////////////////////////
	/**
	 * 
	 */
	public void consultarProveedor() {
		this.mensage = null;
		if (this.fecha_inicio != null) {
			if (this.fecha_fin != null) {
				ProveedorController dao = new ProveedorController();
				Fecha fecha = new Fecha();
				this.fecha_formato_inicio = fecha.darFormato(fecha_inicio, formato_fecha);
				this.fecha_formato_fin = fecha.darFormato(fecha_fin, formato_fecha);
				System.out.println(this.fecha_formato_inicio + "   " + this.fecha_formato_fin);
				this.proveedores = dao.consultaProveedor(this.fecha_formato_inicio, this.fecha_formato_fin);
				if (this.proveedores.size() > 0) {
					this.mensage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
							"Se han obtenido " + this.proveedores.size() + " proveedores.");
				} else {
					this.mensage = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"No se ha encontrado ningun proveedor.");
				}

			} else {
				this.mensage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"El campo fecha fin es obligatorio.");
			}
		} else {
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"El campo fecha inicio es obligatorio.");
		}

		if (this.mensage != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensage);
		}
	}
	
	/**
	 * 
	 */
	public void limpirarFiltroProveedor() {
		if(this.proveedores!= null && this.proveedores.size() > 0) {
			this.proveedores = null;
			this.fecha_fin = null;
			this.fecha_inicio = null;
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"Se ha limpiado el filtro de proveedor.");
		}else {
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"No has filtrado ninguna proveedor.");
		}
		
		if (this.mensage != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensage);
		}
	}

	/*
	 * 
	 */
	public void exportarPDF(ActionEvent actionEvent) throws Exception {
		this.reporte_fecha_proveedor = new Reporte<Proveedor>(this.proveedores, this.path + "reporteProveedores.jasper",
				sesion.getLogeado());
		this.reporte_fecha_proveedor.exportarPDF(actionEvent);
	}

	/*
	 * 
	 */
	public void verPDF(ActionEvent actionEvent) throws Exception {
		this.reporte_fecha_proveedor = new Reporte<Proveedor>(this.proveedores, this.path + "reporteProveedores.jasper",
				sesion.getLogeado());
		this.reporte_fecha_proveedor.verPDF(actionEvent);
	}

	///////////////////////////////////////////////////////
	// Method Buy
	///////////////////////////////////////////////////////
	/**
	 * 
	 */
	public void consultarCompra() {
		this.mensage = null;
		if (this.fecha_inicio != null) {
			if (this.fecha_fin != null) {
				CompraController dao = new CompraController();
				Fecha fecha = new Fecha();
				this.fecha_formato_inicio = fecha.darFormato(fecha_inicio, formato_fecha);
				this.fecha_formato_fin = fecha.darFormato(fecha_fin, formato_fecha);
				this.compras = dao.consultarCompra(this.fecha_formato_inicio, this.fecha_formato_fin);
				if (this.compras.size() > 0) {
					this.mensage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
							"Se han obtenido " + this.compras.size() + " compras.");
				} else {
					this.mensage = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"No se ha encontrado ninguna compra.");
				}

			} else {
				this.mensage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"El campo fecha fin es obligatorio.");
			}
		} else {
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"El campo fecha inicio es obligatorio.");
		}

		if (this.mensage != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensage);
		}
	}
	
	/**
	 * 
	 */
	public void limpirarFiltroCompra() {
		if(this.compras!= null && this.compras.size() > 0) {
			this.compras = null;
			this.fecha_fin = null;
			this.fecha_inicio = null;
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"Se ha limpiado el filtro de compra.");
		}else {
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"No has filtrado ninguna compra.");
		}
		
		if (this.mensage != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensage);
		}
	}

	/**
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void exportarPDFCompra(ActionEvent actionEvent) throws Exception {
		this.reporte_fecha_compra = new Reporte<Compra>(this.compras, this.path + "reporteCompras.jasper",
				sesion.getLogeado());
		this.reporte_fecha_compra.exportarPDF(actionEvent);
		this.reporte_fecha_compra.verPDF(actionEvent);
	}

	public void verPDFCompra(ActionEvent actionEvent) throws Exception {
		this.reporte_fecha_compra = new Reporte<Compra>(this.compras, this.path + "reporteCompras.jasper",
				sesion.getLogeado());
		this.reporte_fecha_compra.verPDF(actionEvent);
	}

	///////////////////////////////////////////////////////
	// Method Sale
	///////////////////////////////////////////////////////
	/**
	 * 
	 */
	public void consultarVenta() {
		this.mensage = null;
		if (this.fecha_inicio != null) {
			if (this.fecha_fin != null) {
				VentaController dao = new VentaController();
				Fecha fecha = new Fecha();
				this.fecha_formato_inicio = fecha.darFormato(fecha_inicio, formato_fecha);
				this.fecha_formato_fin = fecha.darFormato(fecha_fin, formato_fecha);

				this.ventas = dao.consultarVenta(this.fecha_formato_inicio, this.fecha_formato_fin);
				if (this.ventas.size() > 0) {
					this.mensage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
							"Se han obtenido " + this.ventas.size() + " ventas.");
				} else {
					this.mensage = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"No se ha encontrado ninguna venta.");
				}

			} else {
				this.mensage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"El campo fecha fin es obligatorio.");
			}
		} else {
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"El campo fecha inicio es obligatorio.");
		}

		if (this.mensage != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensage);
		}
	}
	
	/**
	 * 
	 */
	public void limpirarFiltroVentas() {
		if(this.ventas!= null && this.ventas.size() > 0) {
			this.ventas = null;
			this.fecha_fin = null;
			this.fecha_inicio = null;
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"Se ha limpiado el filtro de venta.");
		}else {
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"No has filtrado ninguna venta.");
		}
		
		if (this.mensage != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensage);
		}
	}
	

	/**
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void exportarPDFVentas(ActionEvent actionEvent) throws Exception {
		this.reporte_fecha_venta = new Reporte<Venta>(this.ventas, this.path + "reporteVentas.jasper",
				sesion.getLogeado());
		this.reporte_fecha_venta.exportarPDF(actionEvent);

	}

	/**
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void verPDFVentas(ActionEvent actionEvent) throws Exception {
		this.reporte_fecha_venta = new Reporte<Venta>(this.ventas, this.path + "reporteVentas.jasper",
				sesion.getLogeado());
		this.reporte_fecha_venta.verPDF(actionEvent);
	}

	///////////////////////////////////////////////////////
	// Method Seller
	///////////////////////////////////////////////////////
	/**
	 * 
	 */
	public void consultarVendedor() {
		this.mensage = null;
		if (this.fecha_inicio != null) {
			if (this.fecha_fin != null) {
				UsuarioController dao = new UsuarioController();
				Fecha fecha = new Fecha();
				this.fecha_formato_inicio = fecha.darFormato(fecha_inicio, formato_fecha);
				this.fecha_formato_fin = fecha.darFormato(fecha_fin, formato_fecha);

				this.vendedores = dao.consultarVendedor(this.fecha_formato_inicio, this.fecha_formato_fin);
				if (this.vendedores.size() > 0) {
					this.mensage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
							"Se han obtenido " + this.vendedores.size() + " vendedores.");
				} else {
					this.mensage = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"No se ha encontrado ningun vendedor.");
				}

			} else {
				this.mensage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"El campo fecha fin es obligatorio.");
			}
		} else {
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"El campo fecha inicio es obligatorio.");
		}

		if (this.mensage != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensage);
		}
	}
	
	/**
	 * 
	 */
	public void limpirarFiltroVendedor() {
		if(this.vendedores!= null && this.vendedores.size() > 0) {
			this.vendedores = null;
			this.fecha_fin = null;
			this.fecha_inicio = null;
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"Se ha limpiado el filtro de vendedor.");
		}else {
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"No has filtrado ninguna vendedor.");
		}
		
		if (this.mensage != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensage);
		}
	}

	/**
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void exportarPDFVendedores(ActionEvent actionEvent) throws Exception {
		this.reporte_fecha_vendedor = new Reporte<Usuario>(this.vendedores, this.path + "reporteVendedores.jasper",
				sesion.getLogeado());
		this.reporte_fecha_vendedor.exportarPDF(actionEvent);
	}

	/**
	 * 
	 * @param actionEvent
	 * @throws Exception
	 */
	public void verPDFVendedores(ActionEvent actionEvent) throws Exception {
		this.reporte_fecha_vendedor = new Reporte<Usuario>(this.vendedores, this.path + "reporteVendedores.jasper",
				sesion.getLogeado());
		this.reporte_fecha_vendedor.verPDF(actionEvent);
	}

	///////////////////////////////////////////////////////
	// Method Coustomer
	///////////////////////////////////////////////////////
	/**
	 * 
	 */
	public void consultarCliente() {
		this.mensage = null;
		if (this.fecha_inicio != null) {
			if (this.fecha_fin != null) {
				UsuarioController dao = new UsuarioController();
				Fecha fecha = new Fecha();
				this.fecha_formato_inicio = fecha.darFormato(fecha_inicio, formato_fecha);
				this.fecha_formato_fin = fecha.darFormato(fecha_fin, formato_fecha);

				this.clientes = dao.consultarCliente(this.fecha_formato_inicio, this.fecha_formato_fin);
				if (this.clientes.size() > 0) {
					this.mensage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
							"Se han obtenido " + this.clientes.size() + " clientes.");
				} else {
					this.mensage = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"No se ha encontrado ningun cliente.");
				}

			} else {
				this.mensage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"El campo fecha fin es obligatorio.");
			}
		} else {
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"El campo fecha inicio es obligatorio.");
		}

		if (this.mensage != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensage);
		}
	}
	
	/**
	 * 
	 */
	public void limpirarFiltroCliente() {
		if(this.clientes!= null && this.clientes.size() > 0) {
			this.clientes = null;
			this.fecha_fin = null;
			this.fecha_inicio = null;
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"Se ha limpiado el filtro de cliente.");
		}else {
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"No has filtrado ninguna cliente.");
		}
		
		if (this.mensage != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensage);
		}
	}

	public void exportarPDFClientes(ActionEvent actionEvent) throws Exception {
		this.reporte_fecha_cliente = new Reporte<Usuario>(this.clientes, this.path + "reporteClientes.jasper",
				sesion.getLogeado());
		this.reporte_fecha_cliente.exportarPDF(actionEvent);

	}

	public void verPDFClientes(ActionEvent actionEvent) throws Exception {
		this.reporte_fecha_cliente = new Reporte<Usuario>(this.clientes, this.path + "reporteClientes.jasper",
				sesion.getLogeado());
		this.reporte_fecha_cliente.verPDF(actionEvent);
	}

	///////////////////////////////////////////////////////
	// Method Product
	///////////////////////////////////////////////////////
	/**
	 * 
	 */
	public void consultarProducto() {
		this.mensage = null;
		if (this.fecha_inicio != null) {
			if (this.fecha_fin != null) {
				ProductoController dao = new ProductoController();
				Fecha fecha = new Fecha();
				this.fecha_formato_inicio = fecha.darFormato(fecha_inicio, formato_fecha);
				this.fecha_formato_fin = fecha.darFormato(fecha_fin, formato_fecha);

				this.productos = dao.consultarProducto(this.fecha_formato_inicio, this.fecha_formato_fin);
				if (this.productos.size() > 0) {
					this.mensage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
							"Se han obtenido " + this.productos.size() + " productos.");
				} else {
					this.mensage = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"No se ha encontrado ningun productos.");
				}
			} else {
				this.mensage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"El campo fecha fin es obligatorio.");
			}
		} else {
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"El campo fecha inicio es obligatorio.");
		}

		if (this.mensage != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensage);
		}
	}
	
	/**
	 * 
	 */
	public void limpirarFiltroProducto() {
		if(this.productos!= null && this.productos.size() > 0) {
			this.productos = null;
			this.fecha_fin = null;
			this.fecha_inicio = null;
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"Se ha limpiado el filtro de producto.");
		}else {
			this.mensage = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"No has filtrado ninguna producto.");
		}
		
		if (this.mensage != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.mensage);
		}
	}

	public void exportarPDFProductos(ActionEvent actionEvent) throws Exception {
		this.reporte_fecha_producto = new Reporte<Producto>(this.productos, this.path + "reporteProductos.jasper",
				sesion.getLogeado());
		this.reporte_fecha_producto.exportarPDF(actionEvent);

	}

	public void verPDFProductos(ActionEvent actionEvent) throws Exception {
		this.reporte_fecha_producto = new Reporte<Producto>(this.productos, this.path + "reporteProductos.jasper",
				sesion.getLogeado());
		this.reporte_fecha_producto.verPDF(actionEvent);
	}

	///////////////////////////////////////////////////////
	// Method Getter and Setters
	///////////////////////////////////////////////////////
	public SessionBean getSesion() {
		return sesion;
	}

	public Reporte<Usuario> getReporte_fecha_vendedor() {
		return reporte_fecha_vendedor;
	}

	public void setReporte_fecha_vendedor(Reporte<Usuario> reporte_fecha_vendedor) {
		this.reporte_fecha_vendedor = reporte_fecha_vendedor;
	}

	public List<Usuario> getVendedores() {
		return vendedores;
	}

	public void setVendedores(List<Usuario> vendedores) {
		this.vendedores = vendedores;
	}

	public List<Usuario> getVendedor_filter() {
		return vendedor_filter;
	}

	public void setVendedor_filter(List<Usuario> vendedor_filter) {
		this.vendedor_filter = vendedor_filter;
	}

	public Reporte<Compra> getReporte_fecha_compra() {
		return reporte_fecha_compra;
	}

	public void setReporte_fecha_compra(Reporte<Compra> reporte_fecha_compra) {
		this.reporte_fecha_compra = reporte_fecha_compra;
	}

	public List<Compra> getCompras() {
		return compras;
	}

	public void setCompras(List<Compra> compras) {
		this.compras = compras;
	}

	public void setSesion(SessionBean sesion) {
		this.sesion = sesion;
	}

	public FacesMessage getMensage() {
		return mensage;
	}

	public void setMensage(FacesMessage mensage) {
		this.mensage = mensage;
	}

	public Reporte<Proveedor> getReporte_fecha_proveedor() {
		return reporte_fecha_proveedor;
	}

	public void setReporte_fecha_proveedor(Reporte<Proveedor> reporte_fecha_proveedor) {
		this.reporte_fecha_proveedor = reporte_fecha_proveedor;
	}

	public List<Proveedor> getProveedores() {
		return proveedores;
	}

	public void setProveedores(List<Proveedor> proveedores) {
		this.proveedores = proveedores;
	}

	public List<Proveedor> getProveedor_filter() {
		return proveedor_filter;
	}

	public void setProveedor_filter(List<Proveedor> proveedor_filter) {
		this.proveedor_filter = proveedor_filter;
	}

	public Date getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public String getFecha_formato_inicio() {
		return fecha_formato_inicio;
	}

	public void setFecha_formato_inicio(String fecha_formato_inicio) {
		this.fecha_formato_inicio = fecha_formato_inicio;
	}

	public Date getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(Date fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	public String getFecha_formato_fin() {
		return fecha_formato_fin;
	}

	public void setFecha_formato_fin(String fecha_formato_fin) {
		this.fecha_formato_fin = fecha_formato_fin;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Compra> getCompra_filter() {
		return compra_filter;
	}

	public void setCompra_filter(List<Compra> compra_filter) {
		this.compra_filter = compra_filter;
	}

	public List<Venta> getVentas() {
		return ventas;
	}

	public void setVentas(List<Venta> ventas) {
		this.ventas = ventas;
	}

	public Reporte<Venta> getReporte_fecha_venta() {
		return reporte_fecha_venta;
	}

	public void setReporte_fecha_venta(Reporte<Venta> reporte_fecha_venta) {
		this.reporte_fecha_venta = reporte_fecha_venta;
	}

	public List<Venta> getVenta_filter() {
		return venta_filter;
	}

	public void setVenta_filter(List<Venta> venta_filter) {
		this.venta_filter = venta_filter;
	}

	public Reporte<Usuario> getReporte_fecha_cliente() {
		return reporte_fecha_cliente;
	}

	public void setReporte_fecha_cliente(Reporte<Usuario> reporte_fecha_cliente) {
		this.reporte_fecha_cliente = reporte_fecha_cliente;
	}

	public List<Usuario> getClientes() {
		return clientes;
	}

	public void setClientes(List<Usuario> clientes) {
		this.clientes = clientes;
	}

	public List<Usuario> getCliente_filter() {
		return cliente_filter;
	}

	public void setCliente_filter(List<Usuario> cliente_filter) {
		this.cliente_filter = cliente_filter;
	}

	public Reporte<Producto> getReporte_fecha_producto() {
		return reporte_fecha_producto;
	}

	public void setReporte_fecha_producto(Reporte<Producto> reporte_fecha_producto) {
		this.reporte_fecha_producto = reporte_fecha_producto;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public List<Producto> getProducto_filter() {
		return producto_filter;
	}

	public void setProducto_filter(List<Producto> producto_filter) {
		this.producto_filter = producto_filter;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFormato_fecha() {
		return formato_fecha;
	}

	public void setFormato_fecha(String formato_fecha) {
		this.formato_fecha = formato_fecha;
	}
}