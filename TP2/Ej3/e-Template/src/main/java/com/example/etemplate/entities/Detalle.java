package com.example.etemplate.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Detalle implements SoftDeletable  {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private int cantidad;
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;

    public Detalle(Articulo articulo, int cantidad) {
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.deleted = false;
    }


    @Override
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    @Override
    public boolean isDeleted() { return deleted; }

    public double getSubtotal() {
        return articulo.getPrice() * cantidad;
    }
}
