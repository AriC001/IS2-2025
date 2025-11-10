package com.nexora.proyecto.gestion.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyecto.gestion.business.persistence.dao.ContactoCorreoElectronicoDAO;
import com.nexora.proyecto.gestion.dto.ContactoCorreoElectronicoDTO;

@Service
public class ContactoCorreoElectronicoService extends BaseService<ContactoCorreoElectronicoDTO, String> {

  @Autowired
  public ContactoCorreoElectronicoService(ContactoCorreoElectronicoDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(ContactoCorreoElectronicoDTO entity) throws Exception {
    if (entity.getEmail() == null || entity.getEmail().trim().isEmpty()) {
      throw new Exception("El correo electrónico es obligatorio");
    }
    if (entity.getTipoContacto() == null) {
      throw new Exception("El tipo de contacto es obligatorio");
    }
  }

}

