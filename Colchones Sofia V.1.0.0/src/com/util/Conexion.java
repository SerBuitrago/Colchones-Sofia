package com.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
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

	private Class<T> entity;

	@PersistenceContext
	protected static EntityManager em = null;
	protected static String database = "sofia";

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public Conexion() {
		this(null);
	}

	@SuppressWarnings("static-access")
	public Conexion(Class<T> entity) {
		this.entity = entity;
		em = this.getEm();	
	}

	///////////////////////////////////////////////////////
	/// Persitence
	///////////////////////////////////////////////////////
	/**
	 * Metodo que hace la persitencia a la base de datos.
	 */
	public static EntityManager getEm() {
		if (em == null) {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(Conexion.database);  
			em = emf.createEntityManager();
			System.out.println("PERCITENCIA");
		}
		return em;
	}
	
	/**
	 * Metodo que permite cerrar la percitencia.
	 */
	public static EntityManager closePersitence(){
		if(em != null) {
			em.close();
			System.out.println("CLOSE PERCITENCIA");
			return null;
		}
		return em;
	}
	
	/**
	 * Metodo que permite reiniciar la conexión a la base datos.
	 */
	public static EntityManager getReset() {
		if(em != null) {
			System.out.println("RESET");
			em.close();
			System.out.println("CLOSE PERCITENCIA");
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(Conexion.database);  
			em = emf.createEntityManager();
			System.out.println("PERCITENCIA");
			System.out.println("RESET COMPLETE");
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
		T object = null;
		object = (T) em.find(entity, id);
		return object;
	}

	/**
	 * Metodo que lista todos los datos de la tabla.
	 * 
	 * @return represeta la lista.
	 */
	public List<T> list() {
		TypedQuery<T> consulta = em.createNamedQuery(entity.getSimpleName() + ".findAll", entity);
		List<T> lista = (List<T>) consulta.getResultList();
		return lista;
	}

	/**
	 * Metodo que inserta un elemento a la tabla.
	 * 
	 * @param o representa el elemento a insertar.
	 */
	@SuppressWarnings("static-access")
	public void insert(T o) {
		em = this.getEm();
		try {
			em.getTransaction().begin();
			em.persist(o);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em = this.getReset();
		}
	}

	/**
	 * Metodo que actualiza un elemento de la tabla.
	 * 
	 * @param o representa el elemento a actualizar.
	 */
	@SuppressWarnings("static-access")
	public void update(T o) {
		em = this.getEm();
		try {
			em.getTransaction().begin();
			em.merge(o);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em = this.getReset();
		}
	}

	/**
	 * Metodo que elimina un elemento en la tabla.
	 * 
	 * @param o representa el elemento.
	 */
	@SuppressWarnings("static-access")
	public void delete(T o) {
		em = this.getEm();
		try {
			em.getTransaction().begin();
			em.remove(o);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em = this.getReset();
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
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entity);
		Root<T> root = criteriaQuery.from(entity);
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
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entity);
		Root<T> root = criteriaQuery.from(entity);
		criteriaQuery.select(root);

		@SuppressWarnings("unchecked")
		ParameterExpression<E> params = criteriaBuilder.parameter((Class<E>) Object.class);
		criteriaQuery.where(criteriaBuilder.equal(root.get(fieldName), params));

		TypedQuery<T> query = (TypedQuery<T>) em.createQuery(criteriaQuery);
		query.setParameter(params, (E) fieldValue);

		List<T> queryResult = query.getResultList();
		return queryResult;
	}

	///////////////////////////////////////////////////////
	/// Getters and Setters
	///////////////////////////////////////////////////////

	public Class<T> getEntity() {
		return entity;
	}

	public void setEntity(Class<T> entity) {
		this.entity = entity;
	}

	public static void setEm(EntityManager em) {
		Conexion.em = em;
	}

	public static String getDatabase() {
		return database;
	}

	public static void setDatabase(String database) {
		Conexion.database = database;
	}
}
