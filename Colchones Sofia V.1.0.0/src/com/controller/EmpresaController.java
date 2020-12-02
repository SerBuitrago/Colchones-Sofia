package com.controller;

import java.util.List;

import javax.persistence.Query;

import com.model.*;
import com.model.other.*;
import com.util.*;

/**
 * Implementation EmpresaDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class EmpresaController extends Conexion<Empresa>{

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public EmpresaController() {
		super(Empresa.class);
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * 
	 * @param nit
	 * @param nombre
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	public boolean existe(String nit, String nombre) {
		if(Convertidor.isCadena(nit) && Convertidor.isCadena(nombre)) {
			Query query = getEm().createQuery("FROM Empresa e WHERE e.nit='"+nit+"' or e.nombre='"+nombre+"'");
			List<Empresa> aux=query.getResultList();
			if(aux!=null && aux.size()>0){
				return true;
			}
		}
		return false;
	} 
}
