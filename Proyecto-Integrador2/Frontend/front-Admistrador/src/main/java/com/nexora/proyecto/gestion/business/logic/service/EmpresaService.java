package com.nexora.proyecto.gestion.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyecto.gestion.business.persistence.dao.EmpresaDAO;
import com.nexora.proyecto.gestion.dto.EmpresaDTO;

@Service
public class EmpresaService extends BaseService<EmpresaDTO, String> {

  @Autowired
  public EmpresaService(EmpresaDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(EmpresaDTO entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre de la empresa es obligatorio");
    }
    if (entity.getTelefono() == null || entity.getTelefono().trim().isEmpty()) {
      throw new Exception("El teléfono de la empresa es obligatorio");
    }
    if (entity.getEmail() == null || entity.getEmail().trim().isEmpty()) {
      throw new Exception("El email de la empresa es obligatorio");
    }
    if (entity.getDireccion() == null) {
      throw new Exception("La dirección de la empresa es obligatoria");
    }
  }

}

