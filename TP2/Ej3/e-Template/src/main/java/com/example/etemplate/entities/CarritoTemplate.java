package com.example.etemplate.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class CarritoTemplate implements SoftDeletable {
    protected double total;
    protected boolean deleted;




    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }




    // Método plantilla (template method)
    public final void procesarCompra() {
        seleccionarProductos();
        calcularTotal();
        procesarPago();
        confirmarCompra();
    }

    protected abstract void seleccionarProductos();
    protected abstract void calcularTotal();
    protected abstract void procesarPago();
    protected abstract void confirmarCompra();

}
