package com.bean.session;

import java.io.Serializable;
import java.math.BigInteger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.entity.other.Email;

/**
 * Implementation EmailBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "mail")
@SessionScoped
public class EmailBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Email mail;

	private String de;
	private String clave;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public EmailBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void ini() {
		mail = new Email();
		de = "abisaidgc@ufps.edu.co";
		clave = "niky.jam";
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite enviar un email.
	 * 
	 * @param para       representa el email para quien va.
	 * @param encabezado representa el mensaje de encabezado.
	 * @param cuerpo     representa el mensaje de cuerpo.
	 */
	public void send(String para, String encabezado, String cuerpo) {
		try {
			Email mailSender = new Email(de, de, clave, para, encabezado, cuerpo);
			mailSender.sendMail();
		} catch (Exception e) {
			System.out.println("Error send(String para, String encabezado, String cuerpo): " + e.getMessage());
		}
	}

	///////////////////////////////////////////////////////
	// Format
	///////////////////////////////////////////////////////
	/**
	 * 
	 * @param clave
	 * @param telefono
	 * @param direccion
	 * @return
	 */
	public String formatRecuperar(String clave, String telefono, String direccion) {
		String rta;
		rta = "<img id=':1q_6-e' name=':1q'\r\n"
				+ "src='https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSphVKwFQ5ctLmU2yQp0vAe0yvZYsBEDBGSHg&usqp=CAU'\r\n"
				+ " class='ajn' data-name='Colchones Sofia'  width='50px' height='50px'>\r\n"
				+ "<div dir='ltr'>¡Cordial saludo!<div>\r\n"
				+ "<br></div><div>Recibimos una solicitud para reestablecer la contraseña de nuestro portal.</div>\r\n"
				+ "<div><br></div>\r\n" + "<div>Su contraseña ha sido reestablecida exitosamente.&nbsp;</div>\r\n"
				+ "<div><br></div>\r\n" + "<div>Su nueva contraseña es: <strong>" + clave + "</strong> &nbsp;</div>\r\n"
				+ "<div></div>\r\n" + "\r\n" + "<div> <br></div>\r\n"
				+ "<div>Gracias por&nbsp;su atención<br></div>\r\n" + "<div><br></div>\r\n" + "<div><br></div>\r\n"
				+ "<div>Atentamente,&nbsp;</div>\r\n" + "<font color='#888888'><div><br></div>-- <br>\r\n"
				+ "<div dir='ltr' data-smartmail='gmail_signature'>\r\n" + "<div dir='ltr'><div>\r\n"
				+ "<div dir='ltr'><div>\r\n" + "<div dir='ltr'><div dir='ltr'>\r\n"
				+ "<div dir='ltr'><div dir='ltr'>\r\n" + formatPiePagina(telefono, direccion);
		return rta;
	}
	
	/**
	 * 
	 * @param id
	 * @param cantidad
	 * @param total
	 * @param direccion
	 * @return
	 */
	public String formatVenta(int id, int cantidad, BigInteger total,String telefono, String direccion) {
		String aux= ""+formatPiePagina(telefono, direccion);
		return aux;
	}

	/**
	 * 
	 * @param telefono
	 * @param direccion
	 * @return
	 */
	public String formatPiePagina(String telefono, String direccion) {
		String pie = "<div>\r\n" + "<b>Colchones Sofia</b>\r\n" + "<br></div>\r\n" + "<div>" + telefono + "</div>\r\n"
				+ "<div style='font-size:12.8px'>Direccion: &nbsp;<span style='font-size:12.8px'> " + direccion
				+ " </span></div>\r\n" + "<div style='font-size:12.8px'>\r\n"
				+ "<a href='http://localhost:8080/colchones/faces/login.xhtml;jsessionid=3027F81BBC2C3C51FD70CF29A74B195A' style='font-size:12.8px' target='_blank' >http://colchonessofia.com</a>\r\n"
				+ "<br></div>\r\n" + "<div><span></span><span></span>\r\n" + "<br>&nbsp;<br></div>\r\n"
				+ "</div></div></div></div></div></div></div></div></div></font></div>";
		return pie;
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public Email getMail() {
		return mail;
	}

	public void setMail(Email mail) {
		this.mail = mail;
	}

	public String getDe() {
		return de;
	}

	public void setDe(String de) {
		this.de = de;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
