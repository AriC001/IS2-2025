package com.nexora.proyecto.gestion.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyecto.gestion.dto.ContactoTelefonicoDTO;

@Component
public class ContactoTelefonicoDAO extends BaseDAO<ContactoTelefonicoDTO, String> {

  @Autowired
  public ContactoTelefonicoDAO(RestTemplate restTemplate) {
    super(restTemplate, "/contactos-telefonicos");
  }

  @Override
  protected Class<ContactoTelefonicoDTO> getEntityClass() {
    return ContactoTelefonicoDTO.class;
  }

}

