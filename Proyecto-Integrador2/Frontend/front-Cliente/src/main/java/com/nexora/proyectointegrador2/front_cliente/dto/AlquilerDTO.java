package com.nexora.proyectointegrador2.front_cliente.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AlquilerDTO extends BaseDTO {

  private Date fechaDesde;
  private Date fechaHasta;
  private ClienteDTO cliente;
  private VehiculoDTO vehiculo;
  private DocumentoDTO documento;

}

