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
  // para la lista
  private String paisNombre;

  private boolean eliminado;

  public static ProvinciaDTO fromEntity(Provincia p) {
    if (p == null) return new ProvinciaDTO();
    String paisId = p.getPais() != null ? p.getPais().getId() : null;
    String paisNombre = p.getPais() != null ? p.getPais().getNombre() : "";
    ProvinciaDTO dto = new ProvinciaDTO();
    dto.setId(p.getId());
    dto.setNombre(p.getNombre());
    dto.setPaisId(paisId);
    dto.setPaisNombre(paisNombre);
    dto.setEliminado(p.isEliminado());
    return dto;
  }
}
