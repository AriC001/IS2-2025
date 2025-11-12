package com.nexora.proyecto.gestion.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexora.proyecto.gestion.business.persistence.dao.FacturaDAO;
import com.nexora.proyecto.gestion.dto.FacturaDTO;

@Service
public class FacturaService extends BaseService<FacturaDTO, String> {

  @Autowired
  public FacturaService(FacturaDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(FacturaDTO entity) throws Exception {
    if (entity.getNumeroFactura() == null) {
      throw new Exception("El número de factura es obligatorio");
    }
    if (entity.getFechaFactura() == null) {
      throw new Exception("La fecha de factura es obligatoria");
    }
  }

}

