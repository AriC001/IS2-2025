package com.tpa.servicios;

import com.tpa.entidades.Rectangulo;

public class RectanguloServicio {
  public Double calcularArea(Rectangulo r){
    return r.getAltura() * r.getBase();
  }

  public Double calcularPerimetro(Rectangulo r){
    return 2 * (r.getAltura() + r.getBase());
  }

}
