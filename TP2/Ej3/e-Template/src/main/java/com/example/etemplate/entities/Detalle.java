package com.example.etemplate.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Detalle {
    @Id
    @Generated
    private String id;
    private boolean deleted;

    @OneToMany
    @JoinColumn(name = "detalle_id")
    private List<Articulo> articulos;
}
