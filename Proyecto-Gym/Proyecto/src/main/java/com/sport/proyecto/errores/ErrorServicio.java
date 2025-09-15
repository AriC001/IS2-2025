package com.sport.proyecto.errores;

public class ErrorServicio extends RuntimeException {
  public ErrorServicio(String message) {
    super(message);
  }
}
