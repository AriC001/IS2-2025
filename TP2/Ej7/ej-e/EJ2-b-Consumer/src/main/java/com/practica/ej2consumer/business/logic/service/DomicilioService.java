package com.practica.ej2consumer.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej2consumer.business.domain.dto.DomicilioDTO;
import com.practica.ej2consumer.business.persistence.rest.DomicilioDAORest;


@Service
public class DomicilioService extends BaseService<DomicilioDTO, Long> {

  public DomicilioService(DomicilioDAORest dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(DomicilioDTO entity) throws Exception {
    if (entity.getCalle() == null || entity.getCalle().isEmpty()) {
      throw new Exception("La calle no puede ser nula o vacía");
    }
    if (entity.getNumero() == null || entity.getNumero().isEmpty()) {
      throw new Exception("El número no puede ser nulo o vacío");
    }
    if (entity.getLocalidad() == null) {
      throw new Exception("La localidad no puede ser nula");
    }
  }

}
