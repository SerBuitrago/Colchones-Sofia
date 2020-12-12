package com.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import com.model.*;
import com.model.other.ChartJS;
import com.model.other.Convertidor;
import com.model.report.VendedorReport;
import com.util.Conexion;

/**
 * Implementation UsuarioDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class UsuarioController extends Conexion<Usuario>{

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public UsuarioController() {
		super(Usuario.class);
	}
	
	///////////////////////////////////////////////////////
	// Method Report
	///////////////////////////////////////////////////////
	/**
	 * 
	 * @param inicio
	 * @param fin
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	public List<Usuario> consultarVendedor(String inicio, String fin) {
		String vendedor = "VENDEDOR";
		Query query = getEm().createQuery("FROM Usuario WHERE rol = '" + vendedor + "' AND fechaCreacion BETWEEN '"
				+ inicio + "' AND '" + fin + "' ORDER BY fechaCreacion");
		return query.getResultList();
	}
	
	/**
	 * 
	 * @param inicio
	 * @param fin
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	public List<Usuario> consultarCliente(String inicio, String fin) {
		String cliente = "CLIENTE";
		Query query = getEm().createQuery("FROM Usuario WHERE rol = '" + cliente + "' AND fechaCreacion BETWEEN '"
				+ inicio + "' AND '" + fin + "' ORDER BY fechaCreacion");
		return query.getResultList();
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite consultar el ultimo usuario agregado en la bd.
	 * 
	 * @return representa el usuario.
	 */
	@SuppressWarnings({ "unchecked"})
	public Usuario ultimoAdd() {
		Query query = getEm().createQuery("FROM Usuario u ORDER BY u.fechaCreacion DESC");
		List<Usuario> list = query.getResultList();
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Metodo que permite validar los datos ingresados en el login.
	 * 
	 * @param email representa el email.
	 * @param clave representa la calve.
	 * @param tipo  representa el tipo rol.
	 * @return el usuario encontrado.
	 */
	@SuppressWarnings({ "unchecked"})
	public Usuario ingresar(String email, String clave, String tipo) {
		if (Convertidor.isCadena(email) && Convertidor.isCadena(clave) && Convertidor.isCadena(tipo)) {
			Query query = getEm().createQuery("FROM Usuario u WHERE u.persona.email='" + email + "' AND u.clave='"
					+ clave + "' AND u.rolBean.rol='" + tipo + "'");
			List<Usuario> list = query.getResultList();
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}

	/**
	 * Metodo que permite traer un usuario por su email.
	 * 
	 * @param email representa el email del usuario.
	 * @return el usuario encontrado.
	 */
	@SuppressWarnings({ "unchecked"})
	public Usuario usuario(String email) {
		if (Convertidor.isCadena(email)) {
			Query query = getEm().createQuery("FROM Usuario u WHERE u.persona.email='" + email + "'");
			List<Usuario> list = query.getResultList();
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}

	/**
	 * Metodo que permite conocer los usuarios logeados.
	 * 
	 * @return representa una lista con los usuarios logeados.
	 */
	@SuppressWarnings({ "unchecked"})
	public List<Usuario> logeados() {
		Query query = getEm().createQuery("SELECT u FROM Usuario u WHERE u.sesion=true ORDER BY u.fechaSesion");
		List<Usuario> list = query.getResultList();
		return list;
	}

	///////////////////////////////////////////////////////
	// Method Rol
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite traer una lista de usuarios por su rol y estado.
	 * 
	 * @param rol    representa el rol a filtrar.
	 * @param estado representa el estado 0:true, 1:false, -1: none.
	 * @return los usuarios encontrados.
	 */
	@SuppressWarnings({ "unchecked"})
	public List<Usuario> countRol(String rol, byte estado) {
		String tmp = "";
		String jpa = "FROM Usuario u WHERE u.rolBean.rol='" + rol + "'";
		if (estado >= 0) {
			tmp = "u.estado=" + (estado == 0 ? true : false);
			jpa = jpa + " AND " + tmp + "";
		}
		Query query = getEm().createQuery(jpa);
		List<Usuario> list = query.getResultList();
		return list;
	}

	/**
	 * Metodo que permite traer todos los usuarios con permiso de login.
	 * 
	 * @return la lista con los usuarios.
	 */
	@SuppressWarnings({ "unchecked"})
	public List<Usuario> usuariosPermisosLogin() {
		Query query = getEm().createQuery("FROM Usuario u WHERE u.rolBean.sistema=true");
		List<Usuario> list = query.getResultList();
		return list;
	}

	/**
	 * 
	 * @param rol
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	public List<Usuario> roles(String rol) {
		Query query = getEm().createQuery("FROM Usuario u WHERE u.rolBean.rol='" + rol + "' ORDER BY u.fechaCreacion");
		List<Usuario> list = query.getResultList();
		return list;
	}

	/**
	 * 
	 * @param rol
	 * @param documento
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	public Usuario usuarioRol(String rol, String documento) {
		Query query = getEm().createQuery(
				"FROM Usuario u WHERE u.rolBean.rol='" + rol + "' AND u.persona.documento='" + documento + "'");
		List<Usuario> list = query.getResultList();
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param rol
	 * @return
	 */
	public List<ChartJS> generoRol(String rol) {
		List<ChartJS> aux = new ArrayList<ChartJS>();
		Query query = getEm()
				.createQuery("SELECT u.persona.genero, COUNT(u.persona.genero) FROM Usuario u WHERE u.rolBean.rol='"
						+ rol + "' GROUP BY u.persona.genero ORDER BY COUNT(u.persona.genero) DESC");
		@SuppressWarnings("rawtypes")
		List result = query.getResultList();
		@SuppressWarnings("rawtypes")
		Iterator res = result.iterator();
		while (res.hasNext()) {
			Object[] tupla = (Object[]) res.next();
			ChartJS cc = new ChartJS();
			cc.setNombre(String.valueOf(tupla[0]));
			cc.setCantidad(Integer.parseInt(String.valueOf(tupla[1])));
			aux.add(cc);
		}
		return aux;
	}

	///////////////////////////////////////////////////////
	// Method Cliente
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer cuales son los clientes que mas compran y la plata
	 * que dejan.
	 * 
	 * @return la lista con el resultado
	 */
	public List<ChartJS> ventasCliente() {
		String jpa = "SELECT COUNT(v.usuario1.persona),CONCAT(v.usuario1.persona.nombre,' ',v.usuario1.persona.apellido)"
				+ ", SUM(v.total)" + " FROM Venta v WHERE v.usuario1.rolBean.rol='CLIENTE' GROUP BY v.usuario1.persona ORDER BY COUNT(v.usuario1.persona) DESC";
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

	///////////////////////////////////////////////////////
	// Method Seller Report
	///////////////////////////////////////////////////////
	@SuppressWarnings({"rawtypes" })
	public List<VendedorReport> consultarVentaVendedor(String fecha_inicio, String fecha_fin) {
		List<VendedorReport> list = new ArrayList<VendedorReport>();
		if (Convertidor.isCadena(fecha_inicio) && Convertidor.isCadena(fecha_fin)) {
			Query query = getEm()
					.createQuery("SELECT u.persona.documento, u.persona.nombre, u.fechaCreacion, COUNT(v.usuario2) "
							+ "FROM Usuario u JOIN Venta v ON(u=v.usuario2) WHERE u.rolBean.rol='VENDEDOR' AND u.fechaCreacion BETWEEN '"
							+ fecha_inicio + "' AND '" + fecha_fin + "' GROUP BY v.usuario2");
			List result = query.getResultList();
			Iterator res = result.iterator();
			while (res.hasNext()) {
				Object[] tupla = (Object[]) res.next();
				VendedorReport a= new VendedorReport();
				a.setDocumento(String.valueOf(tupla[0]));
				a.setNombre(String.valueOf(tupla[1]));
				a.setFecha(String.valueOf(tupla[2]));
				a.setVentas(Integer.parseInt(String.valueOf(tupla[3]))); 
				list.add(a);
			}
		}
		return list;
	}
	
	@SuppressWarnings({ "unchecked"})
	public List<Usuario> consultarVentaVendedor2(String fecha_inicio, String fecha_fin) {
		if (Convertidor.isCadena(fecha_inicio) && Convertidor.isCadena(fecha_fin)) {
			Query query = getEm().createQuery("FROM Usuario u WHERE u.rolBean.rol='VENDEDOR' AND u.fechaCreacion BETWEEN '"
							+ fecha_inicio + "' AND '" + fecha_fin + "'");
			return query.getResultList();
		}
		return new ArrayList<Usuario>();
	}
}
