package nexora.proyectointegrador2.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Cliente;
import nexora.proyectointegrador2.business.domain.entity.Contacto;
import nexora.proyectointegrador2.business.domain.entity.Direccion;
import nexora.proyectointegrador2.business.persistence.repository.ClienteRepository;

@Service
public class ClienteService extends BaseService<Cliente, String> {

  private final ClienteRepository clienteRepository;
  
  @Autowired
  private DireccionService direccionService;
  

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

  /**
   * Persiste las entidades relacionadas (Dirección y Contacto) antes de guardar el Cliente.
   * Este método se ejecuta automáticamente antes de save() gracias al hook preAlta() en BaseService.
   */
  @Override
  protected void preAlta(Cliente entity) throws Exception {
    // Persistir Dirección si existe y es nueva (no tiene ID)
    if (entity.getDireccion() != null && entity.getDireccion().getId() == null) {
      // La Dirección necesita tener su Localidad cargada antes de persistir
      // Esto debería manejarse en el mapper o aquí mismo
      Direccion direccionGuardada = direccionService.save(entity.getDireccion());
      entity.setDireccion(direccionGuardada);
    } else if (entity.getDireccion() != null && entity.getDireccion().getId() != null) {
      // Si tiene ID, cargar la entidad desde el repositorio para asegurar que existe
      Direccion direccionExistente = direccionService.findById(entity.getDireccion().getId());
      entity.setDireccion(direccionExistente);
    }

    // Asegurar que los contactos tengan la referencia a la persona
    // NO guardarlos aquí, JPA los guardará automáticamente gracias a CascadeType.ALL
    if (entity.getContactos() != null && !entity.getContactos().isEmpty()) {
      for (Contacto contacto : entity.getContactos()) {
        if (contacto.getPersona() == null) {
          contacto.setPersona(entity);
        }
      }
    }
  }

  /**
   * Actualiza las entidades relacionadas antes de actualizar el Cliente.
   * Este método se ejecuta automáticamente antes de update() gracias al hook preUpdate() en BaseService.
   */
  @Override
  protected void preUpdate(String id, Cliente entity) throws Exception {
    // Similar a preAlta, pero para actualización
    // Si la Dirección viene con datos pero sin ID, crear nueva
    // Si viene con ID diferente, actualizar
    // Si viene con ID igual, mantener la referencia
    
    if (entity.getDireccion() != null && entity.getDireccion().getId() == null) {
      Direccion direccionGuardada = direccionService.save(entity.getDireccion());
      entity.setDireccion(direccionGuardada);
    } else if (entity.getDireccion() != null && entity.getDireccion().getId() != null) {
      // Verificar si la dirección cambió comparando con la entidad existente
      Cliente clienteExistente = findById(id);
      if (clienteExistente.getDireccion() == null || 
          !clienteExistente.getDireccion().getId().equals(entity.getDireccion().getId())) {
        // La dirección cambió, cargar la nueva
        Direccion direccionExistente = direccionService.findById(entity.getDireccion().getId());
        entity.setDireccion(direccionExistente);
      }
    }

    // Asegurar que los contactos nuevos tengan la referencia a la persona
    // NO guardarlos aquí, JPA los guardará automáticamente gracias a CascadeType.ALL
    if (entity.getContactos() != null && !entity.getContactos().isEmpty()) {
      for (Contacto contacto : entity.getContactos()) {
        if (contacto.getId() == null && contacto.getPersona() == null) {
          contacto.setPersona(entity);
        }
      }
    }
  }

}
