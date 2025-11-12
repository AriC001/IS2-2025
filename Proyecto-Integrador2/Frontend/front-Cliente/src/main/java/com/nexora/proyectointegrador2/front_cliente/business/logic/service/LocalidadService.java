package com.nexora.proyectointegrador2.front_cliente.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyectointegrador2.front_cliente.business.persistence.dao.LocalidadDAO;
import com.nexora.proyectointegrador2.front_cliente.dto.LocalidadDTO;

@Service
public class LocalidadService extends BaseService<LocalidadDTO, String> {

  @Autowired
  public LocalidadService(LocalidadDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(LocalidadDTO entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre de la localidad es obligatorio");
    }

    if (entity.getCodigoPostal() == null || entity.getCodigoPostal().trim().isEmpty()) {
      throw new Exception("El código postal es obligatorio");
    }
    if (entity.getDepartamento() == null || entity.getDepartamento().getId() == null
        || entity.getDepartamento().getId().trim().isEmpty()) {
      throw new Exception("El departamento es obligatorio");
    }
  }

}
