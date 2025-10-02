package com.practica.ej1b.business.domain.dto;

import com.practica.ej1b.business.domain.entity.Departamento;
import com.practica.ej1b.business.domain.entity.Localidad;
import com.practica.ej1b.business.domain.entity.Provincia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LocalidadDTO {
  private String id;
  private String nombre;
  private String codigoPostal;
  // para el formulario:
  private String paisId;
  private String provinciaId;
  private String departamentoId;

  public static LocalidadDTO fromEntity(Localidad l) {
    if (l == null) return new LocalidadDTO();
    Departamento dep = l.getDepartamento();
    Provincia prov = (dep != null) ? dep.getProvincia() : null;
    String depId = dep != null ? dep.getId() : null;
    String provId = prov != null ? prov.getId() : null;
    String paisId = (prov != null && prov.getPais() != null) ? prov.getPais().getId() : null;
    return new LocalidadDTO(l.getId(), l.getNombre(), l.getCodigoPostal(), paisId, provId, depId);
  }

}
