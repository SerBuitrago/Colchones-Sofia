package com.util;

import java.io.Serializable;
import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * Implementation Face.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class Face implements Serializable {

	private static final long serialVersionUID = 1L;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public Face() {
	}

	///////////////////////////////////////////////////////
	// Static
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite obtener un atributo de la vista.
	 * 
	 * @param atributo representa el nombre atributo.
	 * @return valor del atributo.
	 */
	public static String get(String atributo) {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		return params.get(atributo);
	}

	/**
	 * Metodo que permite obtener un atributo de la vista.
	 * 
	 * @param atributo representa el nombre atributo.
	 * @return valor del atributo.
	 */
	public static int getInt(String atributo) {
		int index = -1;
		String aux = get(atributo);
		if (aux != null) {
			index = Integer.parseInt(aux);
		}
		return index;
	}
}
