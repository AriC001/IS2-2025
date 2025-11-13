package com.nexora.proyectointegrador2.front_cliente.dto;

import java.util.Date;
import java.util.List;

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
public class FacturaDTO extends BaseDTO {

  private Long numeroFactura;
  private Date fechaFactura;
  private Double totalPagado;
  private String estado; // EstadoFactura como String para evitar problemas de serialización
  // Nota: formaDePago y detalles pueden causar problemas de serialización, 
  // por lo que los ignoramos si no son necesarios para la descarga del PDF
}

