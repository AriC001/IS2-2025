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
public abstract class CarritoTemplate implements SoftDeletable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;
    protected double total;
    protected boolean deleted;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "carrito_id")
    protected List<Detalle> detalles;

    @OneToOne
    protected Usuario usuarios;

    public CarritoTemplate() {}

    public CarritoTemplate(String id, double total, boolean deleted, List<Detalle> detalles, Usuario usuarios) {
        this.id = id;
        this.total = total;
        this.deleted = deleted;
        this.detalles = detalles;
        this.usuarios = usuarios;
    }

    public void setUsuario(Usuario usuarios) {
        this.usuarios = usuarios;
    }

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

    public List<Detalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detalle> detalles) {
        this.detalles = detalles;
    }

    public Usuario getUsuarios() {
        return usuarios;
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
