package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "localidad")
public class Localidad implements Serializable {
  // Atributos

  @Id
  private String id;

  private String nombre;

  private String codigoPostal;

  private boolean eliminado;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "departamento_id")
  private Departamento departamento;
}
