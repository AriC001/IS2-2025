package com.sport.proyecto.entidades;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="valorCuota")
public class ValorCuota {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaDesde;

    @Column
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaHasta;

    @Column
    private Long valor;

    @Column
    private boolean eliminado;
}
