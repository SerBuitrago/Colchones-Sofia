package com.controller;

import java.util.List;

import javax.persistence.Query;

import com.model.Producto;
import com.util.Conexion;

/**
 * Implementation ProductoDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class ProductoController extends Conexion<Producto>{
	
	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public ProductoController() {
		super(Producto.class);
	}
	
	///////////////////////////////////////////////////////
	// Method Report
	///////////////////////////////////////////////////////
	/**
	 * 
	 * @param inicio
	 * @param fin
	 * @return
	 */
	@SuppressWarnings({"unchecked"})
	public List<Producto> consultarProducto(String inicio, String fin) {
		Query query = getEm().createQuery("FROM Producto WHERE fechaCreacion BETWEEN '"+inicio+"' AND '"+fin+"' ORDER BY fechaCreacion");	
		return query.getResultList();
	}
}