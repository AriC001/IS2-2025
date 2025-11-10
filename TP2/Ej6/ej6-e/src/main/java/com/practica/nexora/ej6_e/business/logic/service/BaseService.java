package com.practica.nexora.ej6_e.business.logic.service;

import java.util.List;
import java.util.Optional;

import com.practica.nexora.ej6_e.business.domain.entity.BaseEntity;
import com.practica.nexora.ej6_e.business.persistence.repository.BaseRepository;

public abstract class BaseService<T extends BaseEntity<ID>, ID> {

  protected final BaseRepository<T, ID> repository;

  protected BaseService(BaseRepository<T, ID> repository) {
    this.repository = repository;
  }

  public Optional<T> findById(ID id) throws IllegalArgumentException {
    if (id == null) {
      throw new IllegalArgumentException("ID no puede ser nulo");
    }
    return repository.findByIdAndEliminadoFalse(id);
  }

  public T save(T entity) throws IllegalArgumentException {
    if (entity == null) {
      throw new IllegalArgumentException("Entidad no puede ser nula");
    }
    return repository.save(entity);
  }

  public void delete(ID id) throws IllegalArgumentException {
    if (id == null) {
      throw new IllegalArgumentException("ID no puede ser nulo");
    }
    Optional<T> entityOpt = repository.findByIdAndEliminadoFalse(id);
    if (entityOpt.isEmpty()) {
      throw new IllegalArgumentException("Entidad con ID " + id + " no existe");
    }
    T entity = entityOpt.get();
    entity.setEliminado(true);
    repository.save(entity);
  }

  public T update(ID id, T entity) throws IllegalArgumentException {
    if (id == null) {
      throw new IllegalArgumentException("ID no puede ser nulo");
    }
    if (entity == null) {
      throw new IllegalArgumentException("Entidad no puede ser nula");
    }
    Optional<T> existingEntityOpt = repository.findByIdAndEliminadoFalse(id);
    if (existingEntityOpt.isEmpty()) {
      throw new IllegalArgumentException("Entidad con ID " + id + " no existe");
    }
    T existingEntity = existingEntityOpt.get();
    entity.setId(existingEntity.getId());
    return repository.save(entity);
  }

  public List<T> findAllActives() throws IllegalArgumentException {
    return repository.findAllByEliminadoFalse();
  }

  protected abstract void validate(T entity) throws IllegalArgumentException;

}
