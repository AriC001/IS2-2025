package com.nexora.proyectointegrador2.front_cliente.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyectointegrador2.front_cliente.business.persistence.dao.NacionalidadDAO;
import com.nexora.proyectointegrador2.front_cliente.dto.NacionalidadDTO;

@Service
public class NacionalidadService extends BaseService<NacionalidadDTO, String> {

  @Autowired
  public NacionalidadService(NacionalidadDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(NacionalidadDTO entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre de la nacionalidad es obligatorio");
    }
  }



}
