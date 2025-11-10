package com.practica.ej2b.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej2b.business.domain.entity.Domicilio;
import com.practica.ej2b.business.persistence.repository.DomicilioRepository;

@Service
public class DomicilioService extends BaseService<Domicilio, Long> {

  public DomicilioService(DomicilioRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(Domicilio entity) throws Exception {
    if (entity.getCalle() == null || entity.getCalle().isEmpty()) {
      throw new Exception("La calle no puede ser nula o vacía");
    }
    if (entity.getNumero() == null || entity.getNumero().isEmpty()) {
      throw new Exception("El número no puede ser nulo o vacío");
    }
    if (entity.getLocalidad() == null) {
      throw new Exception("La localidad no puede ser nula o vacía");
    }

  }

}
