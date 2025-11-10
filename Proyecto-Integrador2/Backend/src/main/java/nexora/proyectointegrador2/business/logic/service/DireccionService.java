package nexora.proyectointegrador2.business.logic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Direccion;
import nexora.proyectointegrador2.business.domain.entity.Localidad;
import nexora.proyectointegrador2.business.persistence.repository.DireccionRepository;

@Service
public class DireccionService extends BaseService<Direccion, String> {

  private final DireccionRepository direccionRepository;

  @Autowired
  private LocalidadService localidadService;

  public DireccionService(DireccionRepository repository) {
    super(repository);
    this.direccionRepository = repository;
  }

  @Override
  protected void validar(Direccion entity) throws Exception {
    if (entity.getCalle() == null || entity.getCalle().trim().isEmpty()) {
      throw new Exception("La calle es obligatoria");
    }
    if (entity.getNumero() == null || entity.getNumero().trim().isEmpty()) {
      throw new Exception("El número es obligatorio");
    }
    if (entity.getLocalidad() == null) {
      throw new Exception("La localidad es obligatoria");
    }
  }

  @Override
  protected void preAlta(Direccion entity) throws Exception {
    if (entity.getLocalidad() != null && entity.getLocalidad().getId() == null) {
      Localidad localidadGuardada = localidadService.save(entity.getLocalidad());
      entity.setLocalidad(localidadGuardada);
    } else if (entity.getLocalidad() != null && entity.getLocalidad().getId() != null) {
      Localidad localidadExistente = localidadService.findById(entity.getLocalidad().getId());
      entity.setLocalidad(localidadExistente);
    }
  }

  @Override
  protected void preUpdate(String id, Direccion entity) throws Exception {
    if (entity.getLocalidad() != null && entity.getLocalidad().getId() == null) {
      Localidad localidadGuardada = localidadService.save(entity.getLocalidad());
      entity.setLocalidad(localidadGuardada);
    } else if (entity.getLocalidad() != null && entity.getLocalidad().getId() != null) {
      Direccion direccionExistente = findById(id);
      if (direccionExistente.getLocalidad() == null ||
          !direccionExistente.getLocalidad().getId().equals(entity.getLocalidad().getId())) {
        Localidad localidadExistente = localidadService.findById(entity.getLocalidad().getId());
        entity.setLocalidad(localidadExistente);
      }
    }
  }

  @Override
  public Collection<Direccion> findAllActives() throws Exception {
    return direccionRepository.findAllActivesWithLocalidad();
  }

  public Direccion findByIdWithLocalidad(String id) throws Exception {
    if (id == null) {
      throw new Exception("El ID no puede ser nulo");
    }
    return direccionRepository.findByIdWithLocalidad(id)
        .orElseThrow(() -> new Exception("Dirección no encontrada con ID: " + id));
  }

}
