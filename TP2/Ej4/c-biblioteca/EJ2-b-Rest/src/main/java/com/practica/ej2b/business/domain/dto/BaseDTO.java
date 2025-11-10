package com.practica.ej2b.business.domain.dto;


public abstract class BaseDTO {

  private Long id;

  private boolean eliminado;

  public Long getId() {
    return id;
  }

  public boolean isEliminado() {
    return eliminado;
  }

  public void setEliminado(boolean eliminado) {
    this.eliminado = eliminado;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
