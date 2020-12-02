package com.bean.session;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FlowEvent;

import com.controller.*;
import com.model.*;
import com.model.other.Convertidor;
import com.util.Face;

/**
 * Implementation VentaOnlineBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "venta_online")
@SessionScoped
public class VentaOnlineBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private FacesMessage message;

	private Venta venta;
	private List<DetalleCompraVenta> tabla_venta;
	private List<DetalleCompraVenta> tabla_venta_copia;

	private int index;
	private int contador_detalle_venta;
	private int cantidad_actualizar;
	private List<DetalleCompraVenta> all_detalle_venta;

	private int id_detalle_producto;
	private boolean active_filert_detalle_venta;
	private boolean filter_detalle_venta;
	private boolean continuar_detalle_venta;
	private boolean hidden_detalle_venta;
	private DetalleCompraVenta detalle_venta;

	private Usuario cliente;
	private boolean hidden_cliente;
	private boolean editar_cliente;
	private boolean filter_cliente;
	private boolean ingresar_cliente;
	private boolean continuar_cliente;

	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{app}")
	private AppBean app;

	@ManagedProperty("#{catalogo}")
	private CatalogoBean catalogo;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public VentaOnlineBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		initVenta();
		initAllDetalleVenta();
		this.contador_detalle_venta = 0;
		this.cantidad_actualizar = 0;
		this.initAllDetalles();
	}

	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * Metodo que inicializa la venta.
	 */
	public void initVenta() {
		this.venta = new Venta();
		this.venta.setId(this.generarKEY());
		this.venta.setTotal(BigInteger.ZERO);
		this.initCliente();
		this.venta.setUsuario2(null);
		this.venta.setMetodoPagoBean(new MetodoPago());
		this.venta.setUsuario3(null);
		this.venta.setIva(this.app.getEmpresa().getIva());
		this.venta.setCostoEnvio(BigInteger.ZERO);
		this.tabla_venta = new ArrayList<DetalleCompraVenta>();
	}

	/**
	 * Metodo que inicializa lod detalles de la empresa.
	 */
	public void initAllDetalles() {
		if (catalogo != null) {
			if (this.catalogo.getDetalles_productos() == null || this.catalogo.getDetalles_productos().size() == 0) {
				this.catalogo.initProducto();
			}
		}
	}

	/**
	 * Metodo que inicializa el cliente.
	 */
	public void initCliente() {
		this.cliente = null;
		this.hidden_cliente = false;
		this.filter_cliente = false;
		this.editar_cliente = false;
		this.continuar_cliente = false;
		this.ingresar_cliente = false;
		if (this.venta != null) {
			this.venta.setUsuario1(new Usuario());
			this.venta.getUsuario1().setPersona(new Persona());
			this.venta.getUsuario1().getPersona().setTipoDocumentoBean(new TipoDocumento());
			this.venta.getUsuario1().getPersona().setGenero("");
		}
	}

	/**
	 * Metodo que permite inicializar la detalle venta.
	 */
	public void initDetalleVenta() {
		this.id_detalle_producto = -1;
		this.active_filert_detalle_venta = true;
		this.filter_detalle_venta = false;
		this.continuar_detalle_venta = false;
		this.hidden_detalle_venta = false;
		this.detalle_venta = new DetalleCompraVenta();
		this.detalle_venta.setDetalleProducto(new DetalleProducto());
		this.detalle_venta.getDetalleProducto().setProductoBean(new Producto());
		this.detalle_venta.getDetalleProducto().getProductoBean().setCategoriaBean(new Categoria());
	}

	/**
	 * Metodo que permite consultar todos los detalles productos.
	 */
	public void initAllDetalleVenta() {
		if (this.catalogo.getProductos() == null) {
			this.catalogo.initProducto();
		}
		this.index = -1;
		this.all_detalle_venta = new ArrayList<DetalleCompraVenta>();
		List<Producto> list = catalogo.getProductos();
		for (Producto p : list) {
			if (p.getStock() > 0) {
				List<DetalleProducto> list2 = p.getDetalleProductos();
				for (DetalleProducto dp : list2) {
					if (dp.getStock() > 0) {
						DetalleCompraVenta aux = new DetalleCompraVenta();
						aux.setDetalleProducto(dp);
						aux.setSubtotal(BigInteger.ZERO);
						aux.setCantidad(0);
						aux.setGarantia(dp.getProductoBean().getGarantia());
						aux.setPrecio(dp.getPrecioVenta());
						all_detalle_venta.add(aux);
					}
				}
			}
		}
	}
	
	///////////////////////////////////////////////////////
	// Wizard
	///////////////////////////////////////////////////////
	/**
	 * 
	 * @param event
	 * @return
	 */
	public String onFlowProcess(FlowEvent event) {
		String siguiente = "wizard-productos";
		if (this.tabla_venta.size() > 0) {
			siguiente = event.getNewStep();
		}
		return siguiente;
	}

	///////////////////////////////////////////////////////
	// Method Customers
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite filtrar la información de un cliente.
	 */
	public void filtrarCliente() {
		this.hidden_cliente = false;
		this.editar_cliente = false;
		this.filter_cliente = false;
		this.cliente = null;
		this.message = null;
		this.continuar_cliente = false;
		this.ingresar_cliente = false;
		if (this.venta != null && this.venta.getUsuario1() != null && this.venta.getUsuario1().getPersona() != null
				&& Convertidor.isCadena(this.venta.getUsuario1().getPersona().getDocumento())) {
			PersonaController dao = new PersonaController();
			Persona aux = dao.find(this.venta.getUsuario1().getPersona().getDocumento());
			if (aux != null) {
				UsuarioController cDao = new UsuarioController();
				Usuario cliente = cDao.usuarioRol("CLIENTE", this.venta.getUsuario1().getPersona().getDocumento());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Se ha filtrado la persona con documento "
								+ this.venta.getUsuario1().getPersona().getDocumento() + "."));
				if (cliente != null) {
					this.venta.setUsuario1(cliente);
					this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "La persona con documento "
							+ this.venta.getUsuario1().getPersona().getDocumento() + ",si es cliente de la empresa.");
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "La persona con documento "
							+ this.venta.getUsuario1().getPersona().getDocumento() + ",no es cliente de la empresa.");
				}

				if (this.venta.getUsuario1() == null) {
					this.initCliente();
				}
				if (aux.getTipoDocumentoBean() == null) {
					aux.setTipoDocumentoBean(new TipoDocumento());
				}

				this.venta.getUsuario1().setPersona(aux);
				this.hidden_cliente = true;
				this.filter_cliente = true;

			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"No se ha encontrado ninguna persona con ese documento "
								+ this.venta.getUsuario1().getPersona().getDocumento() + ".");
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
	 * Metodo que permite limpiar filtro cliente.
	 */
	public void limpiarFiltro() {
		this.initCliente();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Se ha limpiado el filtro cliente."));
	}

	/**
	 * Metodo que permite activar o bloquear la opción de editar un cliente.
	 */
	public void estadoEditarCliente() {
		if (this.hidden_cliente) {
			this.hidden_cliente = false;
			this.editar_cliente = true;
			cliente(this.venta.getUsuario1());
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
	 * Metodo que permite conocer la información unica del cliente.
	 * 
	 * @param a representa el usuario.
	 */
	public void cliente(Usuario a) {
		this.cliente = new Usuario();
		this.cliente.setPersona(new Persona());
		this.cliente.getPersona().setDocumento(a.getPersona().getDocumento());
		this.cliente.getPersona().setEmail(a.getPersona().getEmail());
		this.cliente.getPersona().setTelefono(a.getPersona().getTelefono());
		this.cliente.getPersona().setDireccion(a.getPersona().getDireccion());
	}

	/**
	 * Metodo que permite validar el proveedor.
	 */
	public void validarCliente() {
		this.continuar_cliente = false;
		this.ingresar_cliente = false;
		this.message = validar(this.venta.getUsuario1());
		boolean seguir = true;
		if (this.message == null) {
			if (this.editar_cliente) {
				seguir = validarCliente(this.venta.getUsuario1(), this.cliente);
				if (seguir) {
					continuar_cliente = true;
				} else {
					boolean error = false;
					if (!Convertidor.equals(this.venta.getUsuario1().getPersona().getDocumento(),
							this.cliente.getPersona().getDocumento())) {
						PersonaController dao = new PersonaController();
						Persona aux = dao.find(this.venta.getUsuario1().getPersona().getDocumento());
						if (aux == null) {
							error = false;
						} else {
							error = true;
							this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
									"Ya existe una persona con ese documento " + aux.getDocumento() + ".");
						}
					}
					if (!error) {
						ProveedorController dao = new ProveedorController();
						boolean existe = false;
						String numero = Convertidor.telefono(this.venta.getUsuario1().getPersona().getTelefono());
						if (!Convertidor.equals(this.venta.getUsuario1().getPersona().getEmail(),
								this.cliente.getPersona().getEmail())) {
							existe = dao.registrar(this.venta.getUsuario1().getPersona().getEmail(), null);
						} else if (!Convertidor.equals(numero, this.cliente.getPersona().getTelefono())) {
							existe = dao.registrar(null, numero);
						} /*else {
							existe = dao.registrar(this.venta.getUsuario1().getPersona().getEmail(),
									this.venta.getUsuario1().getPersona().getTelefono());
						}*/
						
						if (!existe) {
							continuar_cliente = true;
						} else {
							error = true;
							this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
									"Ya existe una persona registrada con ese telefono o email.");
						}
					}
				}
			} else {
				if (!filter_cliente) {
					boolean error;
					PersonaController dao = new PersonaController();
					Persona aux = dao.find(this.venta.getUsuario1().getPersona().getDocumento());
					if (aux == null) {
						error = false;
					} else {
						error = true;
						this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
								"Ya existe una persona con ese documento " + aux.getDocumento() + ".");
					}
					if (!error) {
						if (this.venta.getUsuario1() != null && this.venta.getUsuario1().getPersona() != null) {
							ProveedorController pDao = new ProveedorController();
							String numero = Convertidor.telefono(this.venta.getUsuario1().getPersona().getTelefono());
							boolean existe = pDao.registrar(this.venta.getUsuario1().getPersona().getEmail(), numero);
							if (!existe) {
								this.ingresar_cliente = true;
								this.continuar_cliente = true;
							} else {
								error = true;
								this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
										"Ya existe una persona registrada con ese telefono o email.");
							}
						} else {
							error = true;
							this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
									"Se ha presentado un error al obtener los datos de la persona, vuelva a intentarlo.");
						}
					}
				} else {
					continuar_cliente = true;
				}
			}
		}

		if (this.continuar_cliente && this.message == null) {
			this.hidden_cliente = true;
			this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "El cliente con documento "
					+ this.venta.getUsuario1().getPersona().getDocumento() + " esta validado.");
		}

		if (message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * Metodo que permite validar los datos de un usuario.
	 * 
	 * @param u representa el usuario.
	 * @return el error presentado.
	 */
	public FacesMessage validar(Usuario u) {
		FacesMessage aux = null;
		if (u != null && u.getPersona() != null && Convertidor.isCadena(u.getPersona().getDocumento())) {
			Persona p = u.getPersona();
			if (p.getTipoDocumentoBean() != null && Convertidor.isCadena(p.getTipoDocumentoBean().getTipoDocumento())) {
				if (Convertidor.isCadena(p.getNombre())) {
					if (!Convertidor.containsNumber(p.getNombre())) {
						if (Convertidor.isCadena(p.getApellido())) {
							if (!Convertidor.containsNumber(p.getApellido())) {
								if (Convertidor.isCadena(p.getEmail())) {
									if (Convertidor.isCadena(p.getTelefono())) {
										String prueba = Convertidor.telefono(p.getTelefono());
										if (Convertidor.isNumber(prueba)) {
											if (Convertidor.isCadena(p.getDireccion())) {
												if (Convertidor.isCadena(p.getGenero())) {
													if (p.getFechaNacimiento() != null) {
														return null;
													} else {
														aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
																"El campo fecha nacimiento es obligatorio.");
													}
												} else {
													aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
															"El campo genero es obligatorio.");
												}
											} else {
												aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
														"El campo dirección es obligatorio.");
											}
										} else {
											aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
													"El campo telefono solo debe tener caracteres numericos.");
										}
									} else {
										aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
												"El campo telefono es obligatorio.");
									}
								} else {
									aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
											"El campo email es obligatorio.");
								}
							} else {
								aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
										"El campo apellido no puede tener caracteres numericos.");
							}
						} else {
							aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo apellido es obligatorio.");
						}
					} else {
						aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
								"El campo nombre no puede tener caracteres numericos.");
					}
				} else {
					aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo nombre es obligatorio.");
				}
			} else {
				aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo tipo documento es obligatorio.");
			}
		} else {
			aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo documento es obligatorio.");
		}
		return aux;
	}

	/**
	 * Metodo que permite verificar si dos usuarios son iguales.
	 * 
	 * @param i representa usuario 1.
	 * @param j representa usuario 2.
	 * @return true si son iguales false si no.
	 */
	public boolean validarCliente(Usuario i, Usuario j) {
		if (i != null && j != null) {
			if (i.getPersona() != null && j.getPersona() != null) {
				if (i.getPersona().getDocumento().equals(j.getPersona().getDocumento())) {
					if (i.getPersona().getEmail().equals(j.getPersona().getEmail())) {
						if (i.getPersona().getTelefono().equals(j.getPersona().getTelefono())) {
							if (i.getPersona().getDireccion().equals(j.getPersona().getDireccion())) {
								return true;
							}
						}

					}
				}
			}
		}
		return false;
	}

	///////////////////////////////////////////////////////
	// Method Sale
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite generar el ID de una venta.
	 * 
	 * @return representa el id generado.
	 */
	public int generarKEY() {
		VentaController dao = new VentaController();
		Venta v = dao.ultimoAdd();
		if (v != null) {
			return v.getId() + 1;
		} else {
			return 1;
		}
	}

	///////////////////////////////////////////////////////
	// Method Detail Sale
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer un detalle venta.
	 * 
	 * @return representa el detalle venta.
	 */
	public DetalleCompraVenta consultarDetalleVenta() {
		DetalleCompraVenta dp = null;
		this.message = null;
		String id = Face.get("id-detalle-producto");
		if (Convertidor.isCadena(id)) {
			if (Convertidor.isNumber(id)) {
				this.initDetalleVenta();
				dp = this.detalle_venta;
				this.id_detalle_producto = Integer.parseInt(id);
				dp.setDetalleProducto(this.catalogo.consultarDetalleProducto(this.id_detalle_producto));
				if (dp.getDetalleProducto() == null) {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
							"No se ha encontrado ningun detalle producto con ese ID " + this.id_detalle_producto + ".");
				} else {
					dp.setPrecio(dp.getDetalleProducto().getPrecioVenta());
					dp.setDescuento(dp.getDetalleProducto().getDescuento());
					dp.setGarantia(dp.getDetalleProducto().getProductoBean().getGarantia());
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
						"El ID producto debe tener contener puros numeros.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"No se ha recibido ningun detalle producto");
		}
		return dp;
	}

	///////////////////////////////////////////////////////
	// Method Datil Product
	///////////////////////////////////////////////////////

	/**
	 * Metodo que permite generar el ID de un detalle venta.
	 * 
	 * @return representa el id generado.
	 */
	public int generarDetalleVenta() {
		DetalleCompraVentaController dao = new DetalleCompraVentaController();
		DetalleCompraVenta dv = dao.ultimoAdd();
		this.contador_detalle_venta++;
		if (dv != null) {
			return dv.getId() + this.contador_detalle_venta;
		} else {
			return this.contador_detalle_venta;
		}
	}

	/**
	 * Metodo que permite buscar en una lista de detalle de venta un detalle de
	 * producto.
	 * 
	 * @param list    representa la lista.
	 * @param detalle representa del detalle producto a buscar.
	 * @return el detalle venta encontrado.
	 */
	public DetalleCompraVenta indexDetalleProducto(List<DetalleCompraVenta> list, int detalle, int cantidad,
			boolean isCantidad, boolean isVenta) {
		this.index = -1;
		if (list != null && list.size() > 0 && detalle > 0) {
			int i = 0;
			for (DetalleCompraVenta dp : list) {
				if (dp.getDetalleProducto().getId() > 0 && dp.getDetalleProducto().getId() == detalle) {
					this.index = i;
					return detalleVenta(dp, cantidad, isCantidad, isVenta);
				}
				i++;
			}
		}
		return null;
	}

	/**
	 * Metodo que permite crear una instancia del detalle venta.
	 * 
	 * @param dp representa la instancia anterior.
	 * @return instancia nueva.
	 */
	public DetalleCompraVenta detalleVenta(DetalleCompraVenta dp, int cantidad, boolean isCantidad, boolean isVenta) {
		DetalleCompraVenta aux = null;
		if (dp != null && dp.getDetalleProducto() != null && dp.getDetalleProducto().getProductoBean() != null) {
			aux = new DetalleCompraVenta();

			if (isVenta) {
				aux.setId(dp.getId());
			}

			aux.setDetalleProducto(new DetalleProducto());
			aux.getDetalleProducto().setProductoBean(new Producto());
			aux.getDetalleProducto().getProductoBean().setCategoriaBean(new Categoria());

			if (isCantidad) {
				aux.setCantidad(cantidad);
			} else {
				aux.setCantidad(dp.getCantidad());
			}

			aux.setPrecio(dp.getDetalleProducto().getPrecioVenta());
			aux.setDescuento(dp.getDetalleProducto().getDescuento());

			DetalleProducto x = detalleProducto(dp.getDetalleProducto());
			aux.setDetalleProducto(x);

			aux.setGarantia(x.getProductoBean().getGarantia());
		}
		return aux;
	}

	/**
	 * Metodo que permite crear una instancia nueva de detalle producto.
	 * 
	 * @param dp representa el detalle producto.
	 * @return la insancia nueva.
	 */
	public DetalleProducto detalleProducto(DetalleProducto dp) {
		DetalleProducto aux = null;
		if (dp != null && dp.getProductoBean() != null) {
			aux = new DetalleProducto();
			aux.setProductoBean(new Producto());
			aux.getProductoBean().setCategoriaBean(new Categoria());

			aux.setId(dp.getId());
			aux.setColor(dp.getColor());
			aux.setDimension(dp.getDimension());
			aux.setPrecioVenta(dp.getPrecioVenta());
			aux.setDescuento(dp.getDescuento());
			aux.setFoto(dp.getFoto());
			aux.setStock(dp.getStock());
			aux.setStockMinimo(dp.getStockMinimo());

			aux.getProductoBean().setNombre(dp.getProductoBean().getNombre());
			aux.getProductoBean().setGarantia(dp.getProductoBean().getGarantia());
			aux.getProductoBean().getCategoriaBean().setNombre(dp.getProductoBean().getCategoriaBean().getNombre());
		}
		return aux;
	}

	///////////////////////////////////////////////////////
	// Method Product
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite agregar uno por uno de los productos seleccionados.
	 */
	public void addProductosCliente() {
		this.message = null;
		this.index = -1;
		boolean reset = false;

		if (this.detalle_venta == null) {
			this.detalle_venta = consultarDetalleVenta();
			this.detalle_venta.setCantidad(1);
			reset = true;
		}

		if (this.message == null) {
			if (this.detalle_venta != null && this.detalle_venta.getDetalleProducto() != null
					&& this.detalle_venta.getDetalleProducto().getProductoBean() != null) {
				DetalleCompraVenta aux = indexDetalleProducto(this.all_detalle_venta,
						detalle_venta.getDetalleProducto().getId(), 0, true, false);
				if (index >= 0 && aux != null) {
					if (this.detalle_venta.getCantidad() > 0) {
						if (this.detalle_venta.getDetalleProducto().getStock() >= this.detalle_venta.getCantidad()) {
							int resta = this.detalle_venta.getDetalleProducto().getStock()
									- this.detalle_venta.getCantidad();

							this.detalle_venta.getDetalleProducto().setStock(resta);
							int cantidad = this.detalle_venta.getCantidad();
							// this.detalle_venta.getDetalleProducto().getProductoBean().setStock(resta);
							/* OTHER */
							int index = this.index;
							if (addDetalleProductoCliente(this.detalle_venta)) {
								this.detalle_venta.setCantidad(0);
								this.all_detalle_venta.set(index, this.detalle_venta);
								this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
										"Se ha agregado el producto "
												+ this.detalle_venta.getDetalleProducto().getProductoBean().getNombre()
												+ " con ID del detalle "
												+ this.detalle_venta.getDetalleProducto().getId() + ",la cantidad fue "
												+ cantidad + ".");
							} else {
								this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
										"No se ha podido agregar el detalle producto con ID "
												+ this.getDetalle_venta().getDetalleProducto().getId() + ".");
							}
							this.initDetalleVenta();
							if (reset) {
								this.detalle_venta = null;
							}

						} else {
							this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
									"La cantidad digita supera el stock del detalle producto ID "
											+ this.getDetalle_venta().getDetalleProducto().getId() + ".");
						}
					} else {
						this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
								"La cantidad digita del detalle producto ID "
										+ this.getDetalle_venta().getDetalleProducto().getId()
										+ ", debe ser mayor a cero.");
					}
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"El ID " + this.getDetalle_venta().getDetalleProducto().getId()
									+ " no es valido para un detalle de producto.");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"No se ha filtrado ningun detalle producto.");
			}
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
	public boolean addDetalleProductoCliente(DetalleCompraVenta dcv) {
		boolean respuesta = false;

		if (this.tabla_venta == null) {
			this.tabla_venta = new ArrayList<DetalleCompraVenta>();
		}

		if (dcv != null && dcv.getDetalleProducto() != null && dcv.getDetalleProducto().getId() > 0
				&& dcv.getCantidad() > 0) {
			respuesta = true;

			int id = dcv.getDetalleProducto().getId();
			int cantidad = dcv.getCantidad();

			DetalleCompraVenta aux = indexDetalleProducto(this.tabla_venta, id, 0, false, true);
			if (aux != null) {
				int suma = aux.getCantidad() + cantidad;
				aux.setCantidad(suma);
				aux.setSubtotal(subTotal(aux));
				aux.getDetalleProducto().setStock(dcv.getDetalleProducto().getStock());
				this.tabla_venta.set(index, aux);
			} else {
				DetalleCompraVenta aux2 = new DetalleCompraVenta();
				aux2.setCantidad(cantidad);
				aux2.setDescuento(dcv.getDescuento());
				aux2.setPrecio(dcv.getPrecio());

				DetalleCompraVenta aux3 = new DetalleCompraVenta();
				aux3.setId(generarDetalleVenta());
				aux3.setCantidad(cantidad);
				aux3.setDescuento(aux2.getDescuento());
				aux3.setPrecio(aux2.getPrecio());
				aux3.setSubtotal(subTotal(aux2));
				aux3.setDetalleProducto(detalleProducto(dcv.getDetalleProducto()));

				this.tabla_venta.add(aux3);
			}
			this.total();
		}

		return respuesta;
	}

	///////////////////////////////////////////////////////
	// Method Operation
	///////////////////////////////////////////////////////
	/**
	 * Metodo que calcula un subtotal de los productos seleccionados.
	 * 
	 * @return representa el valor obtenido.
	 */
	public BigInteger subTotal(DetalleCompraVenta dp) {
		BigInteger valor = BigInteger.ZERO;
		if (dp != null) {
			DetalleCompraVenta venta = dp;
			BigInteger cantidad = new BigInteger(String.valueOf(venta.getCantidad()));
			valor = cantidad.multiply(venta.getPrecio());
			valor = valor.subtract(venta.getDescuento());
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
		for (DetalleCompraVenta v : this.tabla_venta) {
			valor = valor.add(subTotal(v));
		}
		this.venta.setTotal(valor);
		return valor;
	}

	/**
	 * 
	 * @return
	 */
	public BigInteger recalcular() {
		BigInteger valor = BigInteger.ZERO;
		BigInteger aux = BigInteger.ZERO;
		int index = 0;
		String id = Face.get("id-detalle-producto");
		if (Convertidor.isCadena(id)) {
			if (Convertidor.isNumber(id)) {
				int a = Integer.parseInt(id);
				for (DetalleCompraVenta v : this.tabla_venta) {
					aux = subTotal(v);
					valor = valor.add(aux);
					System.out.println("" + v.getDetalleProducto().getId() + " == " + a);
					if (v.getDetalleProducto().getId() == a) {
						System.out.println("ENTRO");
						v.setSubtotal(aux);
						tabla_venta.set(index, v);
					}
					index++;
				}
				this.venta.setTotal(valor);
			}
		}
		return valor;
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

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public List<DetalleCompraVenta> getTabla_venta() {
		return tabla_venta;
	}

	public void setTabla_venta(List<DetalleCompraVenta> tabla_venta) {
		this.tabla_venta = tabla_venta;
	}

	public List<DetalleCompraVenta> getTabla_venta_copia() {
		return tabla_venta_copia;
	}

	public void setTabla_venta_copia(List<DetalleCompraVenta> tabla_venta_copia) {
		this.tabla_venta_copia = tabla_venta_copia;
	}

	public Usuario getCliente() {
		return cliente;
	}

	public void setCliente(Usuario cliente) {
		this.cliente = cliente;
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

	public boolean isIngresar_cliente() {
		return ingresar_cliente;
	}

	public void setIngresar_cliente(boolean ingresar_cliente) {
		this.ingresar_cliente = ingresar_cliente;
	}

	public boolean isContinuar_cliente() {
		return continuar_cliente;
	}

	public void setContinuar_cliente(boolean continuar_cliente) {
		this.continuar_cliente = continuar_cliente;
	}

	public AppBean getApp() {
		return app;
	}

	public void setApp(AppBean app) {
		this.app = app;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<DetalleCompraVenta> getAll_detalle_venta() {
		return all_detalle_venta;
	}

	public void setAll_detalle_venta(List<DetalleCompraVenta> all_detalle_venta) {
		this.all_detalle_venta = all_detalle_venta;
	}

	public int getId_detalle_producto() {
		return id_detalle_producto;
	}

	public void setId_detalle_producto(int id_detalle_producto) {
		this.id_detalle_producto = id_detalle_producto;
	}

	public boolean isActive_filert_detalle_venta() {
		return active_filert_detalle_venta;
	}

	public void setActive_filert_detalle_venta(boolean active_filert_detalle_venta) {
		this.active_filert_detalle_venta = active_filert_detalle_venta;
	}

	public boolean isFilter_detalle_venta() {
		return filter_detalle_venta;
	}

	public void setFilter_detalle_venta(boolean filter_detalle_venta) {
		this.filter_detalle_venta = filter_detalle_venta;
	}

	public boolean isContinuar_detalle_venta() {
		return continuar_detalle_venta;
	}

	public void setContinuar_detalle_venta(boolean continuar_detalle_venta) {
		this.continuar_detalle_venta = continuar_detalle_venta;
	}

	public boolean isHidden_detalle_venta() {
		return hidden_detalle_venta;
	}

	public void setHidden_detalle_venta(boolean hidden_detalle_venta) {
		this.hidden_detalle_venta = hidden_detalle_venta;
	}

	public DetalleCompraVenta getDetalle_venta() {
		return detalle_venta;
	}

	public void setDetalle_venta(DetalleCompraVenta detalle_venta) {
		this.detalle_venta = detalle_venta;
	}

	public CatalogoBean getCatalogo() {
		return catalogo;
	}

	public void setCatalogo(CatalogoBean catalogo) {
		this.catalogo = catalogo;
	}

	public int getContador_detalle_venta() {
		return contador_detalle_venta;
	}

	public void setContador_detalle_venta(int contador_detalle_venta) {
		this.contador_detalle_venta = contador_detalle_venta;
	}

	public int getCantidad_actualizar() {
		return cantidad_actualizar;
	}

	public void setCantidad_actualizar(int cantidad_actualizar) {
		this.cantidad_actualizar = cantidad_actualizar;
	}
}
