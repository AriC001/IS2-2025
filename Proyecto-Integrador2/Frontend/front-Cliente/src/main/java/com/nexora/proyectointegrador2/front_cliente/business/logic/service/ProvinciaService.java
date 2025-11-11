package com.nexora.proyectointegrador2.front_cliente.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyectointegrador2.front_cliente.business.persistence.dao.ProvinciaDAO;
import com.nexora.proyectointegrador2.front_cliente.dto.ProvinciaDTO;

@Service
public class ProvinciaService extends BaseService<ProvinciaDTO, String> {
  @Autowired
  public ProvinciaService(ProvinciaDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(ProvinciaDTO entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre de la provincia es obligatorio");
    }
    if (entity.getPais() == null) {
      throw new Exception("El país es obligatorio");
    }
  }
}
