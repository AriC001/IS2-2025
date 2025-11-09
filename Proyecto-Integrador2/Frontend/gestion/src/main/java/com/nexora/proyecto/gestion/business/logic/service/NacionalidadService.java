package com.nexora.proyecto.gestion.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyecto.gestion.business.persistence.dao.NacionalidadDAO;
import com.nexora.proyecto.gestion.dto.NacionalidadDTO;

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
