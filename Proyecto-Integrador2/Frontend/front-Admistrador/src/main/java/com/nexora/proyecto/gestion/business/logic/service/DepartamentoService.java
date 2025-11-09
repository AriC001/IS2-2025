package com.nexora.proyecto.gestion.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyecto.gestion.business.persistence.dao.DepartamentoDAO;
import com.nexora.proyecto.gestion.dto.DepartamentoDTO;
import com.nexora.proyecto.gestion.dto.ProvinciaDTO;


@Service
public class DepartamentoService extends BaseService<DepartamentoDTO, String> {
  
  @Autowired
  private ProvinciaService provinciaService;

  @Autowired
  public DepartamentoService(DepartamentoDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(DepartamentoDTO entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre del departamento es obligatorio");
    }
    if (entity.getProvincia() == null) {
      throw new Exception("La provincia es obligatoria");
    }
    if (entity.getProvincia().getId() == null) {
      ProvinciaDTO provincia = provinciaService.findById(entity.getProvincia().getId());
      entity.setProvincia(provincia);
    }
  }
}
