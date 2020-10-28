package com.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import com.entity.*;
import com.entity.other.ChartJS;
import com.util.Conexion;

/**
 * Implementation CompraDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class CompraDao extends Conexion<Compra> implements Interface<Compra> {

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public CompraDao() {
		super(Compra.class);
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer el ultima compra agregada.
	 * 
	 * @return representa el ultima compra agregada.
	 */
	@SuppressWarnings("unchecked")
	public Compra ultimoAdd() {
		Query query = getEm().createQuery("FROM Compra ORDER BY idCompra DESC");
		List<Compra> list = query.getResultList();
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	/**
	 * Metodo que permite conocer las ventas mensuales.
	 * 
	 * @param anio represenat el año a consultar.
	 * @return representa la lista obtenida.
	 */
	public List<ChartJS> compraMes(String anio) {
		String jpa = "SELECT SUM(c.total), MONTHNAME(c.fechaRegistro) FROM Compra c WHERE YEAR(c.fechaRegistro) = '"
				+ anio + "' GROUP BY MONTHNAME(c.fechaRegistro) ORDER BY MONTH(c.fechaRegistro)";
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
		String jpa = "SELECT SUM(c.total), YEAR(c.fechaRegistro) FROM Compra c GROUP BY YEAR(c.fechaRegistro) ORDER BY YEAR(c.fechaRegistro)";
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
		String jpa = "SELECT SUM(c.total), MONTHNAME(c.fechaRegistro) FROM Compra c WHERE MONTHNAME(c.fechaRegistro)='"
				+ mes + "' AND YEAR(c.fechaRegistro) = '" + anio + "' GROUP BY MONTHNAME(c.fechaRegistro)";
		Query query = getEm().createQuery(jpa);
		@SuppressWarnings({ "rawtypes" })
		List result = query.getResultList();
		ChartJS c = new ChartJS();
		if(result.size() == 0) {
			c = null;
		}else {
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
		String jpa = "SELECT SUM(dc.cantidad), dc.detalleProducto.producto.nombre FROM DetalleCompra dc "
				+ "GROUP BY dc.detalleProducto.producto.idProducto ORDER BY SUM(dc.cantidad) DESC";
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
