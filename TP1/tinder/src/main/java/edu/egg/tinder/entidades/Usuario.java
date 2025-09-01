package edu.egg.tinder.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String mail;

    @Column(nullable = false)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date alta;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date baja;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "zona_id")
    private Zona zona;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Mascota> mascotas = new ArrayList<Mascota>();

    @OneToOne
    @JoinColumn(name = "foto_id")
    private Foto foto;

}
