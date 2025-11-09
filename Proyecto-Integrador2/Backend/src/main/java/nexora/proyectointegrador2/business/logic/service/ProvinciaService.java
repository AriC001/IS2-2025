package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Provincia;
import nexora.proyectointegrador2.business.persistence.repository.ProvinciaRepository;

@Service
public class ProvinciaService extends BaseService<Provincia, String> {

  public ProvinciaService(ProvinciaRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(Provincia entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre de la provincia es obligatorio");
    }
    if (entity.getPais() == null) {
      throw new Exception("El país es obligatorio");
    }
  }

}
