package com.nexora.proyectointegrador2.front_cliente.business.persistence.dao;

import org.springframework.web.client.RestTemplate;
import com.nexora.proyectointegrador2.front_cliente.dto.AlquilerDTO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AlquilerDAO extends BaseDAO<AlquilerDTO, String> {

  public AlquilerDAO(RestTemplate restTemplate) {
    super(restTemplate, "/alquiler");
  }

  @Override
  protected Class<AlquilerDTO> getEntityClass() {
    return AlquilerDTO.class;
  }

  public List<AlquilerDTO> findByUsuarioId(String usuarioId) {
    Map<String, String> params = new HashMap<>();
    params.put("usuarioId", usuarioId);
    return findByQueryParams(params);
  }
    
}
