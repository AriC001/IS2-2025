package com.nexora.proyectointegrador2.front_cliente.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyectointegrador2.front_cliente.dto.DepartamentoDTO;


@Component
public class DepartamentoDAO extends BaseDAO<DepartamentoDTO, String> {
  @Autowired
  public DepartamentoDAO(RestTemplate restTemplate) {
    super(restTemplate, "/departamentos");
  }

  @Override
  protected Class<DepartamentoDTO> getEntityClass() {
    return DepartamentoDTO.class;
  }

}
