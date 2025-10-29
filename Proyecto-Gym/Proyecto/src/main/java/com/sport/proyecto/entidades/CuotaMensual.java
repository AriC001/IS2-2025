package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import com.sport.proyecto.enums.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "cuotaMensual")
public class CuotaMensual {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column
    @Enumerated(EnumType.STRING)
    private mes mes;

    @Column
    private Long anio;

    @Column
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaVencimiento;

    @ManyToOne
    @JoinColumn(name="valorCuota_id")
    private ValorCuota valorCuota;

    @Column
    @Enumerated(EnumType.STRING)
    private estadoCuota estado;

    @Column
    private boolean eliminado;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public mes getMes() {
        return mes;
    }

    public void setMes(mes mes) {
        this.mes = mes;
    }

    public Long getAnio() {
        return anio;
    }

    public void setAnio(Long anio) {
        this.anio = anio;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public ValorCuota getValorCuota() {
        return valorCuota;
    }

    public void setValorCuota(ValorCuota valorCuota) {
        this.valorCuota = valorCuota;
    }

    public estadoCuota getEstado() {
        return estado;
    }

    public void setEstado(estadoCuota estado) {
        this.estado = estado;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
}
