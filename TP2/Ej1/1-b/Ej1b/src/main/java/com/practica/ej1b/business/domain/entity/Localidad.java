package com.practica.ej1b.business.domain.entity;

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
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String nombre;

  private String codigoPostal;

  private boolean eliminado;

  @ManyToOne(fetch = FetchType.LAZY)
  private Departamento departamento;

}
