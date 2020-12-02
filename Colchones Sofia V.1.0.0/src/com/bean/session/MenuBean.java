package com.bean.session;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.controller.*;
import com.model.*;

/**
 * Implementation AppBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "menu")
@SessionScoped
public class MenuBean { 

	private Menu menu;
	private String rol;

	private int renderizar;
	private List<Submenu> submenu;
	
	private Menu inicio;
	private int id_menu_inicio=3;
	private List<Submenu> submenu_inicio;
	private int renderizar_submenu_inicio;
	
	///////////////////////////////////////////////////////
	// ManagedBean
	///////////////////////////////////////////////////////
	@ManagedProperty("#{sesion}")
	private SessionBean session;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public MenuBean() {
	}
	
	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		if(this.session != null && this.session.getLogeado() != null) {
			this.rol = this.session.getPath();
			this.rol = this.rol.toUpperCase();
			this.renderizar = 0;
			this.submenu = new ArrayList<Submenu>();
		}else {
			this.renderizar =-1;
		}
		this.submenu_inicio = new ArrayList<Submenu>();
		this.renderizar_submenu_inicio = 0;
	}
	
	/**
	 * Metodo que permite reseterar el menu.
	 */
	public void resetMenu() {
		if(this.renderizar == -1) {
			this.rol = this.session.getPath();
			this.rol = this.rol.toUpperCase();
			this.renderizar = 0;
			this.submenu = new ArrayList<Submenu>();
		}
	}
	
	///////////////////////////////////////////////////////
	// Init
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite consultar el menu del usuario logeado.
	 */
	public List<Submenu> initMenu() {
		resetMenu();
		if(renderizar == 0) {
			this.submenu = new ArrayList<Submenu>();
			int id_menu = (this.rol.equals("ADMINISTRADOR")) ? 1 : 2;
			MenuController dao = new MenuController();
			this.menu = dao.menuRol(this.rol, id_menu);
			this.submenu= this.menu.getSubmenus();
			renderizar = 1;
		}
		return submenu;
	}
	
	/**
	 * Metodo que permite consultar el menu de inicio.
	 */
	public List<Submenu> initMenuInicio(){
		if(this.renderizar_submenu_inicio == 0) {
			this.submenu_inicio = new ArrayList<Submenu>();
			MenuController dao = new MenuController();
			this.inicio = dao.find(id_menu_inicio);
			if(inicio != null && inicio.getEstado()) {
				this.submenu_inicio= this.inicio.getSubmenus();
			}
			this.renderizar_submenu_inicio =1;
		}
		return submenu_inicio;
	}
	
	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
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

	public int getRenderizar() {
		return renderizar;
	}

	public void setRenderizar(int renderizar) {
		this.renderizar = renderizar;
	}

	public List<Submenu> getSubmenu() {
		return submenu;
	}

	public void setSubmenu(List<Submenu> submenu) {
		this.submenu = submenu;
	}

	public Menu getInicio() {
		return inicio;
	}

	public void setInicio(Menu inicio) {
		this.inicio = inicio;
	}

	public void setSubmenu_inicio(List<Submenu> submenu_inicio) {
		this.submenu_inicio = submenu_inicio;
	}

	public int getRenderizar_submenu_inicio() {
		return renderizar_submenu_inicio;
	}

	public void setRenderizar_submenu_inicio(int renderizar_submenu_inicio) {
		this.renderizar_submenu_inicio = renderizar_submenu_inicio;
	}
	
	public List<Submenu> getSubmenu_inicio() {
		return submenu_inicio;
	}

	public int getId_menu_inicio() {
		return id_menu_inicio;
	}

	public void setId_menu_inicio(int id_menu_inicio) {
		this.id_menu_inicio = id_menu_inicio;
	}
}
