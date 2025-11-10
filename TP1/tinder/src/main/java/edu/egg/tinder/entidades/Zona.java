package edu.egg.tinder.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "zona")
public class Zona implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    private boolean activo;

    @OneToMany(mappedBy = "zona", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Usuario> usuarios = new ArrayList<Usuario>();

}
