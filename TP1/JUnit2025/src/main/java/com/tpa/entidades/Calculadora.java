package com.tpa.entidades;

public class Calculadora {
  public Integer sumar(Integer a, Integer b) {
    if (a == null){
      a=0;
    }

    if (b == null){
      b=0;
    }

    return a + b;
  }

  public Double dividir(Double a, Double b) throws Exception {
    if (b == null || b == 0) {
      throw new Exception("Denominador no puede ser nulo o cero");
    }

    return a / b;
  }


}
