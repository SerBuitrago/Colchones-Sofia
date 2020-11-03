package com.bean.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.dao.*;
import com.entity.*;
import com.entity.other.*;
import com.util.*;

/**
 * Implementation AppBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "app")
@SessionScoped 
public class AppBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private App app;

	private final String NIT = "1090492502-4"; 
	private String mes;
	private int anio;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public AppBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		this.app();
		Fecha fecha = new Fecha();
		this.mes = fecha.mesActualCadenaESP();
		this.anio = fecha.anioActual();

	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que trae la información de la empresa.
	 */
	public void app() {
		EmpresaDao eDao = new EmpresaDao();
		EmpresaInformacionDao iDao = new EmpresaInformacionDao();
		
		Empresa empresa = eDao.find(this.NIT);
		System.out.println(empresa.getNombre());  
		if(empresa != null) {
			List<EmpresaInformacion> list = iDao.findByFieldList("empresaBean", empresa);
			this.app = new App(emails(list), telefonos(list), empresa, carrusel(list)); 
		}
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public List<EmpresaInformacion> telefonos(List<EmpresaInformacion> list) {
		List<EmpresaInformacion> telefonos = new ArrayList<EmpresaInformacion>();
		for (EmpresaInformacion i : list) {
			if (Convertidor.isCadena(i.getTelefono())) {
				telefonos.add(i);
			}
		}
		return telefonos;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public List<EmpresaInformacion> emails(List<EmpresaInformacion> list) {
		List<EmpresaInformacion> telefonos = new ArrayList<EmpresaInformacion>();
		for (EmpresaInformacion i : list) {
			if (Convertidor.isCadena(i.getEmail())) {
				telefonos.add(i);
			}
		}
		return telefonos;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public List<EmpresaInformacion> carrusel(List<EmpresaInformacion> list) {
		List<EmpresaInformacion> telefonos = new ArrayList<EmpresaInformacion>();
		for (EmpresaInformacion i : list) {
			if (Convertidor.isVector(i.getFoto())) {
				telefonos.add(i);
			}
		}
		return telefonos;
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////
	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNIT() {
		return NIT;
	}
}
