package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.Cliente;
import nexora.proyectointegrador2.business.domain.entity.ContactoTelefonico;
import nexora.proyectointegrador2.business.domain.entity.Empleado;
import nexora.proyectointegrador2.business.logic.service.ClienteService;
import nexora.proyectointegrador2.business.logic.service.EmpleadoService;
import nexora.proyectointegrador2.utils.dto.ContactoTelefonicoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre ContactoTelefonico (entidad) y ContactoTelefonicoDTO.
 * Usa builders de Lombok para construir los objetos.
 */
@Component
public class ContactoTelefonicoMapper implements BaseMapper<ContactoTelefonico, ContactoTelefonicoDTO, String> {

  @Autowired
  private ClienteService clienteService;
  
  @Autowired
  private EmpleadoService empleadoService;

  @Override
  public ContactoTelefonicoDTO toDTO(ContactoTelefonico entity) {
    if (entity == null) {
      return null;
    }

    ContactoTelefonicoDTO.ContactoTelefonicoDTOBuilder builder = ContactoTelefonicoDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .tipoContacto(entity.getTipoContacto())
        .observacion(entity.getObservacion())
        .telefono(entity.getTelefono())
        .tipoTelefono(entity.getTipoTelefono());
    
    // Establecer personaId si la persona está asociada
    if (entity.getPersona() != null) {
      if (entity.getPersona() instanceof Cliente) {
        builder.personaId("CLIENTE-" + entity.getPersona().getId());
      } else if (entity.getPersona() instanceof Empleado) {
        builder.personaId("EMPLEADO-" + entity.getPersona().getId());
      }
    }
    
    return builder.build();
  }

  @Override
  public ContactoTelefonico toEntity(ContactoTelefonicoDTO dto) {
    if (dto == null) {
      return null;
    }

    ContactoTelefonico contacto = ContactoTelefonico.builder()
        .telefono(dto.getTelefono())
        .tipoTelefono(dto.getTipoTelefono())
        .build();

    // Establecer propiedades de BaseEntity
    contacto.setId(dto.getId());
    contacto.setEliminado(dto.isEliminado());

    // Establecer propiedades heredadas de Contacto
    contacto.setTipoContacto(dto.getTipoContacto());
    contacto.setObservacion(dto.getObservacion());

    // Asociar persona si se proporciona personaId
    if (dto.getPersonaId() != null && !dto.getPersonaId().trim().isEmpty()) {
      try {
        if (dto.getPersonaId().startsWith("CLIENTE-")) {
          String clienteId = dto.getPersonaId().substring("CLIENTE-".length());
          Cliente cliente = clienteService.findById(clienteId);
          contacto.setPersona(cliente);
        } else if (dto.getPersonaId().startsWith("EMPLEADO-")) {
          String empleadoId = dto.getPersonaId().substring("EMPLEADO-".length());
          Empleado empleado = empleadoService.findById(empleadoId);
          contacto.setPersona(empleado);
        }
      } catch (Exception e) {
        // Si no se puede encontrar la persona, se deja sin asociar
        // El error se manejará en la validación si es necesario
      }
    }

    return contacto;
  }

  @Override
  public List<ContactoTelefonicoDTO> toDTOList(Collection<ContactoTelefonico> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<ContactoTelefonico> toEntityList(List<ContactoTelefonicoDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }

}

