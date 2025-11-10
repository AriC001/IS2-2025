package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Pais;
import nexora.proyectointegrador2.business.persistence.repository.PaisRepository;

@Service
public class PaisService extends BaseService<Pais, String> {

  public PaisService(PaisRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(Pais entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre del país es obligatorio");
    }
  }

}
