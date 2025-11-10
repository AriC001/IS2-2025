package com.practica.ej1b.business.domain.dto;

import com.practica.ej1b.business.domain.entity.Provincia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinciaDTO {
  private String id;
  private String nombre;
  // bind con th:field="*{paisId}"
  private String paisId;

  public static ProvinciaDTO fromEntity(Provincia p) {
    if (p == null) return new ProvinciaDTO();
    String paisId = p.getPais() != null ? p.getPais().getId() : null;
    return new ProvinciaDTO(p.getId(), p.getNombre(), paisId);
  }
}
