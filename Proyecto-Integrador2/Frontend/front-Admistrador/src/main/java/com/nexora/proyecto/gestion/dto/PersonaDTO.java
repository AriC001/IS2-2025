package com.nexora.proyecto.gestion.dto;

import java.util.Date;
import java.util.List;

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
  private List<ContactoDTO> contactos;
  private String imagenPerfilId;

}
