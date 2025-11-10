package com.practica.ej2consumer.business.logic.service;

import java.util.List;

import com.practica.ej2consumer.business.domain.dto.BaseDTO;
import com.practica.ej2consumer.business.persistence.rest.BaseDAORest;


public abstract class BaseService<T extends BaseDTO, ID> {

  protected BaseDAORest<T, ID> dao;

  public BaseService(BaseDAORest<T, ID> dao) {
    this.dao = dao;
  }

  public T create(T entity) throws Exception {
    try {
      validateEntity(entity);
      return dao.save(entity);
    } catch (Exception e) {
      throw new Exception(e.getMessage(), e);
    }
  }

  public T update(ID id, T entity) throws Exception {
    try {
      if (id == null) {
        throw new IllegalArgumentException("ID no puede ser null");
      }
      validateEntity(entity);
      return dao.update(id, entity);
    } catch (IllegalArgumentException e) {
      throw new Exception(e.getMessage(), e);
    }
  }

  public void delete(ID id) throws Exception {
    try {
      if (id == null) {
        throw new IllegalArgumentException("ID no puede ser null");
      }
      dao.delete(id);
    } catch (IllegalArgumentException e) {
      throw new Exception(e.getMessage(), e);
    }
  }

  public T findById(ID id) throws Exception {
    try {
      if (id == null) {
        throw new IllegalArgumentException("ID no puede ser null");
      }
      return dao.findById(id);
    } catch (IllegalArgumentException e) {
      throw new Exception(e.getMessage(), e);
    }
  }

  public List<T> findAllActives() throws Exception {
    try {
      return dao.findAllActives();
    } catch (Exception e) {
      throw new Exception(e.getMessage(), e);
    }
  }

  protected abstract void validateEntity(T entity) throws Exception;
}
