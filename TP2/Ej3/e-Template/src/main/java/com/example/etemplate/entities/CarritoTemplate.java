package com.example.etemplate.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_carrito", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class CarritoTemplate {
    @Id
    @Generated
    protected String id;
    protected double total;
    protected boolean deleted;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "carrito_id")
    protected List<Detalle> detalles;

    @OneToOne
    protected Usuario usuarios;


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
