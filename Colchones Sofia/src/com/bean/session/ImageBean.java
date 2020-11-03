package com.bean.session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.dao.*;
import com.entity.*;
import com.entity.other.*;
import com.util.*;

/**
 * Implementation ImageBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "image")
@SessionScoped
public class ImageBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private FacesMessage mensaje;

	private StreamedContent logo;
	private StreamedContent informacion_empresa;
	private StreamedContent persona;
	
	private byte image[];
	private boolean error;

	///////////////////////////////////////////////////////
	// Managed Bean
	///////////////////////////////////////////////////////
	@ManagedProperty("#{app}")
	private AppBean app;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public ImageBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		this.image = null;
		this.error = true;
	}

	///////////////////////////////////////////////////////
	// Generic
	///////////////////////////////////////////////////////
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
	
	/**
	 * Metodo que permite convertir un array byte en una foto.
	 * 
	 * @param image representa la foto en el array.
	 * @return la imagen obtenida.
	 * @throws IOException el error obtenido.
	 */
	@SuppressWarnings("deprecation")
	public StreamedContent image(byte[] image) throws IOException {
		StreamedContent stream = null;
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE)
			stream = new DefaultStreamedContent();
		else {
			if (image != null) {
				stream = new DefaultStreamedContent(new ByteArrayInputStream(image), "image/png");
			} else {
				stream = null;
			}
		}
		return stream;
	}

	///////////////////////////////////////////////////////
	// Upload
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
		FacesMessage message = validarArchivo(file, "([^\\s]+(\\.(?i)(jpg|jpeg|png|PNG|JPG|JPEG))$)", 100000,
				"imagen");
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
	
	///////////////////////////////////////////////////////
	// Validator
	///////////////////////////////////////////////////////
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

	///////////////////////////////////////////////////////
	// Renderizar
	///////////////////////////////////////////////////////
	/**
	 * Metodo que obtiene el logo de la empresa.
	 * 
	 * @return retorna el logo de la empresa.
	 * @throws IOException representa el logo de la empresa.
	 */
	public StreamedContent getLogo() throws IOException {
		if (this.app != null && this.app.getApp() != null) {
			Empresa e = null;
			if (this.app.getApp().getEmpresa() != null) {
				e = this.app.getApp().getEmpresa();
				this.logo = image(e.getLogo());
			} else {
				this.logo = null;
			}
		} else {
			this.logo = null;
		}
		return this.logo;
	}

	/**
	 * Metodo que retorna las fotos del carrosusel.
	 * 
	 * @return representa la foto del carrosusel.
	 */
	@SuppressWarnings("deprecation")
	public StreamedContent getInformacion_empresa() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE)
			this.informacion_empresa = new DefaultStreamedContent();
		else {
			int index = Face.getInt("id-informacion-empresa");
			if (index > 0) {
				EmpresaInformacionDao dao = new EmpresaInformacionDao();
				EmpresaInformacion i = dao.find(index);
				if (i != null) {
					this.informacion_empresa = new DefaultStreamedContent(new ByteArrayInputStream(i.getFoto()),
							"image/png");
				} else {
					this.informacion_empresa = null;
				}
			} else {
				this.informacion_empresa = null;
			}
		}
		return this.informacion_empresa;
	}

	/**
	 * Metodo que permite traer la imagen de una persona.
	 * 
	 * @return representa la imagen de la persona.
	 */
	@SuppressWarnings("deprecation")
	public StreamedContent getPersona() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE)
			this.persona = new DefaultStreamedContent();
		else {
			String id = Face.get("documento-persona");
			if (Convertidor.isCadena(id)) {
				PersonaDao dao = new PersonaDao();
				Persona p = dao.find(id);
				if (p != null && p.getFoto() != null) {
					this.persona = new DefaultStreamedContent(new ByteArrayInputStream(p.getFoto()), "image/png");
				} else {
					this.persona = null;
				}
			}
		}
		return persona;
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////
	public FacesMessage getMensaje() {
		return mensaje;
	}

	public void setMensaje(FacesMessage mensaje) {
		this.mensaje = mensaje;
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

	public void setLogo(StreamedContent logo) {
		this.logo = logo;
	}

	public void setInformacion_empresa(StreamedContent informacion_empresa) {
		this.informacion_empresa = informacion_empresa;
	}

	public void setPersona(StreamedContent persona) {
		this.persona = persona;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}
}
