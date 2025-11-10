package com.sport.proyecto.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
public class Promocion extends Mensaje {

  private Date fechaEnvioPromocion;

  private long cantidadSociosEnviados;

  public Date getFechaEnvioPromocion() {
    return fechaEnvioPromocion;
  }

  public void setFechaEnvioPromocion(Date fechaEnvioPromocion) {
    this.fechaEnvioPromocion = fechaEnvioPromocion;
  }

  public long getCantidadSociosEnviados() {
    return cantidadSociosEnviados;
  }

  public void setCantidadSociosEnviados(long cantidadSociosEnviados) {
    this.cantidadSociosEnviados = cantidadSociosEnviados;
  }

  

}
