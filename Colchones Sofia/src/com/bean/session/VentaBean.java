package com.bean.session;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

import com.entity.*;
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

	private Venta venta;
	private TipoPago tipo_pago;

	private boolean hidden_cliente;
	private boolean editar_cliente;
	private boolean filter_cliente;

	private List<VentaDetalleProducto> tabla_productos;
	private List<VentaDetalleProducto> tabla_filter_productos;
	private boolean renderizar_tabla_productos;
	private boolean hidden_tabla_productos;

	private List<DetalleVenta> tabla_venta;
	private int contador_detalle_venta;
	private int documento_vendedor;
	private DetalleVenta detalle_venta;
	private VentaDetalleProducto detalle_venta_producto;
	private int cantidad_actualizar;

	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean sesion;

	@ManagedProperty("#{mail}")
	private EmailBean emial;

	@ManagedProperty("#{app}")
	private AppBean app;

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
		initVenta();
		initRenderizarTablaProductos();
		this.tabla_venta = new ArrayList<DetalleVenta>();
		this.contador_detalle_venta = 0;
		this.detalle_venta = null;
		this.detalle_venta_producto = null;
		this.cantidad_actualizar = 0;
	}

	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * Metodo que inicializa la venta.
	 */
	public void initVenta() {
		this.venta = new Venta();
		this.venta.setIdVenta(this.generarVenta());
		this.venta.setTotal(BigInteger.ZERO);
		this.initCliente();
		this.initVendedor();
		this.venta.setTipoPago(new TipoPago());
	}

	/**
	 * Metodo que inicializa el cliente.
	 */
	public void initCliente() {
		this.hidden_cliente = false;
		this.filter_cliente = false;
		this.editar_cliente = false;
		if (this.venta != null) {
			this.venta.setCliente(new Cliente());
			this.venta.getCliente().setPersona(new Persona());
			this.venta.getCliente().getPersona().setTipoDocumento(new TipoDocumento());
			this.venta.getCliente().getPersona().setGenero("");
		}
	}

	/**
	 * Metodo que inicializa el vendedor.
	 */
	public void initVendedor() {
		if (this.venta != null) {
			this.documento_vendedor = 0;
			this.venta.setVendedor(new Vendedor());
		}
	}

	/**
	 * Metodo que renderizar la tabla productos.
	 */
	public void initRenderizarTablaProductos() {
		this.renderizar_tabla_productos = true;
		this.hidden_tabla_productos = false;
	}

	/**
	 * Metodo que cambia el estado a dialogo de formulario.
	 * 
	 * @param estado representa el estado.
	 */
	public void initDialogForm(int estado) {
		PrimeFaces current = PrimeFaces.current();
		switch (estado) {
		case 1:
			current.executeScript("PF('sofia-dialog-update').show();");
			break;
		case 2:
			current.executeScript("PF('sofia-dialog-update').hide();");
			break;
		default:
			break;
		}
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite filtrar la información de un cliente.
	 */
	public void filtrarCliente() {
		this.hidden_cliente = false;
		this.editar_cliente = false;
		this.filter_cliente = false;
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
					this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes", "La persona con documento "
							+ this.venta.getCliente().getDocumento() + ",si es cliente de la empresa.");
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "La persona con documento  "
							+ this.venta.getCliente().getDocumento() + ",no es cliente de la empresa.");
				}
				this.venta.getCliente().setPersona(aux);
				this.hidden_cliente = true;
				this.filter_cliente = true;
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"No se ha encontrado ninguna persona con ese documento "
								+ this.venta.getCliente().getDocumento() + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "No has seleccionado ningun cliente.");
		}
		if (!this.hidden_cliente) {
			initCliente();
		}
		if (message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * Metodo que permite activar o bloquear la opción de editar un cliente.
	 */
	public void estadoEditarCliente() {
		if (this.hidden_cliente) {
			this.hidden_cliente = false;
			this.editar_cliente = true;
		} else {
			if (!editar_cliente) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "No has filtrado ningun cliente."));
			} else {
				this.hidden_cliente = true;
				this.editar_cliente = false;
			}
		}
	}

	/**
	 * Metodo que permite agregar uno por uno de los productos seleccionados.
	 */
	public void addProductosCliente() {
		this.message = null;
		int index = Integer.parseInt(Face.get("id-detalle-producto"));
		if (index > 0) {
			index = indexDetalleProducto(this.tabla_productos, index);
			if (index >= 0) {
				VentaDetalleProducto vdp = this.tabla_productos.get(index);
				if (vdp != null) {
					this.validarCantidadProductos(vdp);
					if (this.message == null) {
						if (vdp.getDetalle().getStock() >= vdp.getCantidad() && vdp.getCantidad() > 0) {
							int resta = vdp.getDetalle().getStock() - vdp.getCantidad();
							vdp.getDetalle().setStock(resta);
							int cantidad = vdp.getCantidad();
							vdp.setCantidad(0);
							resta = vdp.getDetalle().getProducto().getStock() - vdp.getCantidad();
							vdp.getDetalle().getProducto().setStock(resta);
							this.tabla_productos.set(index, vdp);
							/* OTHER */
							if (addDetalleProductoCliente(vdp.getDetalle().getId(), cantidad)) {
								this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
										"Se ha agregado el producto " + vdp.getDetalle().getProducto().getNombre()
												+ " con ID del detalle " + vdp.getDetalle().getId()
												+ ",la cantidad fue " + cantidad + ".");
							}
						}
					}
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"No se encontro ningun detalle de producto con ese ID " + index + ".");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"El ID " + index + " no es valido para un detalle de producto.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"El ID " + index + " no es valido para un detalle de producto.");
		}

		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * Metodo que permite agregar un detalle productos.
	 * 
	 * @param vdp representa el detalle producto.
	 * @return true si se agrego y false si no.
	 */
	public boolean addDetalleProductoCliente(int id, int cantidad) {
		if (cantidad > 0 && id > 0) {
			int index = indexDetalleVenta(this.tabla_venta, id);
			if (index >= 0) {
				DetalleVenta aux = this.tabla_venta.get(index);
				int suma = aux.getCantidad() + cantidad;
				aux.setCantidad(suma);
				aux.setSubtotal(subTotal(aux.getDetalleProducto().getId()));
				this.tabla_venta.set(index, aux);
			} else {
				index = indexDetalleProducto(this.tabla_productos, id);
				VentaDetalleProducto vdp = this.tabla_productos.get(index);
				DetalleVenta aux = new DetalleVenta();
				aux.setId(generarDetalleVenta());
				aux.setCantidad(cantidad);
				aux.setDetalleProducto(vdp.getDetalle());
				aux.setDescuento(vdp.getDetalle().getDescuento());
				aux.setPrecio(vdp.getDetalle().getPrecioVenta());
				aux.setSubtotal(subTotal(vdp.getDetalle().getId()));
				this.tabla_venta.add(aux);
			}
			return true;
		}
		return false;
	}

	///////////////////////////////////////////////////////
	// CRUD
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite registrar la venta.
	 */
	@SuppressWarnings("deprecation")
	public void registrarVenta() {
		if (venta != null) {
			VentaDao dao = new VentaDao();
			Venta aux = dao.find(this.venta.getIdVenta());
			if (aux == null) {
				if (this.tabla_venta != null && this.tabla_venta.size() > 0) {
					registrarCliente(venta.getCliente());
					if (this.message == null) {
						this.venta.setTipoPago(this.tipo_pago);
						this.validarVenta(this.venta);
						if (this.message == null) {
							Fecha fecha = new Fecha();
							this.venta.setFechaRegistro(new Date(fecha.fecha()));
							this.venta.setUsuario(this.sesion.getLogeado());
							dao = new VentaDao();
							dao.insert(this.venta);
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_INFO, "succes",
											"Se ha registrado la venta con ID " + venta.getIdVenta() + "."));
							registrarDetalleVenta();
							if (this.message == null) {
								registrarHistorialPresupuesto();
								if (this.message == null) {
									// AQUI VA CORREO
									List<Telefono> telefonos = app.getApp().getTelefono();
									String cadena= this.emial.formatVenta(this.venta.getIdVenta(), this.tabla_venta.size(),
											this.venta.getTotal(),
											((telefonos != null && telefonos.size() > 0)
													? String.valueOf(telefonos.get(0).getTelefono())
													: ""),
											app.getApp().getGlobal().getDireccion());
									this.emial.send(this.venta.getCliente().getPersona().getEmail(), "Confirmación Venta", cadena);

									this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "succes",
											"Se ha completado la venta sin errores.");
									this.initVenta();
									initVenta();
									initRenderizarTablaProductos();
									this.tabla_venta = new ArrayList<DetalleVenta>();
									this.contador_detalle_venta = 0;
									this.detalle_venta = null;
									this.detalle_venta_producto = null;
									this.cantidad_actualizar = 0;
								}
							}
						}
					}
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"No has selecionado ningun producto en la venta con ID " + venta.getIdVenta() + ".");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"Ya existe una venta con ese ID " + venta.getIdVenta() + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"No se ha podido comenzar el registro de la venta.");
		}
		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * Metodo que permite registrar un cliente.
	 * 
	 * @param cliente representa el cliente a registrar.
	 */
	@SuppressWarnings("deprecation")
	public void registrarCliente(Cliente cliente) {
		this.message = null;
		if (cliente != null) {
			validarCliente(cliente);
			if (this.message == null) {
				ClienteDao dao = new ClienteDao();
				Cliente aux = dao.find(cliente.getDocumento());
				if (aux != null) {
					Fecha fecha = new Fecha();
					aux.setFechaActualizacion(new Date(fecha.fecha()));
					aux.setFechaRegistro(cliente.getFechaRegistro());
					aux.setEstado(cliente.getEstado());
					aux.setUsuario(this.sesion.getLogeado());
					aux.setPuntos(cliente.getPuntos());
					aux.getPersona().setNombre(cliente.getPersona().getNombre().toUpperCase());
					aux.getPersona().setApellido(cliente.getPersona().getApellido().toUpperCase());
					aux.getPersona().setEmail(cliente.getPersona().getEmail());
					aux.getPersona().setTelefono(cliente.getPersona().getTelefono());
					aux.getPersona().setDireccion(cliente.getPersona().getDireccion());
					aux.getPersona().setNacimiento(cliente.getPersona().getNacimiento());
					aux.getPersona().setGenero(cliente.getPersona().getGenero());
					dao = new ClienteDao();
					dao.update(aux);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"succes", "Se ha actualizado el cliente con documento " + cliente.getDocumento() + "."));
					this.venta.setCliente(aux);
				} else {
					Fecha fecha = new Fecha();
					cliente.setFechaRegistro(new Date(fecha.fecha()));
					cliente.setUsuario(sesion.getLogeado());
					cliente.setEstado(true);
					cliente.setPuntos(BigInteger.ZERO);
					cliente.getPersona().setDocumento(cliente.getDocumento());
					cliente.getPersona().setNombre(cliente.getPersona().getNombre().toUpperCase());
					cliente.getPersona().setApellido(cliente.getPersona().getApellido().toUpperCase());
					dao = new ClienteDao();
					dao.insert(cliente);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"succes", "Se ha registrado el cliente con documento " + cliente.getDocumento() + "."));
					this.venta.setCliente(cliente);
				}
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"No se ha podido registrar el cliente.");
		}
	}

	/**
	 * Metodo que permite registrar los detalles de venta.
	 */
	public void registrarDetalleVenta() {
		this.message = null;
		if (this.venta != null) {
			if (this.tabla_venta != null && this.tabla_venta.size() > 0) {
				int contador = 0;
				String error = "";
				for (DetalleVenta dv : this.tabla_venta) {
					DetalleVentaDao dao = new DetalleVentaDao();
					DetalleVenta aux = dao.find(dv.getId());
					if (aux == null) {
						dv.setVenta(this.venta);
						dv.setSubtotal(subTotal(dv));
						dao.insert(dv);
						contador++;
					} else {
						error += "El detalle venta con ID " + dv.getId() + " ya se encuentra registrado. \n";
					}
				}
				if (contador == this.tabla_venta.size()) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "succes",
									"Se han registrado (" + contador + "/" + this.tabla_venta.size()
											+ ") detalles de venta de la venta con ID " + venta.getIdVenta() + "."));
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
									"Se han registrado (" + contador + "/" + this.tabla_venta.size()
											+ ") detalles de venta de la venta con ID " + venta.getIdVenta()
											+ "\nLos errores fueron: \n" + error + "."));
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"No se ha podido registrar los detalles de ventas de la venta " + venta.getIdVenta() + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Primero debes registrar la venta, al momento de buscar la venta esta no existe.");
		}
	}

	/**
	 * Metodo que permite registrar la devolucion garantia
	 */
	@SuppressWarnings("deprecation")
	public void registrarDevoluciónGarantia() {
		this.message = null;
		if (this.venta != null) {
			DevolucionGarantiaDao dao = new DevolucionGarantiaDao();
			DevolucionGarantia aux = dao.findByField("venta", this.venta);
			DevolucionGarantia aux2 = dao.find(this.venta.getIdVenta());
			if (aux2 == null) {
				if (aux == null) {
					Fecha fecha = new Fecha();
					dao = new DevolucionGarantiaDao();
					DevolucionGarantia garantia = new DevolucionGarantia();
					garantia.setId(this.venta.getIdVenta());
					garantia.setVenta(this.venta);
					garantia.setUsuario(this.sesion.getLogeado());
					garantia.setFechaIregistro(new Date(fecha.fecha()));
					garantia.setEstado("NO HA INGRESADO A GARANTIA");
					dao.insert(garantia);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"succes", "Se han registrado la garantia con ID " + venta.getIdVenta() + "."));
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"La venta con ID " + venta.getIdVenta() + " ya tiene registrada la garantia.");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"Ya existe una garantia registrada con ese ID " + venta.getIdVenta() + ".");
			}

		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"No se ha podido comenzar a registrar la garantia de la venta.");
		}
	}

	/**
	 * Metodo que permite registrar el historial presupuesto.
	 */
	public void registrarHistorialPresupuesto() {
		if (this.venta != null) {
			HistorialPresupuestoDao dao = new HistorialPresupuestoDao();
			HistorialPresupuesto aux = dao.findByField("venta", this.venta);
			if (aux == null) {
				HistorialPresupuesto historial = new HistorialPresupuesto();
				historial.setId(generarHistorial());
				historial.setVenta(this.venta);
				historial.setEstado(true);
				historial.setDescripcion("Se ha realizado una venta con ID " + venta.getIdVenta() + ", el cliente fue: "
						+ this.venta.getCliente().getPersona().getNombre() + " "
						+ this.venta.getCliente().getPersona().getApellido() + ", el total de productos fue "
						+ this.tabla_venta.size() + ".");
				dao.insert(historial);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"succes", "Se ha agregado al historial de la empresa."));
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"Ya se ha registrado un historial para la venta con ID" + this.venta.getIdVenta() + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"No se ha podido comenzar a registrar el historial.");
		}
	}

	/**
	 * Metodo que permite actualizar un detalle de producto.
	 */
	public void actualizar() {
		this.message = null;
		if (this.detalle_venta != null && this.detalle_venta_producto != null) {
			if (this.cantidad_actualizar > 0) {
				int index = indexDetalleProducto(this.tabla_productos,
						this.detalle_venta_producto.getDetalle().getId());
				if (index >= 0) {
					int resta = this.detalle_venta_producto.getDetalle().getStock() - this.cantidad_actualizar;
					this.detalle_venta_producto.getDetalle().setStock(resta);
					this.detalle_venta_producto.setCantidad(0);
					resta = this.detalle_venta_producto.getDetalle().getProducto().getStock()
							- this.cantidad_actualizar;
					this.detalle_venta_producto.getDetalle().getProducto().setStock(resta);
					this.tabla_productos.set(index, this.detalle_venta_producto);
					if (this.addDetalleProductoCliente(this.detalle_venta_producto.getDetalle().getId(),
							this.cantidad_actualizar)) {
						this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
								"Se ha actualizado el detalle de producto con el ID "
										+ this.detalle_venta_producto.getDetalle().getId() + ".");
						this.initDialogForm(2);
					} else {
						this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
								"No se ha actualizado el detalle de producto con el ID "
										+ this.detalle_venta_producto.getDetalle().getId() + ".");
					}
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"No de ha encontrado ningun detalle de producto con ese ID "
									+ this.detalle_venta_producto.getDetalle().getId() + ".");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"La cantidad debe ser mayor a 0 y menor o igual a "
								+ this.detalle_venta_producto.getDetalle().getStock() + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Se presento un error en el detalle de venta a actualizar, no fue encontrado.");
		}

		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * Metodo que permite eliminar un detalle de venta.
	 */
	public void eliminarDetalleVenta() {
		this.message = null;
		this.detalle_venta = null;
		this.detalle_venta_producto = null;
		this.cantidad_actualizar = 0;
		int id = Integer.parseInt(Face.get("id-detalle-venta"));
		if (id > 0) {
			int index = indexDetalleVentaID(this.tabla_venta, id);
			if (index >= 0) {
				this.detalle_venta = this.tabla_venta.get(index);
				index = indexDetalleProducto(this.tabla_productos, this.detalle_venta.getDetalleProducto().getId());
				if (index >= 0) {
					this.detalle_venta_producto = this.tabla_productos.get(index);
					this.addDetalleVentaEliminado(index);
				} else {
					this.detalle_venta = null;
					this.detalle_venta_producto = null;
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"El ID detalle de producto no fue encontrado.");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"El ID detalle de venta no fue encontrado.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"El ID detalle de venta no fue encontrado.");
		}
		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * Metodo que permite eliminar el detalle de venta.
	 * 
	 * @param index representa el index del detalle de producto.
	 * @return el mensaje obtenido.
	 */
	public FacesMessage addDetalleVentaEliminado(int index) {
		this.message = null;
		if (detalle_venta != null && this.detalle_venta_producto != null) {
			int suma = this.detalle_venta.getCantidad() + this.detalle_venta_producto.getDetalle().getStock();
			this.detalle_venta_producto.getDetalle().setStock(suma);
			suma = this.detalle_venta.getCantidad() + this.detalle_venta_producto.getDetalle().getProducto().getStock();
			this.detalle_venta_producto.getDetalle().getProducto().setStock(suma);
			this.tabla_productos.set(index, detalle_venta_producto);
			int aux = indexDetalleVentaID(this.tabla_venta, this.detalle_venta.getId());
			if (aux >= 0) {
				this.tabla_venta.remove(aux);
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
						"Se ha eliminado el detalle de venta con el ID " + this.detalle_venta.getId() + ".");
				if (this.tabla_venta.size() == 0) {
					this.contador_detalle_venta = 0;
					this.venta.setTotal(BigInteger.ZERO);
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"No se ha podido eliminar el detalle de venta con ID " + this.detalle_venta.getId() + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"No se ha encontrado el detalle del producto ni el detalle de venta a eliminar.");
		}
		return this.message;
	}

	/**
	 * Metodo que permite seleccionar el tipo de pago.
	 */
	public void seleccionarTipoPago() {
		this.tipo_pago = null;
		this.message = null;
		if (this.venta != null && this.venta.getTipoPago() != null) {
			if (this.venta.getTipoPago().getNombre() != null && this.venta.getTipoPago().getNombre().length() > 0) {
				TipoPagoDao dao = new TipoPagoDao();
				this.tipo_pago = dao.find(this.venta.getTipoPago().getNombre());
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
						"Se ha seleccionado el tipo de pago " + this.venta.getTipoPago().getNombre() + ".");
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"No se ha selecionado ningun tipo de pago.");
			}

		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"No se ha podido seleccionar el tipo de pago.");
		}
		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * Metodo que permite seleccionar el vendedor.
	 */
	public void seleccionarVendedor() {
		this.message = null;
		this.venta.setVendedor(null);
		if (venta != null && documento_vendedor > 0) {
			VendedorDao dao = new VendedorDao();
			Vendedor aux = dao.find(this.documento_vendedor);
			if (aux != null) {
				this.venta.setVendedor(aux);
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
						"Se ha seleccionado el vendedor con documento " + this.venta.getVendedor().getDocumento()
								+ ".");
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"No existe ningun vendedor con documento " + this.venta.getVendedor().getDocumento() + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El ID del vendedor no es valido.");
		}

		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	///////////////////////////////////////////////////////
	// Value
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite calular el total a pagar.
	 * 
	 * @return el total a pagar.
	 */
	public BigInteger total() {
		BigInteger valor = BigInteger.ZERO;
		for (DetalleVenta v : this.tabla_venta) {
			valor = valor.add(subTotal(v.getDetalleProducto().getId()));
		}
		this.venta.setTotal(valor);
		return valor;
	}

	/**
	 * Metodo que calcula un subtotal de los productos seleccionados.
	 * 
	 * @return representa el valor obtenido.
	 */
	public BigInteger subTotal(int id) {
		BigInteger valor = BigInteger.ZERO;
		if (id > 0) {
			int index = indexDetalleVenta(this.tabla_venta, id);
			if (index >= 0) {
				DetalleVenta venta = this.tabla_venta.get(index);
				BigInteger cantidad = new BigInteger(String.valueOf(venta.getCantidad()));
				valor = cantidad.multiply(venta.getDetalleProducto().getPrecioVenta());
				valor = valor.subtract(venta.getDetalleProducto().getDescuento());
			}
		}
		return valor;
	}

	/**
	 * Metodo que calcula un subtotal de los productos seleccionados.
	 * 
	 * @return representa el valor obtenido.
	 */
	public BigInteger subTotal(DetalleVenta venta) {
		BigInteger valor = BigInteger.ZERO;
		if (venta != null) {
			BigInteger cantidad = new BigInteger(String.valueOf(venta.getCantidad()));
			valor = cantidad.multiply(venta.getDetalleProducto().getPrecioVenta());
			valor = valor.subtract(venta.getDetalleProducto().getDescuento());
		}
		return valor;
	}

	///////////////////////////////////////////////////////
	// Search
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer el indice de un detalle de venta en una lista.
	 * 
	 * @param list representa la lista de detalle de venta.
	 * @param id   representa el detalle del producto.
	 * @return el indice en la lista.
	 */
	public int indexDetalleProducto(List<VentaDetalleProducto> list, int id) {
		int index = -1;
		if (list != null && list.size() > 0) {
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
	 * Metodo que permite conocer el indice de un producto en el detalle venta.
	 * 
	 * @param list representa la lista de detalles ventas.
	 * @param id   representa el id del detalle producto a buscar.
	 * @return el indice en la lista.
	 */
	public int indexDetalleVenta(List<DetalleVenta> list, int id) {
		int index = -1;
		if (list != null && list.size() > 0) {
			int contador = 0;
			for (DetalleVenta v : list) {
				if (v.getDetalleProducto().getId() == id) {
					return contador;
				}
				contador++;
			}
		}
		return index;
	}

	/**
	 * Metodo que permite conocer el indice de un producto en el detalle venta.
	 * 
	 * @param list representa la lista de detalles ventas.
	 * @param id   representa el id del detalle producto a buscar.
	 * @return el indice en la lista.
	 */
	public int indexDetalleVentaID(List<DetalleVenta> list, int id) {
		int index = -1;
		if (list != null && list.size() > 0) {
			int contador = 0;
			for (DetalleVenta v : list) {
				if (v.getId() == id) {
					return contador;
				}
				contador++;
			}
		}
		return index;
	}

	/**
	 * Metodo que permite conocer el indice de una venta en una lista.
	 * 
	 * @param list representa la lista de ventas.
	 * @param id   representa el id de la venta a buscar.
	 * @return el indice en la lista.
	 */
	public int indexVenta(List<Venta> list, int id) {
		int index = -1;
		if (list != null && list.size() > 0) {
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

	/**
	 * Metodo que permite generar el ID de un detalle venta.
	 * 
	 * @return representa el id generado.
	 */
	public int generarDetalleVenta() {
		DetalleVentaDao dao = new DetalleVentaDao();
		DetalleVenta dv = dao.ultimoAdd();
		this.contador_detalle_venta++;
		if (dv != null) {
			return dv.getId() + this.contador_detalle_venta;
		} else {
			return this.contador_detalle_venta;
		}
	}

	/**
	 * Metodo que permite generar el ID de una venta.
	 * 
	 * @return representa el id generado.
	 */
	public int generarVenta() {
		VentaDao dao = new VentaDao();
		Venta v = dao.ultimoAdd();
		if (v != null) {
			return v.getIdVenta() + 1;
		} else {
			return 1;
		}
	}

	/**
	 * Metodo que permite generar el ID de un historial.
	 * 
	 * @return representa el id generado.
	 */
	public int generarHistorial() {
		HistorialPresupuestoDao dao = new HistorialPresupuestoDao();
		HistorialPresupuesto v = dao.ultimoAdd();
		if (v != null) {
			return v.getId() + 1;
		} else {
			return 1;
		}
	}

	///////////////////////////////////////////////////////
	// Validator
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite validar las cantidades de los productos seleccionados.
	 * 
	 * @param list representa la lista de productos.
	 * @return el error obtenido.
	 */
	public FacesMessage validarCantidadProductos(List<VentaDetalleProducto> list) {
		this.message = null;
		for (VentaDetalleProducto v : list) {
			if (validarCantidadProductos(v) != null) {
				return message;
			}
		}
		return this.message;
	}

	/**
	 * Metodo que permite validar las cantidad de producto seleccionado.
	 * 
	 * @param v representa el producto.
	 * @return el error obtenido.
	 */
	public FacesMessage validarCantidadProductos(VentaDetalleProducto v) {
		this.message = null;
		if (v != null) {
			if (v.getCantidad() <= 0) {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"La cantidad del producto " + v.getDetalle().getProducto().getNombre() + " con ID detalle "
								+ v.getDetalle().getId() + " debe ser mayor a 0 y menor o igual a "
								+ v.getDetalle().getStock() + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"El detalle de venta no fue encontrado.");
		}
		return message;
	}

	/**
	 * Metodo que permite validar la información de la venta.
	 * 
	 * @param venta representa la venta a validar.
	 * @return el error obtenido;
	 */
	public FacesMessage validarVenta(Venta venta) {
		this.message = null;
		if (venta != null) {
			if (venta.getIdVenta() > 0) {
				if (venta.getCliente() != null && venta.getCliente().getDocumento() > 0) {
					if (venta.getVendedor() != null && venta.getVendedor().getDocumento() > 0) {
						if (venta.getTipoPago() != null && venta.getTipoPago().getNombre() != null
								&& venta.getTipoPago().getNombre().length() > 0) {
						} else {
							this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
									"El campo tipo de pago de la venta es obligatorio.");
						}
					} else {
						this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
								"El campo vendedor de la venta es obligatorio.");
					}
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"El campo cliente de la venta es obligatorio.");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"El campo id de la venta es obligatorio.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Se ha presentado un error al validar la información de la venta.");
		}
		return message;
	}

	/**
	 * Metodo que permite validar los datos de un cliente.
	 * 
	 * @param cliente representa el cliente a validar.
	 * @return representa el error obtenido.
	 */
	public FacesMessage validarCliente(Cliente cliente) {
		this.message = null;
		if (cliente != null && cliente.getDocumento() > 0) {
			Persona aux = cliente.getPersona();
			if (aux != null && aux.getTipoDocumento().getNombre() != null
					&& aux.getTipoDocumento().getNombre().length() > 0) {
				if (aux.getNombre() != null && aux.getNombre().length() > 0) {
					if (aux.getApellido() != null && aux.getApellido().length() > 0) {
						if (aux.getEmail() != null && aux.getEmail().length() > 0) {
							if (aux.getTelefono() != null && aux.getTelefono().length() > 0) {
								if (aux.getDireccion() != null && aux.getDireccion().length() > 0) {
									if (aux.getNacimiento() != null) {
										if (aux.getGenero() != null && aux.getGenero().length() > 0) {
										} else {
											this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
													"El campo genero del cliente es obligatorio.");
										}
									} else {
										this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
												"El campo fecha nacimiento del cliente es obligatorio.");
									}
								} else {
									this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
											"El campo dirección del cliente es obligatorio.");
								}
							} else {
								this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
										"El campo telefono del cliente es obligatorio.");
							}
						} else {
							this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
									"El campo email del cliente es obligatorio.");
						}
					} else {
						this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
								"El campo apellido del cliente es obligatorio.");
					}
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"El campo nombre del cliente es obligatorio.");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"El campo tipo documento del cliente es obligatorio.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
					"El campo documento del cliente es obligatorio.");
		}
		return message;
	}

	///////////////////////////////////////////////////////
	// Statu
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite activar el dialog de actualizar.
	 */
	public void statuActualizar() {
		this.message = null;
		this.detalle_venta = null;
		this.detalle_venta_producto = null;
		this.cantidad_actualizar = 0;
		int id = Integer.parseInt(Face.get("id-detalle-venta"));
		if (id > 0) {
			int index = indexDetalleVentaID(this.tabla_venta, id);
			if (index >= 0) {
				this.detalle_venta = this.tabla_venta.get(index);
				index = indexDetalleProducto(this.tabla_productos, this.detalle_venta.getDetalleProducto().getId());
				if (index >= 0) {
					this.detalle_venta_producto = this.tabla_productos.get(index);
					this.initDialogForm(1);
				} else {
					this.detalle_venta = null;
					this.detalle_venta_producto = null;
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"El ID detalle de producto no fue encontrado.");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"El ID detalle de venta no fue encontrado.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"El ID detalle de venta no fue encontrado.");
		}
		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	///////////////////////////////////////////////////////
	// Renderizar
	///////////////////////////////////////////////////////
	/**
	 * Metodo que consulta todos los productos.
	 * 
	 * @return una lista con todos los productos.
	 */
	public List<VentaDetalleProducto> getTabla_productos() {
		if (this.renderizar_tabla_productos) {
			tabla_productos = new ArrayList<VentaDetalleProducto>();
			ProductoDao dao = new ProductoDao();
			List<Producto> list = dao.findByFieldList("estado", true);
			for (Producto p : list) {
				if (p.getStock() > 0) {
					DetalleProductoDao dpDao = new DetalleProductoDao();
					List<DetalleProducto> list2 = dpDao.findByFieldList("producto", p);
					for (DetalleProducto dp : list2) {
						if (dp.getStock() > 0) {
							tabla_productos.add(new VentaDetalleProducto(dp, 0));
						}
					}
				}
			}
			this.renderizar_tabla_productos = false;
			this.hidden_tabla_productos = false;
		}
		return tabla_productos;
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

	public SessionBean getSesion() {
		return sesion;
	}

	public void setSesion(SessionBean sesion) {
		this.sesion = sesion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public boolean isHidden_cliente() {
		return hidden_cliente;
	}

	public void setHidden_cliente(boolean hidden_cliente) {
		this.hidden_cliente = hidden_cliente;
	}

	public boolean isEditar_cliente() {
		return editar_cliente;
	}

	public void setEditar_cliente(boolean editar_cliente) {
		this.editar_cliente = editar_cliente;
	}

	public boolean isFilter_cliente() {
		return filter_cliente;
	}

	public void setFilter_cliente(boolean filter_cliente) {
		this.filter_cliente = filter_cliente;
	}

	public List<DetalleVenta> getTabla_venta() {
		return tabla_venta;
	}

	public void setTabla_venta(List<DetalleVenta> tabla_venta) {
		this.tabla_venta = tabla_venta;
	}

	public boolean isRenderizar_tabla_productos() {
		return renderizar_tabla_productos;
	}

	public void setRenderizar_tabla_productos(boolean renderizar_tabla_productos) {
		this.renderizar_tabla_productos = renderizar_tabla_productos;
	}

	public boolean isHidden_tabla_productos() {
		return hidden_tabla_productos;
	}

	public void setHidden_tabla_productos(boolean hidden_tabla_productos) {
		this.hidden_tabla_productos = hidden_tabla_productos;
	}

	public void setTabla_productos(List<VentaDetalleProducto> tabla_productos) {
		this.tabla_productos = tabla_productos;
	}

	public List<VentaDetalleProducto> getTabla_filter_productos() {
		return tabla_filter_productos;
	}

	public void setTabla_filter_productos(List<VentaDetalleProducto> tabla_filter_productos) {
		this.tabla_filter_productos = tabla_filter_productos;
	}

	public int getContador_detalle_venta() {
		return contador_detalle_venta;
	}

	public void setContador_detalle_venta(int contador_detalle_venta) {
		this.contador_detalle_venta = contador_detalle_venta;
	}

	public DetalleVenta getDetalle_venta() {
		return detalle_venta;
	}

	public void setDetalle_venta(DetalleVenta detalle_venta) {
		this.detalle_venta = detalle_venta;
	}

	public VentaDetalleProducto getDetalle_venta_producto() {
		return detalle_venta_producto;
	}

	public void setDetalle_venta_producto(VentaDetalleProducto detalle_venta_producto) {
		this.detalle_venta_producto = detalle_venta_producto;
	}

	public int getCantidad_actualizar() {
		return cantidad_actualizar;
	}

	public void setCantidad_actualizar(int cantidad_actualizar) {
		this.cantidad_actualizar = cantidad_actualizar;
	}

	public TipoPago getTipo_pago() {
		return tipo_pago;
	}

	public void setTipo_pago(TipoPago tipo_pago) {
		this.tipo_pago = tipo_pago;
	}

	public int getDocumento_vendedor() {
		return documento_vendedor;
	}

	public void setDocumento_vendedor(int documento_vendedor) {
		this.documento_vendedor = documento_vendedor;
	}
}
