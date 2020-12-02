package com.bean.view;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

import com.bean.session.*;
import com.controller.*;
import com.model.*;
import com.model.other.*;
import com.util.*;

/**
 * Implementation GarantiaBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "garantia")
@ViewScoped
public class GarantiaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Garantia garantia;

	private FacesMessage message;

	private int id;

	private String[] select;
	private String estado;
	private int index;
	private int index_Motivo;

	private boolean insert;
	private boolean search;
	private boolean update;
	private boolean remove;
	private boolean hidden;
	private boolean error;
	private boolean active;

	///////////////////////////////////////////////////////
	// Managed
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean sesion;

	@ManagedProperty("#{image}")
	private ImageBean image;

	@ManagedProperty("#{table}")
	private DataTableBean table;

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		initGarantia();
		this.estado = "";
		this.index = -1;
		this.update = false;
		this.error = false;
		this.hidden = false;
		this.insert = false;
		this.remove = false;
		this.search = false;
		this.active = false;
		this.index_Motivo = -1;
	}

	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite inicializar la garantia.
	 */
	public void initGarantia() {
		this.garantia = new Garantia();
		this.garantia.setDetalleCompraVenta(new DetalleCompraVenta());
		this.garantia.setId(generarKEY());
		this.select = null;
	}

	/**
	 * Metodo que limpia el filtro de la tabla. este si
	 */
	public void initTable() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('sofia-table-update').clearFilters());");
	}

	/**
	 * Metodo que cambia el estado a dialogo de formulario.
	 * 
	 * @param estado representa el estado.
	 */
	public void initDialogMotivoForm(int estado) {
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
	 * Metodo que permite generar la llave primaria.
	 * 
	 * @return la llave generada.
	 */
	public int generarKEY() {
		GarantiaController dao = new GarantiaController();
		Garantia aux = dao.ultimaAdd();
		if (aux != null) {
			return aux.getId() + 1;
		}
		return 1;
	}

	/**
	 * Metodo que permite conocer una garantia por su indice en la tabla. este si
	 */
	public void garantia() {
		this.message = null;
		this.error = true;
		this.index = -1;
		String id = Face.get("id-garantia");
		if (Convertidor.isCadena(id)) {
			int id2 = Integer.parseInt(id);
			this.index = index(id2);
			if (this.table.getGarantia() != null && this.index >= 0 && this.index < this.table.getGarantia().size()) {
				this.garantia = this.table.getGarantia().get(this.index);
				if (this.garantia != null) {
					this.error = false;
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"La garantia no se encuentra registrada.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "La garantia no se encuentra registrada.");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se ha seleccionado ninguna garantia.");
		}
	}

	/**
	 * Metodo que permite buscar en una lista el indice de un proveedor. este si
	 * 
	 * @param id2 representa el id del proveedor.
	 * @return representa el indice del proveedor en la lista.
	 */
	public int index(int id2) {
		int aux = 0;
		for (Garantia g : this.table.getGarantia()) {
			if (g.getId() == id2) {
				return aux;
			}
			aux++;
		}
		return -1;
	}

	///////////////////////////////////////////////////////
	// Date
	///////////////////////////////////////////////////////
	/**
	 * 
	 * @param fechaIn
	 * @param fechaFinal
	 * @return
	 */
	public static int restarFechas(Date fechaIn, Date fechaFinal) {
		GregorianCalendar fechaInicio = new GregorianCalendar();
		fechaInicio.setTime(fechaIn);
		GregorianCalendar fechaFin = new GregorianCalendar();
		fechaFin.setTime(fechaFinal);
		int dias = 0;
		if (fechaFin.get(Calendar.YEAR) == fechaInicio.get(Calendar.YEAR)) {
			dias = (fechaFin.get(Calendar.DAY_OF_YEAR) - fechaInicio.get(Calendar.DAY_OF_YEAR)) + 1;
		} else {
			int rangoAnyos = fechaFin.get(Calendar.YEAR) - fechaInicio.get(Calendar.YEAR);
			for (int i = 0; i <= rangoAnyos; i++) {
				int diasAnio = fechaInicio.isLeapYear(fechaInicio.get(Calendar.YEAR)) ? 366 : 365;
				if (i == 0) {
					dias = 1 + dias + (diasAnio - fechaInicio.get(Calendar.DAY_OF_YEAR));
				} else if (i == rangoAnyos) {
					dias = dias + fechaFin.get(Calendar.DAY_OF_YEAR);
				} else {
					dias = dias + diasAnio;
				}
			}
		}
		return dias;

	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public boolean fechaGarantia(DetalleCompraVenta dcv) {
		if (dcv != null) {
			Fecha fecha = new Fecha();
			Date fechaActual = new Date(fecha.fecha());
			Date fechaVenta = dcv.getVentaBean().getFechaRegistro();
			if (restarFechas(fechaVenta, fechaActual) < (dcv.getGarantia() * 365)) {
				return true;
			}
		} 
		return false;
	}

	///////////////////////////////////////////////////////
	// CRUD
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite registrar una garantia.
	 */
	@SuppressWarnings("deprecation")
	public void registrar() {
		if (this.validar()) {
			GarantiaController dao = new GarantiaController();
			Garantia aux = dao.find(this.getGarantia().getId());
			if (aux == null) {
				DetalleCompraVentaController scvDao = new DetalleCompraVentaController();
				DetalleCompraVenta dcv = scvDao.find(this.garantia.getDetalleCompraVenta().getId());
				if (dcv != null) {
					if (fechaGarantia(dcv)) {
						aux = dao.findByField("detalleCompraVenta", this.getGarantia().getDetalleCompraVenta());
						if (aux == null) {
							Fecha fecha = new Fecha();
							this.garantia.setEstado(false);
							this.garantia.setFechaCreacion(new Date(fecha.fecha()));
							dao.insert(this.garantia);
							this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
									"Se ha registrado una garantia con ese ID " + this.getGarantia().getId() + ".");
							initGarantia();
						} else {
							this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
									"Ya se encuentra registrada esta venta " + aux.getDetalleCompraVenta().getId()
											+ " en garantida con el ID " + aux.getId() + ".");
						}
					} else {
						this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
								"El detalle producto con ID " + dcv.getId() + " y fecha venta "
										+ dcv.getVentaBean().getFechaRegistro() + ",ya expiro la garantia que era de "
										+ dcv.getGarantia() + " año.");
					}
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
							"No se encontro ningun detalle venta con el ID "
									+ this.getGarantia().getDetalleCompraVenta().getId() + ".");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"Ya se encuentra registrada una garantia con ese ID " + this.getGarantia().getId() + ".");
			}
		}

		if (this.message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	/**
	 * Metodo que cambia el valor del estado del gARANTIA. este si
	 */
	public void estadoGarantia() {
		this.update = false;
		this.garantia();
		if (!this.error && this.index >= 0) {
			GarantiaController dao = new GarantiaController();
			boolean estado = garantia.getEstado();
			estado = (estado) ? false : true;
			this.garantia.setEstado(estado);
			this.table.getGarantia().set(this.index, this.garantia);
			this.update = true;
			dao.update(this.garantia);
			this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					"Se ha cambiado el estado la garantia con id " + garantia.getId() + " a estado"
							+ ((estado) ? " Leido." : " No Leido."));
			// Init
			initTable();
			this.index = -1;
			this.initGarantia();
		}
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}

	/**
	 * Metodo que cambia el valor del motivo del garantia.
	 */
	public void motivoGarantia() {
		this.update = false;
		if (!this.error && this.index >= 0) {
			GarantiaController dao = new GarantiaController();
			Garantia aux = dao.find(this.getGarantia().getId());
			if (aux != null) {
				aux.setMotivo(this.garantia.getMotivo().toUpperCase());
				dao.update(aux);
				this.table.getGarantia().set(this.index, this.garantia);
				this.update = true;
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha cambiado el motivo la garantia con id " + garantia.getId());
				// Init
				initTable();
				this.index = -1;
				this.initGarantia();
				this.initDialogMotivoForm(0);
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"no existe ninguna garantia con id " + garantia.getId());
			}
		}
		FacesContext.getCurrentInstance().addMessage(null, this.message);
	}

	///////////////////////////////////////////////////////
	// Statu
	///////////////////////////////////////////////////////
	/**
	 * Metodo que obtiene el estato del proveedor a registrar.
	 */
	public void statuRegistrar() {
		if (this.update) {
			this.initGarantia();
			this.update = false;
		}
		this.initDialogMotivoForm(1);
		this.hidden = false;
		this.estado = "Registrar";
	}

	/**
	 * Metodo que permite abirir el dilogo de editar con la garantia.
	 */
	public void statuEditarGarantia() {
		this.index_Motivo = -1;
		garantia();
		if (this.message == null && this.garantia != null) {
			this.initDialogMotivoForm(1);
		} else {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	///////////////////////////////////////////////////////
	// Validator
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite validar los campos de garantia.
	 * 
	 * @return true si son validos false si no.
	 */
	public boolean validar() {
		if (this.garantia != null && this.garantia.getId() > 0) {
			if (this.garantia.getDetalleCompraVenta() != null && this.garantia.getDetalleCompraVenta().getId() > 0) {
				if (Convertidor.isCadena(this.garantia.getMotivo())) {
					return true;
				} else {
					this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo movito es obligatorio.");
				}
			} else {
				this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "",
						"No has seleccionado ningun detalle venta.");
			}
		} else {
			this.message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El campo ID es obligatorio.");
		}
		return false;
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public Garantia getGarantia() {
		return garantia;
	}

	public void setGarantia(Garantia garantia) {
		this.garantia = garantia;
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

	public FacesMessage getMessage() {
		return message;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String[] getSelect() {
		return select;
	}

	public void setSelect(String[] select) {
		this.select = select;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex_Motivo() {
		return index_Motivo;
	}

	public void setIndex_Motivo(int index_Motivo) {
		this.index_Motivo = index_Motivo;
	}

	public boolean isInsert() {
		return insert;
	}

	public void setInsert(boolean insert) {
		this.insert = insert;
	}

	public boolean isSearch() {
		return search;
	}

	public void setSearch(boolean search) {
		this.search = search;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public boolean isRemove() {
		return remove;
	}

	public void setRemove(boolean remove) {
		this.remove = remove;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public ImageBean getImage() {
		return image;
	}

	public void setImage(ImageBean image) {
		this.image = image;
	}

	public DataTableBean getTable() {
		return table;
	}

	public void setTable(DataTableBean table) {
		this.table = table;
	}
}
