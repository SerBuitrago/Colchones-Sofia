package com.entity.other;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.entity.Usuario;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;

/**
 * Implementation Reporte.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@SuppressWarnings("deprecation")
public class Reporte<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<T> entidad;
	private String ruta;
	private Usuario usuario;

	private JasperPrint jasperPrint;
	private JasperReport jrxml;
	private Map<String, Object> parametros;
	private File jasper;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public Reporte() {
		this(null, null, new Usuario());
	}

	public Reporte(List<T> entidad, String ruta, Usuario usuario) {
		super();
		this.entidad = entidad;
		this.ruta = ruta;
		this.usuario = usuario;
		if (usuario != null) {
			System.out.println("RUTA: " + ruta);
		}

	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metood que permite inicializar el reporte.
	 */
	public void init() throws JRException {
		// jrxml =
		// JasperCompileManager.compileReport(FacesContext.getCurrentInstance().getExternalContext().getRealPath(this.ruta));
		parametros = new HashMap<String, Object>();
		parametros.put("txtUsuario",
				String.valueOf(this.usuario.getPersona().getNombre() + " " + this.usuario.getPersona().getApellido()));
		jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath(this.ruta));
		jasperPrint = JasperFillManager.fillReport(jrxml, parametros, new JRBeanCollectionDataSource(this.entidad));
	}

	/**
	 * Metodo que permite descargar el reporte en PDF.
	 */
	public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException {
		this.init();

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		response.addHeader("Content-disposition", "attachment; filename=jsfReporte.pdf");
		ServletOutputStream stream = response.getOutputStream();

		JasperExportManager.exportReportToPdfStream(jasperPrint, stream);

		stream.flush();
		stream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

	/**
	 * Metodo que permite descargar el reporte en EXCEL.
	 */
	public void exportarExcel(ActionEvent actionEvent) throws JRException, IOException {
		this.init();

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		response.addHeader("Content-disposition", "attachment; filename=jsfReporte.xls");
		ServletOutputStream stream = response.getOutputStream();

		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
		exporter.exportReport();

		stream.flush();
		stream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

	/**
	 * Metodo que permite descargar el reporte en PPT.
	 */
	public void exportarPPT(ActionEvent actionEvent) throws JRException, IOException {
		this.init();

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		response.addHeader("Content-disposition", "attachment; filename=jsfReporte.ppt");
		ServletOutputStream stream = response.getOutputStream();

		JRPptxExporter exporter = new JRPptxExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
		exporter.exportReport();

		stream.flush();
		stream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

	/**
	 * Metodo que permite descargar el reporte en DOC.
	 */
	public void exportarDOC(ActionEvent actionEvent) throws JRException, IOException {
		this.init();

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		response.addHeader("Content-disposition", "attachment; filename=jsfReporte.doc");
		ServletOutputStream stream = response.getOutputStream();

		JRDocxExporter exporter = new JRDocxExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
		exporter.exportReport();

		stream.flush();
		stream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

	/**
	 * Metodo que permite ver el reporte en PDF.
	 */
	public void verPDF(ActionEvent actionEvent) throws Exception {
		this.init();

		File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath(this.ruta));
		byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), null,
				new JRBeanCollectionDataSource(this.entidad));
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);
		ServletOutputStream outStream = response.getOutputStream();
		outStream.write(bytes, 0, bytes.length);
		outStream.flush();
		outStream.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

	///////////////////////////////////////////////////////
	// Getter and Setters
	///////////////////////////////////////////////////////
	public List<T> getEntidad() {
		return entidad;
	}

	public void setEntidad(List<T> entidad) {
		this.entidad = entidad;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}

	public void setJasperPrint(JasperPrint jasperPrint) {
		this.jasperPrint = jasperPrint;
	}

	public Map<String, Object> getParametros() {
		return parametros;
	}

	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}

	public File getJasper() {
		return jasper;
	}

	public void setJasper(File jasper) {
		this.jasper = jasper;
	}

	public JasperReport getJrxml() {
		return jrxml;
	}

	public void setJrxml(JasperReport jrxml) {
		this.jrxml = jrxml;
	}
}
