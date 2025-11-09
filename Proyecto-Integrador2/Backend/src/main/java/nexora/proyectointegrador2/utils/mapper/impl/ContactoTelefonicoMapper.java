package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.ContactoTelefonico;
import nexora.proyectointegrador2.utils.dto.ContactoTelefonicoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre ContactoTelefonico (entidad) y ContactoTelefonicoDTO.
 * Usa builders de Lombok para construir los objetos.
 */
@Component
public class ContactoTelefonicoMapper implements BaseMapper<ContactoTelefonico, ContactoTelefonicoDTO, String> {

  @Override
  public ContactoTelefonicoDTO toDTO(ContactoTelefonico entity) {
    if (entity == null) {
      return null;
    }

    return ContactoTelefonicoDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .tipoContacto(entity.getTipoContacto())
        .observacion(entity.getObservacion())
        .telefono(entity.getTelefono())
        .tipoTelefono(entity.getTipoTelefono())
        .build();
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

