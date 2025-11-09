package com.nexora.proyecto.gestion.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyecto.gestion.business.persistence.dao.DocumentoDAO;
import com.nexora.proyecto.gestion.dto.DocumentoDTO;

@Service
public class DocumentoService extends BaseService<DocumentoDTO, String> {

  @Autowired
  public DocumentoService(DocumentoDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(DocumentoDTO entity) throws Exception {
    if (entity.getTipoDocumento() == null) {
      throw new Exception("El tipo de documento es obligatorio");
    }
    if (entity.getPathArchivo() == null || entity.getPathArchivo().trim().isEmpty()) {
      throw new Exception("El path del archivo es obligatorio");
    }
    if (entity.getNombreArchivo() == null || entity.getNombreArchivo().trim().isEmpty()) {
      throw new Exception("El nombre del archivo es obligatorio");
    }
  }

}

