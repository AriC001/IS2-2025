package com.practica.ej2consumer.business.domain.dto;

import java.util.Date;
import java.util.Set;

import org.antlr.v4.runtime.misc.NotNull;

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
  
  @NotNull
  private String titulo;

  @NotNull
  private String genero;

  @NotNull
  private int paginas;

  @Temporal(TemporalType.DATE)
  private Date fecha;

  private Set<AutorDTO> autores;

  private PersonaDTO persona;

}
