package nexora.proyectointegrador2.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Departamento;
import nexora.proyectointegrador2.business.domain.entity.Localidad;
import nexora.proyectointegrador2.business.persistence.repository.LocalidadRepository;

@Service
public class LocalidadService extends BaseService<Localidad, String> {

  @Autowired
  private DepartamentoService departamentoService;

  public LocalidadService(LocalidadRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(Localidad entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre de la localidad es obligatorio");
    }
    if (entity.getCodigoPostal() == null || entity.getCodigoPostal().trim().isEmpty()) {
      throw new Exception("El código postal es obligatorio");
    }
    if (entity.getDepartamento() == null) {
      throw new Exception("El departamento es obligatorio");
    }
  }

  @Override
  protected void preAlta(Localidad entity) throws Exception {
    if (entity.getDepartamento() != null && entity.getDepartamento().getId() == null) {
      Departamento departamentoGuardado = departamentoService.save(entity.getDepartamento());
      entity.setDepartamento(departamentoGuardado);
    } else if (entity.getDepartamento() != null && entity.getDepartamento().getId() != null) {
      Departamento departamentoExistente = departamentoService.findById(entity.getDepartamento().getId());
      entity.setDepartamento(departamentoExistente);
    }
  }

  @Override
  protected void preUpdate(String id, Localidad entity) throws Exception {
    if (entity.getDepartamento() != null && entity.getDepartamento().getId() == null) {
      Departamento departamentoGuardado = departamentoService.save(entity.getDepartamento());
      entity.setDepartamento(departamentoGuardado);
    } else if (entity.getDepartamento() != null && entity.getDepartamento().getId() != null) {
      Localidad localidadExistente = findById(id);
      if (localidadExistente.getDepartamento() == null ||
          !localidadExistente.getDepartamento().getId().equals(entity.getDepartamento().getId())) {
        Departamento departamentoExistente = departamentoService.findById(entity.getDepartamento().getId());
        entity.setDepartamento(departamentoExistente);
      }
    }
  }

}
