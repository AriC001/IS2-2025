package com.tpa.entidades;

public class Rectangulo {

  // Atributos
  private Double base;
  private Double altura;
  private String color;
  private boolean activo;

  // Constructores

  public Rectangulo() {}

  public Rectangulo(Double base, Double altura) {
    this.base = base;
    this.altura = altura;
    this.color = "Rojo";
    this.activo = true;
  }

  // Getters y Setters

  public Double getBase() {
    return base;
  }

  public void setBase(Double base) {
    this.base = base;
  }

  public boolean isActivo() {
    return activo;
  }

  public void setActivo(boolean activo) {
    this.activo = activo;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public Double getAltura() {
    return altura;
  }

  public void setAltura(Double altura) {
    this.altura = altura;
  }
}
