package com.practica.nexora.ej6_e.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.nexora.ej6_e.business.domain.entity.ContactoCorreoElectronico;
import com.practica.nexora.ej6_e.business.persistence.repository.ContactoCorreoElectronicoRepository;

@Service
public class ContactoCorreoElectronicoService extends BaseService<ContactoCorreoElectronico, Long> {
  private final ContactoCorreoElectronicoRepository contactoCorreoElectronicoRepository;

  public ContactoCorreoElectronicoService(ContactoCorreoElectronicoRepository contactoCorreoElectronicoRepository) {
    super(contactoCorreoElectronicoRepository);
    this.contactoCorreoElectronicoRepository = contactoCorreoElectronicoRepository;
  }

  @Override
  protected void validate(ContactoCorreoElectronico entity) throws IllegalArgumentException {
    if (entity == null) {
      throw new IllegalArgumentException("El Contacto de Correo electronico no puede ser nulo");
    }
    // Additional validation logic can be added here
    if (entity.getEmail() == null || entity.getEmail().isEmpty()) {
      throw new IllegalArgumentException("Correo electronico no puede ser nulo o vacio");
    }
    if (!entity.getEmail().contains("@")) {
      throw new IllegalArgumentException("Correo electronico no es valido");
    }
    if (entity.getTipoContacto() == null) {
      throw new IllegalArgumentException("Tipo de contacto no puede ser nulo");
    }

  }

}
