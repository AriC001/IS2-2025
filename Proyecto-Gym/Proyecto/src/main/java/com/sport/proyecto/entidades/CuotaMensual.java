package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import com.sport.proyecto.enums.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cuotaMensual")
public class CuotaMensual {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column
    @Enumerated(EnumType.STRING)
    private mes mes;

    @Column
    private Long anio;

    @Column
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaVencimiento;

    @ManyToOne
    @JoinColumn(name="valorCuota_id")
    private ValorCuota valorCuota;

    @Column
    @Enumerated(EnumType.STRING)
    private estadoCuota estado;

    @Column
    private boolean eliminado;
}
