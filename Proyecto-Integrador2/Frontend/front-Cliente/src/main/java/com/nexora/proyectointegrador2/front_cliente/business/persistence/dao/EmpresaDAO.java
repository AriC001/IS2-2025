package com.nexora.proyectointegrador2.front_cliente.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyectointegrador2.front_cliente.dto.EmpresaDTO;

@Component
public class EmpresaDAO extends BaseDAO<EmpresaDTO, String> {

  @Autowired
  public EmpresaDAO(RestTemplate restTemplate) {
    super(restTemplate, "/empresas");
  }

  @Override
  protected Class<EmpresaDTO> getEntityClass() {
    return EmpresaDTO.class;
  }

}

