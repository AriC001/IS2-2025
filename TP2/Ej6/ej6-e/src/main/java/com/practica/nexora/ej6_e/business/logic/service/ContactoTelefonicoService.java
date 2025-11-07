package com.practica.nexora.ej6_e.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.nexora.ej6_e.business.domain.entity.ContactoTelefonico;
import com.practica.nexora.ej6_e.business.persistence.repository.ContactoTelefonicoRepository;

@Service
public class ContactoTelefonicoService extends BaseService<ContactoTelefonico, Long> {
  private final ContactoTelefonicoRepository contactoTelefonicoRepository;

  public ContactoTelefonicoService(ContactoTelefonicoRepository contactoTelefonicoRepository) {
    super(contactoTelefonicoRepository);
    this.contactoTelefonicoRepository = contactoTelefonicoRepository;
  }

  @Override
  protected void validate(ContactoTelefonico entity) throws IllegalArgumentException {
    if (entity == null) {
      throw new IllegalArgumentException("El Contacto Telefonico no puede ser nulo");
    }
    // Additional validation logic can be added here
    if (entity.getNumeroTelefono() == null || entity.getNumeroTelefono().isEmpty()) {
      throw new IllegalArgumentException("Numero de telefono no puede ser nulo o vacio");
    }
    if (entity.getTipoContacto() == null) {
      throw new IllegalArgumentException("Tipo de contacto no puede ser nulo");
    }
    if (entity.getTipoTelefono() == null) {
      throw new IllegalArgumentException("Tipo de telefono no puede ser nulo");
    }
  }
}
