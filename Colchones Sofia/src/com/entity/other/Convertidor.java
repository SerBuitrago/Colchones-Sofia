package com.entity.other;

import java.io.Serializable;
import java.util.Locale;

/**
 * Implementation Convertidor.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class Convertidor implements Serializable {
	private static final long serialVersionUID = 1L;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public Convertidor() {
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que retorna el genero de una persona.
	 * 
	 * @param nombre representa el genero.
	 * @return el nombre del genero.
	 */
	public static String genero(String genero) {
		String aux = "";
		switch (genero) {
		case "F":
			aux = "Femenino";
			break;
		case "M":
			aux = "Masculino";
			break;
		default:
			aux = "Otro";
			break;
		}
		return aux;
	}

	/**
	 * Metodo que permite convertir un numero a cadena.
	 * 
	 * @param telefono representa el numero.
	 * @return la cadena resultante.
	 */
	public static String telefono(String telefono) {
		String aux = null;
		if (telefono != null && telefono.length() > 0) {
			aux = telefono.replace("-", "");
		}
		return aux;
	}

	/**
	 * 
	 * @param value
	 * @param filter
	 * @param locale
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean filterByInteger(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim();
		if (filterText == null || filterText.equals("")) {
			return true;
		}
		if (value == null) {
			return false;
		}
		return ((Comparable) value).compareTo(Convertidor.getInteger(filterText)) >= 0;
	}
	
	/**
	 * 
	 * @param cadena
	 * @return
	 */
	public static boolean isCadena(String cadena) {
		if(cadena!= null && cadena.length() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param <E>
	 * @param vector
	 * @return
	 */
	public static boolean isVector(byte [] vector){
		if(vector!= null && vector.length > 0) {
			return true;
		}
		return false;
	}
	
	///////////////////////////////////////////////////////
	// Method Private
	///////////////////////////////////////////////////////
	/**
	 * 
	 * @param string
	 * @return
	 */
	private static int getInteger(String string) {
		try {
			return Integer.valueOf(string);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	///////////////////////////////////////////////////////
	// Getter and Setter
	///////////////////////////////////////////////////////
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
