package com.nexora.proyecto.gestion.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteAlquilerDTO {

  private String idAlquiler;
  private String nombreCliente;
  private String apellidoCliente;
  private String documentoCliente;
  private String patenteVehiculo;
  private String modeloVehiculo;
  private String marcaVehiculo;
  private Date fechaDesde;
  private Date fechaHasta;
  private Double montoPagado;
  private Long numeroFactura;
  private Date fechaFactura;

  /**
   * Obtiene el nombre completo del cliente (nombre + apellido).
   */
  public String getNombreCompletoCliente() {
    if (nombreCliente == null && apellidoCliente == null) {
      return "";
    }
    if (nombreCliente == null) {
      return apellidoCliente;
    }
    if (apellidoCliente == null) {
      return nombreCliente;
    }
    return nombreCliente + " " + apellidoCliente;
  }
}

