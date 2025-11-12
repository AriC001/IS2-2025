package com.nexora.proyecto.gestion.dto;

import java.util.Date;
import java.util.List;

import com.nexora.proyecto.gestion.dto.enums.EstadoFactura;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FacturaDTO extends BaseDTO {

  private Long numeroFactura;
  private Date fechaFactura;
  private Double totalPagado;
  private EstadoFactura estado;
  private FormaDePagoDTO formaDePago;
  private List<DetalleFacturaDTO> detalles;
}

