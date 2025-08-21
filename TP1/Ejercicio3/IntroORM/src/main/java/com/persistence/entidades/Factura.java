package com.persistence.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionId;
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
@Table(name = "factura")
@Audited
public class Factura implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false)
    private String fecha;

    @Column(name = "numero", nullable = false)
    private int numero;

    @Column(name = "total", nullable = false)
    private int total;

    @Column(name = "borrado", nullable = false)
    private boolean borrado;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<DetalleFactura> detalles = new ArrayList<DetalleFactura>();

}
