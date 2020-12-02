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
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

import com.controller.*;
import com.model.other.*;
import com.util.*;

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

	private PieChartModel pie_venta_unidad_producto;
	private PieChartModel pie_compra_unidad_producto;
	private PieChartModel pie_proveedor_producto;
	private PieChartModel pie_proveedor_cantidad_compras;
	private PieChartModel pie_cliente_genero;
	private PieChartModel pie_asistente_genero;

	private BarChartModel bar_cliente_cantidad_venta;
	private BarChartModel bar_cliente_total_venta;

	private HorizontalBarChartModel horizontal_chart_model_ventas_mensuales;
	private HorizontalBarChartModel horizontal_chart_model_ventas_anuales;
	private HorizontalBarChartModel horizontal_chart_model_compras_mensuales;
	private HorizontalBarChartModel horizontal_chart_model_compras_anuales;
	private HorizontalBarChartModel horizontal_chart_model_categoria_cantidad_productos;

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
	// Method - BarChartModel
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer el numero de ventas realizas por los clientes.
	 */
	public void barClienteCantidadVenta() {
		this.bar_cliente_cantidad_venta = new BarChartModel();
		ChartData data = new ChartData();

		BarChartDataSet barDataSet = new BarChartDataSet();
		barDataSet.setLabel("Cantidad Ventas");

		// SQL
		UsuarioController dao = new UsuarioController();
		List<ChartJS> list = dao.ventasCliente();

		int index_color = 0;
		int index_border = 0;

		// LIST
		List<Number> values = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		List<String> bgColors = new ArrayList<>();
		List<String> bgBorders = new ArrayList<String>();

		// ITERRAR
		for (ChartJS c : list) {
			values.add(c.getCantidad());
			labels.add(c.getNombre());

			bgColors.add(this.colores.getColores().get(index_color));
			bgBorders.add(this.colores.getBordes().get(index_border));

			if ((index_color + 1) >= this.colores.getColores().size()) {
				index_color = 0;
			} else {
				index_color++;
			}

			if ((index_border + 1) >= this.colores.getBordes().size()) {
				index_border = 0;
			} else {
				index_border++;
			}
		}

		// ADD LIST
		barDataSet.setData(values);
		barDataSet.setBackgroundColor(bgColors);
		barDataSet.setBorderColor(bgBorders);
		barDataSet.setBorderWidth(1);
		data.addChartDataSet(barDataSet);

		data.setLabels(labels);
		this.bar_cliente_cantidad_venta.setData(data);

		// Options
		BarChartOptions options = new BarChartOptions();
		CartesianScales cScales = new CartesianScales();
		CartesianLinearAxes linearAxes = new CartesianLinearAxes();
		CartesianLinearTicks ticks = new CartesianLinearTicks();
		ticks.setBeginAtZero(true);
		linearAxes.setTicks(ticks);
		cScales.addYAxesData(linearAxes);
		options.setScales(cScales);

		Title title = new Title();
		title.setDisplay(true);
		title.setText("Clientes");
		options.setTitle(title);

		Legend legend = new Legend();
		legend.setDisplay(true);
		legend.setPosition("top");
		LegendLabel legendLabels = new LegendLabel();
		legendLabels.setFontStyle("bold");
		legendLabels.setFontColor("#2980B9");
		legendLabels.setFontSize(24);
		legend.setLabels(legendLabels);
		options.setLegend(legend);

		this.bar_cliente_cantidad_venta.setOptions(options);
	}

	/**
	 * Metodo que permite conocer la plata que deja los clientes en la empresa.
	 */
	public void barClienteTotalVenta() {
		this.bar_cliente_total_venta = new BarChartModel();
		ChartData data = new ChartData();

		BarChartDataSet barDataSet = new BarChartDataSet();
		barDataSet.setLabel("Dinero Ventas");

		int index_color = 0;
		int index_border = 0;

		// SQL
		UsuarioController dao = new UsuarioController();
		List<ChartJS> list = dao.ventasCliente();

		// LIST
		List<Number> values = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		List<String> bgColors = new ArrayList<>();
		List<String> bgBorders = new ArrayList<String>();

		// ITERRAR
		for (ChartJS c : list) {
			values.add(c.getTotal());
			labels.add(c.getNombre());

			bgColors.add(this.colores.getColores().get(index_color));
			bgBorders.add(this.colores.getBordes().get(index_border));

			if ((index_color + 1) >= this.colores.getColores().size()) {
				index_color = 0;
			} else {
				index_color++;
			}

			if ((index_border + 1) >= this.colores.getBordes().size()) {
				index_border = 0;
			} else {
				index_border++;
			}
		}

		// ADD LIST
		barDataSet.setData(values);
		barDataSet.setBackgroundColor(bgColors);
		barDataSet.setBorderColor(bgBorders);
		barDataSet.setBorderWidth(1);
		data.addChartDataSet(barDataSet);

		data.setLabels(labels);
		this.bar_cliente_total_venta.setData(data);

		// Options
		BarChartOptions options = new BarChartOptions();
		CartesianScales cScales = new CartesianScales();
		CartesianLinearAxes linearAxes = new CartesianLinearAxes();
		CartesianLinearTicks ticks = new CartesianLinearTicks();
		ticks.setBeginAtZero(true);
		linearAxes.setTicks(ticks);
		cScales.addYAxesData(linearAxes);
		options.setScales(cScales);

		Title title = new Title();
		title.setDisplay(true);
		title.setText("Clientes");
		options.setTitle(title);

		Legend legend = new Legend();
		legend.setDisplay(true);
		legend.setPosition("top");
		LegendLabel legendLabels = new LegendLabel();
		legendLabels.setFontStyle("bold");
		legendLabels.setFontColor("#2980B9");
		legendLabels.setFontSize(24);
		legend.setLabels(legendLabels);
		options.setLegend(legend);

		this.bar_cliente_total_venta.setOptions(options);
	}

	///////////////////////////////////////////////////////
	// Method - PieChartModel
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer la cantidad de asistentes por genero.
	 */
	public void pieAsistenteGenero() {
		// INIT
		pie_asistente_genero = new PieChartModel();
		ChartData data = new ChartData();

		PieChartDataSet dataSet = new PieChartDataSet();

		int index_color = 0;

		// SQL
		UsuarioController dao = new UsuarioController();
		List<ChartJS> list = dao.generoRol("ASISTENTE ADMINISTRATIVO");

		// LIST
		List<Number> values = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		List<String> bgColors = new ArrayList<>();

		// ITERRAR
		for (ChartJS c : list) {
			values.add(c.getCantidad());
			labels.add(Convertidor.genero(c.getNombre()));
			bgColors.add(this.colores.getColores().get(index_color));

			if ((index_color + 1) >= this.colores.getColores().size()) {
				index_color = 0;
			} else {
				index_color++;
			}
		}

		// ADD LIST
		data.setLabels(labels);
		dataSet.setData(values);
		dataSet.setBackgroundColor(bgColors);

		// ADD
		data.addChartDataSet(dataSet);
		pie_asistente_genero.setData(data);
	}

	/**
	 * Metodo que permite consultar los clientes por su genero.
	 */
	public void pieClienteGenero() {
		// INIT
		pie_cliente_genero = new PieChartModel();
		ChartData data = new ChartData();

		PieChartDataSet dataSet = new PieChartDataSet();

		int index_color = 0;

		// SQL
		UsuarioController dao = new UsuarioController();
		List<ChartJS> list = dao.generoRol("CLIENTE");

		// LIST
		List<Number> values = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		List<String> bgColors = new ArrayList<>();

		// ITERRAR
		for (ChartJS c : list) {
			values.add(c.getCantidad());
			labels.add(Convertidor.genero(c.getNombre()));
			bgColors.add(this.colores.getColores().get(index_color));

			if ((index_color + 1) >= this.colores.getColores().size()) {
				index_color = 0;
			} else {
				index_color++;
			}
		}

		// ADD LIST
		data.setLabels(labels);
		dataSet.setData(values);
		dataSet.setBackgroundColor(bgColors);

		// ADD
		data.addChartDataSet(dataSet);
		pie_cliente_genero.setData(data);
	}
	/**
	 * Metodo que permite saber cuantas unidades se han vendido de un producto.
	 */
	public void pieVendedorVentaUnidadProducto() {
		// INIT
		pie_venta_unidad_producto = new PieChartModel();
		ChartData data = new ChartData();

		PieChartDataSet dataSet = new PieChartDataSet();

		int index_color = 0;

		// SQL
		VentaController dao = new VentaController();
		List<ChartJS> list = dao.productos();

		// LIST
		List<Number> values = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		List<String> bgColors = new ArrayList<>();

		// ITERRAR
		for (ChartJS c : list) {
			values.add(c.getCantidad());
			labels.add(c.getNombre());
			bgColors.add(this.colores.getColores().get(index_color));

			if ((index_color + 1) >= this.colores.getColores().size()) {
				index_color = 0;
			} else {
				index_color++;
			}
		}

		// ADD LIST
		data.setLabels(labels);
		dataSet.setData(values);
		dataSet.setBackgroundColor(bgColors);

		// ADD
		data.addChartDataSet(dataSet);
		pie_venta_unidad_producto.setData(data);
	}

	/**
	 * Metodo que permite saber cuantas unidades se han vendido de un producto.
	 */
	public void pieCompraUnidadProducto() {
		// INIT
		pie_compra_unidad_producto = new PieChartModel();
		ChartData data = new ChartData();

		PieChartDataSet dataSet = new PieChartDataSet();

		int index_color = 0;

		// SQL
		CompraController dao = new CompraController();
		List<ChartJS> list = dao.productos();

		// LIST
		List<Number> values = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		List<String> bgColors = new ArrayList<>();

		// ITERRAR
		for (ChartJS c : list) {
			values.add(c.getCantidad());
			labels.add(c.getNombre());
			bgColors.add(this.colores.getColores().get(index_color));

			if ((index_color + 1) >= this.colores.getColores().size()) {
				index_color = 0;
			} else {
				index_color++;
			}
		}

		// ADD LIST
		data.setLabels(labels);
		dataSet.setData(values);
		dataSet.setBackgroundColor(bgColors);

		// ADD
		data.addChartDataSet(dataSet);
		pie_compra_unidad_producto.setData(data);
	}

	/**
	 * Metodo que permite saber la cantidad de productos que tiene asosiado un
	 * proveedor.
	 */
	public void pieProveedorProducto() {
		// INIT
		pie_proveedor_producto = new PieChartModel();
		ChartData data = new ChartData();

		PieChartDataSet dataSet = new PieChartDataSet();

		int index_color = 0;

		// SQL
		ProveedorController dao = new ProveedorController();
		List<ChartJS> list = dao.proveedorProductos();

		// LIST
		List<Number> values = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		List<String> bgColors = new ArrayList<>();

		// ITERRAR
		for (ChartJS c : list) {
			values.add(c.getCantidad());
			labels.add(c.getNombre());
			bgColors.add(this.colores.getColores().get(index_color));

			if ((index_color + 1) >= this.colores.getColores().size()) {
				index_color = 0;
			} else {
				index_color++;
			}
		}

		// ADD LIST
		data.setLabels(labels);
		dataSet.setData(values);
		dataSet.setBackgroundColor(bgColors);

		// ADD
		data.addChartDataSet(dataSet);
		pie_proveedor_producto.setData(data);
	}

	/**
	 * Metodo que permite saber la cantidad de compras que tiene asosiado un
	 * proveedor.
	 */
	public void pieProveedorCantidadCompra() {
		// INIT
		pie_proveedor_cantidad_compras = new PieChartModel();
		ChartData data = new ChartData();

		PieChartDataSet dataSet = new PieChartDataSet();

		int index_color = 0;

		// SQL
		ProveedorController dao = new ProveedorController();
		List<ChartJS> list = dao.cantidadCompras();

		// LIST
		List<Number> values = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		List<String> bgColors = new ArrayList<>();

		// ITERRAR
		for (ChartJS c : list) {
			values.add(c.getCantidad());
			labels.add(c.getNombre());
			bgColors.add(this.colores.getColores().get(index_color));

			if ((index_color + 1) >= this.colores.getColores().size()) {
				index_color = 0;
			} else {
				index_color++;
			}
		}

		// ADD LIST
		data.setLabels(labels);
		dataSet.setData(values);
		dataSet.setBackgroundColor(bgColors);

		// ADD
		data.addChartDataSet(dataSet);
		pie_proveedor_cantidad_compras.setData(data);
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

		String anio = String.valueOf(fecha.anioActual());
		HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();

		int index_color = 0;
		int index_border = 0;

		/* TITLE */
		hbarDataSet.setLabel("Ventas " + anio);

		// SQL
		VentaController dao = new VentaController();
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
			bgColors.add(this.colores.getColores().get(index_color));
			bgBorders.add(this.colores.getBordes().get(index_border));

			if ((index_color + 1) >= this.colores.getColores().size()) {
				index_color = 0;
			} else {
				index_color++;
			}

			if ((index_border + 1) >= this.colores.getBordes().size()) {
				index_border = 0;
			} else {
				index_border++;
			}
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

		HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();

		int index_color = 0;
		int index_border = 0;

		/* TITLE */
		hbarDataSet.setLabel("Ventas Anuales");

		// SQL
		VentaController dao = new VentaController();
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
			bgColors.add(this.colores.getColores().get(index_color));
			bgBorders.add(this.colores.getBordes().get(index_border));

			if ((index_color + 1) >= this.colores.getColores().size()) {
				index_color = 0;
			} else {
				index_color++;
			}

			if ((index_border + 1) >= this.colores.getBordes().size()) {
				index_border = 0;
			} else {
				index_border++;
			}
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

	/**
	 * Metodo que permite conocer la plata recibida en las ventas mensuales.
	 */
	public void horizontalChartModelComprasMensuales() {
		// INIT
		horizontal_chart_model_compras_mensuales = new HorizontalBarChartModel();
		ChartData data = new ChartData();
		Fecha fecha = new Fecha();

		String anio = String.valueOf(fecha.anioActual());
		HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();

		int index_color = 0;
		int index_border = 0;

		/* TITLE */
		hbarDataSet.setLabel("Compra " + anio);

		// SQL
		CompraController dao = new CompraController();
		List<ChartJS> list = dao.compraMes(anio);

		// LIST
		List<Number> values = new ArrayList<Number>();
		List<String> labels = new ArrayList<String>();
		List<String> bgColors = new ArrayList<String>();
		List<String> bgBorders = new ArrayList<String>();

		// ITERRAR
		for (ChartJS c : list) {
			values.add(c.getTotal());
			labels.add(c.getNombre());
			bgColors.add(this.colores.getColores().get(index_color));
			bgBorders.add(this.colores.getBordes().get(index_border));

			if ((index_color + 1) >= this.colores.getColores().size()) {
				index_color = 0;
			} else {
				index_color++;
			}

			if ((index_border + 1) >= this.colores.getBordes().size()) {
				index_border = 0;
			} else {
				index_border++;
			}
		}

		// ADD LIST
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColors);
		hbarDataSet.setBorderColor(bgBorders);
		hbarDataSet.setBorderWidth(1);
		data.addChartDataSet(hbarDataSet);

		data.setLabels(labels);
		horizontal_chart_model_compras_mensuales.setData(data);

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
		title.setText("Compra");
		options.setTitle(title);

		horizontal_chart_model_compras_mensuales.setOptions(options);
	}

	/**
	 * Metodo que permite conocer la plata recibida en las ventas mensuales.
	 */
	public void horizontalChartModelComprasAnuales() {
		// INIT
		horizontal_chart_model_compras_anuales = new HorizontalBarChartModel();
		ChartData data = new ChartData();
		HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();

		int index_color = 0;
		int index_border = 0;

		/* TITLE */
		hbarDataSet.setLabel("Compras Anuales");

		// SQL
		CompraController dao = new CompraController();
		List<ChartJS> list = dao.comprasAnuales();

		// LIST
		List<Number> values = new ArrayList<Number>();
		List<String> labels = new ArrayList<String>();
		List<String> bgColors = new ArrayList<String>();
		List<String> bgBorders = new ArrayList<String>();

		// ITERRAR
		for (ChartJS c : list) {
			values.add(c.getTotal());
			labels.add(c.getNombre());
			bgColors.add(this.colores.getColores().get(index_color));
			bgBorders.add(this.colores.getBordes().get(index_border));

			if ((index_color + 1) >= this.colores.getColores().size()) {
				index_color = 0;
			} else {
				index_color++;
			}

			if ((index_border + 1) >= this.colores.getBordes().size()) {
				index_border = 0;
			} else {
				index_border++;
			}
		}

		// ADD LIST
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColors);
		hbarDataSet.setBorderColor(bgBorders);
		hbarDataSet.setBorderWidth(1);
		data.addChartDataSet(hbarDataSet);

		data.setLabels(labels);
		horizontal_chart_model_compras_anuales.setData(data);

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
		title.setText("Compra");
		options.setTitle(title);

		horizontal_chart_model_compras_anuales.setOptions(options);
	}

	/**
	 * Metodo que permite conocer la cantidad de productos que tiene una categoria.
	 */
	public void horizontalChartModelCategoriaCantidadProductos() {
		// INIT
		horizontal_chart_model_categoria_cantidad_productos = new HorizontalBarChartModel();
		ChartData data = new ChartData();

		HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();

		int index_color = 0;
		int index_border = 0;

		/* TITLE */
		hbarDataSet.setLabel("Cantidad Productos");

		// SQL
		CategoriaController dao = new CategoriaController();
		List<ChartJS> list = dao.productoCategoria();

		// LIST
		List<Number> values = new ArrayList<Number>();
		List<String> labels = new ArrayList<String>();
		List<String> bgColors = new ArrayList<String>();
		List<String> bgBorders = new ArrayList<String>();

		// ITERRAR
		for (ChartJS c : list) {
			values.add(c.getCantidad());
			labels.add(c.getNombre());
			bgColors.add(this.colores.getColores().get(index_color));
			bgBorders.add(this.colores.getBordes().get(index_border));

			if ((index_color + 1) >= this.colores.getColores().size()) {
				index_color = 0;
			} else {
				index_color++;
			}

			if ((index_border + 1) >= this.colores.getBordes().size()) {
				index_border = 0;
			} else {
				index_border++;
			}
		}

		// ADD LIST
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColors);
		hbarDataSet.setBorderColor(bgBorders);
		hbarDataSet.setBorderWidth(1);
		data.addChartDataSet(hbarDataSet);

		data.setLabels(labels);
		horizontal_chart_model_categoria_cantidad_productos.setData(data);

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
		title.setText("Categoria");
		options.setTitle(title);

		horizontal_chart_model_categoria_cantidad_productos.setOptions(options);
	}


	///////////////////////////////////////////////////////
	// Renderizar - BarChartModel
	///////////////////////////////////////////////////////

	public BarChartModel getBar_cliente_cantidad_venta() {
		this.barClienteCantidadVenta();
		return bar_cliente_cantidad_venta;
	}

	public BarChartModel getBar_cliente_total_venta() {
		this.barClienteTotalVenta();
		return bar_cliente_total_venta;
	}

	///////////////////////////////////////////////////////
	// Renderizar - PieChartModel
	///////////////////////////////////////////////////////
	public PieChartModel getPie_venta_unidad_producto() {
		this.pieVendedorVentaUnidadProducto();
		return pie_venta_unidad_producto;
	}

	public PieChartModel getPie_compra_unidad_producto() {
		this.pieCompraUnidadProducto();
		return pie_compra_unidad_producto;
	}

	public PieChartModel getPie_proveedor_producto() {
		this.pieProveedorProducto();
		return pie_proveedor_producto;
	}

	public PieChartModel getPie_proveedor_cantidad_compras() {
		this.pieProveedorCantidadCompra();
		return pie_proveedor_cantidad_compras;
	}
	
	public PieChartModel getPie_asistente_genero() {
		this.pieAsistenteGenero();
		return pie_asistente_genero;
	}
	
	public PieChartModel getPie_cliente_genero() {
		this.pieClienteGenero();
		return pie_cliente_genero;
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

	public HorizontalBarChartModel getHorizontal_chart_model_compras_anuales() {
		this.horizontalChartModelComprasAnuales();
		return horizontal_chart_model_compras_anuales;
	}

	public HorizontalBarChartModel getHorizontal_chart_model_compras_mensuales() {
		this.horizontalChartModelComprasMensuales();
		return horizontal_chart_model_compras_mensuales;
	}

	public HorizontalBarChartModel getHorizontal_chart_model_categoria_cantidad_productos() {
		this.horizontalChartModelCategoriaCantidadProductos();
		return horizontal_chart_model_categoria_cantidad_productos;
	}

	///////////////////////////////////////////////////////
	// Getter y Setters
	///////////////////////////////////////////////////////

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setPie_cliente_genero(PieChartModel pie_cliente_genero) {
		this.pie_cliente_genero = pie_cliente_genero;
	}

	public void setPie_asistente_genero(PieChartModel pie_asistente_genero) {
		this.pie_asistente_genero = pie_asistente_genero;
	}

	public Color getColores() {
		return colores;
	}

	public void setColores(Color colores) {
		this.colores = colores;
	}

	public void setHorizontal_chart_model_compras_mensuales(
			HorizontalBarChartModel horizontal_chart_model_compras_mensuales) {
		this.horizontal_chart_model_compras_mensuales = horizontal_chart_model_compras_mensuales;
	}

	public void setHorizontal_chart_model_compras_anuales(
			HorizontalBarChartModel horizontal_chart_model_compras_anuales) {
		this.horizontal_chart_model_compras_anuales = horizontal_chart_model_compras_anuales;
	}

	public void setPie_venta_unidad_producto(PieChartModel pie_venta_unidad_producto) {
		this.pie_venta_unidad_producto = pie_venta_unidad_producto;
	}

	public void setPie_compra_unidad_producto(PieChartModel pie_compra_unidad_producto) {
		this.pie_compra_unidad_producto = pie_compra_unidad_producto;
	}

	public void setHorizontal_chart_model_ventas_mensuales(
			HorizontalBarChartModel horizontal_chart_model_ventas_mensuales) {
		this.horizontal_chart_model_ventas_mensuales = horizontal_chart_model_ventas_mensuales;
	}

	public void setHorizontal_chart_model_ventas_anuales(
			HorizontalBarChartModel horizontal_chart_model_ventas_anuales) {
		this.horizontal_chart_model_ventas_anuales = horizontal_chart_model_ventas_anuales;
	}

	public void setBar_cliente_cantidad_venta(BarChartModel bar_cliente_cantidad_venta) {
		this.bar_cliente_cantidad_venta = bar_cliente_cantidad_venta;
	}

	public void setBar_cliente_total_venta(BarChartModel bar_cliente_total_venta) {
		this.bar_cliente_total_venta = bar_cliente_total_venta;
	}

	public void setPie_proveedor_producto(PieChartModel pie_proveedor_producto) {
		this.pie_proveedor_producto = pie_proveedor_producto;
	}

	public void setPie_proveedor_cantidad_compras(PieChartModel pie_proveedor_cantidad_compras) {
		this.pie_proveedor_cantidad_compras = pie_proveedor_cantidad_compras;
	}

	public void setHorizontal_chart_model_categoria_cantidad_productos(
			HorizontalBarChartModel horizontal_chart_model_categoria_cantidad_productos) {
		this.horizontal_chart_model_categoria_cantidad_productos = horizontal_chart_model_categoria_cantidad_productos;
	}
}
