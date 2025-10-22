package com.practica.ej1b.business.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
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
@Table(name = "provincia")
public class Provincia extends BaseEntity<String> {

  private String nombre;

  @ManyToOne(fetch = FetchType.LAZY)
  private Pais pais;

}