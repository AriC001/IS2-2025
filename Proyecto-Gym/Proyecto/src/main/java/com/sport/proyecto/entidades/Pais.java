package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import lombok.*;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder

@Entity
@Table(name = "pais")
public class Pais {
  // Atributos

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombre;

  private boolean eliminado;

}
