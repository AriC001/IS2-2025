package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "direccion")
public class Direccion implements Serializable {
  // Atributos

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;

  private String calle;

  private String numeracion;

  private String barrio;

  private String manzanaPiso;

  private String casaDepartamento;

  private String referencia;

  private boolean eliminado;

  @Column
  private String latitud;

  @Column
  private String longitud;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "localidad_id")
  private Localidad localidad;

  public String getGoogleMapsLink() {
    if (latitud != null && longitud != null && !latitud.isEmpty() && !longitud.isEmpty()) {
      return "https://www.google.com/maps?q=" + latitud + "," + longitud;
    }
    return null;
  }

  public String getDescripcion() {
    StringBuilder sb = new StringBuilder();
    sb.append(calle).append(" ").append(numeracion);
    if (barrio != null && !barrio.isEmpty()) {
      sb.append(", Barrio: ").append(barrio);
    }
    if (manzanaPiso != null && !manzanaPiso.isEmpty()) {
      sb.append(", Mz/Piso: ").append(manzanaPiso);
    }
    if (casaDepartamento != null && !casaDepartamento.isEmpty()) {
      sb.append(", Casa/Depto: ").append(casaDepartamento);
    }
    if (referencia != null && !referencia.isEmpty()) {
      sb.append(", Ref: ").append(referencia);
    }
    if (localidad != null) {
      sb.append(", ").append(localidad.getNombre());
      if (localidad.getDepartamento() != null) {
        sb.append(", ").append(localidad.getDepartamento().getNombre());
        if (localidad.getDepartamento().getProvincia() != null) {
          sb.append(", ").append(localidad.getDepartamento().getProvincia().getNombre());
          if (localidad.getDepartamento().getProvincia().getPais() != null) {
            sb.append(", ").append(localidad.getDepartamento().getProvincia().getPais().getNombre());
          }
        }
      }
    }
    return sb.toString();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCalle() {
    return calle;
  }

  public void setCalle(String calle) {
    this.calle = calle;
  }

  public String getNumeracion() {
    return numeracion;
  }

  public void setNumeracion(String numeracion) {
    this.numeracion = numeracion;
  }

  public String getBarrio() {
    return barrio;
  }

  public void setBarrio(String barrio) {
    this.barrio = barrio;
  }

  public String getManzanaPiso() {
    return manzanaPiso;
  }

  public void setManzanaPiso(String manzanaPiso) {
    this.manzanaPiso = manzanaPiso;
  }

  public String getCasaDepartamento() {
    return casaDepartamento;
  }

  public void setCasaDepartamento(String casaDepartamento) {
    this.casaDepartamento = casaDepartamento;
  }

  public String getReferencia() {
    return referencia;
  }

  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }

  public boolean isEliminado() {
    return eliminado;
  }

  public void setEliminado(boolean eliminado) {
    this.eliminado = eliminado;
  }

  public String getLatitud() {
    return latitud;
  }

  public void setLatitud(String latitud) {
    this.latitud = latitud;
  }

  public String getLongitud() {
    return longitud;
  }

  public void setLongitud(String longitud) {
    this.longitud = longitud;
  }

  public Localidad getLocalidad() {
    return localidad;
  }

  public void setLocalidad(Localidad localidad) {
    this.localidad = localidad;
  }

  
}
