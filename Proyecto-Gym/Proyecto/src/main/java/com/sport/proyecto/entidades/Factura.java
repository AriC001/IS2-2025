package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import com.sport.proyecto.enums.*;

import java.time.LocalDate;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "factura")
public class Factura {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "numeroFactura")
    private Long numeroFactura;

    @Column
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaFactura;

    @Column
    private Long totalPagado;

    @OneToMany (mappedBy = "factura", cascade=CascadeType.ALL)
    private Collection<DetalleFactura> detalles;

    @Column
    @Enumerated(EnumType.STRING)
    private estadoFactura estadoFactura;

    @Column
    private boolean eliminado;


}
