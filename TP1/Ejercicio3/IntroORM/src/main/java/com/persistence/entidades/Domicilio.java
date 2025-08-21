package com.persistence.entidades;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
// @RequiredArgsConstructor
@Builder

@Entity
@Table(name = "domicilio")
@Audited
public class Domicilio implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_calle", nullable = false)
    private String nombreCalle;

    @Column(name = "numero")
    private int numero;

    @Column(name = "borrado", nullable = false)
    private boolean borrado;

    @OneToOne(mappedBy = "domicilio")
    private Cliente cliente;
}
