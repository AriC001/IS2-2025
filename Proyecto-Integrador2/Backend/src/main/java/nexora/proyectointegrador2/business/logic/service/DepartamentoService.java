package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Departamento;
import nexora.proyectointegrador2.business.persistence.repository.DepartamentoRepository;

@Service
public class DepartamentoService extends BaseService<Departamento, String> {

  public DepartamentoService(DepartamentoRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(Departamento entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre del departamento es obligatorio");
    }
    if (entity.getProvincia() == null) {
      throw new Exception("La provincia es obligatoria");
    }
  }

}
