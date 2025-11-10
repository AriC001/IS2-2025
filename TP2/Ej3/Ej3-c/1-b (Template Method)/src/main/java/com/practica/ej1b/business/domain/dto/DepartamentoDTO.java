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
  // para la lista: nombres de las entidades relacionadas
  private String provinciaNombre;

  private boolean eliminado;

  public static DepartamentoDTO fromEntity(Departamento d) {
    if (d == null) return new DepartamentoDTO();
    Provincia prov = d.getProvincia();
    String provId = prov != null ? prov.getId() : null;
    String paisId = (prov != null && prov.getPais() != null) ? prov.getPais().getId() : null;
    String provNombre = prov != null ? prov.getNombre() : "";
    DepartamentoDTO dto = new DepartamentoDTO();
    dto.setId(d.getId());
    dto.setNombre(d.getNombre());
    dto.setPaisId(paisId);
    dto.setProvinciaId(provId);
    dto.setProvinciaNombre(provNombre);
    dto.setEliminado(d.isEliminado());
    return dto;
  }
}
