package com.nexora.proyecto.gestion.dto;

import java.util.Date;

import com.nexora.proyecto.gestion.dto.enums.TipoDocumentacion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class PersonaDTO extends BaseDTO {

  private String nombre;
  private String apellido;
  private Date fechaNacimiento;
  private TipoDocumentacion tipoDocumento;
  private String numeroDocumento;
  private String usuarioId;
  private DireccionDTO direccion;
  private ContactoDTO contacto;
  private String imagenPerfilId;

}
