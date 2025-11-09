package com.nexora.proyecto.gestion.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyecto.gestion.business.persistence.dao.VehiculoDAO;
import com.nexora.proyecto.gestion.dto.VehiculoDTO;

@Service
public class VehiculoService extends BaseService<VehiculoDTO, String> {

  @Autowired
  public VehiculoService(VehiculoDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(VehiculoDTO entity) throws Exception {
    if (entity.getPatente() == null || entity.getPatente().trim().isEmpty()) {
      throw new Exception("La patente es obligatoria");
    }
    if (entity.getEstadoVehiculo() == null) {
      throw new Exception("El estado del vehículo es obligatorio");
    }
    if (entity.getCaracteristicaVehiculo() == null) {
      throw new Exception("La característica del vehículo es obligatoria");
    }
  }

}

