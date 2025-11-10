package com.practica.ej2b.business.domain.entity;


import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "domicilio")
public class Domicilio extends BaseEntity<Long> implements Serializable{

  @Column(length=100)
  private String calle;

  @Column(length=10)
  private String numero;

  @ManyToOne(fetch=FetchType.EAGER, optional=false)
  private Localidad localidad;

}
