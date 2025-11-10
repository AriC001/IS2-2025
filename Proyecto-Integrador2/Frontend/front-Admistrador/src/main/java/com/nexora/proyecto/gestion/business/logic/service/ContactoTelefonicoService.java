package com.nexora.proyecto.gestion.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyecto.gestion.business.persistence.dao.ContactoTelefonicoDAO;
import com.nexora.proyecto.gestion.dto.ContactoTelefonicoDTO;

@Service
public class ContactoTelefonicoService extends BaseService<ContactoTelefonicoDTO, String> {

  @Autowired
  public ContactoTelefonicoService(ContactoTelefonicoDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(ContactoTelefonicoDTO entity) throws Exception {
    if (entity.getTelefono() == null || entity.getTelefono().trim().isEmpty()) {
      throw new Exception("El teléfono es obligatorio");
    }
    if (entity.getTipoTelefono() == null) {
      throw new Exception("El tipo de teléfono es obligatorio");
    }
    if (entity.getTipoContacto() == null) {
      throw new Exception("El tipo de contacto es obligatorio");
    }
  }

}

