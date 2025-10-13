package com.example.etemplate.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IdGeneratorType;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Articulo implements SoftDeletable  {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private double price;
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "articulo_id")
    private Proveedor proveedor;

    @OneToMany
    @JoinColumn(name = "articulo_id")
    private List<Imagen> imagenes;

    @Override
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    @Override
    public boolean isDeleted() { return deleted; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Proveedor getProvedores() {
        return proveedor;
    }

    public void setProvedores(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Articulo() {
    }

    public void setImage(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }
}
