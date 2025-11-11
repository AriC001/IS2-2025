package com.nexora.proyectointegrador2.front_cliente.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyectointegrador2.front_cliente.business.persistence.dao.DireccionDAO;
import com.nexora.proyectointegrador2.front_cliente.dto.DireccionDTO;

@Service
public class DireccionService extends BaseService<DireccionDTO, String> {

  @Autowired
  public DireccionService(DireccionDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(DireccionDTO entity) throws Exception {
    if (entity.getCalle() == null || entity.getCalle().trim().isEmpty()) {
      throw new Exception("La calle es obligatoria");
    }
    if (entity.getNumero() == null || entity.getNumero().trim().isEmpty()) {
      throw new Exception("El número es obligatorio");
    }
    if (entity.getLocalidad() == null || entity.getLocalidad().getId() == null) {
      throw new Exception("La localidad es obligatoria");
    }
  }
}

