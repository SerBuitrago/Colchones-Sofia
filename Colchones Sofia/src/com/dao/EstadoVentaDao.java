package com.dao;

import com.entity.*;
import com.util.Conexion;

/**
 * Implementation EstadoVentaDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class EstadoVentaDao extends Conexion<EstadoVenta> implements Interface<EstadoVenta> {

	public EstadoVentaDao() {
		super(EstadoVenta.class);
	}
}
