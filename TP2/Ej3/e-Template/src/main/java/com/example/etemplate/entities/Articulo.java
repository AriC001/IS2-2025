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
public class Articulo {
    @Id
    @Generated
    private String id;

    private String name;
    private double price;
    private boolean deleted;

    @OneToMany
    @JoinColumn(name = "articulo_id")
    private List<Proveedor> provedores;

    @OneToMany
    @JoinColumn(name = "articulo_id")
    private List<Imagen> imagenes;

}
