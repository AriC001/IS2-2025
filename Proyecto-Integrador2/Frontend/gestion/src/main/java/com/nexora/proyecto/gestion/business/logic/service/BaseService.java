package com.nexora.proyecto.gestion.business.logic.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nexora.proyecto.gestion.business.persistence.dao.BaseDAO;
import com.nexora.proyecto.gestion.dto.BaseDTO;

public abstract class BaseService<T extends BaseDTO, ID> {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());
  protected BaseDAO<T, ID> dao;

  public BaseService(BaseDAO<T, ID> dao) {
    this.dao = dao;
  }

  public T create(T entity) throws Exception {
    logger.info("Creando nueva entidad: {}", entity.getClass().getSimpleName());
    validateEntity(entity);
    T created = dao.save(entity);
    logger.info("Entidad creada exitosamente con ID: {}", created.getId());
    return created;
  }

  public T update(ID id, T entity) throws Exception {
    if (id == null) {
      throw new IllegalArgumentException("ID no puede ser null");
    }
    logger.info("Actualizando entidad con ID: {}", id);
    validateEntity(entity);
    T updated = dao.update(id, entity);
    logger.info("Entidad actualizada exitosamente con ID: {}", id);
    return updated;
  }

  public void delete(ID id) throws Exception {
    if (id == null) {
      throw new IllegalArgumentException("ID no puede ser null");
    }
    logger.info("Eliminando entidad con ID: {}", id);
    dao.delete(id);
    logger.info("Entidad eliminada exitosamente con ID: {}", id);
  }

  public T findById(ID id) throws Exception {
    if (id == null) {
      throw new IllegalArgumentException("ID no puede ser null");
    }
    logger.debug("Buscando entidad con ID: {}", id);
    return dao.findById(id);
  }

  public List<T> findAllActives() throws Exception {
    logger.debug("Obteniendo todas las entidades activas");
    return dao.findAllActives();
  }

  protected abstract void validateEntity(T entity) throws Exception;
}
