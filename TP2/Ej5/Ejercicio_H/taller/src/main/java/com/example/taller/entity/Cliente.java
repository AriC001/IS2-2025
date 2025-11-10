package com.example.taller.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.*;
import jakarta.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name="cliente")
public class Cliente extends Persona {
    private String documento;
    private String email;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Vehiculo> vehiculos;


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
