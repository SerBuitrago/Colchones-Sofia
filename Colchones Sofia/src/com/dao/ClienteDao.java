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
 * Implementation ClienteDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class ClienteDao extends Conexion<Cliente> implements Interface<Cliente> {

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public ClienteDao() {
		super(Cliente.class);
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////

	/**
	 * Metodo que permite conocer cuales son los clientes que mas compran y la plata
	 * que dejan.
	 * 
	 * @return la lista con el resultado
	 */
	public List<ChartJS> ventasCliente() {
		String jpa = "SELECT COUNT(v.cliente),CONCAT(v.cliente.persona.nombre,' ',v.cliente.persona.apellido)"
				+ ", SUM(v.total)" + " FROM Venta v GROUP BY v.cliente ORDER BY COUNT(v.cliente) DESC";
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
			cc.setTotal(new BigInteger(String.valueOf(tupla[2])));
			c.add(cc);
		}
		return c;
	}
}
