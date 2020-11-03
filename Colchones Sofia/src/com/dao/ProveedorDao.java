package com.dao;

import javax.persistence.Query;

import com.entity.Proveedor;
import com.util.Conexion;

public class ProveedorDao extends Conexion<Proveedor> implements Interface<Proveedor> {

	public ProveedorDao() {
		super(Proveedor.class);
	}

	/**
	 * 
	 * @param proveedor
	 * @param producto
	 * @return
	 */
	@SuppressWarnings("static-access")
	public boolean insertProveedorProducto(String proveedor, String producto) {
		boolean valid = false;
		try {
			this.getEm().getTransaction().begin();
			String queryString2 = "INSERT INTO proveedor_producto(proveedor, producto) values (?,?)";
			Query query2 = getEm().createNativeQuery(queryString2);
			
			query2.setParameter(1, proveedor);
			query2.setParameter(2, producto);
			query2.executeUpdate();
			
			getEm().getTransaction().commit();
			getEm().close();
			return true;
		} catch (Exception e) {
			return valid;
		}
	}
}
