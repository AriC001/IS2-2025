package com.practica.nexora.ej6_e.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.nexora.ej6_e.business.domain.entity.Contacto;
import com.practica.nexora.ej6_e.business.persistence.repository.ContactoRepository;

@Service
public class ContactoService extends BaseService<Contacto, Long> {
  private final ContactoRepository contactoRepository;

  public ContactoService(ContactoRepository contactoRepository) {
    super(contactoRepository);
    this.contactoRepository = contactoRepository;
  }

  @Override
  protected void validate(Contacto entity) throws IllegalArgumentException {
    if (entity == null) {
      throw new IllegalArgumentException("El Contacto no puede ser nulo");
    }
    
  }

}
