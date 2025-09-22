package com.sport.proyecto.entidades;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import org.hibernate.annotations.GenericGenerator;

import com.sport.proyecto.enums.EstadoDetalleRutina;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class DetalleRutina {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column
    private EstadoDetalleRutina estado;
    @Column
    private String actividad;
    @Column
    private Date fecha;
    @Column
    private boolean eliminado;

    public boolean isEliminado() {
        return this.eliminado;
    }
}
