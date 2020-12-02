package com.controller;

import java.util.List;

import javax.persistence.Query;

import com.model.*;
import com.model.other.Convertidor;
import com.util.*;

/**
 * Implementation SubmenuDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class SubmenuController extends Conexion<Submenu> {

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public SubmenuController() {
		super(Submenu.class);
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@SuppressWarnings({ "unchecked" })
	public Submenu existe(int menu, int submenu) {
		String JPQL= null;
		if(submenu > 0) {
			if(menu == -1) {
				JPQL = "FROM Submenu s WHERE s.id=" + submenu;
			}else if(menu > 0){
				JPQL = "FROM Submenu s WHERE s.menuBean.id=" + menu + " AND s.id=" + submenu;
			}
		}
		if (Convertidor.isCadena(JPQL)) { 
			Query query = getEm().createQuery(JPQL);
			List<Submenu> aux = query.getResultList();
			if (aux != null && aux.size() > 0) {
				return aux.get(0);
			}
		}
		return null;
	}
}