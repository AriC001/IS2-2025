package com.practica.ej2consumer.business.persistence.rest;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.practica.ej2consumer.business.domain.dto.LibroDTO;

@Repository
public class LibroDAORest extends BaseDAORest<LibroDTO, Long> {
  
  public LibroDAORest(RestTemplate restTemplate) {
    super(restTemplate, "/libros");
  }

  // Método de búsqueda por criterio y valor
  public List<LibroDTO> findByCriterioYValor(String criterio, String valor) {
    String url = baseUrl + "/libros/busqueda?criterio=" + criterio + "&valor=" + valor;
    System.out.println("🌐 Llamando a: " + url);
    return List.of(restTemplate.getForObject(url, LibroDTO[].class));
  }

  @Override
  protected Class<LibroDTO> getEntityClass() {
    return LibroDTO.class;
  }
}