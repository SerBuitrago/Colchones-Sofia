package com.bean.session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import com.controller.*;
import com.model.*;
import com.model.other.*;
import com.util.*;

/**
 * Implementation AppBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "app")
@SessionScoped
public class AppBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private App app;
	private Empresa empresa;
        
	private String NIT = "1090492502-4";
	private String mes;
	private int anio;

	private EmpresaInformacion empresaInformacion;
	private FacesMessage aviso;

	private boolean agregar_telefono_empresa;
	private List<EmpresaInformacion> empresa_informacion_telefono;
	private List<EmpresaInformacion> filter_empresa_informacion_telefono;
	private int renderizar_empresa_informacion_telefono;

	private boolean agregar_email_empresa;
	private List<EmpresaInformacion> empresa_informacion_mail;
	private List<EmpresaInformacion> filter_empresa_informacion_mail;
	private int renderizar_empresa_informacion_mail;

	private int index_telefono;
	private int index_mail;
	private String statu_email;
	private String statu_phone;

	private boolean error;
	private byte[] image;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public AppBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		this.app();
		Fecha fecha = new Fecha();
		this.mes = fecha.mesActualCadenaESP();
		this.anio = fecha.anioActual();
		empresaInformacion = new EmpresaInformacion();
		this.renderizar_empresa_informacion_telefono = 0;
		this.renderizar_empresa_informacion_mail = 0;
		this.index_telefono = -1;
		this.index_mail = -1;
		this.image = null;
		this.error = false;
		this.empresa = app.getEmpresa();
		this.agregar_email_empresa = false;
		this.agregar_telefono_empresa = false;
	}

	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * Metodo que limpia el filtro de la tabla.
	 */
	public void initTable(String tabla) {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('" + tabla + "').clearFilters();");
	}

	/**
	 * Metodo que cambia el estado a dialogo de formulario.
	 * 
	 * @param estado representa el estado.
	 */
	public void initDialogTelefonoForm(int estado) {
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

	/**
	 * Metodo que cambia el estado a dialogo de formulario mail.
	 * 
	 * @param estado representa el estado.
	 */
	public void initDialogMailForm(int estado) {
		PrimeFaces current = PrimeFaces.current();
		switch (estado) {
		case 1:
			current.executeScript("PF('sofia-dialog-mail-update').show();");
			break;
		case 2:
			current.executeScript("PF('sofia-dialog-mail-update').hide();");
			break;
		default:
			break;
		}
	}

	///////////////////////////////////////////////////////
	// Method Index
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite generar la PK empresa información.
	 * 
	 * @return
	 */
	public int getKey() {
		EmpresaInformacionController e = new EmpresaInformacionController();
		EmpresaInformacion aux = e.ultimoAdd();
		if (aux != null) {
			return aux.getId() + 1;
		}
		return 1;
	}

	/**
	 * Metodo que permite conocer una empresa información.
	 */
	public int index(List<EmpresaInformacion> list, int id) {
		int ix = 0;
		for (EmpresaInformacion e : list) {
			if (e.getId() == id)
				return ix;
			ix++;
		}
		return -1;
	}

	///////////////////////////////////////////////////////
	// Method Phone
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite consultar un telefono
	 */
	public void consultarTelefono() {
		this.aviso = null;
		this.empresaInformacion = new EmpresaInformacion();
		this.index_telefono = -1;
		String id = Face.get("id-telefono");
		if (Convertidor.isCadena(id)) {
			int idInt = Integer.parseInt(id);
			index_telefono = index(this.empresa_informacion_telefono, idInt);
			if (index_telefono >= 0) {
				EmpresaInformacion aux = this.empresa_informacion_telefono.get(index_telefono);
				this.empresaInformacion.setId(aux.getId());
				this.empresaInformacion.setDescripcion(aux.getDescripcion());
				this.empresaInformacion.setEstado(aux.getEstado());
				this.empresaInformacion.setTelefono(aux.getTelefono());
			} else {
				this.aviso = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No se encuentra registrado ningun telefono con ese id " + idInt + ".");
			}
		} else {
			this.aviso = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se recibio el ID del telefono.");
		}
	}

	/**
	 * Metodo que permite activar el formulario de agregar telefono.
	 */
	public void activarAddTelefono() {
		empresaInformacion = new EmpresaInformacion();
		this.agregar_telefono_empresa = (!agregar_telefono_empresa);
	}

	/**
	 * Metodo que permite agregar un telefono.
	 */
	public void addTelefono() {
		if (Convertidor.isCadena(empresaInformacion.getTelefono())) {
			EmpresaInformacionController e = new EmpresaInformacionController();
			String numero = Convertidor.telefono(empresaInformacion.getTelefono());
			EmpresaInformacion aux = e.findByField("telefono", numero);
			if (aux == null) {
				this.empresaInformacion.setEmail(null);
				this.empresaInformacion.setFoto(null);
				this.empresaInformacion.setTelefono(numero);
				this.empresaInformacion.setEstado(true);
				this.empresaInformacion.setId(getKey());
				this.empresaInformacion.setEmpresaBean(app.getEmpresa());
				e.insert(empresaInformacion);
				this.agregar_telefono_empresa = false;
				empresaInformacion = new EmpresaInformacion();
				aviso = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "El telefono se ha registrado.");
				this.renderizar_empresa_informacion_telefono = 0;
				this.initTable("sofia-table-update");
			} else {
				aviso = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El telefono ya esta registrado.");
			}

		} else {
			aviso = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El telefono no puede estar vacio.");
		}
		if (this.aviso != null) {
			FacesContext.getCurrentInstance().addMessage(null, aviso);
		}
	}

	/**
	 * Metodo que permite eliminar un telefono.
	 */
	public void eliminarTelefono() {
		consultarTelefono();
		if (this.aviso == null && index_telefono >= 0) {
			if (this.empresaInformacion != null && this.empresaInformacion.getId() > 0) {
				EmpresaInformacionController e = new EmpresaInformacionController();
				this.empresaInformacion = e.find(this.empresaInformacion.getId());
				if (this.empresaInformacion != null) {
					e.delete(this.empresaInformacion);
					aviso = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							"Se ha eliminado el telefono con ID " + this.empresaInformacion.getId() + ".");
					this.renderizar_empresa_informacion_telefono = 0;
					empresaInformacion = new EmpresaInformacion();
					this.index_telefono = -1;
					this.agregar_telefono_empresa = false;
					this.app();
				} else {
					this.aviso = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							"No existe ningun telefono con el ID " + this.empresaInformacion.getId() + ".");
				}
			} else {
				this.aviso = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se recibio el ID del telefono.");
			}
		}

		if (this.aviso != null) {
			FacesContext.getCurrentInstance().addMessage(null, aviso);
		}
	}

	/**
	 * Metodo que permite cambiar el estado a un telefono.
	 */
	public void estadoTelefono() {
		String id = Face.get("id-telefono");
		if (Convertidor.isCadena(id)) {
			int idInt = Integer.parseInt(id);
			EmpresaInformacionController e = new EmpresaInformacionController();
			EmpresaInformacion aux = e.find(idInt);
			if (aux != null) {
				boolean estado = (!aux.getEstado());
				aux.setEstado(estado);
				e.update(aux);
				aviso = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Se ha cambiado el estado del telefono con ID "
						+ aux.getId() + " a estado " + ((estado) ? " Activo." : " Bloqueado.") + ".");
				this.renderizar_empresa_informacion_telefono = 0;
				this.agregar_telefono_empresa = false;
			} else {
				this.aviso = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No existe ningun telefono con ID " + idInt + ".");
			}
		} else {
			this.aviso = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se recibio el ID del telefono.");
		}
		if (this.aviso != null) {
			FacesContext.getCurrentInstance().addMessage(null, aviso);
		}
	}

	/**
	 * Metodo que permite editar un telefono.
	 */
	public void editarTelefono() {
		this.aviso = null;
		if (this.statu_phone != null) {
			if (this.empresaInformacion != null && this.empresaInformacion.getId() > 0 && this.index_telefono >= 0) {
				EmpresaInformacionController e = new EmpresaInformacionController();
				String numero = Convertidor.telefono(empresaInformacion.getTelefono());
				boolean entrar = false;
				EmpresaInformacion aux = null;
				if (this.statu_phone.equals(numero)) {
					entrar = true;
				} else {
					aux = e.findByField("telefono", numero);
					if (aux != null) {
						entrar = true;
					}
				}
				if (entrar) {
					this.empresaInformacion.setEmpresaBean(this.empresa);
					this.empresaInformacion.setTelefono(numero);
					e.update(empresaInformacion);
					aviso = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							"Se ha actualizado el telefono con ID " + this.empresaInformacion.getId() + ".");
					this.empresa_informacion_telefono.set(this.index_telefono, empresaInformacion);
					empresaInformacion = new EmpresaInformacion();
					initDialogTelefonoForm(2);
					this.index_telefono = -1;
					this.agregar_telefono_empresa = false;
					this.statu_phone = null;
				} else {
					aviso = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El telefono ya esta registrado.");
				}
			} else {
				aviso = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El telefono no puede estar vacio.");
			}
		} else {
			aviso = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No has seleccionado ningun telefono.");
		}
		if (aviso != null) {
			FacesContext.getCurrentInstance().addMessage(null, aviso);
		}
	}

	///////////////////////////////////////////////////////
	// Method Email
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite consultar un email.
	 */
	public void consultarMail() {
		this.aviso = null;
		this.empresaInformacion = new EmpresaInformacion();
		this.index_mail = -1;
		String id = Face.get("id-mail");
		if (Convertidor.isCadena(id)) {
			int idInt = Integer.parseInt(id);
			index_mail = index(empresa_informacion_mail, idInt);
			if (index_mail >= 0) {
				EmpresaInformacion aux = this.empresa_informacion_mail.get(index_mail);
				this.empresaInformacion.setId(aux.getId());
				this.empresaInformacion.setDescripcion(aux.getDescripcion());
				this.empresaInformacion.setEstado(aux.getEstado());
				this.empresaInformacion.setEmail(aux.getEmail());
			} else {
				this.aviso = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						"No se encuentra registrado ningun correo con ese id " + idInt + ".");
			}
		} else {
			this.aviso = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se recibio el ID del correo.");
		}
	}

	/**
	 * Metodo que permite activar el formulario de agregar email.
	 */
	public void activarAddEmail() {
		this.empresaInformacion = new EmpresaInformacion();
		this.agregar_email_empresa = (!agregar_email_empresa);
	}

	/**
	 * Metodo que permite agregar un email.
	 */
	public void addMail() {
		if (Convertidor.isCadena(empresaInformacion.getEmail())) {
			EmpresaInformacionController e = new EmpresaInformacionController();
			EmpresaInformacion aux = e.findByField("email", empresaInformacion.getEmail());
			if (aux == null) {
				this.empresaInformacion.setTelefono(null);
				this.empresaInformacion.setFoto(null);
				this.empresaInformacion.setEstado(true);
				this.empresaInformacion.setId(getKey());
				this.empresaInformacion.setEmpresaBean(app.getEmpresa());
				e.insert(empresaInformacion);
				empresaInformacion = new EmpresaInformacion();
				aviso = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "El correo se ha registrado exitosamente.");
				this.renderizar_empresa_informacion_mail = 0;
				this.initTable("sofia-table-mail-update");
				this.agregar_email_empresa = false;
			} else {
				aviso = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Este correo ya esta registrado.");
			}
		} else {
			aviso = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El correo no puede estar vacio.");
		}
		FacesContext.getCurrentInstance().addMessage(null, aviso);
	}

	/**
	 * Metodo que permite eliminar un telefono.
	 */
	public void eliminarEmail() {
		consultarMail();
		if (this.aviso == null && index_mail >= 0) {
			if (this.empresaInformacion != null && this.empresaInformacion.getId() > 0) {
				EmpresaInformacionController e = new EmpresaInformacionController();
				e.delete(this.empresaInformacion);
				aviso = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha eliminado el email con ID " + this.empresaInformacion.getId() + ".");
				this.empresa_informacion_mail.remove(this.index_mail);
				empresaInformacion = new EmpresaInformacion();
				this.index_mail = -1;
				this.agregar_email_empresa = false;
				this.app();
			} else {
				this.aviso = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se recibio el ID del email.");
			}
		}

		if (this.aviso != null) {
			FacesContext.getCurrentInstance().addMessage(null, aviso);
		}
	}

	/**
	 * Metodo que permite cambiar el estado a un email.
	 */
	public void estadoMail() {
		consultarMail();
		if (this.aviso == null && index_mail >= 0) {
			if (this.empresaInformacion != null && this.empresaInformacion.getId() > 0) {
				EmpresaInformacionController e = new EmpresaInformacionController();
				boolean estado = (!this.empresaInformacion.getEstado());
				this.empresaInformacion.setEstado(estado);
				e.update(this.empresaInformacion);
				aviso = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						"Se ha cambiado el estado del correo con ID " + this.empresaInformacion.getId() + " a estado "
								+ ((estado) ? " Activo." : " Bloqueado.") + ".");
				this.empresa_informacion_mail.set(index_mail, this.empresaInformacion);
				empresaInformacion = new EmpresaInformacion();
				this.index_mail = -1;
				this.agregar_email_empresa = false;
			} else {
				this.aviso = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se recibio el ID del email.");
			}
		}
		FacesContext.getCurrentInstance().addMessage(null, aviso);
	}

	/**
	 * Metodo que permite editar un email.
	 */
	public void editarMail() {
		this.aviso = null;
		if (this.statu_email != null) {
			if (this.empresaInformacion != null && this.empresaInformacion.getId() > 0 && this.index_mail >= 0) {
				EmpresaInformacionController e = new EmpresaInformacionController();
				boolean entrar = false;

				EmpresaInformacion aux = null;
				if (this.statu_email.equals(empresaInformacion.getEmail())) {
					entrar = true;
				} else {
					aux = e.findByField("email", empresaInformacion.getEmail());
					if (aux != null) {
						entrar = true;
					}
				}
				if (entrar) {
					empresaInformacion.setEmpresaBean(app.getEmpresa());
					e.update(empresaInformacion);
					aviso = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							"Se ha actualizado el correo con ID " + this.empresaInformacion.getId() + ".");
					this.empresa_informacion_mail.set(this.index_mail, empresaInformacion);
					empresaInformacion = new EmpresaInformacion();
					initDialogMailForm(2);
					this.index_mail = -1;
					this.statu_email = null;
				} else {
					aviso = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El correo ya esta registrado.");
				}
			} else {
				aviso = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El correo no puede estar vacio.");
			}
		} else {
			aviso = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No has seleccionado ningun correo.");
		}
		this.renderizar_empresa_informacion_mail = 0;
		if (this.aviso != null) {
			FacesContext.getCurrentInstance().addMessage(null, aviso);
		}
	}

	///////////////////////////////////////////////////////
	// Validator
	///////////////////////////////////////////////////////
	/**
	 * 
	 * @return
	 */
	public FacesMessage validarCamposEmpresa() {
		FacesMessage aux = null;
		return aux;
	}

	///////////////////////////////////////////////////////
	// Statu
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite abrir el dialogo telefono.
	 */
	public void statuEditarTelefono() {
		this.index_telefono = -1;
		this.statu_phone = null;
		consultarTelefono();
		if (this.aviso == null && index_telefono >= 0) {
			this.statu_phone = Convertidor.telefono(this.empresaInformacion.getTelefono());
			this.initDialogTelefonoForm(1);
		} else {
			FacesContext.getCurrentInstance().addMessage(null, aviso);
		}
	}

	/**
	 * Metodo que permite abrir el dialogo de email.
	 */
	public void statuEditarMail() {
		this.index_mail = -1;
		this.statu_email = null;
		consultarMail();
		if (this.aviso == null && index_mail >= 0) {
			this.statu_email = this.empresaInformacion.getEmail();
			this.initDialogMailForm(1);
		} else {
			FacesContext.getCurrentInstance().addMessage(null, aviso);
		}
	}

	///////////////////////////////////////////////////////
	// Method Company
	///////////////////////////////////////////////////////
	/**
	 * Metodo que trae la información de la empresa.
	 */
	public void app() {
		EmpresaController eDao = new EmpresaController();
		EmpresaInformacionController iDao = new EmpresaInformacionController();
		Empresa empresa = eDao.find(this.NIT);
		if (empresa != null) {
			List<EmpresaInformacion> list = iDao.findByFieldList("empresaBean", empresa);
			this.app = new App(emails(list), telefonos(list), empresa, carrusel(list));
		}
	}

	/**
	 * Metodo que permite editar la información empresa.
	 */
	public void editarEmpresa() {
		this.aviso = validarCamposEmpresa();
		if (aviso == null) {
			if (this.empresa != null && Convertidor.isCadena(this.empresa.getNit())) {
				EmpresaController e = new EmpresaController();
				boolean aux = e.existe(empresa.getNit(), empresa.getNombre());
				if (!aux || empresa.getNit().equals(app.getEmpresa().getNit())) {
					if (image != null) {
						empresa.setLogo(image);
						image = null;
					}
					e.update(empresa);
					aviso = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							"Se ha actualizado la informacion de la empresa con nit " + this.empresa.getNit() + ".");
					this.app();
					this.empresa = this.app.getEmpresa();
				} else {
					aviso = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El nit o el nombre ya existe.");
				}
			} else {
				aviso = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El Nit no puede estar vacio.");
			}
		}
		if (this.aviso != null) {
			FacesContext.getCurrentInstance().addMessage(null, aviso);
		}
	}

	/**
	 * Metodo que permite traer todos los telefono de la empresa.
	 * 
	 * @param lista con información empresa.
	 * @return la lista resultante.
	 */
	public List<EmpresaInformacion> telefonos(List<EmpresaInformacion> list) {
		List<EmpresaInformacion> telefonos = new ArrayList<EmpresaInformacion>();
		for (EmpresaInformacion i : list) {
			if (Convertidor.isCadena(i.getTelefono())) {
				telefonos.add(i);
			}
		}
		return telefonos;
	}

	/**
	 * Metodo que permite traer todos los emails de la empresa.
	 * 
	 * @param lista con información empresa.
	 * @return la lista resultante.
	 */
	public List<EmpresaInformacion> emails(List<EmpresaInformacion> list) {
		List<EmpresaInformacion> telefonos = new ArrayList<EmpresaInformacion>();
		for (EmpresaInformacion i : list) {
			if (Convertidor.isCadena(i.getEmail())) {
				telefonos.add(i);
			}
		}
		return telefonos;
	}

	/**
	 * Metodo que permite traer todas las imagenes de la empresa.
	 * 
	 * @param lista con información empresa.
	 * @return la lista resultante.
	 */
	public List<EmpresaInformacion> carrusel(List<EmpresaInformacion> list) {
		List<EmpresaInformacion> telefonos = new ArrayList<EmpresaInformacion>();
		for (EmpresaInformacion i : list) {
			if (Convertidor.isVector(i.getFoto())) {
				telefonos.add(i);
			}
		}
		return telefonos;
	}

	///////////////////////////////////////////////////////
	// Renderizar
	///////////////////////////////////////////////////////
	/**
	 * Renderizando la tabla empresa informacion telefono.
	 * 
	 * @return una lista nueva.
	 */
	public List<EmpresaInformacion> getEmpresa_informacion_telefono() {
		if (this.renderizar_empresa_informacion_telefono == 0) {
			EmpresaInformacionController dao = new EmpresaInformacionController();
			this.empresa_informacion_telefono = dao.telefonos();
			this.renderizar_empresa_informacion_telefono = 1;
		}
		return empresa_informacion_telefono;
	}

	/**
	 * Renderizando la tabla empresa informacion telefono.
	 * 
	 * @return una lista nueva.
	 */
	public List<EmpresaInformacion> getEmpresa_informacion_mail() {
		if (this.renderizar_empresa_informacion_mail == 0) {
			EmpresaInformacionController dao = new EmpresaInformacionController();
			this.empresa_informacion_mail = dao.mails();
			this.renderizar_empresa_informacion_mail = 1;
		}
		return empresa_informacion_mail;
	}

	///////////////////////////////////////////////////////
	// Method Image
	///////////////////////////////////////////////////////
	/**
	 * Metodo que prepara la imagen.
	 * 
	 * @param event representa la imagen.
	 */
	public void uploadImage(FileUploadEvent event) {
		FacesContext.getCurrentInstance().addMessage(null, uploadImage(event.getFile()));
	}

	/**
	 * Metodo que prepara la imagen.
	 * 
	 * @param file representa la imagen.
	 */
	public FacesMessage uploadImage(UploadedFile file) {
		this.error = true;
		FacesMessage message = validarArchivo(file, "([^\\s]+(\\.(?i)(jpg|jpeg|png|PNG|JPG|JPEG))$)", 100000, "imagen");
		if (message == null) {

			boolean cambiar = false;
			byte aux[] = null;
			if (this.image != null) {
				cambiar = true;
				aux = this.image;
			}

			this.image = null;
			try {
				this.image = this.leer(file.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (this.image != null && cambiar) {
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
						file.getFileName() + ", se ha cambiado la foto.");
				this.error = false;
			} else if (this.image != null && !cambiar) {
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
						file.getFileName() + ", foto valida (tamaño " + this.image.length + ").");
				this.error = false;
			} else if (this.image == null && cambiar) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "No se ha cambiado la foto.");
				if (aux != null && aux.length > 0) {
					FacesContext.getCurrentInstance().addMessage(null, message);
					message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Se ha dejado la foto anterior.");
					this.image = aux;
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "No has seleccionado ninguna imagen.");
			}
		}
		return message;
	}

	/**
	 * Metodo que permite validar un archivo.
	 * 
	 * @param file       representa el archivo.
	 * @param expression representa la expresión.
	 * @param size       representa el tamaño maximo.
	 * @param type       representa el tipo de archivo.
	 * @return el error obtenido.
	 */
	public FacesMessage validarArchivo(UploadedFile file, String expression, long size, String type) {
		FacesMessage message = null;
		if (file != null) {
			if ((file.getFileName().matches(expression))) {
				if (file.getSize() <= size) {
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "La " + type + " "
							+ file.getFileName() + " es demasiado grande el tamaño maximo es " + size + ".");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
						"El archivo " + file.getFileName() + " no es una imagen.");
			}
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No has seleccionado ninguna imagen.");
		}
		return message;
	}

	/**
	 * Metodo que permite leer una image.
	 * 
	 * @param input representa la image.
	 * @return la imagen en bytes.
	 * @throws IOException representa el error.
	 */

	public byte[] leer(InputStream input) throws IOException {
		byte[] byteArray = null;
		InputStream is = input;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[1024];
		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		buffer.flush();
		byteArray = buffer.toByteArray();
		return byteArray;
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////
	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNIT() {
		return NIT;
	}

	public EmpresaInformacion getEmpresaInformacion() {
		return empresaInformacion;
	}

	public void setEmpresaInformacion(EmpresaInformacion empresaInformacion) {
		this.empresaInformacion = empresaInformacion;
	}

	public FacesMessage getAviso() {
		return aviso;
	}

	public void setAviso(FacesMessage aviso) {
		this.aviso = aviso;
	}

	public List<EmpresaInformacion> getFilter_empresa_informacion_telefono() {
		return filter_empresa_informacion_telefono;
	}

	public void setFilter_empresa_informacion_telefono(List<EmpresaInformacion> filter_empresa_informacion_telefono) {
		this.filter_empresa_informacion_telefono = filter_empresa_informacion_telefono;
	}

	public int getRenderizar_empresa_informacion_telefono() {
		return renderizar_empresa_informacion_telefono;
	}

	public void setRenderizar_empresa_informacion_telefono(int renderizar_empresa_informacion_telefono) {
		this.renderizar_empresa_informacion_telefono = renderizar_empresa_informacion_telefono;
	}

	public void setEmpresa_informacion_telefono(List<EmpresaInformacion> empresa_informacion_telefono) {
		this.empresa_informacion_telefono = empresa_informacion_telefono;
	}

	public int getIndex_telefono() {
		return index_telefono;
	}

	public void setIndex_telefono(int index_telefono) {
		this.index_telefono = index_telefono;
	}

	public void setEmpresa_informacion_mail(List<EmpresaInformacion> empresa_informacion_mail) {
		this.empresa_informacion_mail = empresa_informacion_mail;
	}

	public List<EmpresaInformacion> getFilter_empresa_informacion_mail() {
		return filter_empresa_informacion_mail;
	}

	public void setFilter_empresa_informacion_mail(List<EmpresaInformacion> filter_empresa_informacion_mail) {
		this.filter_empresa_informacion_mail = filter_empresa_informacion_mail;
	}

	public int getRenderizar_empresa_informacion_mail() {
		return renderizar_empresa_informacion_mail;
	}

	public void setRenderizar_empresa_informacion_mail(int renderizar_empresa_informacion_mail) {
		this.renderizar_empresa_informacion_mail = renderizar_empresa_informacion_mail;
	}

	public int getIndex_mail() {
		return index_mail;
	}

	public void setIndex_mail(int index_mail) {
		this.index_mail = index_mail;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public boolean isAgregar_telefono_empresa() {
		return agregar_telefono_empresa;
	}

	public void setAgregar_telefono_empresa(boolean agregar_telefono_empresa) {
		this.agregar_telefono_empresa = agregar_telefono_empresa;
	}

	public boolean isAgregar_email_empresa() {
		return agregar_email_empresa;
	}

	public void setAgregar_email_empresa(boolean agregar_email_empresa) {
		this.agregar_email_empresa = agregar_email_empresa;
	}

	public void setNIT(String nIT) {
		NIT = nIT;
	}

	public String getStatu_email() {
		return statu_email;
	}

	public void setStatu_email(String statu_email) {
		this.statu_email = statu_email;
	}

	public String getStatu_phone() {
		return statu_phone;
	}

	public void setStatu_phone(String statu_phone) {
		this.statu_phone = statu_phone;
	}
}
