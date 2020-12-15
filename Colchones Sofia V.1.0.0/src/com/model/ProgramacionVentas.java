/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author OMAR
 */
@Entity
@Table(name = "programacion_ventas")
@NamedQuery(name="ProgramacionVentas.findAll", query="SELECT d FROM ProgramacionVentas d")
public class ProgramacionVentas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    private Integer estado = 0;
    
    @Transient
    private String estadoName;
    
    @Column(name = "numero_dias")
    private Integer numeroDias;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getNumeroDias() {
        return numeroDias;
    }

    public void setNumeroDias(Integer numeroDias) {
        this.numeroDias = numeroDias;
    }
    
    public String getEstadoName() {
        switch (estado) {
            case 0:
                return "Despacho";
            case 1:
                return "Entregado";
        }
        return "";
    }

}
