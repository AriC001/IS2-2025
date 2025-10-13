package com.practica.ej1b.business.domain.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "direccion")
public class Direccion implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false, length = 100)
  private String calle;

  @Column(nullable = false, length = 10)
  private String numeracion;

  @Column(nullable = false, length = 100)
  private String barrio;

  @Column(nullable = false, length = 100)
  private String manzanaPiso;

  @Column(length = 10)
  private String casaDepartamento;

  @Column(length = 100)
  @Nullable
  private String referencia;

  @Column
  private String latitud;

  @Column
  private String longitud;

  private boolean eliminado;

  public String getGoogleMapsLink(String latitud, String longitud) {
    if (latitud != null && longitud != null && !latitud.isEmpty() && !longitud.isEmpty()) {
      return "https://www.google.com/maps?q=" + latitud + "," + longitud;
    }
    return null;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  private Localidad localidad;

  // en com.practica.ej1b.business.domain.entity.Direccion
  public String getFormatted() {
    StringBuilder sb = new StringBuilder();
    if (calle != null) sb.append(calle);
    if (numeracion != null) sb.append(" ").append(numeracion);
    if (barrio != null) sb.append(", ").append(barrio);
    if (manzanaPiso != null) sb.append(", ").append(manzanaPiso);
    if (casaDepartamento != null) sb.append(", ").append(casaDepartamento);
    if (referencia == null || referencia.isBlank()) {
      sb.append(", -");
    } else {
      sb.append(", ").append(referencia);
    }
    if (localidad != null && localidad.getNombre() != null) sb.append(", ").append(localidad.getNombre());
    return sb.toString();
  }


}
