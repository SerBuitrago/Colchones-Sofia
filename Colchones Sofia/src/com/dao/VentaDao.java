package com.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import com.entity.*;
import com.entity.other.*;
import com.util.Conexion;

/**
 * Implementation VentaDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class VentaDao extends Conexion<Venta> implements Interface<Venta> {

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public VentaDao() {
		super(Venta.class);
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer el ultima venta agregada.
	 * 
	 * @return representa el ultimo venta agregada.
	 */
	@SuppressWarnings("unchecked")
	public Venta ultimoAdd() {
		Query query = getEm().createQuery("FROM Venta ORDER BY idVenta DESC");
		List<Venta> list = query.getResultList();
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
	public List<ChartJS> ventasMes(String anio) {
		String jpa = "SELECT SUM(v.total), MONTHNAME(v.fechaRegistro) FROM Venta v WHERE YEAR(v.fechaRegistro) = '"
				+ anio + "' GROUP BY MONTHNAME(v.fechaRegistro) ORDER BY MONTH(v.fechaRegistro)";
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
	 * Metodo que permite conocer las ventas anuales.
	 * 
	 * @param anio represenat el año a consultar.
	 * @return representa la lista obtenida.
	 */
	public List<ChartJS> ventasAnuales() {
		String jpa = "SELECT SUM(v.total), YEAR(v.fechaRegistro) FROM Venta v GROUP BY YEAR(v.fechaRegistro) ORDER BY YEAR(v.fechaRegistro)";
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
	 * Metodo que permite conocer las ventas mensuales.
	 * 
	 * @param anio represenat el año a consultar.
	 * @return representa la lista obtenida.
	 */
	public ChartJS ventasMensual(String mes, String anio) {
		String jpa = "SELECT SUM(v.total), MONTHNAME(v.fechaRegistro) FROM Venta v WHERE MONTHNAME(v.fechaRegistro)='"
				+ mes + "' AND YEAR(v.fechaRegistro) = '" + anio + "' GROUP BY MONTHNAME(v.fechaRegistro)";
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
	 * Metodo que permite conocer las ventas mensuales.
	 * 
	 * @param anio represenat el año a consultar.
	 * @return representa la lista obtenida.
	 */
	@SuppressWarnings("unchecked")
	public List<Venta> ventaMensual(String mes, String anio) {
		String jpa = "FROM Venta v WHERE MONTHNAME(v.fechaRegistro)='"+ mes + "' AND YEAR(v.fechaRegistro) = '" + anio + "'";
		Query query = getEm().createQuery(jpa);
		return query.getResultList();
	} 

	/**
	 * Metodo que permite conocer conocer la cantidad de unidades vendidas de un
	 * producto.
	 * 
	 * @return representa la cantidad de productos.
	 */
	public List<ChartJS> productos() {
		String jpa = "SELECT SUM(dv.cantidad), dv.detalleProducto.producto.nombre FROM DetalleVenta dv "
				+ "GROUP BY dv.detalleProducto.producto.idProducto ORDER BY SUM(dv.cantidad) DESC";
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
