package com.controller;

import java.util.List;

import javax.persistence.Query;

import com.model.*;
import com.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.hibernate.query.NativeQuery;

/**
 * Implementation EstadoVentaDao.
 *
 * @author DeveUp.
 * @phone 3118398189.
 * @email deveup@gmail.com.
 * @version 1.0.0.0.
 */
public class EstadoVentaController extends Conexion<EstadoVenta> {

///////////////////////////////////////////////////////
// Builders
///////////////////////////////////////////////////////
    public EstadoVentaController() {
        super(EstadoVenta.class);
    }

    @SuppressWarnings({"unchecked"})
    public void consultaProveedor(int id) {

    }

    public static void main(String[] args) {

        EstadoVentaController e = new EstadoVentaController();

        e.consultaProveedor(1);

    }

}
