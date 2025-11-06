package com.example.taller.entity;
import lombok.*;
import jakarta.persistence.*;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="historialArreglo")
public class HistorialArreglo extends BaseEntity<String> {

  
    private Date fecha;
    private String detalle;
    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;
    @ManyToOne
    @JoinColumn(name = "mecanico_id")
    private Mecanico mecanico;
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
