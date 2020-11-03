package com.dao;

import com.entity.*;
import com.util.*;


public class DetalleProductoDao extends Conexion<DetalleProducto> implements Interface<DetalleProducto> {
	
	public DetalleProductoDao() {
		super(DetalleProducto.class);
	}
}
