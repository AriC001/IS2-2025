package com.sport.proyecto.entidades;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import com.sport.proyecto.enums.*;
import java.util.Date;

import org.springframework.data.mapping.model.SpELContext;

import java.util.Collection;
import com.sport.proyecto.entidades.DetalleRutina;
import com.sport.proyecto.entidades.Empleado;
@Entity
@Table(name = "rutina")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Rutina implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column
    private Date fechaInicio;

    @Column
    private Date fechaFin;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "detalle_rutina_id")
    private Collection<DetalleRutina> detalleRutina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "socio")
    private Persona socio;

    @Enumerated(EnumType.STRING)
    private EstadoRutina estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor")
    private Persona profesor;

    @Column
    private boolean eliminado;

    public DetalleRutina crearDetalleRutina(String actividad, Date fecha) {
        DetalleRutina detalle = new DetalleRutina();
        detalle.setActividad(actividad);
        detalle.setFecha(fecha);
        detalle.setEstado(EstadoDetalleRutina.SIN_REALIZAR);
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
    public Persona getProfesor() {
        return this.profesor;
    }
    public Persona getSocio() {
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
}
