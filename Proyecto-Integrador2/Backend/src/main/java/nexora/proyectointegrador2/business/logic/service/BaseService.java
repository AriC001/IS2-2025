package nexora.proyectointegrador2.business.logic.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import nexora.proyectointegrador2.business.domain.entity.BaseEntity;
import nexora.proyectointegrador2.business.persistence.repository.BaseRepository;


public abstract class BaseService<T extends BaseEntity<ID>, ID> {
  
  protected final BaseRepository<T, ID> repository;

  protected BaseService(BaseRepository<T, ID> repository) {
    this.repository = repository;
  }

  @Transactional
  public T save(T entity) throws Exception {
    if (entity == null) {
      throw new Exception("La entidad no puede ser nula");
    }
    validar(entity);
    entity.setEliminado(false);
    return repository.save(entity);
  }

  @Transactional
  public T update(ID id, T entity) throws Exception {
    if (id == null) {
      throw new Exception("El ID no puede ser nulo");
    }
    if (entity == null) {
      throw new Exception("La entidad no puede ser nula");
    }
    if (!repository.existsById(id)) {
      throw new Exception("La entidad no existe con ID: " + id);
    }
    validar(entity);
    entity.setId(id);
    return repository.save(entity);
  }

  @Transactional(readOnly = true)
  public T findById(ID id) throws Exception {
    if (id == null) {
      throw new Exception("El ID no puede ser nulo");
    }
    return repository.findById(id)
        .orElseThrow(() -> new Exception("Entidad no encontrada con ID: " + id));
  }

  @Transactional(readOnly = true)
  public Optional<T> findByIdOptional(ID id) {
    if (id == null) {
      return Optional.empty();
    }
    return repository.findById(id);
  }

  @Transactional(readOnly = true)
  public Collection<T> findAll() throws Exception {
    return repository.findAll();
  }

  @Transactional(readOnly = true)
  public Collection<T> findAllActives() throws Exception {
    return repository.findAllByEliminadoFalse();
  }

  @Transactional
  public void logicDelete(ID id) throws Exception {
    if (id == null) {
      throw new Exception("El ID no puede ser nulo");
    }
    T entity = repository.findById(id)
        .orElseThrow(() -> new Exception("Entidad no encontrada con ID: " + id));
    entity.setEliminado(true);
    repository.save(entity);
  }

  @Transactional
  public void delete(ID id) throws Exception {
    if (id == null) {
      throw new Exception("El ID no puede ser nulo");
    }
    if (!repository.existsById(id)) {
      throw new Exception("La entidad no existe con ID: " + id);
    }
    repository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public boolean existsById(ID id) {
    if (id == null) {
      return false;
    }
    return repository.existsById(id);
  }

  @Transactional(readOnly = true)
  public long count() {
    return repository.count();
  }

  protected abstract void validar(T entity) throws Exception;

}