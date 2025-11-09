package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.ContactoCorreoElectronico;
import nexora.proyectointegrador2.utils.dto.ContactoCorreoElectronicoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre ContactoCorreoElectronico (entidad) y ContactoCorreoElectronicoDTO.
 * Usa builders de Lombok para construir los objetos.
 */
@Component
public class ContactoCorreoElectronicoMapper implements BaseMapper<ContactoCorreoElectronico, ContactoCorreoElectronicoDTO, String> {

  @Override
  public ContactoCorreoElectronicoDTO toDTO(ContactoCorreoElectronico entity) {
    if (entity == null) {
      return null;
    }

    return ContactoCorreoElectronicoDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .tipoContacto(entity.getTipoContacto())
        .observacion(entity.getObservacion())
        .email(entity.getEmail())
        .build();
  }

  @Override
  public ContactoCorreoElectronico toEntity(ContactoCorreoElectronicoDTO dto) {
    if (dto == null) {
      return null;
    }

    ContactoCorreoElectronico contacto = ContactoCorreoElectronico.builder()
        .email(dto.getEmail())
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
  public List<ContactoCorreoElectronicoDTO> toDTOList(Collection<ContactoCorreoElectronico> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<ContactoCorreoElectronico> toEntityList(List<ContactoCorreoElectronicoDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }

}

