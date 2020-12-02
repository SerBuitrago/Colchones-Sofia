package com.controller;

import java.util.List;

import javax.persistence.Query;

import com.model.*;
import com.util.*;

/**
 * Implementation EmpresaInformacionDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class EmpresaInformacionController extends Conexion<EmpresaInformacion> {

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public EmpresaInformacionController() {
		super(EmpresaInformacion.class);
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/*
	 * 
	 */
	@SuppressWarnings({ "unchecked"})
	public EmpresaInformacion ultimoAdd() {
		Query query = getEm().createQuery("FROM EmpresaInformacion e ORDER BY e.id DESC");
		List<EmpresaInformacion> list=query.getResultList();
		if(list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	public List<EmpresaInformacion> telefonos(){
		Query query = getEm().createQuery("FROM EmpresaInformacion e WHERE e.telefono IS NOT NULL");
		List<EmpresaInformacion> list = query.getResultList();
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	public List<EmpresaInformacion> mails(){
		Query query = getEm().createQuery("FROM EmpresaInformacion e WHERE e.email IS NOT NULL");
		List<EmpresaInformacion> list = query.getResultList();
		return list;
	}
}