package com.sport.proyecto.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Promocion extends Mensaje {

  private Date fechaEnvioPromocion;

  private long cantidadSociosEnviados;

}
