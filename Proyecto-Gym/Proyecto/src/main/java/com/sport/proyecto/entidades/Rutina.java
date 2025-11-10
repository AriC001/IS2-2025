package com.sport.proyecto.entidades;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import com.sport.proyecto.enums.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.mapping.model.SpELContext;

import com.sport.proyecto.entidades.DetalleRutina;
import com.sport.proyecto.entidades.Empleado;

@Entity
@Table(name = "rutina")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rutina implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column
    private LocalDate fechaInicio;

    @Column
    private LocalDate fechaFin;

    @OneToMany(mappedBy = "rutina", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<DetalleRutina> detalleRutina = new ArrayList<>();
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "socio")
    private Socio socio;

    @Enumerated(EnumType.STRING)
    private EstadoRutina estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor")
    private Empleado profesor;

    @Column
    private boolean eliminado;

      public DetalleRutina crearDetalleRutina(String actividad, LocalDate fecha) {
        DetalleRutina detalle = new DetalleRutina();
        detalle.setActividad(actividad);
        detalle.setFecha(fecha);
        detalle.setEstado(EstadoDetalleRutina.SIN_REALIZAR);
        detalle.setEliminado(false);
        detalle.setRutina(this); // clave: relacionar detalle con rutina
        this.detalleRutina.add(detalle);
        return detalle;
    }
    public void setDetalleRutina(Collection<DetalleRutina> detalleRutina) {
        this.detalleRutina = detalleRutina;
    }

    public DetalleRutina buscarDetalleRutina(String idDetalle) {
        for (DetalleRutina detalle : this.detalleRutina) {
            if (detalle.getId().equals(idDetalle) && detalle.isEliminado() == false) {
                return detalle;
            }
        }
        return null;
    }
    public boolean isEliminado() {
        return this.eliminado;
    }
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
    public void eliminarDetalleRutina(String idDetalle) {
        DetalleRutina detalle = buscarDetalleRutina(idDetalle);
        if (detalle != null) {
            detalle.setEliminado(true);
        }
    }
    public Empleado getProfesor() {
        return this.profesor;
    }
    public Socio getSocio() {
        return this.socio;
    }
    public void setProfesor(Empleado profesor) {
        this.profesor = profesor;
    }
    public void setSocio(Socio socio) {
        this.socio = socio;
    }
    public Collection<DetalleRutina> listarDetalle() {
        return this.detalleRutina;
    }

    public void setEstado(EstadoRutina estado) {
        this.estado = estado;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getId() {
        return id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public Collection<DetalleRutina> getDetalleRutina() {
        return detalleRutina;
    }

    public EstadoRutina getEstado() {
        return estado;
    }



}
