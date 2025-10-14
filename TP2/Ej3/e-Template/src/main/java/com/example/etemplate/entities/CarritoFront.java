package com.example.etemplate.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;


public class CarritoFront extends CarritoTemplate{

    private List<DetalleFront> detallesFront = new ArrayList<>();
    private Usuario usuario;


    @Override
    protected void seleccionarProductos() {

    }



    public double getTotal(){
        calcularTotal();
        return this.total;
    }

    @Override
    protected void calcularTotal() {
        this.total = detallesFront.stream()
                .mapToDouble(d -> d.getPrecio() * d.getCantidad())
                .sum();
    }



    public List<DetalleFront> getDetallesFront(){
        return this.detallesFront;
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


    public void setDetallesFront(ArrayList<DetalleFront> detallesFront) {
        this.detallesFront = detallesFront;
    }
}
