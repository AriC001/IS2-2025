package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.ContactoCorreoElectronico;
import nexora.proyectointegrador2.business.domain.entity.ContactoTelefonico;
import nexora.proyectointegrador2.business.domain.entity.Empresa;
import nexora.proyectointegrador2.utils.dto.ContactoCorreoElectronicoDTO;
import nexora.proyectointegrador2.utils.dto.ContactoDTO;
import nexora.proyectointegrador2.utils.dto.ContactoTelefonicoDTO;
import nexora.proyectointegrador2.utils.dto.EmpresaDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre Empresa (entidad) y EmpresaDTO.
 * Usa builders de Lombok para construir los objetos.
 */
@Component
public class EmpresaMapper implements BaseMapper<Empresa, EmpresaDTO, String> {

  @Autowired
  private DireccionMapper direccionMapper;

  @Autowired
  private ContactoTelefonicoMapper contactoTelefonicoMapper;

  @Autowired
  private ContactoCorreoElectronicoMapper contactoCorreoElectronicoMapper;

  @Override
  public EmpresaDTO toDTO(Empresa entity) {
    if (entity == null) {
      return null;
    }

    // Mapear lista de contactos según su tipo
    List<ContactoDTO> contactosDTO = null;
    if (entity.getContactos() != null && !entity.getContactos().isEmpty()) {
      contactosDTO = entity.getContactos().stream()
          .map(contacto -> {
            if (contacto instanceof ContactoTelefonico contactoTelefonico) {
              return contactoTelefonicoMapper.toDTO(contactoTelefonico);
            } else if (contacto instanceof ContactoCorreoElectronico contactoCorreo) {
              return contactoCorreoElectronicoMapper.toDTO(contactoCorreo);
            }
            return null;
          })
          .filter(contacto -> contacto != null)
          .collect(Collectors.toList());
    }

    EmpresaDTO empresaDTO = EmpresaDTO.builder()
        .nombre(entity.getNombre())
        .telefono(entity.getTelefono())
        .email(entity.getEmail())
        .direccion(direccionMapper.toDTO(entity.getDireccion()))
        .contactos(contactosDTO)
        .build();
    
    // Establecer propiedades de BaseDTO
    empresaDTO.setId(entity.getId());
    empresaDTO.setEliminado(entity.isEliminado());

    return empresaDTO;
  }

  @Override
  public Empresa toEntity(EmpresaDTO dto) {
    if (dto == null) {
      return null;
    }

    Empresa empresa = Empresa.builder()
        .nombre(dto.getNombre())
        .telefono(dto.getTelefono())
        .email(dto.getEmail())
        .direccion(direccionMapper.toEntity(dto.getDireccion()))
        .build();

    // Establecer propiedades de BaseEntity
    empresa.setId(dto.getId());
    empresa.setEliminado(dto.isEliminado());

    // Mapear lista de contactos según su tipo
    if (dto.getContactos() != null && !dto.getContactos().isEmpty()) {
      List<nexora.proyectointegrador2.business.domain.entity.Contacto> contactos = dto.getContactos().stream()
          .map(contactoDTO -> {
            if (contactoDTO instanceof ContactoTelefonicoDTO contactoTelefonicoDTO) {
              ContactoTelefonico contacto = contactoTelefonicoMapper.toEntity(contactoTelefonicoDTO);
              contacto.setEmpresa(empresa);
              return contacto;
            } else if (contactoDTO instanceof ContactoCorreoElectronicoDTO contactoCorreoDTO) {
              ContactoCorreoElectronico contacto = contactoCorreoElectronicoMapper.toEntity(contactoCorreoDTO);
              contacto.setEmpresa(empresa);
              return contacto;
            }
            return null;
          })
          .filter(contacto -> contacto != null)
          .collect(Collectors.toList());
      empresa.setContactos(contactos);
    }

    return empresa;
  }

  @Override
  public List<EmpresaDTO> toDTOList(Collection<Empresa> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<Empresa> toEntityList(List<EmpresaDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }

}

