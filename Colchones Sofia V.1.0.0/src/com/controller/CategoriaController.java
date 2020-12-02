package com.controller;

import java.util.ArrayList;
import java.util.List;

import com.model.*;
import com.model.other.*;
import com.util.*;

/**
 * Implementation CategoriaDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class CategoriaController extends Conexion<Categoria> {

	///////////////////////////////////////////////////////
	// Builders
	///////////////////////////////////////////////////////
	public CategoriaController() {
		super(Categoria.class);
	}

	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	/**
	 * Metodo que permite conocer la cantidad de productos por categoria.
	 * @return la lista con el resultado obtenido.
	 */
	public List<ChartJS> productoCategoria(){
		List<ChartJS> list = new ArrayList<ChartJS>();
		List<Categoria> categorias = findByFieldList("estado", true);
		for(Categoria c:  categorias) {
			ChartJS charj= new ChartJS();
			charj.setNombre(c.getNombre());
			charj.setCantidad(c.getProductos().size());
			list.add(charj);
		}
		return list;
	}
}