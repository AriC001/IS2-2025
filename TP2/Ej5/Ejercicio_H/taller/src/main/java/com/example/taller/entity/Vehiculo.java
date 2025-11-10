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
@Table(name="vehiculo")
public class Vehiculo extends BaseEntity<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    private String patente;

    private String modelo;

    private String marca;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL)
    private List<HistorialArreglo> historialArreglo;


    private Boolean eliminado;

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
