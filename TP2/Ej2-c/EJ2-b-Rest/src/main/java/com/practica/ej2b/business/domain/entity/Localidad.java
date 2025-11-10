package com.practica.ej2b.business.domain.entity;


import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "localidad")
public class Localidad extends BaseEntity<Long> implements Serializable {

  @Column(length=50)
  private String nombre;

}
