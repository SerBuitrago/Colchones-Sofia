package com.dao;

import java.util.List;

import javax.persistence.Query;

import com.entity.*;
import com.util.Conexion;

/**
 * Implementation EmailDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class UsuarioDao extends Conexion<Usuario> implements Interface<Usuario> {
	
	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public UsuarioDao() {
		super(Usuario.class);
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer los usuarios logeados.
	 * @return representa una lista con los usuarios logeados.
	 */
	@SuppressWarnings("unchecked")
	public List<Usuario> sesion(){
		Query query = getEm().createQuery("SELECT u FROM Usuario u WHERE u.session=true ORDER BY u.fechaSesion");
		return query.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Usuario> consultarAsistente() {
		Query query = getEm().createQuery("FROM Usuario u WHERE u.rol.nombre='Asistente Administrativo'");
		return query.getResultList();
	}
	
}
