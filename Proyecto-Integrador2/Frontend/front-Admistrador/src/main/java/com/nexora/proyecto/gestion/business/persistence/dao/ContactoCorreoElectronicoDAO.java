package com.nexora.proyecto.gestion.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyecto.gestion.dto.ContactoCorreoElectronicoDTO;

@Component
public class ContactoCorreoElectronicoDAO extends BaseDAO<ContactoCorreoElectronicoDTO, String> {

  @Autowired
  public ContactoCorreoElectronicoDAO(RestTemplate restTemplate) {
    super(restTemplate, "/contactos-correo-electronico");
  }

  @Override
  protected Class<ContactoCorreoElectronicoDTO> getEntityClass() {
    return ContactoCorreoElectronicoDTO.class;
  }

}

