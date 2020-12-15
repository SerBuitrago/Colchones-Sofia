package com.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import com.model.*;
import com.model.other.*;
import com.util.*;
import static com.util.Conexion.getEm;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Implementation VentaDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class VentaController extends Conexion<Venta> {

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public VentaController() {
		super(Venta.class);
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
	@SuppressWarnings({"unchecked"})
	public List<Venta> consultarVenta(String inicio, String fin) {
		Query query = getEm().createQuery("FROM Venta  WHERE fechaRegistro BETWEEN '"+inicio+"' AND '"+fin+"' ORDER BY fechaRegistro");
		return query.getResultList();
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer el ultima venta agregada.
	 * 
	 * @return representa el ultimo venta agregada.
	 */
	@SuppressWarnings({ "unchecked"})
	public Venta ultimoAdd() {
		Query query = getEm().createQuery("FROM Venta ORDER BY id DESC");
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
	@SuppressWarnings({"unchecked"})
	public List<Venta> ventaMensual(String mes, String anio) {
		String jpa = "FROM Venta v WHERE MONTHNAME(v.fechaRegistro)='" + mes + "' AND YEAR(v.fechaRegistro) = '" + anio
				+ "'";
		Query query = getEm().createQuery(jpa);
		List<Venta> list = query.getResultList();
		return list;
	}

	/**
	 * Metodo que permite conocer conocer la cantidad de unidades vendidas de un
	 * producto.
	 * 
	 * @return representa la cantidad de productos.
	 */
	public List<ChartJS> productos() {
		String jpa = "SELECT SUM(dv.cantidad), dv.detalleProducto.productoBean.nombre FROM DetalleCompraVenta dv WHERE dv.ventaBean IS NOT NULL "
				+ "GROUP BY dv.detalleProducto.productoBean.id ORDER BY SUM(dv.cantidad) DESC";
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
        
        

	/**
	 * Metodo para crear tarea programada de cambiar estado de pedido 
	 * 
	 */
        @SuppressWarnings("rawtypes")
	public void crearEventosEstadoPedido(Venta venta) {
		String jpa = "SELECT numero_dias FROM programacion_ventas WHERE estado = 0";
		Query query = getEm().createNativeQuery(jpa);
		Object result = (Object) query.getSingleResult();
                
                int dias_despacho = 0;
                try {
                    dias_despacho = (int) result;
                } catch (Exception e) {
                }
                
                
		jpa = "SELECT numero_dias FROM programacion_ventas WHERE estado = 1";
		query = getEm().createNativeQuery(jpa);           
		result = (Object) query.getSingleResult();
                
                int dias_entrega = 0;
                try {
                    dias_entrega = (int) result;
                } catch (Exception e) {
                }
                
                String evento = "CREATE EVENT %s ON SCHEDULE AT '%s' DO UPDATE venta SET estado_pedido = %d WHERE id = %d";
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(venta.getFechaRegistro());
                
                calendar.add(Calendar.DAY_OF_YEAR, dias_despacho);
                String fechaDespacho = formato.format(calendar.getTime());
                
                calendar.setTime(venta.getFechaRegistro());                
                
                calendar.add(Calendar.DAY_OF_YEAR, dias_entrega);
                String fechaEntrega = formato.format(calendar.getTime());
                
                EntityManager emf = getEm();  
                EntityTransaction  trans = emf.getTransaction();
                trans.begin();
                
                String query1 = String.format(evento, "evento_despacho_venta_"+venta.getId(), fechaDespacho, 1, venta.getId());
                String query2 = String.format(evento, "evento_entrega_venta_"+venta.getId(), fechaEntrega, 2, venta.getId());
                                
                Query n = emf.createNativeQuery(query1);
                Query m = emf.createNativeQuery(query2);
                
                n.executeUpdate();
                m.executeUpdate();

                trans.commit();  
                
	}
}
