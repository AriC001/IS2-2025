package com.example.etemplate.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.ArrayList;

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

    public CarritoBack finalizarCompra() {
        CarritoBack back = new CarritoBack();
        back.setDetalles(new ArrayList<>(this.detalles));
        back.setTotal(this.total);
        back.setUsuario(this.usuarios);
        return back;
    }

    @Override
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    @Override
    public boolean isDeleted() { return deleted; }
}
