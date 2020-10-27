package com.bean.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.optionconfig.title.Title;

import com.entity.other.*;
import com.util.*;
import com.dao.*;

/**
 * Implementation ChartJsBean.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
@ManagedBean(name = "chartjs")
@RequestScoped
public class ChartJsBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private DonutChartModel donut_vendedor_venta_genero;

	private HorizontalBarChartModel horizontal_chart_model_ventas_mensuales;
	private HorizontalBarChartModel horizontal_chart_model_ventas_anuales;
	
	private Color colores;

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public ChartJsBean() {
	}

	///////////////////////////////////////////////////////
	// Post
	///////////////////////////////////////////////////////
	@PostConstruct
	public void init() {
		this.colores = new Color();
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////

	///////////////////////////////////////////////////////
	// Method - DonutChartModel
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite saber cuantas vendedores por genero trabajan en la
	 * empresa.
	 */
	public void donutVendedorVentaGenero() {
		// INIT
		donut_vendedor_venta_genero = new DonutChartModel();
		ChartData data = new ChartData();
		int index = 0;
		DonutChartDataSet dataSet = new DonutChartDataSet();

		// SQL
		VendedorDao dao = new VendedorDao();
		List<ChartJS> list = dao.cantidadGenero();

		// LIST
		List<Number> values = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		List<String> bgColors = new ArrayList<>();

		// ITERRAR
		for (ChartJS c : list) {
			values.add(c.getCantidad());
			labels.add(Convertidor.genero(c.getNombre()));
			bgColors.add(this.colores.getColores().get(index));
			index++;
		}

		// ADD LIST
		data.setLabels(labels);
		dataSet.setData(values);
		dataSet.setBackgroundColor(bgColors);

		// ADD
		data.addChartDataSet(dataSet);
		donut_vendedor_venta_genero.setData(data);
	}

	///////////////////////////////////////////////////////
	// Method - HorizontalBarChartModel
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer la plata recibida en las ventas mensuales.
	 */
	public void horizontalChartModelVentasMensuales() {
		// INIT
		horizontal_chart_model_ventas_mensuales = new HorizontalBarChartModel();
		ChartData data = new ChartData();
		Fecha fecha = new Fecha();
		int index = 0;
		String anio = String.valueOf(fecha.anioActual());
		HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();

		/* TITLE */
		hbarDataSet.setLabel("Ventas " + anio);

		// SQL
		VentaDao dao = new VentaDao();
		List<ChartJS> list = dao.ventasMes(anio);

		// LIST
		List<Number> values = new ArrayList<Number>();
		List<String> labels = new ArrayList<String>();
		List<String> bgColors = new ArrayList<String>();
		List<String> bgBorders = new ArrayList<String>();

		// ITERRAR
		for (ChartJS c : list) {
			values.add(c.getTotal());
			labels.add(c.getNombre());
			bgColors.add(this.colores.getColores().get(index));
			bgBorders.add(this.colores.getBordes().get(index));
			index++;
		}

		// ADD LIST
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColors);
		hbarDataSet.setBorderColor(bgBorders);
		hbarDataSet.setBorderWidth(1);
		data.addChartDataSet(hbarDataSet);

		data.setLabels(labels);
		horizontal_chart_model_ventas_mensuales.setData(data);

		// OPTIONS
		BarChartOptions options = new BarChartOptions();
		CartesianScales cScales = new CartesianScales();
		CartesianLinearAxes linearAxes = new CartesianLinearAxes();
		CartesianLinearTicks ticks = new CartesianLinearTicks();
		ticks.setBeginAtZero(true);
		linearAxes.setTicks(ticks);
		cScales.addXAxesData(linearAxes);
		options.setScales(cScales);

		Title title = new Title();
		title.setDisplay(true);
		title.setText("Ventas");
		options.setTitle(title);

		horizontal_chart_model_ventas_mensuales.setOptions(options);

	}
	
	/**
	 * Metodo que permite conocer la plata recibida en las ventas mensuales.
	 */
	public void horizontalChartModelVentasAnuales() {
		// INIT
		horizontal_chart_model_ventas_anuales = new HorizontalBarChartModel();
		ChartData data = new ChartData();
		int index = 0;
		HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();

		/* TITLE */
		hbarDataSet.setLabel("Ventas Anuales");

		// SQL
		VentaDao dao = new VentaDao();
		List<ChartJS> list = dao.ventasAnuales();

		// LIST
		List<Number> values = new ArrayList<Number>();
		List<String> labels = new ArrayList<String>();
		List<String> bgColors = new ArrayList<String>();
		List<String> bgBorders = new ArrayList<String>();

		// ITERRAR
		for (ChartJS c : list) {
			values.add(c.getTotal());
			labels.add(c.getNombre());
			bgColors.add(this.colores.getColores().get(index));
			bgBorders.add(this.colores.getBordes().get(index));
			index++;
		}

		// ADD LIST
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColors);
		hbarDataSet.setBorderColor(bgBorders);
		hbarDataSet.setBorderWidth(1);
		data.addChartDataSet(hbarDataSet);

		data.setLabels(labels);
		horizontal_chart_model_ventas_anuales.setData(data);

		// OPTIONS
		BarChartOptions options = new BarChartOptions();
		CartesianScales cScales = new CartesianScales();
		CartesianLinearAxes linearAxes = new CartesianLinearAxes();
		CartesianLinearTicks ticks = new CartesianLinearTicks();
		ticks.setBeginAtZero(true);
		linearAxes.setTicks(ticks);
		cScales.addXAxesData(linearAxes);
		options.setScales(cScales);

		Title title = new Title();
		title.setDisplay(true);
		title.setText("Ventas");
		options.setTitle(title);

		horizontal_chart_model_ventas_anuales.setOptions(options);

	}

	///////////////////////////////////////////////////////
	// Renderizar - DonutChartModel
	///////////////////////////////////////////////////////

	public DonutChartModel getDonut_vendedor_venta_genero() {
		this.donutVendedorVentaGenero();
		return donut_vendedor_venta_genero;
	}

	///////////////////////////////////////////////////////
	// Renderizar - HorizontalBarChartModel
	///////////////////////////////////////////////////////

	public HorizontalBarChartModel getHorizontal_chart_model_ventas_mensuales() {
		this.horizontalChartModelVentasMensuales();
		return horizontal_chart_model_ventas_mensuales;
	}
	
	public HorizontalBarChartModel getHorizontal_chart_model_ventas_anuales() {
		this.horizontalChartModelVentasAnuales();
		return horizontal_chart_model_ventas_anuales;
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setDonut_vendedor_venta_genero(DonutChartModel donut_vendedor_venta_genero) {
		this.donut_vendedor_venta_genero = donut_vendedor_venta_genero;
	}

	public Color getColores() {
		return colores;
	}

	public void setColores(Color colores) {
		this.colores = colores;
	}

	public void setHorizontal_chart_model_ventas_mensuales(
			HorizontalBarChartModel horizontal_chart_model_ventas_mensuales) {
		this.horizontal_chart_model_ventas_mensuales = horizontal_chart_model_ventas_mensuales;
	}

	public void setHorizontal_chart_model_ventas_anuales(HorizontalBarChartModel horizontal_chart_model_ventas_anuales) {
		this.horizontal_chart_model_ventas_anuales = horizontal_chart_model_ventas_anuales;
	}
}
