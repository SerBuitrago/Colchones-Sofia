/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bean.view;

import com.controller.EmpresaInformacionController;
import com.controller.ProductoController;
import com.controller.ProgramacionVentasController;
import com.model.EmpresaInformacion;
import com.model.ProgramacionVentas;
import com.model.other.Convertidor;
import com.util.Face;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

/**
 *
 * @author OMAR
 */
@ManagedBean(name = "programacionVentas")
@ViewScoped
public class ProgramacionVentasBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ProgramacionVentas programacion;

    private List<ProgramacionVentas> programaciones;

    private int index_telefono;

    public ProgramacionVentasBean() {
    }

    ///////////////////////////////////////////////////////
    // Post
    ///////////////////////////////////////////////////////
    @PostConstruct
    public void init() {
        initProgramaciones();
    }

    ///////////////////////////////////////////////////////
    // Init
    ///////////////////////////////////////////////////////
    /**
     * Metodo que permite inicializar los productos.
     */
    public void initProgramaciones() {
        this.programacion = new ProgramacionVentas();
        ProgramacionVentasController dao = new ProgramacionVentasController();
        programaciones = dao.list();

    }

    public ProgramacionVentas getProgramacion() {
        return programacion;
    }

    public void setProgramacion(ProgramacionVentas programacion) {
        this.programacion = programacion;
    }

    public List<ProgramacionVentas> getProgramaciones() {
        return programaciones;
    }

    public void setProgramaciones(List<ProgramacionVentas> programaciones) {
        this.programaciones = programaciones;
    }

    public void statuEditarProgramacion() {
        this.index_telefono = -1;
        consultarTelefono();
        this.initDialogProgramacionForm(1);
    }

    ///////////////////////////////////////////////////////
    // Method Index
    ///////////////////////////////////////////////////////
    /**
     * Metodo que permite conocer una empresa información.
     */
    public int index(List<ProgramacionVentas> list, int id) {
        int ix = 0;
        for (ProgramacionVentas e : list) {
            if (e.getId() == id) {
                return ix;
            }
            ix++;
        }
        return -1;
    }

    ///////////////////////////////////////////////////////
    // Method Phone
    ///////////////////////////////////////////////////////
    /**
     * Metodo que permite consultar un telefono
     */
    public void consultarTelefono() {
        this.programacion = new ProgramacionVentas();
        this.index_telefono = -1;
        String id = Face.get("id-programacion");
        if (Convertidor.isCadena(id)) {
            int idInt = Integer.parseInt(id);
            index_telefono = index(this.programaciones, idInt);
            if (index_telefono >= 0) {
                ProgramacionVentas aux = this.programaciones.get(index_telefono);
                this.programacion.setId(aux.getId());
                this.programacion.setEstado(aux.getEstado());
                this.programacion.setNumeroDias(aux.getNumeroDias());
            }
        }
    }

    /**
     * Metodo que cambia el estado a dialogo de formulario.
     *
     * @param estado representa el estado.
     */
    public void initDialogProgramacionForm(int estado) {
        PrimeFaces current = PrimeFaces.current();
        switch (estado) {
            case 1:
                current.executeScript("PF('sofia-dialog-programacion-update').show();");
                break;
            case 2:
                current.executeScript("PF('sofia-dialog-programacion-update').hide();");
                break;
            default:
                break;
        }
    }

    /**
     * Metodo que permite editar un telefono.
     */
    public void editarProgramacion() {
        if (this.programacion != null && this.programacion.getId() > 0 && this.index_telefono >= 0) {
            ProgramacionVentasController dao = new ProgramacionVentasController();
            dao.update(programacion);
            this.programaciones.set(this.index_telefono, programacion);
            programacion = new ProgramacionVentas();
            initDialogProgramacionForm(2);
            this.index_telefono = -1;
        }
    }

}
