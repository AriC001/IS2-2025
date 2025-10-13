package com.example.etemplate.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("FRONT")
public class CarritoFront extends CarritoTemplate{

    public CarritoFront(String id, double total, boolean deleted, List<Detalle> detalles, Usuario usuarios) {
        super(id, total, deleted, detalles, usuarios);
    }

    public CarritoFront() {
    }

    @Override
    protected void seleccionarProductos() {

    }

    @Override
    protected void calcularTotal() {
        this.total = detalles.stream()
                .mapToDouble(Detalle::getSubtotal)
                .sum();
    }

    @Override
    protected void procesarPago() {

    }

    @Override
    protected void confirmarCompra() {

    }
    @Override
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    @Override
    public boolean isDeleted() { return deleted; }


}
