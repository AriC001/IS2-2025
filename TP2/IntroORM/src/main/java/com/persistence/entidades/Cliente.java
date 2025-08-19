package com.persistence.entidades;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor
@Builder

@Entity
@Table(name = "cliente")
@Audited
public class Cliente implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    //@NonNull
    private String nombre;

    @Column(name = "apellido", nullable = false)
    //@NonNull
    private String apellido;

    @Column(name = "dni", unique = true, nullable = false)
    //@NonNull
    private int dni;

    @Column(name = "borrado", nullable = false)
    private boolean borrado;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "fk_domicilio")
    private Domicilio domicilio;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<Factura> facturas = new ArrayList<Factura>();

}
