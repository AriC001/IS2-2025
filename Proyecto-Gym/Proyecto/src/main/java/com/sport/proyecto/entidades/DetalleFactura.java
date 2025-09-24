package com.sport.proyecto.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Entity
public class DetalleFactura implements Serializable {

    @Id
    private String id;
    @ManyToOne
    private CuotaMensual cuotaMensual;

    private boolean eliminado;

    @ManyToOne
    private Factura factura;

}
