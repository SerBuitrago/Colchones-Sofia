package com.dao;

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
	 * 
	 * @param documento
	 * @return
	 */
	public List<ChartJS> ventasCliente() {
		String jpa = "SELECT COUNT(v.cliente), CONCAT(v.cliente.persona.nombre,v.cliente.persona.apellido)"
				+ " FROM Venta v GROUP BY v.cliente ORDER BY COUNT(v.cliente)";
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

	public static void main(String p[]) {
		ClienteDao dao = new ClienteDao();
		for(ChartJS aux: dao.ventasCliente()) {
			System.out.println(aux);
		}
	}

}
