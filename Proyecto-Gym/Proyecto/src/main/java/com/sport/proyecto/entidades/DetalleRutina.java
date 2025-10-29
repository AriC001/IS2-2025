package com.sport.proyecto.entidades;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import org.hibernate.annotations.GenericGenerator;

import com.sport.proyecto.enums.EstadoDetalleRutina;
import com.sport.proyecto.enums.EstadoRutina;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "detalle_rutina")
public class DetalleRutina {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String id;

    @Enumerated(EnumType.STRING)
    private EstadoDetalleRutina estado;
    @Column
    private String actividad;
    @Column
    private LocalDate fecha;
    @Column
    private boolean eliminado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rutina")
    private Rutina rutina;

    public boolean isEliminado() {
        return this.eliminado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EstadoDetalleRutina getEstado() {
        return estado;
    }

    public void setEstado(EstadoDetalleRutina estado) {
        this.estado = estado;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Rutina getRutina() {
        return rutina;
    }

    public void setRutina(Rutina rutina) {
        this.rutina = rutina;
    }

    
}
