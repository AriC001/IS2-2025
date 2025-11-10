package com.nexora.proyecto.gestion.dto;

import com.nexora.proyecto.gestion.dto.enums.TipoImagen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ImagenDTO extends BaseDTO {

  private String nombre;
  private String mime;
  private byte[] contenido;
  private TipoImagen tipoImagen;

}
