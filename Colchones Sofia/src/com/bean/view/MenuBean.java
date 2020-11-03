package com.bean.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.bean.session.*;
import com.dao.*;
import com.entity.*;

/**
 * Implementation AppBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "menu")
@ViewScoped
public class MenuBean {

	private Menu menu;
	private String rol;

	///////////////////////////////////////////////////////
	// Managed Bean
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean session;

	public MenuBean() {

	}

	@PostConstruct
	public void init() {
		this.rol = this.session.getPath();
		this.rol = this.rol.toUpperCase();
	}

	/**
	 * Metodo que permite consultar el menu del usuario logeado.
	 */
	public List<Submenu> initMenu() {
		int id_menu = 1;
		MenuDao dao = new MenuDao();
		this.menu = dao.menuRol(this.rol, id_menu);
		return (this.menu != null) ? this.menu.getSubmenus() : new ArrayList<Submenu>();
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public SessionBean getSession() {
		return session;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}
}
