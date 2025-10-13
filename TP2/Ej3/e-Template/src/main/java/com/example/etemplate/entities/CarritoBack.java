package com.example.etemplate.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BACK")
public class CarritoBack extends CarritoTemplate{

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
