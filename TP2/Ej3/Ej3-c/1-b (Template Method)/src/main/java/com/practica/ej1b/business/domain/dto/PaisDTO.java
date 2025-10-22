package com.practica.ej1b.business.domain.dto;

import com.practica.ej1b.business.domain.entity.Pais;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PaisDTO {
  private String id;
  private String nombre;

  private boolean eliminado;
  // Mapea desde entidad a DTO
  public static PaisDTO fromEntity(Pais p) {
    if (p == null) return new PaisDTO();
    return PaisDTO.builder()
      .id(p.getId())
      .nombre(p.getNombre())
      .eliminado(p.isEliminado())
      .build();
  }
}
