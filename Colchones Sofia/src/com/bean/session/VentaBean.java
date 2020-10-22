package com.bean.session;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.entity.*;
import com.entity.other.*;
import com.util.Face;
import com.util.Fecha;
import com.dao.*;

/**
 * Implementation VentaBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "venta")
@SessionScoped
public class VentaBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private FacesMessage message;

	private boolean hidden;
	private boolean hidden_tablas;

	private Venta venta;
	private DetalleVenta detalle_venta;

	private List<Producto> tabla_productos_seleccionados;
	private List<Producto> tabla_filter_productos;
	private List<Producto> tabla_productos;
	private boolean renderizar_tabla_productos;

	private List<VentaDetalleProducto> tabla_detalle_producto;
	private List<VentaDetalleProducto> tabla_filter_detalle_producto;
	private List<VentaDetalleProducto> tabla_detalle_producto_seleccionados;
	private boolean renderizar_tabla_detalle_producto;

	private List<VentaDetalleProducto> productos_selecionados;

	private List<Venta> tabla_venta;
	private List<DetalleVenta> tabla_venta_filter_detalle_venta;
	private List<Venta> tabla_filter_venta;
	private boolean renderizar_tabla_venta;

	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean sesion;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public VentaBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		this.venta = new Venta();
		this.venta.setTotal(BigInteger.ZERO);
		initCliente();
		initVenta();
		initProductosSelecionados();
		this.renderizar_tabla_venta = false;
	}

	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * Metodo que inicializa la venta.
	 */
	public void initVenta() {
		this.detalle_venta = new DetalleVenta();
		this.hidden = false;
		initRenderizarTablaProductos();
	}

	/**
	 * Metodo que inicializa el cliente.
	 */
	public void initCliente() {
		this.venta.setCliente(new Cliente());
		this.venta.getCliente().setPersona(new Persona());
		this.venta.getCliente().getPersona().setTipoDocumento(new TipoDocumento());
		this.venta.setTipoPago(new TipoPago());
	}

	/**
	 * Metodo que renderizar la tabla productos.
	 */
	public void initRenderizarTablaProductos() {
		this.renderizar_tabla_productos = true;
		this.renderizar_tabla_detalle_producto = false;
		this.hidden_tablas = false;
	}

	/**
	 * Metodo que renderizar la tabla detalle producto.
	 */
	public void initRederizarTablaDetalleProducto() {
		this.renderizar_tabla_productos = false;
		this.renderizar_tabla_detalle_producto = true;
		this.hidden_tablas = true;
	}

	/**
	 * Metodo que permite inicializar los productos selecionados.
	 */
	public void initProductosSelecionados() {
		this.productos_selecionados = new ArrayList<VentaDetalleProducto>();
	}

	/**
	 * Metodo que permite inicilizar la tabla de ventas.
	 */
	public void initRendedirzarTablaVenta() {
		this.renderizar_tabla_venta = true;
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite filtrar la información de un cliente.
	 */
	public void filtrarCliente() {
		this.hidden = false;
		this.message = null;
		if (this.venta != null && this.venta.getCliente() != null && this.venta.getCliente().getDocumento() > 0) {
			PersonaDao dao = new PersonaDao();
			Persona aux = dao.find(this.venta.getCliente().getDocumento());
			if (aux != null) {
				ClienteDao cDao = new ClienteDao();
				Cliente cliente = cDao.find(this.venta.getCliente().getDocumento());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Succes",
						"Se ha filtrado la persona con documento " + this.venta.getCliente().getDocumento() + "."));
				if (cliente != null) {
					this.venta.setCliente(cliente);
					this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
							"Se ha filtrado el cliente con documento " + this.venta.getCliente().getDocumento() + ".");
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"No se ha encontrado ningun cliente con ese documento "
									+ this.venta.getCliente().getDocumento() + ".");
				}
				this.venta.getCliente().setPersona(aux);
				this.hidden = true;
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"No se ha encontrado ninguna persona con ese documento "
								+ this.venta.getCliente().getDocumento() + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "No has seleccionado ningun cliente.");
		}
		if (!this.hidden) {
			initCliente();
		}
		if (message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * Metodo que agrega los productos seleccionado para consultar su detalles.
	 */
	public void addProductosADetalleProductos() {
		if (this.tabla_productos_seleccionados != null && this.tabla_productos_seleccionados.size() > 0) {
			initRederizarTablaDetalleProducto();
			this.getTabla_detalle_producto();
			this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succces",
					"Se han filtrado " + this.tabla_productos_seleccionados.size() + " detalles de productos.");
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "No has seleccionado ningun producto.");
		}
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}

	/**
	 * Metodo que permite regresar de tabla detalle producto a la de productos.
	 */
	public void regresarTabla() {
		this.initRenderizarTablaProductos();
		this.getTabla_productos();
		this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succces",
				"Se han filtrado " + this.tabla_productos.size() + " productos.");
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}

	/**
	 * Metodo que calcula un subtotal de los productos seleccionados.
	 * 
	 * @return representa el valor obtenido.
	 */
	public BigInteger subTotal(int id) {
		BigInteger valor = BigInteger.ZERO;
		if (id > 0) {
			int index = buscarDetalleVenta(this.productos_selecionados, id);
			if (index >= 0) {
				VentaDetalleProducto venta = this.productos_selecionados.get(index);
				valor = venta.getCantidad().multiply(venta.getDetalle().getPrecioVenta());
				valor = valor.subtract(venta.getDetalle().getDescuento());
			}
		}
		return valor;
	}

	/**
	 * Metodo que permite calular el total a pagar.
	 * 
	 * @return el total a pagar.
	 */
	public BigInteger total() {
		BigInteger valor = BigInteger.ZERO;
		for (VentaDetalleProducto v : this.productos_selecionados) {
			valor = valor.add(subTotal(v.getDetalle().getId()));
		}
		return valor;
	}

	///////////////////////////////////////////////////////
	// CRUD
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite agregar los productos selecionados.
	 */
	public void addDetallesProductos() {
		if (this.productos_selecionados == null) {
			this.initProductosSelecionados();
		}
		if (this.tabla_detalle_producto_seleccionados != null && this.tabla_detalle_producto_seleccionados.size() > 0) {
			int index = 0;
			int agregados = 0;
			for (VentaDetalleProducto vdp : this.tabla_detalle_producto_seleccionados) {
				if (vdp.getStock() >= vdp.getCantidad().intValue() && vdp.getCantidad().intValue() > 0) {
					double resta = (double) vdp.getStock() - vdp.getCantidad().intValue();
					vdp.setStock(resta);
					vdp.getDetalle().setStock((int) resta);
					int tmp = buscarDetalleVenta(this.productos_selecionados, vdp.getDetalle().getId());
					if (tmp >= 0) {
						VentaDetalleProducto v = this.productos_selecionados.get(tmp);
						vdp.setCantidad(vdp.getCantidad().add(v.getCantidad()));
					} else {
						this.productos_selecionados.add(vdp);
					}
					this.tabla_detalle_producto.set(index, vdp);
					agregados++;
				}
				index++;
			}
			if (agregados == this.tabla_detalle_producto_seleccionados.size()) {
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
						"Se han agregado todos los detalles de productos seleccionados (" + agregados + "/"
								+ this.tabla_detalle_producto_seleccionados.size() + ")");
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "Se han agregado (" + agregados
						+ "/" + this.tabla_detalle_producto_seleccionados.size() + ") detalles de productos.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
					"No has seleccionado ningun detalle de producto.");
		}
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}

	/**
	 * Metodo que permite eliminar los productos selecionados.
	 */
	public void removeDetallesProductos() {
		this.initProductosSelecionados();
		this.initRenderizarTablaProductos();
		this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
				"Se ha eliminado todos los productos seleccionados de venta.");
		FacesContext.getCurrentInstance().addMessage(null, this.message);

	}

	/**
	 * Metodo que recarga la tabla productos.
	 */
	public void recargarDetallesProductos() {
		this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se ha recargado la tabla productos.");
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}

	/**
	 * Metodo que permite decrementar el stock de un detalle producto.
	 */
	public void decrementarStock() {
		int id = Integer.parseInt(Face.get("id-producto-detalle"));
		if (id > 0) {
			int index = buscarDetalleVenta(this.tabla_detalle_producto, id);
			if (index >= 0) {
				VentaDetalleProducto v = this.tabla_detalle_producto.get(index);
				if (v != null) {
					if (v.getCantidad().intValue() > 0) {
						int aux = (int) v.getStock() - v.getCantidad().intValue();
						if (aux >= 0) {
							v.getDetalle().setStock(aux);
							this.tabla_detalle_producto.set(index, v);
						}
					}
				}
			}
		}
	}

	/**
	 * Metodo que permite eliminar una venta con sus productos y garantias.
	 */
	public void removeDetallesVentaAll() {
		int id = Integer.parseInt(Face.get("id-venta"));
		FacesContext.getCurrentInstance().addMessage(null, removeDetallesVentaAll(id));
	}

	/**
	 * Metodo que permite eliminar un detalle venta de una venta.
	 */
	public void removeDetalleVenta() {
		int id = Integer.parseInt(Face.get("id-detalle-venta"));
		FacesContext.getCurrentInstance().addMessage(null, removeDetallesVenta(id));
	}

	///////////////////////////////////////////////////////
	// Validator
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite validar los campos de int.
	 * 
	 * @param value  representa el valor.
	 * @param filter representa tipo filtro.
	 * @param locale representa dirección.
	 * @return retorna si se muestra en el filtro o no.
	 */
	public boolean filterByInteger(Object value, Object filter, Locale locale) {
		return Convertidor.filterByInteger(value, filter, locale);
	}

	/**
	 * Metodo que permite si ya existe un detalle de venta.
	 * 
	 * @param list representa la lista a buscar.
	 * @param id   representa el detalle venta.
	 * @return el indice en la lista.
	 */
	public int buscarDetalleVenta(List<VentaDetalleProducto> list, int id) {
		int index = -1;
		if (list != null) {
			int contador = 0;
			for (VentaDetalleProducto v : list) {
				if (v.getDetalle() != null && v.getDetalle().getId() == id) {
					return contador;
				}
				contador++;
			}
		}
		return index;
	}

	/**
	 * Metodo que permite conocer el index de una venta en una lista.
	 * 
	 * @param list representa la lista de ventas.
	 * @param id   representa el id de la venta a buscar.
	 * @return el indice en la lista.
	 */
	public int buscarVenta(List<Venta> list, int id) {
		int index = -1;
		if (list != null) {
			int contador = 0;
			for (Venta v : list) {
				if (v.getIdVenta() == id) {
					return contador;
				}
				contador++;
			}
		}
		return index;
	}

	///////////////////////////////////////////////////////
	// Renderizar
	///////////////////////////////////////////////////////
	/**
	 * Metodo que consulta todos los productos.
	 * 
	 * @return una lista con todos los productos.
	 */
	public List<Producto> getTabla_productos() {
		if (renderizar_tabla_productos) {
			CategoriaDao dao = new CategoriaDao();
			List<Categoria> categorias = dao.findByFieldList("estado", true);
			this.tabla_productos = new ArrayList<Producto>();
			for (Categoria c : categorias) {
				ProductoDao pDao = new ProductoDao();
				List<Producto> productos = pDao.findByFieldList("categoria", c);
				for (Producto p : productos) {
					if (p.getStock() > 0) {
						tabla_productos.add(p);
					}
				}
			}
			this.renderizar_tabla_productos = false;
			this.hidden_tablas = false;
		}
		return tabla_productos;
	}

	/**
	 * Metodo que permite filtrar los detalles de productos de los productos
	 * seleccionados.
	 */
	public List<VentaDetalleProducto> getTabla_detalle_producto() {
		if (this.renderizar_tabla_detalle_producto) {
			this.tabla_detalle_producto = new ArrayList<VentaDetalleProducto>();
			if (this.tabla_productos_seleccionados != null && this.tabla_productos_seleccionados.size() > 0) {
				for (Producto p : this.tabla_productos_seleccionados) {
					if (p.getStock() > 0) {
						DetalleProductoDao dao = new DetalleProductoDao();
						List<DetalleProducto> list = dao.findByFieldList("producto", p);
						for (DetalleProducto dp : list) {
							if (dp.getStock() > 0) {
								this.tabla_detalle_producto.add(new VentaDetalleProducto(dp, BigInteger.ZERO));
							}
						}
					}
				}
			}
			this.renderizar_tabla_detalle_producto = false;
			this.hidden_tablas = true;
		}
		return tabla_detalle_producto;
	}

	/**
	 * Metodo que permite traer todas las ventas realizada en la empresa.
	 */
	public List<Venta> getTabla_venta() {
		if (this.tabla_venta == null) {
			this.renderizar_tabla_venta = true;
		}
		if (this.renderizar_tabla_venta) {
			this.tabla_venta = new ArrayList<Venta>();
			VentaDao dao = new VentaDao();
			List<Venta> list = dao.list();
			for (Venta venta : list) {
				DetalleVentaDao dvDao = new DetalleVentaDao();
				venta.setDetalleVentas(dvDao.findByFieldList("venta", venta));
				EstadoVentaDao evDao = new EstadoVentaDao();
				venta.setEstadoVentas(evDao.findByFieldList("venta", venta));
				this.tabla_venta.add(venta);
			}
			this.renderizar_tabla_venta = false;
		}
		return tabla_venta;
	}

	///////////////////////////////////////////////////////
	// Face
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite eliminar una venta con sus productos y garantias.
	 * 
	 * @param id representa el id de la venta.
	 * @return el mensaje obtenido.
	 */
	@SuppressWarnings("deprecation")
	private FacesMessage removeDetallesVentaAll(int id) {
		int e_garantias = 0, e_detalles = 0;
		int ee_garantias = 0;
		int i_garantias;
		this.message = null;
		if (id > 0) {
			int index = buscarVenta(this.tabla_venta, id);
			if (index >= 0) {
				DetalleVentaDao dao = new DetalleVentaDao();
				Venta venta = this.tabla_venta.get(index);
				List<DetalleVenta> detalles = dao.findByFieldList("venta", venta);
				for (DetalleVenta dv : detalles) {
					i_garantias = 0;
					dao = new DetalleVentaDao();
					DevolucionGarantiaDao vgDao = new DevolucionGarantiaDao();
					List<DevolucionGarantia> garantia = vgDao.findByFieldList("venta", venta);
					for (DevolucionGarantia dg : garantia) {
						vgDao = new DevolucionGarantiaDao();
						vgDao.delete(dg);
						e_garantias++;
						i_garantias++;
					}
					if (garantia.size() != i_garantias) {
						ee_garantias++;
					}
					dao.delete(dv);
					e_detalles++;
				}
				if (detalles.size() != e_detalles) {
					if (e_garantias > 0 && ee_garantias == 0) {
						this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
								"Faltaron algunos detalles de venta por eliminar (" + e_detalles + "/" + detalles.size()
										+ "), y se han eliminado todas las garantias " + e_garantias + ".");
					} else {
						this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
								"Faltaron algunos detalles de venta por eliminar (" + e_detalles + "/" + detalles.size()
										+ "), y no se han eliminado todas las garantias solo fueron " + e_garantias
										+ ".");
					}
					VentaDao vDao = new VentaDao();
					detalles = dao.findByFieldList("venta", venta);
					BigInteger total = BigInteger.ZERO;
					for (DetalleVenta dv : detalles) {
						total = total.add(dv.getSubtotal());
					}
					venta.setTotal(total);
					Fecha fecha = new Fecha();
					venta.setFechaActualizacion(new Date(fecha.fecha()));
					vDao.update(venta);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Warn", "Se han actualizado la venta con el ID " + venta.getIdVenta() + "."));

				} else if (detalles.size() == e_detalles) {
					if (e_garantias > 0 && ee_garantias == 0) {
						this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
								"Se han eliminado todos los detalles de venta (" + e_detalles + "/" + detalles.size()
										+ "), y se han eliminado todas las garantias " + e_garantias + ".");
					} else {
						this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
								"Se han eliminado todos los detalles de venta (" + e_detalles + "/" + detalles.size()
										+ "), y no se han eliminado todas las garantias solo fueron " + e_garantias
										+ ".");
					}
					VentaDao vDao = new VentaDao();
					vDao.delete(venta);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Succes", "Se han eliminado la venta con el ID " + venta.getIdVenta() + "."));
				}
				if (this.message != null) {
					this.initRendedirzarTablaVenta();
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"Se han presentado un error, vuelva a intentarlo.");
				}

			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"La venta con el ID " + id + " no se encuentra registrada.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Se ha presentado un error al selecionar la venta.");
		}
		return message;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	private FacesMessage removeDetallesVenta(int id) {
		this.message = null;
		if (id > 0) {
			DetalleVentaDao dao = new DetalleVentaDao();
			DetalleVenta detalle = dao.find(id);
			if (detalle != null) {
				Venta venta = detalle.getVenta();
				dao.delete(detalle);
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
						"Se ha eliminado el detalle venta con el ID " + id + ".");
				FacesContext.getCurrentInstance().addMessage(null, this.message);
				this.message = calcularTotal(venta);
				this.initRendedirzarTablaVenta();
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"El detalle venta con el ID " + id + " no se encuentra registrado.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Se ha presentado un error al selecionar el detalle venta.");
		}
		return message;
	}

	/**
	 * Metodo que permite actualizar el subtotal de una venta.
	 * 
	 * @param venta representa la venta a recalcular.
	 * @return el mensaje obtenido.
	 */
	@SuppressWarnings("deprecation")
	private FacesMessage calcularTotal(Venta venta) {
		FacesMessage aux = null;
		if (venta != null) {
			VentaDao dao = new VentaDao();
			DetalleVentaDao dvDao = new DetalleVentaDao();
			List<DetalleVenta> list = dvDao.findByFieldList("venta", venta);
			BigInteger total = BigInteger.ZERO;
			for (DetalleVenta dv : list) {
				total = total.add(dv.getSubtotal());
			}
			venta.setTotal(total);
			Fecha fecha = new Fecha();
			venta.setFechaActualizacion(new Date(fecha.fecha()));
			venta.setUsuario(sesion.getLogeado());
			dao.update(venta);
			aux = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
					"Se ha actualizado la venta con el id " + venta.getIdVenta() + ".");
		} else {
			aux = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha actualizado la venta.");
		}
		return aux;
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public FacesMessage getMessage() {
		return message;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public DetalleVenta getDetalle_venta() {
		return detalle_venta;
	}

	public void setDetalle_venta(DetalleVenta detalle_venta) {
		this.detalle_venta = detalle_venta;
	}

	public SessionBean getSesion() {
		return sesion;
	}

	public void setSesion(SessionBean sesion) {
		this.sesion = sesion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Producto> getTabla_productos_seleccionados() {
		return tabla_productos_seleccionados;
	}

	public void setTabla_productos_seleccionados(List<Producto> tabla_productos_seleccionados) {
		this.tabla_productos_seleccionados = tabla_productos_seleccionados;
	}

	public void setTabla_productos(List<Producto> tabla_productos) {
		this.tabla_productos = tabla_productos;
	}

	public boolean isRenderizar_tabla_productos() {
		return renderizar_tabla_productos;
	}

	public void setRenderizar_tabla_productos(boolean renderizar_tabla_productos) {
		this.renderizar_tabla_productos = renderizar_tabla_productos;
	}

	public void setTabla_detalle_producto(List<VentaDetalleProducto> tabla_detalle_producto) {
		this.tabla_detalle_producto = tabla_detalle_producto;
	}

	public List<VentaDetalleProducto> getTabla_detalle_producto_seleccionados() {
		return tabla_detalle_producto_seleccionados;
	}

	public void setTabla_detalle_producto_seleccionados(
			List<VentaDetalleProducto> tabla_detalle_producto_seleccionados) {
		this.tabla_detalle_producto_seleccionados = tabla_detalle_producto_seleccionados;
	}

	public boolean isRenderizar_tabla_detalle_producto() {
		return renderizar_tabla_detalle_producto;
	}

	public void setRenderizar_tabla_detalle_producto(boolean renderizar_tabla_detalle_producto) {
		this.renderizar_tabla_detalle_producto = renderizar_tabla_detalle_producto;
	}

	public boolean isHidden_tablas() {
		return hidden_tablas;
	}

	public void setHidden_tablas(boolean hidden_tablas) {
		this.hidden_tablas = hidden_tablas;
	}

	public List<Producto> getTabla_filter_productos() {
		return tabla_filter_productos;
	}

	public void setTabla_filter_productos(List<Producto> tabla_filter_productos) {
		this.tabla_filter_productos = tabla_filter_productos;
	}

	public List<VentaDetalleProducto> getTabla_filter_detalle_producto() {
		return tabla_filter_detalle_producto;
	}

	public void setTabla_filter_detalle_producto(List<VentaDetalleProducto> tabla_filter_detalle_producto) {
		this.tabla_filter_detalle_producto = tabla_filter_detalle_producto;
	}

	public List<VentaDetalleProducto> getProductos_selecionados() {
		return productos_selecionados;
	}

	public void setProductos_selecionados(List<VentaDetalleProducto> productos_selecionados) {
		this.productos_selecionados = productos_selecionados;
	}

	public void setTabla_venta(List<Venta> tabla_venta) {
		this.tabla_venta = tabla_venta;
	}

	public List<Venta> getTabla_filter_venta() {
		return tabla_filter_venta;
	}

	public void setTabla_filter_venta(List<Venta> tabla_filter_venta) {
		this.tabla_filter_venta = tabla_filter_venta;
	}

	public boolean isRenderizar_tabla_venta() {
		return renderizar_tabla_venta;
	}

	public void setRenderizar_tabla_venta(boolean renderizar_tabla_venta) {
		this.renderizar_tabla_venta = renderizar_tabla_venta;
	}

	public List<DetalleVenta> getTabla_venta_filter_detalle_venta() {
		return tabla_venta_filter_detalle_venta;
	}

	public void setTabla_venta_filter_detalle_venta(List<DetalleVenta> tabla_venta_filter_detalle_venta) {
		this.tabla_venta_filter_detalle_venta = tabla_venta_filter_detalle_venta;
	}
}
