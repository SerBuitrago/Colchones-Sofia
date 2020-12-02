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
 * Implementation PersonaDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class ProveedorController extends Conexion<Proveedor> {

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public ProveedorController() {
		super(Proveedor.class);
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
	@SuppressWarnings({ "unchecked" })
	public List<Proveedor> consultaProveedor(String inicio, String fin) {
		Query query = getEm().createQuery(
				"FROM Proveedor WHERE fechaCreacion BETWEEN '" + inicio + "' AND '" + fin + "' ORDER BY fechaCreacion");
		List<Proveedor> list = query.getResultList();
		return list;
	}

	///////////////////////////////////////////////////////
	// Statistics
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer todos los productos de los proveedores.
	 * 
	 * @return la lista con el resultado obtenido.
	 */
	public List<ChartJS> proveedorProductos() {
		List<Proveedor> proveedores = findByFieldList("estado", true);
		List<ChartJS> list = new ArrayList<ChartJS>();
		for (Proveedor p : proveedores) {
			ChartJS charj = new ChartJS();
			if (p.getDetalleProductos() != null && p.getDetalleProductos().size() > 0) {
				charj.setNombre(p.getPersona().getNombre());
				charj.setCantidad(p.getDetalleProductos().size());
				list.add(charj);
			}
		}
		return list;
	}

	/**
	 * Metood que permite la cantidad de compras de los proveedores.
	 * 
	 * @return la lista con el resultado obtenido.
	 */
	public List<ChartJS> cantidadCompras() {
		List<ChartJS> list = new ArrayList<ChartJS>();
		Query query = getEm().createQuery("SELECT v.usuario2.persona.nombre, COUNT(v.usuario2) FROM "
				+ "Venta v WHERE v.usuario2.estado=true GROUP BY v.usuario2 ORDER BY COUNT(v.usuario2)");
		@SuppressWarnings("rawtypes")
		List result = query.getResultList();
		@SuppressWarnings("rawtypes")
		Iterator res = result.iterator();
		while (res.hasNext()) {
			Object[] tupla = (Object[]) res.next();
			ChartJS cc = new ChartJS();
			cc.setNombre(String.valueOf(tupla[0]));
			cc.setCantidad(Integer.parseInt(String.valueOf(tupla[1])));
			list.add(cc);
		}
		return list;
	}

	///////////////////////////////////////////////////////
	// Method Provider and Product
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite insertar a un proveedor un detalle de producto.
	 * 
	 * @param proveedor representa el proveedor.
	 * @param producto  representa el detalle producto.
	 * @return true si lo inserto false si no.
	 */
	@SuppressWarnings("static-access")
	public boolean insertProveedorProducto(String proveedor, String producto) {
		boolean valid = false;
		em = this.getEm();
		try {
			getEm().getTransaction().begin();

			String queryString2 = "INSERT INTO proveedor_producto(proveedor, producto) values (?,?)";
			Query query2 = getEm().createNativeQuery(queryString2);

			query2.setParameter(1, proveedor);
			query2.setParameter(2, producto);
			query2.executeUpdate();

			getEm().getTransaction().commit();

			em = this.getReset();
			return true;
		} catch (Exception e) {
		}
		return valid;
	}

	/**
	 * Metodo que permite eliminar un detalle producto proveedor.
	 * 
	 * @param p  representa el proveedor.
	 * @param dp representa el detalle producto.
	 * @return true si loe limino false si no.
	 */
	@SuppressWarnings("static-access")
	public boolean eliminarDetalleProductoProveedor(Proveedor p, DetalleProducto dp) {
		em = this.getEm();
		try {
			getEm().getTransaction().begin();

			String queryString2 = "DELETE FROM proveedor_producto WHERE proveedor=" + p.getDocumento()
					+ " AND producto=" + dp.getId();
			Query query2 = getEm().createNativeQuery(queryString2);
			query2.executeUpdate();

			getEm().getTransaction().commit();

			em = this.getReset();
			return true;
		} catch (Exception e) {
			System.out.println("ERROR : " + e.getMessage());
		}
		return false;
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer la categorias de los productos de un proveedor.
	 * 
	 * @param proveedor representa el proveedor.
	 * @return la lista de productos de este proveedor.
	 */
	@SuppressWarnings({ "unchecked" })
	public List<Categoria> categoriasProveedorProdcuto(String proveedor) {
		Query query = getEm().createQuery("FROM Proveedor p WHERE p.documento='" + proveedor + "'");
		List<Proveedor> list = query.getResultList();
		List<Categoria> categorias = new ArrayList<Categoria>();
		for (Proveedor p : list) {
			for (DetalleProducto dp : p.getDetalleProductos()) {
				if (dp.getProductoBean() != null && dp.getProductoBean().getCategoriaBean() != null) {
					Categoria aux = dp.getProductoBean().getCategoriaBean();
					if (!existeCategoriaProveedorProducto(categorias, aux.getId())) {
						aux.setProductos(productoCategoriaProveedorProducto(p.getDetalleProductos(), aux.getId()));
						categorias.add(aux);
					}
				}
			}
		}
		return categorias;
	}

	/**
	 * Metodo que verifica si una categoria ya fue agregada.
	 * 
	 * @param list representa la lista de categorias.
	 * @param id   representa la categoria a agregar.
	 * @return true si la agrego false si no.
	 */
	public boolean existeCategoriaProveedorProducto(List<Categoria> list, String id) {
		if (list != null && Convertidor.isCadena(id)) {
			for (Categoria c : list) {
				if (Convertidor.equals(c.getId(), id)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Metodo que permite listar todos los productos de una categoria.
	 * 
	 * @return representa una lista de productos de una categoria en especifica.
	 */
	public List<Producto> productoCategoriaProveedorProducto(List<DetalleProducto> dp, String categoria) {
		if (dp != null && Convertidor.isCadena(categoria)) {
			List<Producto> list = new ArrayList<Producto>();
			for (DetalleProducto d : dp) {
				if (d.getProductoBean() != null && d.getProductoBean().getCategoriaBean() != null) {
					if (Convertidor.equals(d.getProductoBean().getCategoriaBean().getId(), categoria)) {
						if (!existeProductoCategoriaProveedorProducto(list, d.getProductoBean().getId(),
								d.getProductoBean().getCategoriaBean().getId())) {
							Producto aux = d.getProductoBean();
							aux.setDetalleProductos(detalleProductoProveedorProducto(dp, categoria, aux.getId()));
							list.add(aux);
						}
					}
				}
			}
			return list;
		}
		return new ArrayList<Producto>();
	}

	/**
	 * Metodo que permite averiguar si un producto ya fue agregado a una categoria.
	 * 
	 * @param list     representa la lista de productos.
	 * @param producto representa el producto a buscar.
	 * @return true si existe false si no.
	 */
	public boolean existeProductoCategoriaProveedorProducto(List<Producto> list, String producto, String categoria) {
		if (list != null && Convertidor.isCadena(producto) && Convertidor.isCadena(categoria)) {
			for (Producto p : list) {
				if (Convertidor.equals(p.getId(), producto)
						&& Convertidor.equals(p.getCategoriaBean().getId(), categoria)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Metodo que permite listar los detalles de productos de un producto en
	 * especifico.
	 * 
	 * @param dp        representa todos los detalles de producto.
	 * @param categoria representa la categoria especifica.
	 * @param producto  representa el producto especifico.
	 * @return una lista con los detalles encontrados.
	 */
	public List<DetalleProducto> detalleProductoProveedorProducto(List<DetalleProducto> dp, String categoria,
			String producto) {
		if (dp != null && Convertidor.isCadena(categoria)) {
			List<DetalleProducto> list = new ArrayList<DetalleProducto>();
			for (DetalleProducto d : dp) {
				if (d.getProductoBean() != null && d.getProductoBean().getCategoriaBean() != null) {
					if (Convertidor.equals(d.getProductoBean().getCategoriaBean().getId(), categoria)) {
						if (Convertidor.equals(d.getProductoBean().getId(), producto)) {
							list.add(d);
						}
					}
				}
			}
			return list;
		}
		return new ArrayList<DetalleProducto>();
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer si existe una persona por su telefono o nombre.
	 * 
	 * @param persona representa al persona.
	 * @return true si existe false si no.
	 */
	public boolean registrar(Persona persona) {
		if (persona != null) {
			String jpa = "FROM Persona p WHERE ";
			boolean entro = false;
			if (Convertidor.isCadena(persona.getEmail())) {
				jpa += " p.email='" + persona.getEmail() + "'";
				entro = true;
			}
			if (Convertidor.isCadena(persona.getNombre())) {
				jpa += ((entro) ? "OR" : "") + " p.nombre='" + persona.getNombre() + "'";
				entro = true;
			}
			if (Convertidor.isCadena(persona.getTelefono())) {
				jpa += ((entro) ? "OR" : "") + " p.telefono='" + persona.getTelefono() + "'";
				entro = true;
			}
			if (entro) {
				Query query = getEm().createQuery(jpa);
				@SuppressWarnings("unchecked")
				List<Persona> list = query.getResultList();
				return (list != null && list.size() > 0);
			}
		}
		return true;
	}

	/**
	 * Metodo que permite conocer si existe una persona por su telefono o email.
	 * 
	 * @param persona representa al persona.
	 * @return true si existe false si no.
	 */
	public boolean registrar(String email, String telefono) {
		String sql = "";
		if (Convertidor.isCadena(email) && Convertidor.isCadena(telefono)) {
			sql = "FROM Persona p WHERE p.telefono='" + telefono + "' OR p.email='" + email + "'";
		} else if (Convertidor.isCadena(email) && !Convertidor.isCadena(telefono)) {
			sql = "FROM Persona p WHERE p.email='" + email + "'";
		} else {
			sql = "FROM Persona p WHERE p.telefono='" + telefono + "'";
		}
		Query query = getEm().createQuery(sql);
		@SuppressWarnings("unchecked")
		List<Persona> list = query.getResultList();
		return (list != null && list.size() > 0);
	}
}
