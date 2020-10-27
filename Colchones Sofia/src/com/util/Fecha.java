package com.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Implementation Fecha.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class Fecha implements Serializable {
	private static final long serialVersionUID = 1L;

	private String fecha;
	private DateFormat format;

	private Date diferencia;
	private String formatoHora;
	private long minutos;
	
	private String meses [] = {"January" , "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

	///////////////////////////////////////////////////////
	// Builder
	///////////////////////////////////////////////////////
	public Fecha() {
		this.fecha = null;
		format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	}

	public void init() {
		this.format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer la fecha actual con hora.
	 * 
	 * @return representa una cadena con la fecha actual.
	 */
	public String fecha() {
		Date date = new Date();
		this.fecha = this.format.format(date);
		return fecha;
	}

	/**
	 * Metodo que calcula la diferencia entre dos fechas.
	 * 
	 * @param inicio representa la fecha inicio.
	 * @param fin    representa la fecha fin.
	 * @return true si se puede restar false si no.
	 * @throws ParseException Error presentado.
	 */
	public boolean restarHoras(String inicio, String fin) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm", Locale.US);
		SimpleDateFormat sdfFormato = new SimpleDateFormat("HH:mm:ss", Locale.US);
		this.diferencia = null;
		this.formatoHora = null;
		this.minutos = 0;
		Date i = sdf.parse(inicio);
		Date j = sdf.parse(fin);
		if (i.before(j)) {
			long calculando = j.getTime() - i.getTime();
			this.minutos = TimeUnit.MILLISECONDS.toMinutes(calculando);
			this.diferencia = diferencia(i, j);
			this.formatoHora = sdfFormato.format(diferencia);
			return true;
		}
		return false;
	}
	
	public String darFormato(Date fecha, String formato) {
		format = new SimpleDateFormat(formato);
		String aux = this.format.format(fecha);
		return aux;
	}

	/**
	 * Convierte la fecha actual en un formato.
	 * 
	 * @param format representa el formato.
	 * @return la fecha obtenida.
	 * @throws ParseException el error presentado.
	 */
	public Date fecha(String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
		return sdf.parse(fecha());
	}

	/**
	 * Metodo que permite convertir una fecha en un formato.
	 * 
	 * @param format representa el formato.
	 * @param fecha  representa la fecha.
	 * @return la fecha obtenida.
	 * @throws ParseException el error presentado.
	 */
	public Date fecha(String format, String fecha) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
		return sdf.parse(fecha);
	}

	/**
	 * Metodo que permite sumar minutos a una fecha.
	 * 
	 * @param fecha   representa la fecha.
	 * @param minutos representa los minutos.
	 * @return la fehca resultante.
	 */
	public Date sumarMinutos(Date fecha, int minutos) {
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);
		c.add(Calendar.MINUTE, minutos);
		return c.getTime();
	}

	/**
	 * Metodo que calcula la diferencia entre dos fechas.
	 * 
	 * @param inicio representa la fecha inicio.
	 * @param fin    representa la fecha fin.
	 * @return la fecha resultante.
	 */
	private Date diferencia(Date inicio, Date fin) {
		long milliseconds = fin.getTime() - inicio.getTime();
		int seconds = (int) (milliseconds / 1000) % 60;
		int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
		int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.SECOND, seconds);
		c.set(Calendar.MINUTE, minutes);
		c.set(Calendar.HOUR_OF_DAY, hours);
		return c.getTime();
	}
	
	/**
	 * Metodo que permite calcular el año actual.
	 * @return retorna el año actual.
	 */
	public int anioActual() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * Metodo que permite calcular el mes actual.
	 * @return retorna el mes actual.
	 */
	public int mesActual() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH);
	}
	
	/**
	 * Metodo que permite calcular el mes actual.
	 * @return retorna el mes actual.
	 */
	public String mesActualCadena() {
		int mes= mesActual();
		return this.meses[mes];
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////
	public String getFecha() {
		return fecha;
	}

	public Date getDiferencia() {
		return diferencia;
	}

	public void setDiferencia(Date diferencia) {
		this.diferencia = diferencia;
	}

	public String getFormatoHora() {
		return formatoHora;
	}

	public void setFormatoHora(String formatoHora) {
		this.formatoHora = formatoHora;
	}

	public long getMinutos() {
		return minutos;
	}

	public void setMinutos(long minutos) {
		this.minutos = minutos;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public DateFormat getFormat() {
		return format;
	}

	public void setFormat(DateFormat format) {
		this.format = format;
	}

	public String[] getMeses() {
		return meses;
	}

	public void setMeses(String[] meses) {
		this.meses = meses;
	}
}
