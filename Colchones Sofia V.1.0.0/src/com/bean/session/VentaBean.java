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

import org.primefaces.*;

import com.controller.*;
import com.model.*;
import com.model.other.*;
import com.util.*;

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
	private List<DetalleCompraVenta> tabla_venta;
	private List<DetalleCompraVenta> tabla_venta_copia;

	private int index;
	private List<DetalleCompraVenta> all_detalle_venta;
	private List<DetalleCompraVenta> filter_all_detalle_venta;
	private int contador_detalle_venta;
	private int cantidad_actualizar;

	private Usuario cliente;
	private boolean hidden_cliente;
	private boolean editar_cliente;
	private boolean filter_cliente;
	private boolean ingresar_cliente;
	private boolean continuar_cliente;

	private Usuario vendedor;
	private boolean hidden_vendedor;
	private boolean filtro_vendedor;
	private boolean continuar_vendedor;
	private String documento_vendedor;

	private DetalleCompraVenta detalle_venta;
	private DetalleCompraVenta detalle_venta_producto;
	private int id_detalle_producto;
	private boolean active_filert_detalle_venta;
	private boolean filter_detalle_venta;
	private boolean continuar_detalle_venta;
	private boolean hidden_detalle_venta;

	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean sesion;

	@ManagedProperty("#{mail}")
	private EmailBean email;
	
	@ManagedProperty("#{image}")
	private ImageBean image; 

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
		this.vendedor = null;
		initVenta();
		initDetalleVenta();
		this.contador_detalle_venta = 0;
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
		this.venta.setId(this.generarKEY());
		this.venta.setTotal(BigInteger.ZERO);
		this.initCliente();
		this.initVendedor();
		this.venta.setUsuario2(null);
		this.venta.setMetodoPagoBean(new MetodoPago());
		this.venta.setUsuario3(this.sesion.getLogeado()); 
		this.venta.setIva(this.app.getEmpresa().getIva());
		this.venta.setCostoEnvio(BigInteger.ZERO);
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
		this.all_detalle_venta = null;
		if (this.venta != null) {
			this.venta.setUsuario1(new Usuario());
			this.venta.getUsuario1().setPersona(new Persona());
			this.venta.getUsuario1().getPersona().setTipoDocumentoBean(new TipoDocumento());
			this.venta.getUsuario1().getPersona().setGenero("");
		}
	}

	/**
	 * Metodo que inicializa el vendedor.
	 */
	public void initVendedor() {
		this.continuar_vendedor = false;
		this.hidden_vendedor = false;
		this.filtro_vendedor = false;
		if (this.venta != null) {
			this.documento_vendedor = null;
			this.vendedor = new Usuario();
			this.vendedor.setPersona(new Persona());
			this.vendedor.getPersona().setTipoDocumentoBean(new TipoDocumento());
			this.vendedor.getPersona().setGenero("");
		}
	}

	/**
	 * Metodo que permite consultar todos los detalles productos.
	 */
	public void initAllDetalleVenta() {
		this.index = -1;
		this.all_detalle_venta = new ArrayList<DetalleCompraVenta>();
		ProductoController dao = new ProductoController();
		List<Producto> list = dao.findByFieldList("estado", true);
		for (Producto p : list) {
			if (p.getStock() > 0) {
				DetalleProductoController dpDao = new DetalleProductoController();
				List<DetalleProducto> list2 = dpDao.findByFieldList("productoBean", p);
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

	@SuppressWarnings("deprecation")
	public void registrarVenta() {
		if (venta != null) {
			VentaController dao = new VentaController();
			Venta aux = dao.find(this.venta.getId());
			if (aux == null) {
				if (this.tabla_venta != null && this.tabla_venta.size() > 0) {
					this.validarVenta(this.venta);
					if (this.message == null) {
						registrarCliente(venta.getUsuario1());
						if (this.message == null) {
							Fecha fecha = new Fecha();
							this.venta.setFechaRegistro(new Date(fecha.fecha()));
							this.venta.setEstado(true);
							dao = new VentaController();
							dao.insert(this.venta);
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_INFO, "",
											"Se ha registrado la venta con ID " + venta.getId() + "."));
							registrarDetalleVenta();
							if (this.message == null) {
								// CORREO
								BigInteger presupuesto = app.getEmpresa().getPresupuesto();
								BigInteger bg = presupuesto;
								presupuesto = presupuesto.add(total());
								EmpresaController edao = new EmpresaController();
								Empresa e = edao.find(app.getNIT());
								if (e != null) {
									e.setPresupuesto(presupuesto);
									edao.update(e);
									app.getEmpresa().setPresupuesto(presupuesto);
									FacesContext.getCurrentInstance().addMessage(null,
											new FacesMessage(FacesMessage.SEVERITY_INFO, "",
													"Se ha actualizado el presupuesto de la empresa "
															+ app.getEmpresa().getNombre() + ",de " + bg + " a "
															+ presupuesto + "."));
									this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
											"Se ha completado la venta sin errores.");
									tabla_venta_copia = this.tabla_venta;
									this.initVenta();
									initVenta();
									this.tabla_venta = new ArrayList<DetalleCompraVenta>();
									this.contador_detalle_venta = 0;
									this.detalle_venta = null;
									this.detalle_venta_producto = null;
									this.cantidad_actualizar = 0;
								} else {
									this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
											"No se ha encontrado la empresa.");
								}

							}
						}
					}
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"No has selecionado ningun producto en la venta con ID " + venta.getId() + ".");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
						"Ya existe una venta con ese ID " + venta.getId() + ".");
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
	 * Metodo que permite cambiar el estado a una venta.
	 */
	public void estadoVenta() {
		this.message = null;
		String id = Face.get("id-venta");
		if(Convertidor.isCadena(id)) {
			VentaController dao = new VentaController();
			Venta aux = dao.find(Integer.parseInt(id));
			if(aux != null) {
				boolean estado = (!aux.getEstado());
				aux.setEstado(estado);
				dao.update(aux);
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha cambiado el estado de la venta con ID "+id+", a estado "+(estado ? "Activo" : "Bloqueado")+".");
			}else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No existe ninguna venta con el ID "+id+".");
			}
		}else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"El Id de la venta no es valido.");
		}
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
			if (venta.getId() > 0) {
				if (venta.getUsuario1() != null && venta.getUsuario1().getPersona() != null
						&& Convertidor.isCadena(venta.getUsuario1().getPersona().getDocumento())) {
					if (venta.getMetodoPagoBean() != null
							&& Convertidor.isCadena(venta.getMetodoPagoBean().getBanco())) {
						if (venta.getCostoEnvio() != null && venta.getCostoEnvio().compareTo(BigInteger.ZERO) >= 0) {
						} else {
							this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
									"El campo costo envio es obligatorio.");
						}
					} else {
						this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
								"El campo tipo de pago de la venta es obligatorio.");
					}
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"El campo cliente de la venta es obligatorio.");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"El campo ID de la venta es obligatorio.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Se ha presentado un error al validar la información de la venta.");
		}
		return message;
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
		this.continuar_vendedor = false;
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
				if(aux.getTipoDocumentoBean() == null) {
					aux.setTipoDocumentoBean(new TipoDocumento());
				}

				this.venta.getUsuario1().setPersona(aux);
				this.hidden_cliente = true;
				this.filter_cliente = true;
				this.initAllDetalleVenta();
				this.tabla_venta_copia = this.tabla_venta;
				this.tabla_venta = new ArrayList<DetalleCompraVenta>();

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
		this.tabla_venta_copia = this.tabla_venta;
		this.tabla_venta = new ArrayList<DetalleCompraVenta>();
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
			this.initDetalleVenta();
			this.initAllDetalleVenta();
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

	/**
	 * Metodo que permite registrar un cliente.
	 * 
	 * @param cliente representa el cliente a registrar.
	 */
	@SuppressWarnings("deprecation")
	public void registrarCliente(Usuario cliente) {
		this.message = null;
		if (cliente != null) {
			validar(cliente);
			if (this.message == null) {
				UsuarioController dao = new UsuarioController();
				PersonaController pDao = new PersonaController();
				Usuario aux = dao.usuarioRol("CLIENTE", cliente.getPersona().getDocumento());
				if (aux != null) {
					aux.setFechaCreacion(cliente.getFechaCreacion());
					aux.setEstado(cliente.getEstado());
					aux.setPuntos(cliente.getPuntos());
					aux.getPersona().setNombre(cliente.getPersona().getNombre().toUpperCase());
					aux.getPersona().setApellido(cliente.getPersona().getApellido().toUpperCase());
					aux.getPersona().setEmail(cliente.getPersona().getEmail());
					aux.getPersona().setTelefono(Convertidor.telefono(cliente.getPersona().getTelefono()));
					aux.getPersona().setDireccion(cliente.getPersona().getDireccion());
					aux.getPersona().setFechaNacimiento(cliente.getPersona().getFechaNacimiento());
					aux.getPersona().setGenero(cliente.getPersona().getGenero());
					dao.update(aux);
					
					if(this.image.getImage() != null) {
						aux.getPersona().setFoto(this.image.getImage());
						this.image.setImage(null);
					}
					
					pDao.update(aux.getPersona());
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							"Se ha actualizado el cliente con documento " + cliente.getPersona().getDocumento() + "."));
				} else {
					Fecha fecha = new Fecha();
					cliente.setId(generarKEYUsuario());
					cliente.setFechaCreacion(new Date(fecha.fecha()));
					cliente.setEstado(true);
					cliente.setPuntos(BigInteger.ZERO);
					cliente.setRolBean(new Rol());
					cliente.getRolBean().setRol("CLIENTE");
					
					if(this.image.getImage() != null) {
						cliente.getPersona().setFoto(this.image.getImage());
						this.image.setImage(null);
					}

					Persona p = pDao.find(cliente.getPersona().getDocumento());
					if (p != null) {
						pDao.update(cliente.getPersona());
					} else {
						pDao.insert(cliente.getPersona());
					}
					dao.insert(cliente);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							"Se ha registrado el cliente con documento " + cliente.getPersona().getDocumento() + "."));
				}
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"No se ha podido registrar el cliente.");
		}
	}

	/**
	 * Metodo que permite generar una llave de un usuario.
	 * 
	 * @return representa la llave generada.
	 */
	public String generarKEYUsuario() {
		UsuarioController dao = new UsuarioController();
		Usuario aux = dao.ultimoAdd();
		if (aux != null) {
			int id = Integer.parseInt(aux.getId()) + 1;
			return String.valueOf(id);
		}
		return "1";
	}

	///////////////////////////////////////////////////////
	// Method Detail
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite generar el ID del detalle venta.
	 * 
	 * @return representa el id generado.
	 */
	public int generarDetalleKEY() {
		DetalleCompraVentaController dao = new DetalleCompraVentaController();
		DetalleCompraVenta v = dao.ultimoAdd();
		if (v != null) {
			return v.getId() + 1;
		} else {
			return 1;
		}
	}

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
	 * Metodo que permite filtrar un detalle venta.
	 */
	public void filtrarDetalleProducto() {
		this.active_filert_detalle_venta = true;
		this.filter_detalle_venta = false;
		this.index = -1;
		this.message = null;
		if (this.id_detalle_producto > 0) {
			if (this.detalle_venta != null && this.detalle_venta.getDetalleProducto() != null) {
				int id = this.id_detalle_producto;
				this.initDetalleVenta();
				this.index = -1;
				DetalleCompraVenta aux = indexDetalleProducto(this.all_detalle_venta, id, 0, true, false);
				if (this.index >= 0 && aux != null) {
					if (aux != null && this.index >= 0 && id > 0 && aux.getDetalleProducto() != null
							&& aux.getDetalleProducto().getId() > 0) {
						this.filter_detalle_venta = true;
						this.active_filert_detalle_venta = false;
						this.detalle_venta = aux;
						this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
								"Se ha encontrado detalle producto con el ID " + id + ".");
						this.id_detalle_producto = id;
					} else {
						this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
								"No se ha encontrado ningun detalle producto con ese ID " + id + ".");
					}
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"No se ha encontrado ningun detalle producto con ese ID " + id + ".");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
						"No has digitado ningun detalle de producto.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"El campo ID detalle producto es obligatorio.");
		}

		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}
	
	/**
	 * Metodo que permite seleccionar un detalle producto.
	 */
	public void seleccionarDetalleProducto() {
		String id = Face.get("id-detalle-producto");
		FacesMessage message = null;
		if(Convertidor.isCadena(id)) {
			if(Convertidor.isNumber(id)) {
				id_detalle_producto = Integer.parseInt(id);
				filtrarDetalleProducto();
				if(this.filter_detalle_venta) {
					PrimeFaces current = PrimeFaces.current();
					current.executeScript("PF('sofia-dialog-producto-update').hide();");
				}
			}else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
						"El campo ID detalle producto solo debe contener caracteres numericos.");
			}
		}else {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"El campo ID detalle producto es obligatorio.");
		}
		if (message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	/**
	 * Metodo que permit limpirar filtro detalle venta.
	 */
	public void limpiarFiltroDetalleVenta() {
		this.initDetalleVenta();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Se ha limpiado el filtro."));
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

	/**
	 * Metodo que permite actualizar un detalle de producto.
	 */
	public void actualizar() {
		this.message = null;
		if (this.detalle_venta_producto != null) {
			if (this.cantidad_actualizar > 0
					&& this.detalle_venta_producto.getDetalleProducto().getStock() >= this.cantidad_actualizar) {
				DetalleCompraVenta aux = indexDetalleProducto(this.all_detalle_venta,
						this.detalle_venta_producto.getDetalleProducto().getId(), 0, false, false);
				if (aux != null && aux.getDetalleProducto() != null) {
					int resta = this.detalle_venta_producto.getDetalleProducto().getStock() - this.cantidad_actualizar;
					this.detalle_venta_producto.getDetalleProducto().setStock(resta);
					int index = this.index;
					this.detalle_venta_producto.setCantidad(this.cantidad_actualizar);
					if (this.addDetalleProductoCliente(this.detalle_venta_producto)) {
						this.detalle_venta_producto.setCantidad(0);
						this.all_detalle_venta.set(index, this.detalle_venta_producto);
						this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
								"Se ha actualizado el detalle de producto con el ID "
										+ this.detalle_venta_producto.getDetalleProducto().getId() + ".");
						this.initDialogForm(2);
						this.detalle_venta_producto = null;
						initDetalleVenta();
					} else {
						this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
								"No se ha actualizado el detalle de producto con el ID "
										+ this.detalle_venta_producto.getDetalleProducto().getId() + ".");
					}
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
							"No se ha encontrado ningun detalle de producto con ese ID "
									+ this.detalle_venta_producto.getDetalleProducto().getId() + ".");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"La cantidad debe ser mayor a 0 y menor o igual a "
								+ this.detalle_venta_producto.getDetalleProducto().getStock() + ".");
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
	 * Metodo que permite registrar los detalles de venta.
	 */
	public void registrarDetalleVenta() {
		this.message = null;
		if (this.venta != null) {
			if (this.tabla_venta != null && this.tabla_venta.size() > 0) {
				int contador = 0;
				String error = "";
				for (DetalleCompraVenta dv : this.tabla_venta) {
					DetalleCompraVentaController dao = new DetalleCompraVentaController();
					DetalleCompraVenta aux = dao.find(dv.getId());
					if (aux == null) {
						dv.setVentaBean(this.venta);
						dv.setSubtotal(subTotal(dv));
						dao.insert(dv);
						DetalleProductoController dpDao = new DetalleProductoController();
						DetalleProducto dp = dpDao.find(dv.getDetalleProducto().getId());
						if (dp != null) {
							int resta = dp.getStock() - dv.getCantidad();
							dp.setStock(resta);
							dpDao.update(dp);
						}
						contador++;
					} else {
						error += "El detalle venta con ID " + dv.getId() + " ya se encuentra registrado. \n";
					}
				}
				if (contador == this.tabla_venta.size()) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "",
									"Se han registrado (" + contador + "/" + this.tabla_venta.size()
											+ ") detalles de venta de la venta con ID " + venta.getId() + "."));
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_WARN, "",
									"Se han registrado (" + contador + "/" + this.tabla_venta.size()
											+ ") detalles de venta de la venta con ID " + venta.getId()
											+ "\nLos errores fueron: \n" + error + "."));
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No se ha podido registrar los detalles de ventas de la venta " + venta.getId() + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					"Primero debes registrar la venta, al momento de buscar la venta esta no existe.");
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
		int index1 = this.index;
		if (id > 0) {
			DetalleCompraVenta aux = indexDetalleProducto(this.tabla_venta, id, 0, false, false);
			if (aux != null) {
				DetalleCompraVenta aux2 = indexDetalleProducto(this.all_detalle_venta, id, 0, false, false);
				int index2 = this.index;
				if (aux2 != null) {
					int stock = aux2.getDetalleProducto().getStock() + aux.getCantidad();
					aux2.getDetalleProducto().setStock(stock);

					this.tabla_venta.remove(index1);
					this.all_detalle_venta.set(index2, aux2);

					if (this.tabla_venta.size() == 0) {
						this.contador_detalle_venta = 0;
						this.venta.setTotal(BigInteger.ZERO);
					}
					this.initDetalleVenta();
					this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							"Se ha eliminado el detalle producto con ID " + id + ",stock resultante " + stock + ".");
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
							"El ID detalle de producto no fue encontrado.");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
						"El ID detalle de venta no fue encontrado.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					"El ID detalle de venta no fue encontrado.");
		}
		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	///////////////////////////////////////////////////////
	// Statu
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite activar el dialog de actualizar.
	 */
	public void statuActualizar() {
		this.message = null;
		this.detalle_venta_producto = null;
		this.cantidad_actualizar = 0;
		int id = Integer.parseInt(Face.get("id-detalle-venta"));
		if (id > 0) {
			DetalleCompraVenta aux = indexDetalleProducto(this.tabla_venta, id, 0, false, true);
			if (aux != null) {
				this.detalle_venta_producto = aux;
				this.initDialogForm(1);
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"El ID detalle de producto no fue encontrado.");
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
	// Method Product
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite agregar uno por uno de los productos seleccionados.
	 */
	public void addProductosCliente() {
		this.message = null;
		this.index = -1;
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
						System.out.println(this.detalle_venta);
						if (addDetalleProductoCliente(this.detalle_venta)) {
							this.detalle_venta.setCantidad(0);
							this.all_detalle_venta.set(index, this.detalle_venta);
							this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes",
									"Se ha agregado el producto "
											+ this.detalle_venta.getDetalleProducto().getProductoBean().getNombre()
											+ " con ID del detalle " + this.detalle_venta.getDetalleProducto().getId()
											+ ",la cantidad fue " + cantidad + ".");
						} else {
							this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
									"No se ha podido agregar el detalle producto con ID "
											+ this.getDetalle_venta().getDetalleProducto().getId() + ".");
						}
						this.initDetalleVenta();
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
		
		if(this.tabla_venta == null) {
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
		}
		return respuesta;
	}

	///////////////////////////////////////////////////////
	// Method Seller
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite filtrar un vendedor.
	 */
	public void filtrarVendedor() {
		this.hidden_vendedor = false;
		this.filtro_vendedor = false;
		this.message = null;
		this.continuar_vendedor = false;
		if (this.venta != null && this.vendedor != null && this.vendedor.getPersona() != null
				&& Convertidor.isCadena(this.vendedor.getPersona().getDocumento())) {
			UsuarioController cDao = new UsuarioController();
			Usuario v = cDao.usuarioRol("VENDEDOR", this.vendedor.getPersona().getDocumento());
			if (v != null) {
				if (this.venta != null) {
					this.documento_vendedor = v.getPersona().getDocumento();
					this.vendedor = v;
					this.venta.setUsuario2(v);
					this.filtro_vendedor = true;
					this.continuar_vendedor = true;
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Se ha filtrado el vendedor con documento "
									+ this.vendedor.getPersona().getDocumento() + "."));
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"Error interno al analizar la venta.");
				}

			} else {
				String documento = this.vendedor.getPersona().getDocumento();
				initVendedor();
				this.vendedor.getPersona().setDocumento(documento);
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
						"No existe ningun vendedor con documento " + documento + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No ingresado ningun documento vendedor.");
		}

		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	/**
	 * Metodo que permite limpiar el filtro del vendedor.
	 */
	public void limpiarFiltroVendedor() {
		if (this.filtro_vendedor) {
			initVendedor();
			if (this.venta != null) {
				this.venta.setUsuario2(null);
			}
			this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Se ha limpiado el filtro del vendedor.");
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No has filtrado ningun vendedor.");
		}

		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite selecionar el metodo pago.
	 */
	public void metodoPago() {
		this.message = null;
		if (this.venta != null && Convertidor.isCadena(this.venta.getMetodoPagoBean().getBanco())) {
			MetodoPagoController dao = new MetodoPagoController();
			MetodoPago aux = dao.find(this.venta.getMetodoPagoBean().getBanco());
			if (aux != null) {
				this.venta.setMetodoPagoBean(aux);
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha seleccinado el metodo pago con banco " + this.venta.getMetodoPagoBean().getBanco()
								+ ".");
			} else {
				this.venta.setMetodoPagoBean(new MetodoPago());
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No existe ningun metodo pago con banco " + this.venta.getMetodoPagoBean().getBanco() + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No has seleccionado ningun metodo pago.");
		}

		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
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

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
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

	public boolean isContinuar_cliente() {
		return continuar_cliente;
	}

	public void setContinuar_cliente(boolean continuar_cliente) {
		this.continuar_cliente = continuar_cliente;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

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

	public List<DetalleCompraVenta> getAll_detalle_venta() {
		return all_detalle_venta;
	}

	public void setAll_detalle_venta(List<DetalleCompraVenta> all_detalle_venta) {
		this.all_detalle_venta = all_detalle_venta;
	}

	public boolean isContinuar_vendedor() {
		return continuar_vendedor;
	}

	public void setContinuar_vendedor(boolean continuar_vendedor) {
		this.continuar_vendedor = continuar_vendedor;
	}

	public String getDocumento_vendedor() {
		return documento_vendedor;
	}

	public void setDocumento_vendedor(String documento_vendedor) {
		this.documento_vendedor = documento_vendedor;
	}

	public SessionBean getSesion() {
		return sesion;
	}

	public void setSesion(SessionBean sesion) {
		this.sesion = sesion;
	}

	public EmailBean getEmail() {
		return email;
	}

	public void setEmail(EmailBean email) {
		this.email = email;
	}

	public AppBean getApp() {
		return app;
	}

	public void setApp(AppBean app) {
		this.app = app;
	}

	public Usuario getVendedor() {
		return vendedor;
	}

	public void setVendedor(Usuario vendedor) {
		this.vendedor = vendedor;
	}

	public boolean isHidden_vendedor() {
		return hidden_vendedor;
	}

	public void setHidden_vendedor(boolean hidden_vendedor) {
		this.hidden_vendedor = hidden_vendedor;
	}

	public boolean isFiltro_vendedor() {
		return filtro_vendedor;
	}

	public void setFiltro_vendedor(boolean filtro_vendedor) {
		this.filtro_vendedor = filtro_vendedor;
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public DetalleCompraVenta getDetalle_venta() {
		return detalle_venta;
	}

	public void setDetalle_venta(DetalleCompraVenta detalle_venta) {
		this.detalle_venta = detalle_venta;
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

	public boolean isIngresar_cliente() {
		return ingresar_cliente;
	}

	public void setIngresar_cliente(boolean ingresar_cliente) {
		this.ingresar_cliente = ingresar_cliente;
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

	public int getId_detalle_producto() {
		return id_detalle_producto;
	}

	public void setId_detalle_producto(int id_detalle_producto) {
		this.id_detalle_producto = id_detalle_producto;
	}

	public DetalleCompraVenta getDetalle_venta_producto() {
		return detalle_venta_producto;
	}

	public void setDetalle_venta_producto(DetalleCompraVenta detalle_venta_producto) {
		this.detalle_venta_producto = detalle_venta_producto;
	}

	public ImageBean getImage() {
		return image;
	}

	public void setImage(ImageBean image) {
		this.image = image;
	}

	public List<DetalleCompraVenta> getFilter_all_detalle_venta() {
		return filter_all_detalle_venta;
	}

	public void setFilter_all_detalle_venta(List<DetalleCompraVenta> filter_all_detalle_venta) {
		this.filter_all_detalle_venta = filter_all_detalle_venta;
	}
}
