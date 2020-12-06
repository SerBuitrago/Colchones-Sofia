package com.bean.session;

import java.io.Serializable;
import java.math.BigDecimal;
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

import com.controller.*;
import com.model.*;
import com.model.other.*;
import com.util.*;

/**
 * Implementation ComprarBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "compra")
@SessionScoped
public class CompraBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private FacesMessage message;
	private boolean error;

	private Compra compra;
	private BigInteger presupuesto;
	private List<DetalleCompraVenta> tabla_compra;
	private List<DetalleCompraVenta> filter_tabla_compra;
	private List<DetalleCompraVenta> inserto_detalles_compra;
	private List<DetalleCompraVenta> tabla_compra_copia;
	private boolean editar;

	private DetalleCompraVenta detalle_seleccionado_ver;

	private int index;
	private List<DetalleCompraVenta> all_detalle_compra;
	private List<DetalleCompraVenta> filter_all_detalle_compra;

	private Proveedor proveedor;
	private String documento;
	private boolean hidden_proveedor;
	private boolean editar_proveedor;
	private boolean filtar_proveedor;
	private boolean imagen_proveedor;
	private boolean continuar_proveedor;

	private DetalleCompraVenta detalle_compra;
	private int id_detalle_producto;
	private boolean active_filert_detalle_compra;
	private boolean filter_detalle_compra;
	private boolean continuar_detalle_compra;
	private boolean hidden_detalle_compra;

	private BigInteger subtotal_sin_iva;

	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean sesion;

	@ManagedProperty("#{app}")
	private AppBean app;

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		initCompra();
		this.error = false;
		this.presupuesto = BigInteger.ZERO;
		this.subtotal_sin_iva = BigInteger.ZERO;
	}

	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * Metodo que inicializa la compra.
	 */
	public void initCompra() {
		this.compra = new Compra();
		this.compra.setId(this.generarKey());
		this.compra.setTotal(BigInteger.ZERO);
		this.compra.setTotalSinIva(BigInteger.ZERO);
		this.compra.setIva(this.app.getEmpresa().getIva());
		this.compra.setUsuario(this.sesion.getLogeado());
		initProveedor();
		this.tabla_compra = null;
		this.editar = false;
		this.inserto_detalles_compra = null;
	}

	/**
	 * Metodo que inicializa el proveedor.
	 */
	public void initProveedor() {
		this.hidden_proveedor = false;
		this.editar_proveedor = false;
		this.filtar_proveedor = false;
		this.imagen_proveedor = false;
		this.continuar_proveedor = false;
		this.documento = null;
		this.proveedor = null;
		this.compra.setProveedorBean(new Proveedor());
		this.compra.getProveedorBean().setPersona(new Persona());
		this.compra.getProveedorBean().getPersona().setTipoDocumentoBean(new TipoDocumento());
	}

	/**
	 * Metodo que permite inicializar el presupuesto.
	 */
	public void initPresupuesto() {
		this.presupuesto = app.getApp().getEmpresa().getPresupuesto();
	}

	/**
	 * Metodo que permite consultar todos los detalles productos.
	 */
	public void initAllDetalleCompra() {
		this.index = -1;
		this.all_detalle_compra = new ArrayList<DetalleCompraVenta>();
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
						all_detalle_compra.add(aux);
					}
				}
			}
		}
	}

	/**
	 * Metodo que permite inicializar la detalle venta.
	 */
	public void initDetalleCompra() {
		this.id_detalle_producto = -1;
		this.active_filert_detalle_compra = true;
		this.filter_detalle_compra = false;
		this.continuar_detalle_compra = false;
		this.hidden_detalle_compra = false;
		this.detalle_compra = new DetalleCompraVenta();
		this.detalle_compra.setDetalleProducto(new DetalleProducto());
		this.detalle_compra.getDetalleProducto().setProductoBean(new Producto());
		this.detalle_compra.getDetalleProducto().getProductoBean().setCategoriaBean(new Categoria());
	}

	///////////////////////////////////////////////////////
	// Method Buy
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite generar la key de la compra.
	 * 
	 * @return representa la key generada.
	 */
	public int generarKey() {
		CompraController dao = new CompraController();
		Compra aux = dao.ultimaAdd();
		if (aux != null) {
			return aux.getId() + 1;
		}
		return 1;
	}

	/**
	 * Metodo que permite registrar la compra con sus detalles.
	 */
	@SuppressWarnings("deprecation")
	public void registrarCompra() {
		this.message = validarCompra();
		this.editar = false;
		if (this.message == null) {
			CompraController dao = new CompraController();
			Fecha fecha = new Fecha();
			registrarProveedor(compra.getProveedorBean());
			if (this.message == null) {
				this.compra.setFechaCreacion(new Date(fecha.fecha()));
				this.compra.setEstado(true);
				dao.insert(this.compra);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha registrado la compra con ID " + this.compra.getId() + "."));
				registrarDetalleCompra();
				if (!editar) {
					if (this.inserto_detalles_compra != null && this.inserto_detalles_compra.size() > 0) {
						BigInteger aux = BigInteger.ZERO;
						BigInteger pre = this.app.getApp().getEmpresa().getPresupuesto();
						for (DetalleCompraVenta u : this.inserto_detalles_compra) {
							aux = aux.add(u.getSubtotal());
						}
						if (pre.compareTo(aux) >= 0) {
							this.compra.setTotal(aux);
							dao.update(this.compra);
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_INFO, "",
											"Se ha editado el total la compra con ID " + this.compra.getId()
													+ ", algunos detalles no fueron registrados."));
						} else {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
											"No se ha editado el total la compra con ID " + this.compra.getId()
													+ ", algunos detalles no fueron registrados."));
						}
					} else {
						dao.delete(this.compra);
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Se ha eliminado la compra con ID "
										+ this.compra.getId() + ", no tiene registrado ningun detalle producto."));
					}
				} else {
					EmpresaController edao = new EmpresaController();
					Empresa e = edao.find(app.getNIT());
					if (e != null) {
						e.setPresupuesto(this.presupuesto);
						this.app.getApp().setEmpresa(e);
						edao.update(e);
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"", "Se ha actualizado el presupuesto de la empresa NIT " + app.getNIT() + "."));
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"", "No de ha encontrado ninguna empresa con el NIT " + app.getNIT() + "."));
					}
				}
				this.initCompra();
				this.tabla_compra_copia = this.tabla_compra;
				this.tabla_compra = null;
				this.initDetalleCompra();
				this.initPresupuesto();
			}
		}

		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * Metodo que permite cambiar el estado a una venta.
	 */
	public void estadoCompra() {
		this.message = null;
		String id = Face.get("id-compra");
		if (Convertidor.isCadena(id)) {
			CompraController dao = new CompraController();
			Compra aux = dao.find(Integer.parseInt(id));
			if (aux != null) {
				boolean estado = (!aux.getEstado());
				aux.setEstado(estado);
				dao.update(aux);
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha cambiado el estado de la compra con ID " + id + ", a estado "
								+ (estado ? "Activo" : "Bloqueado") + ".");
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No existe ninguna compra con el ID " + id + ".");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El Id de la venta no es compra.");
		}
	}

	/**
	 * Metodo que permite registrar los detalles de compra.
	 */
	public void registrarDetalleCompra() {
		this.editar = false;
		this.message = null;
		this.inserto_detalles_compra = new ArrayList<DetalleCompraVenta>();
		DetalleCompraVentaController dao = new DetalleCompraVentaController();
		int insert = 0;
		for (DetalleCompraVenta dcv : this.tabla_compra) {
			int key = generarKeyDetalleCompra();
			DetalleCompraVenta aux = dao.find(key);
			if (aux == null) {
				dcv.setId(key);
				dcv.setCompraBean(this.compra);
				this.message = validarDetalleCompra(dcv);
				if (this.message == null) {
					dao.insert(dcv);
					this.inserto_detalles_compra.add(dcv);
					insert++;
					DetalleProductoController dDao = new DetalleProductoController();
					DetalleProducto ad = dDao.find(dcv.getDetalleProducto().getId());
					if (ad != null) {
						int stock = ad.getStock() + dcv.getCantidad();
						ad.setStock(stock);
						dDao.update(ad);
					}
				}
			}
		}
		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
		int size = this.tabla_compra.size();
		if (insert == size) {
			this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"Se han registrado todos los detalles de la compra con ID " + this.compra.getId() + ",(" + insert
							+ "/" + size + ").");
			this.editar = true;
		} else if (insert > 0) {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"No se han registrado todos los detalles de la compra con ID " + this.compra.getId() + ",(" + insert
							+ "/" + size + ").");
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					"No se han registrado ningun detalle de la compra con ID " + this.compra.getId() + ",(" + insert
							+ "/" + size + ").");
		}
	}

	/**
	 * Metodo que permite validar los campos de la compra.
	 * 
	 * @return null si los campos son correctos y si no envia el error presentado.
	 */
	public FacesMessage validarCompra() {
		FacesMessage aux = null;
		if (this.compra != null && this.compra.getId() > 0) {
			if (Convertidor.isNumber(String.valueOf(this.compra.getId()))) {
				if (this.tabla_compra != null && this.tabla_compra.size() > 0) {
					if (this.compra.getProveedorBean() != null
							&& Convertidor.isCadena(this.compra.getProveedorBean().getDocumento())) {
						CompraController dao = new CompraController();
						Compra c = dao.find(this.compra.getId());
						if (c == null) {
							if (Operacion.mayorIgualZero(this.presupuesto)) {
								return aux;
							} else {
								aux = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
										"No se puede completar la compra, no hay presupuesto disponible.");
							}
						} else {
							aux = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
									"Ya existe una compra con ese ID " + c.getId() + ".");
						}
					} else {
						aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No has seleccionado ningun proveedor.");
					}
				} else {
					aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No has seleccionado ningun producto.");
				}
			} else {
				aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo ID compra solo debe tener numeros.");
			}
		} else {
			aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo ID compra es obligatorio.");
		}
		return aux;
	}

	///////////////////////////////////////////////////////
	// Method Provider
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite filtrar un proveedor o persona.
	 */
	public void filtrarProveedor() {
		this.hidden_proveedor = false;
		this.editar_proveedor = false;
		this.filtar_proveedor = false;
		this.imagen_proveedor = false;
		this.continuar_proveedor = false;
		this.proveedor = null;
		this.documento = null;
		this.message = null;
		if (this.compra != null && this.compra.getProveedorBean() != null
				&& Convertidor.isCadena(this.compra.getProveedorBean().getDocumento())) {
			PersonaController dao = new PersonaController();
			Persona aux = dao.find(this.compra.getProveedorBean().getDocumento());
			if (aux != null) {
				ProveedorController prDao = new ProveedorController();
				Proveedor proveedor = prDao.find(aux.getDocumento());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Se ha filtrado la persona con documento "
								+ this.compra.getProveedorBean().getDocumento() + "."));
				if (proveedor != null) {
					this.compra.setProveedorBean(proveedor);
					this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							"La persona con documento " + this.compra.getProveedorBean().getDocumento()
									+ ",si esta registrada como proveedor de la empresa.");
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
							"La persona con documento " + this.compra.getProveedorBean().getDocumento()
									+ ",no esta registrada como proveedor de la empresa.");
				}

				if (this.compra.getProveedorBean() == null) {
					initProveedor();
				}

				this.compra.getProveedorBean().setPersona(aux);
				this.hidden_proveedor = true;
				this.filtar_proveedor = true;
				this.documento = this.compra.getProveedorBean().getDocumento();
				this.initAllDetalleCompra();
				this.tabla_compra_copia = this.tabla_compra;
				this.tabla_compra = new ArrayList<DetalleCompraVenta>();
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No se ha encontrado ninguna persona con ese documento "
								+ this.compra.getProveedorBean().getDocumento() + ".");
			}
		} else {
			this.all_detalle_compra = null;
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"El documento de proveedor es obligatorio para filtrar.");
		}

		if (!this.hidden_proveedor) {
			initProveedor();
		}
		if (message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * Metodo que permite activar o bloquear la opción de editar un cliente.
	 */
	public void estadoEditarProveedor() {
		if (this.hidden_proveedor) {
			this.hidden_proveedor = false;
			this.editar_proveedor = true;
			this.proveedor = proveedor(this.compra.getProveedorBean());
		} else {
			if (!editar_proveedor) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No has filtrado ningun proveedor."));
			} else {
				this.hidden_proveedor = true;
				this.editar_proveedor = false;
			}
		}
	}

	/**
	 * Metodo que permite limpiar filtro cliente.
	 */
	public void limpiarFiltroProveedor() {
		this.initProveedor();
		this.tabla_compra_copia = this.tabla_compra;
		this.tabla_compra = new ArrayList<DetalleCompraVenta>();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Se ha limpiado el filtro proveedor."));
	}

	/**
	 * Metodo que permite validar el proveedor.
	 */
	public void validarProveedor() {
		this.continuar_proveedor = false;
		this.message = validar(this.compra.getProveedorBean());
		boolean seguir = true;
		if (this.message == null) {
			if (this.editar_proveedor) {
				seguir = validarProveedor(this.compra.getProveedorBean(), this.proveedor);
				if (seguir) {
					continuar_proveedor = true;
				} else {
					boolean error = false;
					if (!this.compra.getProveedorBean().getDocumento().equals(this.proveedor.getDocumento())) {
						PersonaController dao = new PersonaController();
						Persona aux = dao.find(this.compra.getProveedorBean().getDocumento());
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
						String numero = Convertidor.telefono(this.compra.getProveedorBean().getPersona().getTelefono());
						if (!Convertidor.equals(this.compra.getProveedorBean().getPersona().getEmail(),
								this.proveedor.getPersona().getEmail())) {
							existe = dao.registrar(this.compra.getProveedorBean().getPersona().getEmail(), null);
						} else if (!Convertidor.equals(numero, this.proveedor.getPersona().getTelefono())) {
							existe = dao.registrar(null, numero);
						}

						if (!existe) {
							continuar_proveedor = true;
						} else {
							error = true;
							this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
									"Ya existe una persona registrada con ese telefono o email.");
						}
					}
				}
			} else {
				if (!filtar_proveedor) {
					boolean error;
					PersonaController dao = new PersonaController();
					Persona aux = dao.find(this.compra.getProveedorBean().getDocumento());
					if (aux == null) {
						error = false;
					} else {
						error = true;
						this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
								"Ya existe una persona con ese documento " + aux.getDocumento() + ".");
					}
					if (!error) {
						if (this.compra.getProveedorBean() != null
								&& this.compra.getProveedorBean().getPersona() != null) {
							ProveedorController pDao = new ProveedorController();
							String numero = Convertidor
									.telefono(this.compra.getProveedorBean().getPersona().getTelefono());
							boolean existe = pDao.registrar(this.compra.getProveedorBean().getPersona().getEmail(),
									numero);
							if (!existe) {
								continuar_proveedor = true;
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
					continuar_proveedor = true;
				}
			}
		}

		if (this.continuar_proveedor && this.message == null) {
			this.hidden_proveedor = true;
			this.initDetalleCompra();
			this.initPresupuesto();
			this.initAllDetalleCompra();
			this.filter_detalle_compra = false;
			this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"Proveedor con documento " + this.compra.getProveedorBean().getDocumento() + " esta validado.");
		}

		if (message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
		}
	}

	/**
	 * Metodo que permite validar los campos del proveedor.
	 * 
	 * @param p representa el proveedor.
	 * @return el mensaje obtenido.
	 */
	public FacesMessage validar(Proveedor p) {
		FacesMessage aux = null;
		if (p != null && Convertidor.isCadena(p.getDocumento())) {
			if (p.getPersona() != null) {
				if (Convertidor.isCadena(p.getPersona().getNombre())) {
					if (!Convertidor.containsNumber(p.getPersona().getNombre())) {
						if (Convertidor.isCadena(p.getPersona().getEmail())) {
							if (Convertidor.isCadena(p.getPersona().getTelefono())) {
								String prueba = Convertidor.telefono(p.getPersona().getTelefono());
								if (Convertidor.isNumber(prueba)) {
									if (Convertidor.isCadena(p.getPersona().getDireccion())) {
										return null;
									} else {
										aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
												"El campo dirección es obligatorio.");
									}
								} else {
									aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
											"El campo telefono no debe tener caracteres diferentes a numeros.");
								}
							} else {
								aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
										"El campo telefono es obligatorio.");
							}
						} else {
							aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo email es obligatorio.");
						}
					} else {
						aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
								"El campo nombre no puede tener caracteres numericos.");
					}
				} else {
					aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo nombre es obligatorio.");
				}
			} else {
				aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo documento es obligatorio.");
			}
		} else {
			aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo documento es obligatorio.");
		}
		return aux;
	}

	/**
	 * Metodo que permite inicializar los valores de un proveedor a otro.
	 * 
	 * @param p representa el proveedor.
	 * @return representa el proveedor obtenido.
	 */
	public Proveedor proveedor(Proveedor p) {
		Proveedor aux = null;
		if (p != null && p.getPersona() != null && Convertidor.isCadena(p.getDocumento())) {
			aux = new Proveedor();
			aux.setDocumento(p.getDocumento());
			Persona persona = new Persona();
			persona.setFoto(p.getPersona().getFoto());
			persona.setDocumento(p.getDocumento());
			persona.setNombre(p.getPersona().getNombre());
			persona.setTelefono(Convertidor.telefono(p.getPersona().getTelefono()));
			persona.setDireccion(p.getPersona().getDireccion());
			persona.setEmail(p.getPersona().getEmail());
			aux.setPersona(persona);
		}
		return aux;
	}

	/**
	 * Metodo que permite verificar si dos proveedores son iguales.
	 * 
	 * @param p1 representa proveedor 1.
	 * @param p2 representa proveedor 2.
	 * @return true si son iguales false si no.
	 */
	public boolean validarProveedor(Proveedor p1, Proveedor p2) {
		if (p1 != null && p2 != null) {
			if (p1.getDocumento().equals(p2.getDocumento())) {
				if (p1.getPersona() != null && p2.getPersona() != null) {
					if (p1.getPersona().getNombre().equals(p2.getPersona().getNombre())) {
						if (p1.getPersona().getEmail().equals(p2.getPersona().getEmail())) {
							if (p1.getPersona().getTelefono().equals(p2.getPersona().getTelefono())) {
								if (p1.getPersona().getDireccion().equals(p2.getPersona().getDireccion())) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Metodo que permite registrar un proveedor.
	 */
	@SuppressWarnings("deprecation")
	public void registrarProveedor(Proveedor aux) {
		this.message = null;
		if (aux != null && Convertidor.isCadena(aux.getDocumento())) {
			PersonaController dao = new PersonaController();
			Persona p = dao.find(aux.getDocumento());
			aux.getPersona().setTelefono(Convertidor.telefono(aux.getPersona().getTelefono()));
			if (p != null) {
				p.setNombre(aux.getPersona().getNombre().toUpperCase());
				p.setTelefono(aux.getPersona().getTelefono());
				p.setDireccion(aux.getPersona().getDireccion());
				p.setEmail(aux.getPersona().getEmail());
				dao.update(p);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha actualizado la persona documento " + aux.getDocumento() + "."));
			} else {
				dao.insert(aux.getPersona());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha registrado la persona documento " + aux.getDocumento() + "."));

			}
			ProveedorController pdao = new ProveedorController();
			Proveedor proveedor = pdao.find(aux.getDocumento());
			if (proveedor == null) {
				Fecha fecha = new Fecha();
				aux.setFechaCreacion(new Date(fecha.fecha()));
				aux.setEstado(true);
				pdao.insert(aux);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha registrado el proveedor documento " + aux.getDocumento() + "."));
			}
			proveedor = pdao.find(aux.getDocumento());
			List<DetalleProducto> detalles_provedor = proveedor.getDetalleProductos();
			for (DetalleCompraVenta dp : this.tabla_compra) {
				if (!agregado(detalles_provedor, dp.getDetalleProducto().getId())) {
					pdao.insertProveedorProducto(proveedor.getDocumento(),
							String.valueOf(dp.getDetalleProducto().getId()));
				}
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"No se ha recibido el proveedor  a registrar.");
		}
	}

	/**
	 * Metodo que permite averiguar si un proveedor a tiene asignado un detalle
	 * producto.
	 * 
	 * @param p        representa el proveedor.
	 * @param producto representa el detalle producto.
	 * @return true si lo tiene agregado false si no.
	 */
	public boolean agregado(List<DetalleProducto> p, int producto) {
		if (p != null) {
			for (DetalleProducto dp : p) {
				if (dp.getId() == producto) {
					return true;
				}
			}
		}
		return false;
	}

	///////////////////////////////////////////////////////
	// Method Detail
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite generar la key del detalle compra venta.
	 * 
	 * @return representa la key generada.
	 */
	public int generarKeyDetalleCompra() {
		DetalleCompraVentaController dao = new DetalleCompraVentaController();
		DetalleCompraVenta aux = dao.ultimoAdd();
		return (aux != null) ? (aux.getId() + 1) : 1;
	}

	/**
	 * Metodo que permite filtrar un detalle venta.
	 */
	public void filtrarDetalleProducto() {
		this.active_filert_detalle_compra = true;
		this.filter_detalle_compra = false;
		this.index = -1;
		this.message = null;
		if (this.id_detalle_producto > 0) {
			if (this.detalle_compra != null && this.detalle_compra.getDetalleProducto() != null) {
				int id = this.id_detalle_producto;
				this.initDetalleCompra();
				this.index = -1;
				DetalleCompraVenta aux = indexDetalleProducto(this.all_detalle_compra, id, 0, true, false);
				System.out.println("INDEX: " + this.index);
				if (this.index >= 0 && aux != null) {
					if (aux != null && this.index >= 0 && id > 0 && aux.getDetalleProducto() != null
							&& aux.getDetalleProducto().getId() > 0) {
						this.filter_detalle_compra = true;
						this.active_filert_detalle_compra = false;
						this.detalle_compra = aux;
						this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
								"Se ha encontrado detalle producto con el ID " + id + ".");
						this.id_detalle_producto = id;
					} else {
						this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
								"Ha ocurrido un error al buscar el detalle producto con ese ID " + id + ".");
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
	 * Metodo que permit limpirar filtro detalle venta.
	 */
	public void limpiarFiltroDetalleCompra() {
		this.initDetalleCompra();
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
		this.index = -2;
		if (list != null && list.size() > 0 && detalle > 0) {
			int i = 0;
			this.index = -1;
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
	 * Metodo que permite seleccionar un detalle producto.
	 */
	public void seleccionarDetalleProducto() {
		String id = Face.get("id-detalle-producto");
		FacesMessage message = null;
		if (Convertidor.isCadena(id)) {
			if (Convertidor.isNumber(id)) {
				id_detalle_producto = Integer.parseInt(id);
				filtrarDetalleProducto();
				if (this.filter_detalle_compra) {
					PrimeFaces current = PrimeFaces.current();
					current.executeScript("PF('sofia-dialog-producto-update').hide();");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
						"El campo ID detalle producto solo debe contener caracteres numericos.");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo ID detalle producto es obligatorio.");
		}
		if (message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	/**
	 * Metodo que permite validar los campos del detalle de la compra.
	 * 
	 * @return null si los campos son correctos y si no envia el error presentado.
	 */
	public FacesMessage validarDetalleCompra(DetalleCompraVenta dcv) {
		FacesMessage aux = null;
		if (dcv != null && dcv.getId() > 0) {
			if (dcv.getDetalleProducto() != null && dcv.getDetalleProducto().getId() > 0
					&& dcv.getDetalleProducto().getProductoBean() != null) {
				if (dcv.getCantidad() > 0) {
					if (Operacion.mayorZero(dcv.getPrecio())) {
						if (Operacion.mayorIgualZero(dcv.getSubtotal())) {
							return null;
						} else {
							aux = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El campo subtotal del producto "
									+ dcv.getDetalleProducto().getProductoBean().getNombre() + " con ID detalle "
									+ dcv.getDetalleProducto().getId() + " debe ser mayor o igual a cero.");
						}
					} else {
						aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
								"El campo precio del producto " + dcv.getDetalleProducto().getProductoBean().getNombre()
										+ " con ID detalle " + dcv.getDetalleProducto().getId()
										+ " debe ser mayor a cero.");
					}
				} else {
					aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
							"El campo cantidad del producto " + dcv.getDetalleProducto().getProductoBean().getNombre()
									+ " con ID detalle " + dcv.getDetalleProducto().getId()
									+ " debe ser mayor a cero.");
				}
			} else {
				aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No has seleccionado ningun producto.");
			}
		} else {
			aux = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"El Id del detalle de compra debe ser mayor a cero.");
		}
		return aux;
	}

	///////////////////////////////////////////////////////
	// Method Product
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite agregar uno por uno de los productos seleccionados.
	 */
	public void addProductosProveedor() {
		if (this.detalle_compra != null && this.detalle_compra.getDetalleProducto() != null) {
			if (this.tabla_compra == null) {
				this.tabla_compra = new ArrayList<DetalleCompraVenta>();
			}
			if (Operacion.mayorZero(this.presupuesto)) {
				if (detalle_compra.getCantidad() > 0) {
					if (Operacion.mayorZero(this.detalle_compra.getPrecio())) {
						if (detalle_compra.getDescuento() == null) {
							detalle_compra.setDescuento(BigInteger.ZERO);
						}
						if (detalle_compra.getPrecio().compareTo(detalle_compra.getDescuento()) >= 0) {
							BigInteger mult = this.detalle_compra.getPrecio()
									.multiply(new BigInteger(String.valueOf(detalle_compra.getCantidad())));
							if (mult.compareTo(detalle_compra.getDescuento()) >= 0) {
								mult = Operacion.resta(mult, this.detalle_compra.getDescuento());
								BigInteger aux = Operacion.resta(this.presupuesto, mult);
								if (Operacion.mayorIgualZero(aux)) {
									this.detalle_compra.setSubtotal(mult);
									this.presupuesto = aux;
									this.continuar_detalle_compra = true;
									DetalleCompraVenta fp = indexDetalleProducto(this.tabla_compra,
											this.detalle_compra.getDetalleProducto().getId(), 0, false, false);
									int index = this.index;
									if (fp != null) {
										fp.setDescuento(this.detalle_compra.getDescuento());
										int cantidad = fp.getCantidad() + this.detalle_compra.getCantidad();
										fp.setCantidad(cantidad);
										fp.setPrecio(this.detalle_compra.getPrecio());
										fp.getDetalleProducto().setPrecioCompra(fp.getPrecio());
										mult = fp.getPrecio()
												.multiply(new BigInteger(String.valueOf(fp.getCantidad())));
										mult = Operacion.resta(mult, fp.getDescuento());
										fp.setSubtotal(subTotal(fp));
										fp.setSubtotalSinIva(this.subtotal_sin_iva);
										this.subtotal_sin_iva = BigInteger.ZERO;
										this.detalle_compra = fp;
										this.tabla_compra.set(index, fp);
									} else {
										this.detalle_compra.setSubtotal(subTotal(this.detalle_compra));
										this.detalle_compra.setSubtotalSinIva(subtotal_sin_iva);
										this.subtotal_sin_iva = BigInteger.ZERO;
										this.tabla_compra.add(this.detalle_compra);
									}
									this.compra.setTotal(this.total(this.tabla_compra));
									this.initDetalleCompra();
									this.index = -1;
									message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
											"Se ha agregado el presupuesto disponible es " + presupuesto + ".");
								} else {
									message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
											"No hay presupuesto para comprar ese producto con esa cantidad, vuelva a intentarlo.");
								}
							} else {
								message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
										"El campo descuento no puede ser mayor a subtotal de precio unidad por la cantidad.");
							}
						}else {
							message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
									"El campo descuento no puede ser mayor al precio de compra del producto.");
						}
					} else {
						message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
								"El campo precio U/D es obligatorio.");
					}
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
							"La cantidad del producto debe ser mayor a cero.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No hay presupuesto para comprar ningun producto.");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No has filtrado ningun detalle producto.");
		}

		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, this.message);
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
		this.subtotal_sin_iva = BigInteger.ZERO;
		if (dp != null) {
			DetalleCompraVenta venta = dp;
			BigInteger cantidad = new BigInteger(String.valueOf(venta.getCantidad()));
			valor = cantidad.multiply(venta.getPrecio());
			valor = valor.subtract(cantidad.multiply(venta.getDescuento()));
			subtotalSinIva(venta.getPrecio(), cantidad, venta.getDescuento());
		}
		return valor;
	}

	public void subtotalSinIva(BigInteger valor, BigInteger cantidad, BigInteger descuento) {
		// SIN IVA
		int iva = Integer.parseInt(this.compra.getIva());
		double per = (double) (iva / 100d);
		BigDecimal precio = new BigDecimal(valor);
		precio = precio.multiply(new BigDecimal(per));
		BigInteger valorIva = precio.toBigInteger();
		BigInteger valorProducto = valor.subtract(valorIva);
		this.subtotal_sin_iva = cantidad.multiply(valorProducto);
		this.subtotal_sin_iva = this.subtotal_sin_iva.subtract(cantidad.multiply(descuento));
	}

	/**
	 * Metodo que permite calular el total a pagar.
	 * 
	 * @return el total a pagar.
	 */
	public BigInteger total(List<DetalleCompraVenta> list) {
		BigInteger valor = BigInteger.ZERO;
		BigInteger valor2 = BigInteger.ZERO;
		for (DetalleCompraVenta v : list) {
			valor = valor.add(subTotal(v));
			valor2 = valor2.add(this.subtotal_sin_iva);
			this.subtotal_sin_iva = BigInteger.ZERO;
		}
		this.compra.setTotal(valor);
		this.compra.setTotalSinIva(valor2);
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

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public boolean isHidden_proveedor() {
		return hidden_proveedor;
	}

	public void setHidden_proveedor(boolean hidden_proveedor) {
		this.hidden_proveedor = hidden_proveedor;
	}

	public boolean isEditar_proveedor() {
		return editar_proveedor;
	}

	public void setEditar_proveedor(boolean editar_proveedor) {
		this.editar_proveedor = editar_proveedor;
	}

	public boolean isFiltar_proveedor() {
		return filtar_proveedor;
	}

	public void setFiltar_proveedor(boolean filtar_proveedor) {
		this.filtar_proveedor = filtar_proveedor;
	}

	public boolean isImagen_proveedor() {
		return imagen_proveedor;
	}

	public void setImagen_proveedor(boolean imagen_proveedor) {
		this.imagen_proveedor = imagen_proveedor;
	}

	public boolean isContinuar_proveedor() {
		return continuar_proveedor;
	}

	public void setContinuar_proveedor(boolean continuar_proveedor) {
		this.continuar_proveedor = continuar_proveedor;
	}

	public SessionBean getSesion() {
		return sesion;
	}

	public void setSesion(SessionBean sesion) {
		this.sesion = sesion;
	}

	public AppBean getApp() {
		return app;
	}

	public void setApp(AppBean app) {
		this.app = app;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public BigInteger getPresupuesto() {
		return presupuesto;
	}

	public void setPresupuesto(BigInteger presupuesto) {
		this.presupuesto = presupuesto;
	}

	public List<DetalleCompraVenta> getTabla_compra() {
		return tabla_compra;
	}

	public void setTabla_compra(List<DetalleCompraVenta> tabla_compra) {
		this.tabla_compra = tabla_compra;
	}

	public List<DetalleCompraVenta> getTabla_compra_copia() {
		return tabla_compra_copia;
	}

	public void setTabla_compra_copia(List<DetalleCompraVenta> tabla_compra_copia) {
		this.tabla_compra_copia = tabla_compra_copia;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<DetalleCompraVenta> getAll_detalle_compra() {
		return all_detalle_compra;
	}

	public void setAll_detalle_compra(List<DetalleCompraVenta> all_detalle_compra) {
		this.all_detalle_compra = all_detalle_compra;
	}

	public DetalleCompraVenta getDetalle_compra() {
		return detalle_compra;
	}

	public void setDetalle_compra(DetalleCompraVenta detalle_compra) {
		this.detalle_compra = detalle_compra;
	}

	public int getId_detalle_producto() {
		return id_detalle_producto;
	}

	public void setId_detalle_producto(int id_detalle_producto) {
		this.id_detalle_producto = id_detalle_producto;
	}

	public boolean isActive_filert_detalle_compra() {
		return active_filert_detalle_compra;
	}

	public void setActive_filert_detalle_compra(boolean active_filert_detalle_compra) {
		this.active_filert_detalle_compra = active_filert_detalle_compra;
	}

	public boolean isFilter_detalle_compra() {
		return filter_detalle_compra;
	}

	public void setFilter_detalle_compra(boolean filter_detalle_compra) {
		this.filter_detalle_compra = filter_detalle_compra;
	}

	public boolean isContinuar_detalle_compra() {
		return continuar_detalle_compra;
	}

	public void setContinuar_detalle_compra(boolean continuar_detalle_compra) {
		this.continuar_detalle_compra = continuar_detalle_compra;
	}

	public boolean isHidden_detalle_compra() {
		return hidden_detalle_compra;
	}

	public void setHidden_detalle_compra(boolean hidden_detalle_compra) {
		this.hidden_detalle_compra = hidden_detalle_compra;
	}

	public List<DetalleCompraVenta> getFilter_tabla_compra() {
		return filter_tabla_compra;
	}

	public void setFilter_tabla_compra(List<DetalleCompraVenta> filter_tabla_compra) {
		this.filter_tabla_compra = filter_tabla_compra;
	}

	public List<DetalleCompraVenta> getInserto_detalles_compra() {
		return inserto_detalles_compra;
	}

	public void setInserto_detalles_compra(List<DetalleCompraVenta> inserto_detalles_compra) {
		this.inserto_detalles_compra = inserto_detalles_compra;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	public DetalleCompraVenta getDetalle_seleccionado_ver() {
		return detalle_seleccionado_ver;
	}

	public void setDetalle_seleccionado_ver(DetalleCompraVenta detalle_seleccionado_ver) {
		this.detalle_seleccionado_ver = detalle_seleccionado_ver;
	}

	public List<DetalleCompraVenta> getFilter_all_detalle_compra() {
		return filter_all_detalle_compra;
	}

	public void setFilter_all_detalle_compra(List<DetalleCompraVenta> filter_all_detalle_compra) {
		this.filter_all_detalle_compra = filter_all_detalle_compra;
	}

	public BigInteger getSubtotal_sin_iva() {
		return subtotal_sin_iva;
	}

	public void setSubtotal_sin_iva(BigInteger subtotal_sin_iva) {
		this.subtotal_sin_iva = subtotal_sin_iva;
	}
}
