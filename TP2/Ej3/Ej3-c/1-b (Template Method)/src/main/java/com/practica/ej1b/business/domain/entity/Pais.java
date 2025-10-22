package com.practica.ej1b.business.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "pais")
public class Pais extends BaseEntity<String> {

  private String nombre;

}
