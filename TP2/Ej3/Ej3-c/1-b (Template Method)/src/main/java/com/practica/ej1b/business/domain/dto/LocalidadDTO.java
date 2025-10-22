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
  // para la lista:
  private String departamentoNombre;

  private boolean eliminado;

  public static LocalidadDTO fromEntity(Localidad l) {
    if (l == null) return new LocalidadDTO();
    Departamento dep = l.getDepartamento();
    Provincia prov = (dep != null) ? dep.getProvincia() : null;
    String depId = dep != null ? dep.getId() : null;
    String provId = prov != null ? prov.getId() : null;
    String paisId = (prov != null && prov.getPais() != null) ? prov.getPais().getId() : null;
    String depNombre = dep != null ? dep.getNombre() : "";
    LocalidadDTO dto = new LocalidadDTO();
    dto.setId(l.getId());
    dto.setNombre(l.getNombre());
    dto.setCodigoPostal(l.getCodigoPostal());
    dto.setPaisId(paisId);
    dto.setProvinciaId(provId);
    dto.setDepartamentoId(depId);
    dto.setDepartamentoNombre(depNombre);
    dto.setEliminado(l.isEliminado());
    return dto;
  }

}
