package com.dao;

import com.entity.*;
import com.util.*;

/**
 * Implementation TipoPagoDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class TipoPagoDao extends Conexion<TipoPago> implements Interface<TipoPago> {

	public TipoPagoDao() {
		super(TipoPago.class);
	}
}
