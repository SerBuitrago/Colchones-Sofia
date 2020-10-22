package com.dao;

import com.entity.*;
import com.util.Conexion;

/**
 * Implementation DetalleVentaDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class DetalleVentaDao extends Conexion<DetalleVenta> implements Interface<DetalleVenta> {

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public DetalleVentaDao() {
		super(DetalleVenta.class);
	}
}
