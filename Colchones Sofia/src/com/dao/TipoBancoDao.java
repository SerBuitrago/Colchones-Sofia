package com.dao;

import com.entity.*;
import com.util.*;

/**
 * Implementation TipoBancoDao.
 * 
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class TipoBancoDao extends Conexion<TipoBanco> implements Interface<TipoBanco> {

	public TipoBancoDao() {
		super(TipoBanco.class);
	}
}
