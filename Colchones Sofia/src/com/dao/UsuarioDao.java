package com.dao;

import java.util.List;

import javax.persistence.Query;

import com.entity.*;
import com.entity.other.Convertidor;
import com.util.Conexion;

/**
 * Implementation UsuarioDao.
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
	 * Metodo que permite validar los datos ingresados en el login.
	 * 
	 * @param email representa el email.
	 * @param clave representa la calve.
	 * @param tipo  representa el tipo rol.
	 * @return el usuario encontrado.
	 */
	@SuppressWarnings("unchecked")
	public Usuario ingresar(String email, String clave, String tipo) {
		if (Convertidor.isCadena(email) && Convertidor.isCadena(clave) && Convertidor.isCadena(tipo)) {
			Query query = getEm().createQuery("FROM Usuario u WHERE u.persona.email='" + email + "' AND u.clave='"
					+ clave + "' AND u.rolBean.rol='" + tipo + "'");
			List<Usuario> list = query.getResultList();
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}

	/**
	 * Metodo que permite traer un usuario por su email.
	 * 
	 * @param email representa el email del usuario.
	 * @return el usuario encontrado.
	 */
	@SuppressWarnings("unchecked")
	public Usuario usuario(String email) {
		if (Convertidor.isCadena(email)) {
			Query query = getEm().createQuery("FROM Usuario u WHERE u.persona.email='" + email + "'");
			List<Usuario> list = query.getResultList();
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}

	/**
	 * Metodo que permite conocer los usuarios logeados.
	 * 
	 * @return representa una lista con los usuarios logeados.
	 */
	@SuppressWarnings("unchecked")
	public List<Usuario> logeados() {
		Query query = getEm().createQuery("SELECT u FROM Usuario u WHERE u.sesion=true ORDER BY u.fechaSesion");
		return query.getResultList();
	}
}
