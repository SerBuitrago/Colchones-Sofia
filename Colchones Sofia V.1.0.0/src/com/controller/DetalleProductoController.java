package com.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import com.model.*;
import com.model.other.ChartJS;
import com.model.other.Convertidor;
import com.util.*;

/**
 * Implementation DetalleProductoDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class DetalleProductoController extends Conexion<DetalleProducto>{

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public DetalleProductoController() {
		super(DetalleProducto.class);
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	public DetalleProducto addUltimo() {
		Query query = getEm().createQuery("FROM DetalleProducto ORDER BY id DESC");
		@SuppressWarnings("unchecked")
		List<DetalleProducto> list = query.getResultList();
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		} 
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ChartJS> colores() {
		Query query = getEm().createQuery("SELECT dp.color, COUNT(dp.color) FROM DetalleProducto dp WHERE dp.productoBean.estado=true AND dp.stock > 0 GROUP BY dp.color");
		@SuppressWarnings("rawtypes")
		List result = query.getResultList();
		List<ChartJS> c = new ArrayList<ChartJS>();
		@SuppressWarnings("rawtypes")
		Iterator res = result.iterator();
		while (res.hasNext()) {
			Object[] tupla = (Object[]) res.next();
			ChartJS cc = new ChartJS();
			cc.setNombre(String.valueOf(tupla[0]));
			cc.setCantidad(Integer.parseInt(String.valueOf(tupla[1])));
			c.add(cc);
		}
		return c;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DetalleProducto> promocion(){
		Query query = getEm().createQuery("FROM DetalleProducto dp WHERE dp.productoBean.estado=true AND dp.stock > 0 AND dp.descuento > 0");
		return query.getResultList();
	}
	
	/**
	 * 
	 * @param color
	 * @param dimension
	 * @return
	 */
	public DetalleProducto existe(String color, String dimension) {
		if(Convertidor.isCadena(color) && Convertidor.isCadena(dimension)) {
			Query query = getEm().createQuery("FROM DetalleProducto dp WHERE dp.color='"+color+"' AND dp.dimension='"+dimension+"'");
			@SuppressWarnings("unchecked")
			List<DetalleProducto> list = query.getResultList();
			if (list != null && list.size() > 0) {
				return list.get(0);
			} 
		}
		return null;
	}
}
