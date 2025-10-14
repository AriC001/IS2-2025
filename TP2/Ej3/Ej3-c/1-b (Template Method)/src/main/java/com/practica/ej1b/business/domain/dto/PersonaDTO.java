package com.practica.ej1b.business.domain.dto;

import com.practica.ej1b.business.domain.entity.Persona;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaDTO {
  private String id;

  private String nombre;

  private String apellido;

  private String telefono;

  private String correoElectronico;

  private boolean eliminado;

  // Mapea desde entidad a DTO
  public static PersonaDTO fromEntity(Persona p) {
    if (p == null) return new PersonaDTO();
    return PersonaDTO.builder()
      .id(p.getId())
      .nombre(p.getNombre())
      .apellido(p.getApellido())
      .telefono(p.getTelefono())
      .correoElectronico(p.getCorreoElectronico())
      .eliminado(p.isEliminado())
      .build();
  }

}
