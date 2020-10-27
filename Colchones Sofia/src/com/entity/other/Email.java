package com.entity.other;

import java.io.Serializable;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Implementation Email.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class Email implements Serializable {

	private static final long serialVersionUID = 1L;
	private String de;
	private String usuario;
	private String clave;
	private String para;
	private String encabezado;
	private String cuerpo;

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public Email() {
	}

	public Email(String de, String usuario, String clave, String para, String encabezado, String cuerpo) {
		super();
		this.de = de;
		this.usuario = usuario;
		this.clave = clave;
		this.para = para;
		this.encabezado = encabezado;
		this.cuerpo = cuerpo;
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite enviar el email.
	 */
	public void sendMail() throws AddressException, MessagingException {
		Properties props = System.getProperties();

		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.fallback", "false");

		Session mailSession = Session.getDefaultInstance(props, null);
		mailSession.setDebug(true);

		Message mailMessage = new MimeMessage(mailSession);

		mailMessage.setFrom(new InternetAddress(de));
		mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(para));
		mailMessage.setContent(cuerpo, "text/html");
		mailMessage.setSubject(encabezado);

		Transport transport = mailSession.getTransport("smtp");
		transport.connect("smtp.gmail.com", de, clave);
		transport.sendMessage(mailMessage, mailMessage.getAllRecipients());

	}

	@Override
	public String toString() {
		return "Email [de=" + de + ", usuario=" + usuario + ", clave=" + clave + ", para=" + para + ", encabezado="
				+ encabezado + ", cuerpo=" + cuerpo + "]";
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////
	public String getDe() {
		return de;
	}

	public void setDe(String de) {
		this.de = de;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	public String getEncabezado() {
		return encabezado;
	}

	public void setEncabezado(String encabezado) {
		this.encabezado = encabezado;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
