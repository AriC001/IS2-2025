package com.practica.ej1b.business.domain.dto;

import com.practica.ej1b.business.domain.entity.Empresa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaDTO {
  private String id;

  @NotBlank(message = "La razón social es obligatoria")
  @Size(max = 200)
  private String razonSocial;

  // Opcional exponer eliminado (para listados/edición interna) — si no querés exponerlo, quitar este campo
  private Boolean eliminado;

  private DireccionDTO direccionDTO;

  public static EmpresaDTO fromEntity(Empresa e) {
    if (e == null) return new EmpresaDTO();
    return EmpresaDTO.builder()
      .id(e.getId())
      .razonSocial(e.getRazonSocial())
      .direccionDTO(e.getDireccion() != null ? DireccionDTO.fromEntity(e.getDireccion()) : null)
      .eliminado(e.isEliminado())
      .build();
  }

}