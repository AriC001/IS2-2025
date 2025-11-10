package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor

@Entity
public class DetalleFactura implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ManyToOne
    @JoinColumn(name = "cuota_id")
    private CuotaMensual cuotaMensual;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

    private boolean eliminado;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CuotaMensual getCuotaMensual() {
        return cuotaMensual;
    }

    public void setCuotaMensual(CuotaMensual cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

}
