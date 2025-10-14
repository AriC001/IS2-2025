package com.practica.ej1b.business.domain.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "empresa")
public class Empresa extends BaseEntity<String> {

  private String razonSocial;

  @OneToOne(fetch = FetchType.LAZY)
  private Direccion direccion;
}
