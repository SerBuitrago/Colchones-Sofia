package com.controller;



import java.util.List;

import javax.persistence.Query;

import com.model.*;
import com.util.*;
/**
 * Implementation EstadoVentaDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */

public class EstadoVentaController extends Conexion<EstadoVenta>{
	
///////////////////////////////////////////////////////
// Builders
///////////////////////////////////////////////////////
public EstadoVentaController() {
super(EstadoVenta.class);
}


@SuppressWarnings({ "unchecked" })
public List<EstadoVenta> consultaProveedor(int id) {
	Query query = getEm().createQuery(
			"FROM EstadoVenta es WHERE es.id_venta = '" + id + "'");
	List<EstadoVenta> list = query.getResultList();
	return list;
}



public static void main (String[]args) {
	
EstadoVentaController e = new EstadoVentaController();
	
List<EstadoVenta> es = e.consultaProveedor(1);

EstadoVenta ess= es.get(0);
	
	
	System.out.println(ess.toString());
	
}


}
