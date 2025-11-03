package com.practica.ej2consumer.business.persistence.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.practica.ej2consumer.business.domain.dto.LibroDTO;

@Repository
public class LibroDAORest extends BaseDAORest<LibroDTO, Long> {
  
  public LibroDAORest(RestTemplate restTemplate) {
    super(restTemplate, "/libros");
  }

  @Override
  protected Class<LibroDTO> getEntityClass() {
    return LibroDTO.class;
  }
}