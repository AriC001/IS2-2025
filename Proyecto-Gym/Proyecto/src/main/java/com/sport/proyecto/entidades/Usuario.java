package com.sport.proyecto.entidades;

import com.sport.proyecto.enums.Rol;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nombre_usuario")
  private String nombreUsuario;

  private String clave;

  @Enumerated(EnumType.STRING)
  private Rol rol;

  private boolean eliminado;
}
