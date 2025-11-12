package com.nexora.proyectointegrador2.front_cliente.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyectointegrador2.front_cliente.business.persistence.dao.VehiculoDAO;
import com.nexora.proyectointegrador2.front_cliente.dto.VehiculoDTO;

@Service
public class VehiculoService extends BaseService<VehiculoDTO, String> {

  private final VehiculoDAO vehiculoDAO;

  @Autowired
  public VehiculoService(VehiculoDAO dao) {
    super(dao);
    this.vehiculoDAO = dao;
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

  public boolean findAvailability(java.util.Map<String, String> filters) throws Exception {
    logger.debug("Obteniendo entidades con filtros: {}", filters);
    if (filters != null && !filters.isEmpty()) {
      return vehiculoDAO.findAvailability(filters);
    }else{
      return false;
    }
  }

}

