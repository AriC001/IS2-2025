package com.practica.nexora.ej6_e.business.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "usuarios")
public class Usuario extends BaseEntity<Long> {

  private String nombreUsuario;
  private String clave;

  @OneToOne(mappedBy = "usuario")
  private Persona persona;

}
