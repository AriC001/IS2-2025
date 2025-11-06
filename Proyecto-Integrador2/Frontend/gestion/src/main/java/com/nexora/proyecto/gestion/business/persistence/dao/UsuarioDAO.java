package com.nexora.proyecto.gestion.business.persistence.dao;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyecto.gestion.dto.UsuarioDTO;

@Repository
public class UsuarioDAO extends BaseDAO<UsuarioDTO, String> {

  public UsuarioDAO(RestTemplate restTemplate) {
    super(restTemplate, "/usuarios");
  }

  @Override
  protected Class<UsuarioDTO> getEntityClass() {
    return UsuarioDTO.class;
  }

}
