package com.sport.proyecto.enums;

public enum tipoDocumento {

  DNI("Documento Nacional de Identidad"),
  LC("Libreta Cívica"),
  LE("Libreta de Enrolamiento"),
  CI("Cédula de Identidad"),
  PASAPORTE("Pasaporte"),
  EXTRANJERO("Documento Extranjero"),
  SIN_DOCUMENTO("Sin Documento");

  private final String descripcion;

  tipoDocumento(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  @Override
  public String toString() {
    return descripcion;
  }

}
