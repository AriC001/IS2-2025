package com.example.taller.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="mecanico")
public class Mecanico extends Persona {
    private String legajo;

    @OneToMany(mappedBy = "mecanico", cascade = CascadeType.ALL)
    private List<HistorialArreglo> arreglos;

    @OneToOne(mappedBy = "mecanico")
    private Usuario usuario;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Boolean getEliminado() {
        return this.eliminado;
    }

    @Override
    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
}