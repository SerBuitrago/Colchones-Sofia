package com.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import com.model.*;
import com.model.other.*;
import com.util.*;

/**
 * Implementation CompraDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class CompraController extends Conexion<Compra> {

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public CompraController() {
		super(Compra.class);
	}

	///////////////////////////////////////////////////////
	// Method Report
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite filtrar las compras en un rango de fechas.
	 * 
	 * @param inicio representa la fecha inicio.
	 * @param fin    representa la fecha fin.
	 * @return la lista obtenida.
	 */
	@SuppressWarnings("unchecked")
	public List<Compra> consultarCompra(String inicio, String fin) {
		Query query = getEm().createQuery(
				"FROM Compra WHERE fechaCreacion BETWEEN '" + inicio + "' AND '" + fin + "' ORDER BY fechaCreacion");
		return query.getResultList();
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer la ultima compra.
	 * 
	 * @return representa la ultima compra agregada.
	 */
	public Compra ultimaAdd() {
		Query query = getEm().createQuery("FROM Compra c ORDER BY c.id DESC");
		@SuppressWarnings("unchecked")
		List<Compra> result = query.getResultList();
		return (result != null && result.size() > 0) ? result.get(0) : null;
	}

	/**
	 * Metodo que permite conocer las ventas mensuales.
	 * 
	 * @param anio represenat el año a consultar.
	 * @return representa la lista obtenida.
	 */
	public List<ChartJS> compraMes(String anio) {
		String jpa = "SELECT SUM(c.total), MONTHNAME(c.fechaCreacion) FROM Compra c WHERE YEAR(c.fechaCreacion) = '"
				+ anio + "' GROUP BY MONTHNAME(c.fechaCreacion) ORDER BY MONTH(c.fechaCreacion)";
		Query query = getEm().createQuery(jpa);
		@SuppressWarnings("rawtypes")
		List result = query.getResultList();
		List<ChartJS> c = new ArrayList<ChartJS>();
		@SuppressWarnings("rawtypes")
		Iterator res = result.iterator();
		while (res.hasNext()) {
			Object[] tupla = (Object[]) res.next();
			ChartJS cc = new ChartJS();
			cc.setTotal(new BigInteger(String.valueOf(tupla[0])));
			cc.setNombre(String.valueOf(tupla[1]));
			c.add(cc);
		}
		return c;
	}

	/**
	 * Metodo que permite conocer las compras anuales.
	 * 
	 * @param anio represenat el año a consultar.
	 * @return representa la lista obtenida.
	 */
	public List<ChartJS> comprasAnuales() {
		String jpa = "SELECT SUM(c.total), YEAR(c.fechaCreacion) FROM Compra c GROUP BY YEAR(c.fechaCreacion) ORDER BY YEAR(c.fechaCreacion)";
		Query query = getEm().createQuery(jpa);
		@SuppressWarnings("rawtypes")
		List result = query.getResultList();
		List<ChartJS> c = new ArrayList<ChartJS>();
		@SuppressWarnings("rawtypes")
		Iterator res = result.iterator();
		while (res.hasNext()) {
			Object[] tupla = (Object[]) res.next();
			ChartJS cc = new ChartJS();
			cc.setTotal(new BigInteger(String.valueOf(tupla[0])));
			cc.setNombre(String.valueOf(tupla[1]));
			c.add(cc);
		}
		return c;
	}

	/**
	 * Metodo que permite conocer las compras mensuales.
	 * 
	 * @param anio represenat el año a consultar.
	 * @return representa la lista obtenida.
	 */
	public ChartJS comprasMensual(String mes, String anio) {
		String jpa = "SELECT SUM(c.total), MONTHNAME(c.fechaCreacion) FROM Compra c WHERE MONTHNAME(c.fechaCreacion)='"
				+ mes + "' AND YEAR(c.fechaCreacion) = '" + anio + "' GROUP BY MONTHNAME(c.fechaCreacion)";
		Query query = getEm().createQuery(jpa);
		@SuppressWarnings({ "rawtypes" })
		List result = query.getResultList();
		ChartJS c = new ChartJS();
		if (result.size() == 0) {
			c = null;
		} else {
			@SuppressWarnings("rawtypes")
			Iterator res = result.iterator();
			while (res.hasNext()) {
				Object[] tupla = (Object[]) res.next();
				c.setTotal(new BigInteger(String.valueOf(tupla[0])));
				c.setNombre(String.valueOf(tupla[1]));
			}
		}
		return c;
	}

	/**
	 * Metodo que permite conocer conocer la cantidad de unidades compradas de un
	 * producto.
	 * 
	 * @return representa la cantidad de productos.
	 */
	public List<ChartJS> productos() {
		String jpa = "SELECT SUM(dc.cantidad), dc.detalleProducto.productoBean.nombre FROM DetalleCompraVenta dc WHERE dc.compraBean IS NOT NULL "
				+ "GROUP BY dc.detalleProducto.productoBean.id ORDER BY SUM(dc.cantidad) DESC";
		Query query = getEm().createQuery(jpa);
		@SuppressWarnings("rawtypes")
		List result = query.getResultList();
		List<ChartJS> c = new ArrayList<ChartJS>();
		@SuppressWarnings("rawtypes")
		Iterator res = result.iterator();
		while (res.hasNext()) {
			Object[] tupla = (Object[]) res.next();
			ChartJS cc = new ChartJS();
			cc.setCantidad(Integer.parseInt((String.valueOf(tupla[0]))));
			cc.setNombre(String.valueOf(tupla[1]));
			c.add(cc);
		}
		return c;
	}
}
