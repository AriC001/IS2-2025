package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Localidad;
import nexora.proyectointegrador2.business.persistence.repository.LocalidadRepository;

@Service
public class LocalidadService extends BaseService<Localidad, String> {

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

}
