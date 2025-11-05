package nexora.proyectointegrador2.business.logic.service;

import jakarta.transaction.Transactional;
import nexora.proyectointegrador2.business.domain.entity.BaseEntity;
import nexora.proyectointegrador2.business.persistence.repository.BaseRepository;


public abstract class BaseService<T extends BaseEntity<ID>, ID>  {
  
  protected final BaseRepository<T, ID> repository;

  public BaseService(BaseRepository<T, ID> repository) {
    this.repository = repository;
  }

  @Transactional
  public T save(T entity) throws Exception {
    try {
      validar(entity);
      entity.setEliminado(false);
      return repository.save(entity);
    } catch (Exception e) {
      throw new Exception("Error al guardar la entidad", e);
    }
  }

  @Transactional
  public T update(T entity, ID id) throws Exception {
    if (id == null || !repository.existsById(id)) {
      throw new Exception("La entidad no existe");
    }
    validar(entity);
    entity.setId(id);
    return repository.save(entity);
  }

  @Transactional
  public T findById(ID id) throws Exception {
    return repository.findById(id).orElseThrow(() -> new Exception("Entidad no encontrada"));
  }

  @Transactional
  public Iterable<T> findAll() throws Exception {
    try {
      return repository.findAll();
    } catch (Exception e) {
      throw new Exception("Error al obtener las entidades", e);
    }
  }

  @Transactional
  public Iterable<T> findAllActives() throws Exception {
    try {
      return repository.findAllByEliminadoFalse();
    } catch (Exception e) {
      throw new Exception("Error al obtener las entidades", e);
    }
  }

  @Transactional
  public void logicDelete(ID id) throws Exception {
    try {
      T entity = repository.findById(id).orElseThrow(() -> new Exception("Entidad no encontrada"));
      entity.setEliminado(true);
      repository.save(entity);
    } catch (Exception e) {
      throw new Exception("Error al eliminar la entidad", e);
    }
  }

  @Transactional
  public void delete(ID id) throws Exception {
    try {
      if (!repository.existsById(id)) {
        throw new Exception("La entidad no existe");
      }
      repository.deleteById(id);
    } catch (Exception e) {
      throw new Exception("Error al eliminar la entidad", e);
    }
  }


  protected abstract void validar(T entity) throws Exception;

}