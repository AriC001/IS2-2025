package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "departamento")
public class Departamento {
  // Atributos

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombre;

  private boolean eliminado;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "provincia_id")
  private Provincia provincia;

}
