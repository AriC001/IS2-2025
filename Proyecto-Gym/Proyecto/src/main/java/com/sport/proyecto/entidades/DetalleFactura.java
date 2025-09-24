package com.sport.proyecto.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetalleFactura implements Serializable {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "cuota_id")
    private CuotaMensual cuotaMensual;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

    private boolean eliminado;

}
