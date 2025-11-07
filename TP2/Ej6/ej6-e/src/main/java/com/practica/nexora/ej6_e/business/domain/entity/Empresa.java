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
@Table(name = "empresas")
public class Empresa extends BaseEntity<Long> {

  private String nombre;

  @OneToOne
  private Contacto contacto;
  
}
