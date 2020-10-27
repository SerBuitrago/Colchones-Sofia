package com.dao;

import java.util.List;

import javax.persistence.Query;

import com.entity.*;
import com.util.*;

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
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer el ultima detalle venta agregada.
	 * @return representa el ultimo detalle venta agregada.
	 */
	@SuppressWarnings("unchecked")
	public DetalleVenta ultimoAdd() {
		Query query = getEm().createQuery("FROM DetalleVenta ORDER BY id DESC");
		List<DetalleVenta> list= query.getResultList();
		if(list != null && list.size() > 0) {
			return list.get(0);
		}else {
			return null;
		}
	}	
}
