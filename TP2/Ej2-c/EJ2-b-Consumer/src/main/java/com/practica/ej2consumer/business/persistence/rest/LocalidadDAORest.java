package com.practica.ej2consumer.business.persistence.rest;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.practica.ej2consumer.business.domain.dto.LocalidadDTO;

@Repository
public class LocalidadDAORest extends BaseDAORest<LocalidadDTO, Long> {

  public LocalidadDAORest(RestTemplate restTemplate) {
    super(restTemplate, "/localidades");
  }

  @Override
  protected Class<LocalidadDTO> getEntityClass() {
    return LocalidadDTO.class;
  }
}
