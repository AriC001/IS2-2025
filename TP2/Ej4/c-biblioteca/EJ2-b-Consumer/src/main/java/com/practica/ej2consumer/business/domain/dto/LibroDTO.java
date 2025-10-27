package com.practica.ej2consumer.business.domain.dto;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LibroDTO extends BaseDTO {
  
  private String titulo;

  private String genero;

  private int paginas;

  @Temporal(TemporalType.DATE)
  private Date fecha;

  private Set<AutorDTO> autores;

  private PersonaDTO persona;

  private DocumentoDTO documento;

}
