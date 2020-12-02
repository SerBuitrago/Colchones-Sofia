package com.controller;

import java.util.List;

import javax.persistence.Query;

import com.model.*;
import com.util.*;

/**
 * Implementation GarantidaDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class GarantiaController extends Conexion<Garantia>{
	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public GarantiaController() {
		super(Garantia.class);
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings({"unchecked" })
	public Garantia ultimaAdd() {
		Query query = getEm().createQuery("FROM Garantia g ORDER BY g.id DESC");
		List<Garantia> list = query.getResultList();
		return (list != null && list.size() > 0) ? list.get(0) : null;	 
	}
}
