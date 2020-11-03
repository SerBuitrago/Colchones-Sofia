package com.bean.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 * Implementation Validator.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "validator")
public class Validator {
	
	private FacesMessage message;
	
	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public Validator() {
		this.message= null;
	}
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que verifica un email.
	 * @param fo representa el contexto.
	 * @param uic representa el componente.
	 * @param o representa el objecto.
	 */
	public void isEmail(FacesContext fo, UIComponent uic, Object o) {
		fo = FacesContext.getCurrentInstance();
		String correo = o.toString().trim();
		if(correo != null) {
			correo= correo.trim();
			Pattern patern = Pattern.compile("\\w+@gmail.com");
			Matcher matcher = patern.matcher((CharSequence) correo);
			if (correo.length() == 0) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Ingrese el campo del email.");
				throw new ValidatorException(message);
			} else if (correo.length() > 30) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "El campo email debe ser menor de 30 caracteres.");
				throw new ValidatorException(message);
			} else if (!matcher.matches()) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "El correo electronico debe ser gmail.");
				throw new ValidatorException(message);
			}
		}else {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Ingrese el campo del email.");
			throw new ValidatorException(message);
		}
	}  
	
	/**
	 * Metodo que verifica la clave.
	 * @param fo representa el contexto.
	 * @param uic representa el componente.
	 * @param o representa el objecto.
	 */
	public void isClave(FacesContext fo, UIComponent uic, Object o) {
	       String clave=o.toString().trim();
	       if(clave.length()==0) {
	    	   message = new FacesMessage(FacesMessage.SEVERITY_WARN,"Warning", "Ingrese el campo de la contraseña.");
	    	   throw new ValidatorException(message);
	       }else if(clave.length() < 5 ) {
	    	   message = new FacesMessage(FacesMessage.SEVERITY_WARN,"Warning", "La contraseña debe ser mayor de 5 caracteres.");
	    	   throw new ValidatorException(message);
	       }
	}
}
