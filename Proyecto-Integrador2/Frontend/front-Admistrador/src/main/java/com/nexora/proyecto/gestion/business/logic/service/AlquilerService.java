package com.nexora.proyecto.gestion.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nexora.proyecto.gestion.business.persistence.dao.AlquilerDAO;
import com.nexora.proyecto.gestion.dto.AlquilerDTO;

@Service
public class AlquilerService extends BaseService<AlquilerDTO, String> {

  private final AlquilerDAO alquilerDAO;

  @Autowired
  public AlquilerService(AlquilerDAO dao) {
    super(dao);
    this.alquilerDAO = dao;
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

  /**
   * Crea un alquiler con documento adjunto.
   */
  public AlquilerDTO createWithDocument(AlquilerDTO entity, MultipartFile archivoDocumento) throws Exception {
    logger.info("Creando nuevo alquiler con documento: {}", entity.getClass().getSimpleName());
    validateEntity(entity);
    AlquilerDTO created = alquilerDAO.saveWithDocument(entity, archivoDocumento);
    logger.info("Alquiler con documento creado exitosamente con ID: {}", created.getId());
    return created;
  }

  /**
   * Actualiza un alquiler con documento adjunto.
   */
  public AlquilerDTO updateWithDocument(String id, AlquilerDTO entity, MultipartFile archivoDocumento) throws Exception {
    if (id == null) {
      throw new IllegalArgumentException("ID no puede ser null");
    }
    logger.info("Actualizando alquiler con documento con ID: {}", id);
    validateEntity(entity);
    AlquilerDTO updated = alquilerDAO.updateWithDocument(id, entity, archivoDocumento);
    logger.info("Alquiler con documento actualizado exitosamente con ID: {}", id);
    return updated;
  }

}

