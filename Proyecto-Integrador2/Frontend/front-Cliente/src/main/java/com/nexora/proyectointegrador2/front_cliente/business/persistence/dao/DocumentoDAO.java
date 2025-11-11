package com.nexora.proyectointegrador2.front_cliente.business.persistence.dao;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyectointegrador2.front_cliente.dto.DocumentoDTO;

@Repository
public class DocumentoDAO extends BaseDAO<DocumentoDTO,String> {

    
  public DocumentoDAO(RestTemplate restTemplate) {
    super(restTemplate, "/documento");
  }

    @Override
    protected Class<DocumentoDTO> getEntityClass() {
        return DocumentoDTO.class;
    }
    
}
