package nexora.proyectointegrador2.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Pais;
import nexora.proyectointegrador2.business.domain.entity.Provincia;
import nexora.proyectointegrador2.business.persistence.repository.ProvinciaRepository;

@Service
public class ProvinciaService extends BaseService<Provincia, String> {

  @Autowired
  private PaisService paisService;

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

  @Override
  protected void preAlta(Provincia entity) throws Exception {
    if (entity.getPais() != null && entity.getPais().getId() != null) {
      Pais paisExistente = paisService.findById(entity.getPais().getId());
      entity.setPais(paisExistente);
    } else if (entity.getPais() != null && entity.getPais().getId() == null) {
      // Si el país viene sin ID, intentar guardarlo primero
      Pais paisGuardado = paisService.save(entity.getPais());
      entity.setPais(paisGuardado);
    }
  }

  @Override
  protected void preUpdate(String id, Provincia entity) throws Exception {
    if (entity.getPais() != null && entity.getPais().getId() != null) {
      Provincia provinciaExistente = findById(id);
      if (provinciaExistente.getPais() == null ||
          !provinciaExistente.getPais().getId().equals(entity.getPais().getId())) {
        Pais paisExistente = paisService.findById(entity.getPais().getId());
        entity.setPais(paisExistente);
      }
    } else if (entity.getPais() != null && entity.getPais().getId() == null) {
      // Si el país viene sin ID, intentar guardarlo primero
      Pais paisGuardado = paisService.save(entity.getPais());
      entity.setPais(paisGuardado);
    }
  }

}
