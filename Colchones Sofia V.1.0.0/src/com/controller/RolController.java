package com.controller;

import java.util.List;

import javax.persistence.Query;

import com.model.*;
import com.util.*;

/**
 * Implementation RolDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class RolController extends Conexion<Rol>{

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public RolController() {
		super(Rol.class);
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer los usuarios logeados.
	 * 
	 * @return representa una lista con los usuarios logeados.
	 */
	@SuppressWarnings({ "unchecked"})
	public List<Rol> rolesLogin() {
		Query query = getEm().createQuery("FROM Rol r WHERE r.estado=true and r.sistema=true ORDER BY r.fechaCreacion");
		List<Rol> list = query.getResultList();
		return list;
	}
}