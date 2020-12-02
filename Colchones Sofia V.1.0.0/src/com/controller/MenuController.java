package com.controller;

import java.util.ArrayList;
import java.util.List;

import com.model.*;
import com.model.other.Convertidor;
import com.util.*;

/**
 * Implementation MenuDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class MenuController extends Conexion<Menu> {

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	@SuppressWarnings("static-access")
	public MenuController() {
		super(Menu.class);
		em = this.getEm();
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite traer el menu por su rol.
	 * 
	 * @param rol  representa el rol.
	 * @param menu representa el menu a traer.
	 * @return el menu.
	 */
	public Menu menuRol(String rol, int menu) {
		if (Convertidor.isCadena(rol) && menu > 0) {
			MenuController dao = new MenuController();
			Menu m = dao.find(menu);
			if (m.getEstado()) {
				List<Submenu> submenu = new ArrayList<Submenu>();
				for (Submenu sb : m.getSubmenus()) {
					if (sb.getEstado() && permisoSubmenu(rol, sb)) {
						submenu.add(sb);
						SubmenuController sDao = new SubmenuController();
						List<Submenu> items = new ArrayList<Submenu>();
						List<Submenu> aux = sDao.findByFieldList("submenu", sb);
						for (Submenu ssb : aux) {
							if (ssb.getEstado() && permisoSubmenu(rol, ssb)) {
								items.add(ssb);
								List<Submenu> subitems = new ArrayList<Submenu>();
								List<Submenu> aux2 = sDao.findByFieldList("submenu", ssb);
								for (Submenu sssb : aux2) {
									if (sssb.getEstado()) {
										subitems.add(sssb);
									}
								}
								ssb.setSubmenus(subitems);
							}
						}
						sb.setSubmenus(items);
					}
				}
				m.setSubmenus(submenu);
				return m;
			}
		}
		return null;
	}

	/**
	 * Metodo que permite averiguar si un rol tiene permisos en un submenu.
	 * 
	 * @param rol     representa el rol.
	 * @param submenu representa los submenus.
	 * @return true si tiene permiso, false si no.
	 */
	public boolean permisoSubmenu(String rol, Submenu submenu) {
		if (Convertidor.isCadena(rol) && submenu != null) {
			for (Rol r : submenu.getRols()) {
				if (r.getRol().equals(rol)) {
					return true;
				}
			}
		}
		return false;
	}
}