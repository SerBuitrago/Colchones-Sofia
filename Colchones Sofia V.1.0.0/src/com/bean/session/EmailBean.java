package com.bean.session;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.model.*;
import com.model.other.*;

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
	private boolean estado;

	private String de;
	private String clave;

	///////////////////////////////////////////////////////
	// Managed Bean
	///////////////////////////////////////////////////////
	@ManagedProperty("#{app}")
	private AppBean app;

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
		de = app.getApp().getEmpresa().getEmail();
		clave = app.getApp().getEmpresa().getClave();
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
		estado=false;
		try {
			Email mailSender = new Email(de, de, clave, para, encabezado, cuerpo);
			mailSender.sendMail();
			estado=true;
		} catch (Exception e) {
			System.out.println("Error send(String para, String encabezado, String cuerpo): " + e.getMessage());
		}
	}

	///////////////////////////////////////////////////////
	// Format
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite hacer el formato del recuperar clave.
	 * @param clave representa la clave.
	 * @param telefono representa el telefono.
	 * @param direccion representa la direccion.
	 * @return cadena con el formato con email.
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
	public String formatVenta(int id, int cantidad, BigInteger total, String telefono, String direccion) {
		String aux = "" + formatPiePagina(telefono, direccion);
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
	
	/**
	 * 
	 * @param persona
	 * @param detalle
	 * @param total
	 * @return
	 */
	public String formatPagar(Persona persona, List<DetalleCompraVenta> detalle, BigInteger total, int id) {
		String rta="<div>"
				+"<h1> VENTA ONLINE </h1>"
				+"<table style='width: 100%;text-align: center;'>"
				+"<thead> </thead>"
				+"<tbody> "
				+ "<tr style='padding:16px;'> "
				+ "<td style='font-weight: bold;text-transform uppercase'>ID Venta</td> "
				+ "<td style='padding:16px'>"+id+"</td>"
				+ "</tr>"
				+ "<tr style='padding:16px;background:#f5f5f5;'> "
				+ "<td style='font-weight: bold;text-transform uppercase'>Nombre</td> "
				+ "<td style='padding:16px'>"+persona.getNombre()+" "+persona.getApellido()+"</td>"
				+ "</tr>"
				+ "<tr> "
				+ "<td style='font-weight: bold;text-transform uppercase'>Email</td> "
				+ "<td style='padding:16px'>"+persona.getEmail()+"</td> "
				+ "</tr>"
				+ "<tr style='padding:16px;background:#f5f5f5;'> "
				+ "<td style='font-weight: bold;text-transform uppercase;padding:16px'>Telefono</td> "
				+ "<td style='padding:16px'>"+persona.getTelefono()+"</td> "
				+ "</tr>"
				+ "<tr> "
				+ "<td style='font-weight: bold;text-transform uppercase;padding:16px'>Direccion</td> "
				+ "<td style='padding:16px'>"+persona.getDireccion()+"</td> "
				+ "</tr>"
				+ "</tbody>"
				+ "</table>"
				+ "</div>";
		
				String aux="<h1>DETALLE VENTA</h1>"
						+"<table style='width: 100%;text-align: center;'> "
						+"<thead style='text-transform: uppercase;'>"
						+ "<tr style='padding:16px;background:#f5f5f5;'>"
						+ "<th style='font-weight: bold;text-transform: uppercase;padding:16px'>ID</th>"
						+ "<th style='font-weight: bold;text-transform: uppercase;padding:16px'>ID Detalle</th>"
						+ "<th style='font-weight: bold;text-transform: uppercase;padding:16px'>Cantidad</th>"
						+ "<th style='font-weight: bold;text-transform uppercase;padding:16px'>Descuento</th>"
						+ "<th style='font-weight: bold;text-transform uppercase;padding:16px'>Subtotal</th>"
						+ "</tr>"
						+ " </thead>"
						+"<tbody>";
				int si = 0;
				for(DetalleCompraVenta p:detalle) {
					aux +=    "<tr "+(si%2 == 0 ? "style='padding:16px;background:#f5f5f5;'" : "")+">"
							+ "<td style='padding:16px'>"+p.getId()+"</td> "
							+ "<td style='padding:16px'>"+p.getDetalleProducto().getId()+"</td> "
							+ "<td style='padding:16px'>"+p.getCantidad()+"</td> "
							+ "<td style='padding:16px'>"+p.getDescuento()+"</td> "
							+ "<td style='padding:16px'>"+p.getSubtotal()+"</td> "
							+ "</tr>"; 
					
					si++;
					
				}
				aux+="<tr "+(si%2 == 0 ? "style='padding:16px;background:#f5f5f5;'" : "")+"><td colspan='4' style='padding:16px'>TOTAL</td><td style='padding:16px'>"+total+"</td></tr>";
				aux+="</tbody>"
						+ "</table>";	
				rta+=aux;
		return rta;
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

	public AppBean getApp() {
		return app;
	}

	public void setApp(AppBean app) {
		this.app = app;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
}
