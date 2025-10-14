package com.practica.ej1b.business.domain.entity;

import java.io.Serializable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity<ID> implements Serializable {
  
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private ID id;

  private boolean eliminado;

  public ID getId() {
    return id;
  }

  public void setId(ID id) {
    this.id = id;
  }

  public boolean isEliminado() {
    return eliminado;
  }

  public void setEliminado(boolean eliminado) {
    this.eliminado = eliminado;
  }

}
