package com.bean.session;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.entity.*;
import com.dao.*;

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
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que trae la información de la empresa.
	 */
	public void app() {
		GlobalDao gDao = new GlobalDao();
		TelefonoDao tDao = new TelefonoDao();
		EmailDao eDao = new EmailDao();
		CarruselDao dao = new CarruselDao();
		// Information
		List<Global> list = gDao.list();
		Global global = new Global();
		if (list.size() > 0) {
			global = gDao.list().get(0);
		}
		List<Telefono> telefonos = tDao.list();
		List<Email> email = eDao.list();
		List<Carrusel> informacion = dao.list();
		this.app = new App(email, telefonos, global);
		this.app.setCarrousel(informacion);
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
