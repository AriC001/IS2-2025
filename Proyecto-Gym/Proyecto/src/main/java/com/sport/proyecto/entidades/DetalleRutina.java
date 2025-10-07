package com.sport.proyecto.entidades;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import org.hibernate.annotations.GenericGenerator;

import com.sport.proyecto.enums.EstadoDetalleRutina;
import com.sport.proyecto.enums.EstadoRutina;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@NoArgsConstructor
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
}
