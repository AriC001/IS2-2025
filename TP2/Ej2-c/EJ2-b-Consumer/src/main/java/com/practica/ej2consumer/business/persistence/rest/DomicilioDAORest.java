package com.practica.ej2consumer.business.persistence.rest;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.practica.ej2consumer.business.domain.dto.DomicilioDTO;

@Repository
public class DomicilioDAORest extends BaseDAORest<DomicilioDTO, Long> {

  public DomicilioDAORest(RestTemplate restTemplate) {
    super(restTemplate, "/domicilios");
  }

  @Override
  protected Class<DomicilioDTO> getEntityClass() {
    return DomicilioDTO.class;
  }
}
