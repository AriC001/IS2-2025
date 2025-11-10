package com.nexora.proyecto.gestion.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyecto.gestion.dto.DocumentoDTO;

@Component
public class DocumentoDAO extends BaseDAO<DocumentoDTO, String> {

  @Autowired
  public DocumentoDAO(RestTemplate restTemplate) {
    super(restTemplate, "/api/v1/documentos");
  }

  @Override
  protected Class<DocumentoDTO> getEntityClass() {
    return DocumentoDTO.class;
  }

}

