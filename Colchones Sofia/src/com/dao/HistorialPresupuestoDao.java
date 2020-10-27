package com.dao;

import java.util.List;

import javax.persistence.Query;

import com.entity.*;
import com.util.Conexion;

/**
 * Implementation HistorialPresupuestoDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class HistorialPresupuestoDao extends Conexion<HistorialPresupuesto> implements Interface<HistorialPresupuesto> {

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public HistorialPresupuestoDao() {
		super(HistorialPresupuesto.class);
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer el ultimo historial de presupuesto registrado.
	 * 
	 * @return representa el ultimo historial de presupuesto.
	 */
	@SuppressWarnings("unchecked")
	public HistorialPresupuesto ultimoAdd() {
		Query query = getEm().createQuery("FROM HistorialPresupuesto ORDER BY id DESC");
		List<HistorialPresupuesto> list = query.getResultList();
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
}