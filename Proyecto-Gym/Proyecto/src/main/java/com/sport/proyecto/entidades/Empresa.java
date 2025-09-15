package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "empresa")
public class Empresa {
  // Atributos

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombre;

  private String telefono;

  private String correoElectronico;

  private boolean eliminado;
}
