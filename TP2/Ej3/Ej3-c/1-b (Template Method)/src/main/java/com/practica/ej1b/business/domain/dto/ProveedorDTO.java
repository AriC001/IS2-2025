package com.practica.ej1b.business.domain.dto;

import com.practica.ej1b.business.domain.entity.Proveedor;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProveedorDTO extends PersonaDTO {

  // CUIT en Argentina suele ser 11 dígitos (ajustá la regex si querés permitir guiones)
  @Pattern(regexp = "\\d{11}", message = "El CUIT debe tener 11 dígitos numéricos")
  private String cuit;

  private DireccionDTO direccionDTO;

  public static ProveedorDTO fromEntity(Proveedor p) {
    if (p == null) return new ProveedorDTO();
    ProveedorDTO dto = new ProveedorDTO();
    // mapear campos de Persona
    PersonaDTO personaPart = PersonaDTO.fromEntity(p);
    dto.setId(personaPart.getId());
    dto.setNombre(personaPart.getNombre());
    dto.setApellido(personaPart.getApellido());
    dto.setTelefono(personaPart.getTelefono());
    dto.setCorreoElectronico(personaPart.getCorreoElectronico());
    dto.setEliminado(personaPart.isEliminado());
    // mapear cuit
    dto.setCuit(p.getCuit());
    // mapear direccion
    dto.setDireccionDTO(DireccionDTO.fromEntity(p.getDireccion()));
    return dto;
  }

}
