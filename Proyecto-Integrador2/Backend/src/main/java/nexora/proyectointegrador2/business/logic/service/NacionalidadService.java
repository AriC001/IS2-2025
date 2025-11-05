package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Nacionalidad;
import nexora.proyectointegrador2.business.persistence.repository.NacionalidadRepository;

@Service
public class NacionalidadService extends BaseService<Nacionalidad, String> {

  public NacionalidadService(NacionalidadRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(Nacionalidad entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre de la nacionalidad es obligatorio");
    }
  }

}
