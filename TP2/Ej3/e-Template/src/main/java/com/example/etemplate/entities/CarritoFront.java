package com.example.etemplate.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("FRONT")
public class CarritoFront extends CarritoTemplate{

    @Override
    protected void seleccionarProductos() {

    }

    @Override
    protected void calcularTotal() {

    }

    @Override
    protected void procesarPago() {

    }

    @Override
    protected void confirmarCompra() {

    }
}
