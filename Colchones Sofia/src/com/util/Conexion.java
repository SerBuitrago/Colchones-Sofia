package com.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

/**
 * Implementation Conexion.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class Conexion<T> {

	private Class<T> c;
	private static EntityManager em = null;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	@SuppressWarnings("static-access")
	public Conexion() {
		em = this.getEm();
	}

	@SuppressWarnings("static-access")
	public Conexion(Class<T> c) {
		em = this.getEm();
		this.c = c;
	}

	///////////////////////////////////////////////////////
	/// Getters and Setters
	///////////////////////////////////////////////////////

	public void setC(Class<T> c) {
		this.c = c;
	}

	public static EntityManager getEm() {
		if (em == null) {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("sofia");
			em = emf.createEntityManager();
		}
		return em;
	}

	///////////////////////////////////////////////////////
	/// Methods
	///////////////////////////////////////////////////////
	/**
	 * Metodo que trae el elemento mediante su PK.
	 * 
	 * @param <E> representa el tipo de dato.
	 * @param id  representa la PK.
	 * @return el elemento generico E.
	 */
	public <E> T find(E id) {
		T object = (T) em.find(c, id);
		return object;
	}

	/**
	 * Metodo que lista todos los datos de la tabla.
	 * 
	 * @return represeta la lista.
	 */
	public List<T> list() {
		TypedQuery<T> consulta = em.createNamedQuery(c.getSimpleName() + ".findAll", c);
		List<T> lista = (List<T>) consulta.getResultList();
		return lista;
	}

	/**
	 * Metodo que inserta un elemento a la tabla.
	 * 
	 * @param o representa el elemento a insertar.
	 */
	public void insert(T o) {
		try {
			em.getTransaction().begin();
			em.persist(o);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// em.close();
		}
	}

	/**
	 * Metodo que actualiza un elemento de la tabla.
	 * 
	 * @param o representa el elemento a actualizar.
	 */
	public void update(T o) {
		try {
			em.getTransaction().begin();
			em.merge(o);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// em.close();
		}
	}

	/**
	 * Metodo que elimina un elemento en la tabla.
	 * 
	 * @param o representa el elemento.
	 */
	public void delete(T o) {
		try {
			em.getTransaction().begin();
			em.remove(o);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// em.close();
		}
	}

	/**
	 * Metodo que trae un elemento depediendo de un fila y su valor.
	 * 
	 * @param <E>        representa el valor.
	 * @param fieldName  representa la fila.
	 * @param fieldValue representa el valor fila.
	 * @return el elemento encontrado.
	 */
	public <E> T findByField(String fieldName, E fieldValue) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(c);
		Root<T> root = criteriaQuery.from(c);
		criteriaQuery.select(root);

		@SuppressWarnings("unchecked")
		ParameterExpression<E> params = criteriaBuilder.parameter((Class<E>) Object.class);
		criteriaQuery.where(criteriaBuilder.equal(root.get(fieldName), params));

		TypedQuery<T> query = (TypedQuery<T>) em.createQuery(criteriaQuery);
		query.setParameter(params, (E) fieldValue);

		List<T> queryResult = query.getResultList();

		T returnObject = null;

		if (!queryResult.isEmpty()) {
			returnObject = queryResult.get(0);
		}
		return returnObject;
	}

	/**
	 * Metodo que lista los elementos depediendo de un fila y su valor.
	 * 
	 * @param <E>        representa el valor.
	 * @param fieldName  representa la fila.
	 * @param fieldValue representa el valor fila.
	 * @return la lista.
	 */
	public <E> List<T> findByFieldList(String fieldName, E fieldValue) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(c);
		Root<T> root = criteriaQuery.from(c);
		criteriaQuery.select(root);

		@SuppressWarnings("unchecked")
		ParameterExpression<E> params = criteriaBuilder.parameter((Class<E>) Object.class);
		criteriaQuery.where(criteriaBuilder.equal(root.get(fieldName), params));

		TypedQuery<T> query = (TypedQuery<T>) em.createQuery(criteriaQuery);
		query.setParameter(params, (E) fieldValue);

		List<T> queryResult = query.getResultList();
		return queryResult;
	}
}
