package nexora.proyectointegrador2.business.logic.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Pais;
import nexora.proyectointegrador2.business.persistence.repository.PaisRepository;

@Service
public class PaisService extends BaseService<Pais, String> {

  private final PaisRepository paisRepository;

  public PaisService(PaisRepository repository) {
    super(repository);
    this.paisRepository = repository;
  }

  @Override
  protected void validar(Pais entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre del país es obligatorio");
    }
  }

  /**
   * Busca un país por nombre (solo activos).
   * 
   * @param nombre nombre del país a buscar
   * @return Optional con el país si existe, vacío si no
   */
  public Optional<Pais> findByNombre(String nombre) {
    if (nombre == null || nombre.trim().isEmpty()) {
      return Optional.empty();
    }
    return paisRepository.findByNombreAndEliminadoFalse(nombre.trim());
  }

}
