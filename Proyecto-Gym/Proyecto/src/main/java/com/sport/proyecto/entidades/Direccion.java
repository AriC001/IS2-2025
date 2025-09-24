package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Getter
@Setter
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "localidad_id")
  private Localidad localidad;

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
}
