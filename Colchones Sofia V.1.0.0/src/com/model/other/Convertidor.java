package com.model.other;

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
		if (isCadena(telefono)) {
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
	
	/**
	 * 
	 * @param cadena
	 * @return
	 */
	public static boolean isNumber(String cadena) {
		if (Convertidor.isCadena(cadena)) {
			char[] array = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
			char[] aux = cadena.toCharArray();
			for (char i : aux) {
				boolean salir = true;
				for (char j : array) {
					if (i == j) {
						salir = false;
					}
				}
				if (salir) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param cadena
	 * @return
	 */
	public static boolean containsNumber(String cadena) {
		if (Convertidor.isCadena(cadena)) {
			char[] array = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
			char[] aux = cadena.toCharArray();
			for (char i : aux) {
				for (char j : array) {
					if (i == j) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(String a, String b) {
		if(Convertidor.isCadena(a) && Convertidor.isCadena(b)) {
			return a.equals(b);
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
