package com.nexora.proyectointegrador2.front_cliente.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyectointegrador2.front_cliente.business.persistence.dao.PaisDAO;
import com.nexora.proyectointegrador2.front_cliente.dto.PaisDTO;

@Service
public class PaisService extends BaseService<PaisDTO, String> {
  @Autowired
  public PaisService(PaisDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(PaisDTO entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre del país es obligatorio");
    }
  }
}
