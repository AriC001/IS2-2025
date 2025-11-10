package com.practica.ej2consumer.business.persistence.rest;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.practica.ej2consumer.business.domain.dto.AutorDTO;

@Repository
public class AutorDAORest extends BaseDAORest<AutorDTO, Long> {
  public AutorDAORest(RestTemplate restTemplate) {
    super(restTemplate, "/autores");
  }

  @Override
  protected Class<AutorDTO> getEntityClass() {
    return AutorDTO.class;
  }

}
