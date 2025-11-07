package com.practica.nexora.ej6_e.business.domain.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
@Table(name = "personas")
public class Persona extends BaseEntity<Long> {

  private String nombre;
  private String apellido;

  @OneToOne(cascade = CascadeType.ALL)
  private Usuario usuario;

  @OneToMany(mappedBy = "persona")
  private List<Contacto> contactos;


}
