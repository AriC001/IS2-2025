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
@Table(name = "empresa")
public class Empresa implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String razonSocial;

  private boolean eliminado;

  @OneToOne(fetch = FetchType.LAZY)
  private Direccion direccion;
}
