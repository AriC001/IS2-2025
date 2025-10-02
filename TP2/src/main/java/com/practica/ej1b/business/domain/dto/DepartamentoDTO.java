package com.practica.ej1b.business.domain.dto;

import com.practica.ej1b.business.domain.entity.Departamento;
import com.practica.ej1b.business.domain.entity.Provincia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DepartamentoDTO {
  private String id;
  private String nombre;
  // para el formulario: paisId se usa para filtrar provincias; provinciaId es la relación real
  private String paisId;
  private String provinciaId;

  public static DepartamentoDTO fromEntity(Departamento d) {
    if (d == null) return new DepartamentoDTO();
    Provincia prov = d.getProvincia();
    String provId = prov != null ? prov.getId() : null;
    String paisId = (prov != null && prov.getPais() != null) ? prov.getPais().getId() : null;
    return new DepartamentoDTO(d.getId(), d.getNombre(), paisId, provId);
  }
}
