package com.bean.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import com.dao.*;
import com.entity.*;

/**
 * Implementation SelectionBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "selection")
@RequestScoped
public class SelectionBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<SelectItem> roles;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public SelectionBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		this.roles = null;
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite traer todos los tipos de roles de la empresa.
	 * 
	 * @return una lista con los roles de la empresa.
	 */
	public List<SelectItem> getRoles() {
		this.roles = new ArrayList<SelectItem>();
		RolDao dao = new RolDao();
		List<Rol> roles = dao.rolesLogin();
		SelectItemGroup s = new SelectItemGroup("No se encontro ningun tipo usuario.");
		if (roles != null && roles.size() > 0) {
			s = new SelectItemGroup("Tipo Usuario");
			SelectItem[] items = new SelectItem[roles.size()];
			for (int i = 0; i < roles.size(); i++) {
				items[i] = new SelectItem(String.valueOf(roles.get(i).getRol()), String.valueOf(roles.get(i).getRol()));
			}
			s.setSelectItems(items);
		}
		this.roles.add(s);
		return this.roles;
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setRoles(List<SelectItem> roles) {
		this.roles = roles;
	}
}
