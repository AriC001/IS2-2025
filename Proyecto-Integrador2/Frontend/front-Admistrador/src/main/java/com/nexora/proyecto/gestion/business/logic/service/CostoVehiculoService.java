package com.nexora.proyecto.gestion.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyecto.gestion.business.persistence.dao.CostoVehiculoDAO;
import com.nexora.proyecto.gestion.dto.CostoVehiculoDTO;

@Service
public class CostoVehiculoService extends BaseService<CostoVehiculoDTO, String> {

  @Autowired
  public CostoVehiculoService(CostoVehiculoDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(CostoVehiculoDTO entity) throws Exception {
    if (entity.getFechaDesde() == null) {
      throw new Exception("La fecha desde es obligatoria");
    }
    if (entity.getFechaHasta() == null) {
      throw new Exception("La fecha hasta es obligatoria");
    }
    if (entity.getCosto() == null || entity.getCosto() <= 0) {
      throw new Exception("El costo es obligatorio y debe ser mayor a 0");
    }
    if (entity.getFechaDesde().after(entity.getFechaHasta())) {
      throw new Exception("La fecha desde no puede ser posterior a la fecha hasta");
    }
  }

}

