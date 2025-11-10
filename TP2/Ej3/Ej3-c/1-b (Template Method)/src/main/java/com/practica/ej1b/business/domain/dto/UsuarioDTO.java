package com.practica.ej1b.business.domain.dto;

import com.practica.ej1b.business.domain.entity.Usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UsuarioDTO extends PersonaDTO {

  @NotBlank(message = "El nombre de usuario es obligatoria")
  @Size(max = 100)
  private String nombreUsuario;

  /**
   * La clave/contraseña: en fromEntity la dejamos nula (no exponer).
   * En el formulario puede venir como texto sin encriptar; el hashing debe hacerse en el service.
   */
  @Size(min = 6, max = 255, message = "La clave debe tener al menos 6 caracteres")
  private String contrasenia;

  public static UsuarioDTO fromEntity(Usuario u) {
    if (u == null) return new UsuarioDTO();
    UsuarioDTO dto = new UsuarioDTO();
    // mapear campos de Persona
    PersonaDTO personaPart = PersonaDTO.fromEntity(u);
    dto.setId(personaPart.getId());
    dto.setNombre(personaPart.getNombre());
    dto.setApellido(personaPart.getApellido());
    dto.setTelefono(personaPart.getTelefono());
    dto.setCorreoElectronico(personaPart.getCorreoElectronico());
    // mapear cuenta, NO mapear clave por seguridad
    dto.setNombreUsuario(u.getNombreUsuario());
    dto.setContrasenia(null);
    dto.setEliminado(u.isEliminado());
    return dto;
  }

}
