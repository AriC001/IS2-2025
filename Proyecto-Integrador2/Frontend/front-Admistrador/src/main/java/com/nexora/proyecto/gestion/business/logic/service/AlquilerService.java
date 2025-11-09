package com.nexora.proyecto.gestion.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyecto.gestion.business.persistence.dao.AlquilerDAO;
import com.nexora.proyecto.gestion.dto.AlquilerDTO;

@Service
public class AlquilerService extends BaseService<AlquilerDTO, String> {

  @Autowired
  public AlquilerService(AlquilerDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(AlquilerDTO entity) throws Exception {
    if (entity.getFechaDesde() == null) {
      throw new Exception("La fecha desde es obligatoria");
    }
    if (entity.getFechaHasta() == null) {
      throw new Exception("La fecha hasta es obligatoria");
    }
    if (entity.getFechaDesde().after(entity.getFechaHasta())) {
      throw new Exception("La fecha desde no puede ser posterior a la fecha hasta");
    }
    if (entity.getCliente() == null) {
      throw new Exception("El cliente es obligatorio");
    }
    if (entity.getVehiculo() == null) {
      throw new Exception("El vehículo es obligatorio");
    }
  }

}

