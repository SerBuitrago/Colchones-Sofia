package com.controller;

import java.util.List;

import javax.persistence.Query;

import com.model.*;
import com.util.*;

/**
 * Implementation DetalleCompraVentaDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class DetalleCompraVentaController extends Conexion<DetalleCompraVenta>{

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public DetalleCompraVentaController() {
		super(DetalleCompraVenta.class);
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer la ultima detalle compra o venta.
	 * @return representa la detalle compra o venta compra agregada.
	 */
	@SuppressWarnings({"unchecked" })
	public DetalleCompraVenta ultimoAdd() {
		Query query = getEm().createQuery("FROM DetalleCompraVenta c ORDER BY c.id DESC");
		List<DetalleCompraVenta> result = query.getResultList();
		return (result != null && result.size() > 0) ? result.get(0) :null; 
	}
}