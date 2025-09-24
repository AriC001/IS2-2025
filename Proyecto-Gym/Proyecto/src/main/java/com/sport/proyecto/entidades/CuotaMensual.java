package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import com.sport.proyecto.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
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

    @Column
    private boolean eliminado;
}
