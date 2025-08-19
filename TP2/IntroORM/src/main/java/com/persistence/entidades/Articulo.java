package com.persistence.entidades;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
// @RequiredArgsConstructor
@Builder

@Entity
@Table(name = "articulo")
@Audited
public class Articulo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad", nullable = false)
    //@NonNull
    private int cantidad;

    @Column(name = "denominacion", nullable = false)
    //@NonNull
    private String donominacion;

    @Column(name = "precio", nullable = false)
    //@NonNull
    private int precio;

    @Column(name = "borrado", nullable = false)
    private boolean borrado;

    @OneToMany(mappedBy = "articulo", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<DetalleFactura> detalles = new ArrayList<DetalleFactura>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "articulo_categoria",
            joinColumns = @JoinColumn(name = "id_articulo"),
            inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    @Builder.Default
    private List<Categoria> categorias = new ArrayList<Categoria>();

}
