package com.practica.nexora.ej6_e.business.domain.entity;

import com.practica.nexora.ej6_e.business.enums.TipoContacto;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "contactos")
public abstract class Contacto extends BaseEntity<Long> {

  private TipoContacto tipoContacto;

  private String observacion;

  @ManyToOne
  private Persona persona;

}
