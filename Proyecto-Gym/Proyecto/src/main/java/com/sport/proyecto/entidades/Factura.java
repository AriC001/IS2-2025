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



@NoArgsConstructor
@AllArgsConstructor

@Entity
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(Long numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDate getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(LocalDate fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public Long getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(Long totalPagado) {
        this.totalPagado = totalPagado;
    }

    public Collection<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(Collection<DetalleFactura> detalles) {
        this.detalles = detalles;
    }

    public estadoFactura getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(estadoFactura estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }


}
