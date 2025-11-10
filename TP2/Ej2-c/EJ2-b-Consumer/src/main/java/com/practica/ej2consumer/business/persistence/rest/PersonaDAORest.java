package com.practica.ej2consumer.business.persistence.rest;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.practica.ej2consumer.business.domain.dto.PersonaDTO;

@Repository
public class PersonaDAORest extends BaseDAORest<PersonaDTO, Long> {

  public PersonaDAORest(RestTemplate restTemplate) {
    super(restTemplate, "/personas");
  }

  @Override
  protected Class<PersonaDTO> getEntityClass() {
    return PersonaDTO.class;
  }
}
