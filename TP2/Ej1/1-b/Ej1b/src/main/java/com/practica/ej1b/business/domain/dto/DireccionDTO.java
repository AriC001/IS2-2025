package com.practica.ej1b.business.domain.dto;

import com.practica.ej1b.business.domain.entity.Direccion;
import com.practica.ej1b.business.domain.entity.Localidad;
import jakarta.persistence.Column;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DireccionDTO {
  private String id;

  private String calle;
  private String barrio;
  private String numeracion;
  private String manzanaPiso;
  private String casaDepartamento;
  private String referencia;

  // Campos para los selects en cascada (no necesariamente almacenados en la entidad Direccion)
  // Localidad es la relación real (Direccion.localidad)
  private String paisId;
  private String provinciaId;
  private String departamentoId;
  private String localidadId;

  private String latitud;
  private String longitud;


  public static DireccionDTO fromEntity(Direccion d) {
    if (d == null) return new DireccionDTO();

    DireccionDTO f = new DireccionDTO();
    f.setId(d.getId());
    f.setCalle(d.getCalle());
    f.setBarrio(d.getBarrio());
    f.setNumeracion(d.getNumeracion());
    f.setManzanaPiso(d.getManzanaPiso());
    f.setCasaDepartamento(d.getCasaDepartamento());
    f.setReferencia(d.getReferencia());
    f.setLatitud(d.getLatitud());
    f.setLongitud(d.getLongitud());

    Localidad loc = d.getLocalidad();
    if (loc != null) {
      f.setLocalidadId(loc.getId());
      var dep = loc.getDepartamento();
      if (dep != null) {
        f.setDepartamentoId(dep.getId());
        var prov = dep.getProvincia();
        if (prov != null) {
          f.setProvinciaId(prov.getId());
          var pais = prov.getPais();
          if (pais != null) {
            f.setPaisId(pais.getId());
          }
        }
      }
    }
    return f;
  }

}