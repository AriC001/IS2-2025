package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Cliente;
import nexora.proyectointegrador2.business.persistence.repository.ClienteRepository;

@Service
public class ClienteService extends BaseService<Cliente, String> {

  private final ClienteRepository clienteRepository;

  public ClienteService(ClienteRepository repository) {
    super(repository);
    this.clienteRepository = repository;
  }

  @Override
  protected void validar(Cliente entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
      throw new Exception("El nombre es obligatorio");
    }
    if (entity.getApellido() == null || entity.getApellido().trim().isEmpty()) {
      throw new Exception("El apellido es obligatorio");
    }
    if (entity.getFechaNacimiento() == null) {
      throw new Exception("La fecha de nacimiento es obligatoria");
    }
    if (entity.getTipoDocumento() == null) {
      throw new Exception("El tipo de documento es obligatorio");
    }
    if (entity.getNumeroDocumento() == null || entity.getNumeroDocumento().trim().isEmpty()) {
      throw new Exception("El número de documento es obligatorio");
    }
    
    // Validar que el número de documento sea único
    if (clienteRepository.findByNumeroDocumentoAndEliminadoFalse(entity.getNumeroDocumento()).isPresent() 
        && (entity.getId() == null || !clienteRepository.findByNumeroDocumentoAndEliminadoFalse(entity.getNumeroDocumento()).get().getId().equals(entity.getId()))) {
      throw new Exception("Ya existe un cliente con el número de documento: " + entity.getNumeroDocumento());
    }
    
    if (entity.getNacionalidad() == null) {
      throw new Exception("La nacionalidad es obligatoria");
    }
  }

}
