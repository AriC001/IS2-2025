package com.nexora.proyectointegrador2.front_cliente.business.persistence.dao;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyectointegrador2.front_cliente.dto.VehiculoDTO;

@Component
public class VehiculoDAO extends BaseDAO<VehiculoDTO, String> {

  @Autowired
  public VehiculoDAO(RestTemplate restTemplate) {
    super(restTemplate, "/vehiculos");
  }

  @Override
  protected Class<VehiculoDTO> getEntityClass() {
    return VehiculoDTO.class;
  }

  public boolean findAvailability(Map<String,String> param) {
    String url = baseUrl + entityPath + "/availability";
    StringJoiner sj = new StringJoiner("&");
    for (Map.Entry<String, String> e : param.entrySet()) {
      if (e.getValue() != null && !e.getValue().isBlank()) {
        sj.add(URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8));
      }
    }
    String q = sj.toString();
    if (!q.isBlank()) {
      url = url + "?" + q;
    }
    // Call backend which returns a boolean indicating availability for the given params.
    // Use Boolean.class to let RestTemplate map the response; handle null safely.
    Boolean resp = restTemplate.getForObject(url, Boolean.class);
    return Boolean.TRUE.equals(resp);
  }

}

