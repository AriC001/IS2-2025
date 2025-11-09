package nexora.proyectointegrador2.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Cliente;
import nexora.proyectointegrador2.business.domain.entity.Contacto;
import nexora.proyectointegrador2.business.domain.entity.ContactoCorreoElectronico;
import nexora.proyectointegrador2.business.domain.entity.ContactoTelefonico;
import nexora.proyectointegrador2.business.domain.entity.Direccion;
import nexora.proyectointegrador2.business.persistence.repository.ClienteRepository;

@Service
public class ClienteService extends BaseService<Cliente, String> {

  private final ClienteRepository clienteRepository;
  
  @Autowired
  private DireccionService direccionService;
  
  @Autowired
  private ContactoTelefonicoService contactoTelefonicoService;
  
  @Autowired
  private ContactoCorreoElectronicoService contactoCorreoElectronicoService;

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

    // Persistir Contacto si existe y es nuevo (no tiene ID)
    if (entity.getContacto() != null && entity.getContacto().getId() == null) {
      Contacto contactoGuardado;
      
      // Determinar el tipo de contacto y usar el servicio correspondiente
      if (entity.getContacto() instanceof ContactoTelefonico) {
        contactoGuardado = contactoTelefonicoService.save((ContactoTelefonico) entity.getContacto());
      } else if (entity.getContacto() instanceof ContactoCorreoElectronico) {
        contactoGuardado = contactoCorreoElectronicoService.save((ContactoCorreoElectronico) entity.getContacto());
      } else {
        throw new Exception("Tipo de contacto no soportado");
      }
      
      entity.setContacto(contactoGuardado);
    } else if (entity.getContacto() != null && entity.getContacto().getId() != null) {
      // Si tiene ID, verificar que existe (esto se puede mejorar cargando la entidad)
      // Por ahora solo validamos que el ID no sea null
      // En una implementación más completa, podrías cargar la entidad aquí
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

    // Similar para Contacto
    if (entity.getContacto() != null && entity.getContacto().getId() == null) {
      Contacto contactoGuardado;
      if (entity.getContacto() instanceof ContactoTelefonico) {
        contactoGuardado = contactoTelefonicoService.save((ContactoTelefonico) entity.getContacto());
      } else if (entity.getContacto() instanceof ContactoCorreoElectronico) {
        contactoGuardado = contactoCorreoElectronicoService.save((ContactoCorreoElectronico) entity.getContacto());
      } else {
        throw new Exception("Tipo de contacto no soportado");
      }
      entity.setContacto(contactoGuardado);
    }
  }

}
