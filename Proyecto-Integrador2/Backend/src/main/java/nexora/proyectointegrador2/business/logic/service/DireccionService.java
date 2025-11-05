package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Direccion;
import nexora.proyectointegrador2.business.persistence.repository.DireccionRepository;

@Service
public class DireccionService extends BaseService<Direccion, String> {

  public DireccionService(DireccionRepository repository) {
    super(repository);
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

}
