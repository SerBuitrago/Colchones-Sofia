package com.dao;
import com.entity.*;
import com.util.Conexion;

/**
 * Implementation DevolucionGarantiaDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class DevolucionGarantiaDao extends Conexion<DevolucionGarantia> implements Interface<DevolucionGarantia> {

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public DevolucionGarantiaDao() {
		super(DevolucionGarantia.class);
	}
}
