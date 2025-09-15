package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "sucursal")
public class Sucursal {
  // Atributos

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombre;

  private boolean eliminado;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "direccion_id")
  private Direccion direccion;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "empresa_id")
  private Empresa empresa;
}
