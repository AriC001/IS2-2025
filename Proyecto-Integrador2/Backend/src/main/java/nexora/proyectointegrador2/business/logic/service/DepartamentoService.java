package nexora.proyectointegrador2.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Departamento;
import nexora.proyectointegrador2.business.domain.entity.Provincia;
import nexora.proyectointegrador2.business.persistence.repository.DepartamentoRepository;

@Service
public class DepartamentoService extends BaseService<Departamento, String> {

  @Autowired
  private ProvinciaService provinciaService;

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

  @Override
  protected void preAlta(Departamento entity) throws Exception {
    if (entity.getProvincia() != null && entity.getProvincia().getId() == null) {
      Provincia provinciaGuardada = provinciaService.save(entity.getProvincia());
      entity.setProvincia(provinciaGuardada);
    } else if (entity.getProvincia() != null && entity.getProvincia().getId() != null) {
      Provincia provinciaExistente = provinciaService.findById(entity.getProvincia().getId());
      entity.setProvincia(provinciaExistente);
    }
  }

  @Override
  protected void preUpdate(String id, Departamento entity) throws Exception {
    if (entity.getProvincia() != null && entity.getProvincia().getId() == null) {
      Provincia provinciaGuardada = provinciaService.save(entity.getProvincia());
      entity.setProvincia(provinciaGuardada);
    } else if (entity.getProvincia() != null && entity.getProvincia().getId() != null) {
      Departamento departamentoExistente = findById(id);
      if (departamentoExistente.getProvincia() == null ||
          !departamentoExistente.getProvincia().getId().equals(entity.getProvincia().getId())) {
        Provincia provinciaExistente = provinciaService.findById(entity.getProvincia().getId());
        entity.setProvincia(provinciaExistente);
      }
    }
  }

}
