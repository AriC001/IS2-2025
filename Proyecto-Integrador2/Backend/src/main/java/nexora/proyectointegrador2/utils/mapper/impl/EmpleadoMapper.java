package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.ContactoCorreoElectronico;
import nexora.proyectointegrador2.business.domain.entity.ContactoTelefonico;
import nexora.proyectointegrador2.business.domain.entity.Empleado;
import nexora.proyectointegrador2.utils.dto.ContactoCorreoElectronicoDTO;
import nexora.proyectointegrador2.utils.dto.ContactoDTO;
import nexora.proyectointegrador2.utils.dto.ContactoTelefonicoDTO;
import nexora.proyectointegrador2.utils.dto.EmpleadoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre Empleado (entidad) y EmpleadoDTO.
 * Usa builders de Lombok para construir los objetos.
 */
@Component
public class EmpleadoMapper implements BaseMapper<Empleado, EmpleadoDTO, String> {

  @Autowired
  private DireccionMapper direccionMapper;

  @Autowired
  private ContactoTelefonicoMapper contactoTelefonicoMapper;

  @Autowired
  private ContactoCorreoElectronicoMapper contactoCorreoElectronicoMapper;

  @Override
  public EmpleadoDTO toDTO(Empleado entity) {
    if (entity == null) {
      return null;
    }

    EmpleadoDTO.EmpleadoDTOBuilder builder = EmpleadoDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .nombre(entity.getNombre())
        .apellido(entity.getApellido())
        .fechaNacimiento(entity.getFechaNacimiento())
        .tipoDocumento(entity.getTipoDocumento())
        .numeroDocumento(entity.getNumeroDocumento())
        .tipoEmpleado(entity.getTipoEmpleado())
        .direccion(direccionMapper.toDTO(entity.getDireccion()));

    // Mapear usuarioId e imagenPerfilId
    if (entity.getUsuario() != null) {
      builder.usuarioId(entity.getUsuario().getId());
    }
    if (entity.getImagenPerfil() != null) {
      builder.imagenPerfilId(entity.getImagenPerfil().getId());
    }

    // Mapear lista de contactos según su tipo
    if (entity.getContactos() != null && !entity.getContactos().isEmpty()) {
      List<ContactoDTO> contactosDTO = entity.getContactos().stream()
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
      builder.contactos(contactosDTO);
    }

    return builder.build();
  }

  @Override
  public Empleado toEntity(EmpleadoDTO dto) {
    if (dto == null) {
      return null;
    }

    Empleado empleado = new Empleado();
    
    // Establecer propiedades de BaseEntity
    empleado.setId(dto.getId());
    empleado.setEliminado(dto.isEliminado());
    
    // Establecer propiedades heredadas de Persona
    empleado.setNombre(dto.getNombre());
    empleado.setApellido(dto.getApellido());
    empleado.setFechaNacimiento(dto.getFechaNacimiento());
    empleado.setTipoDocumento(dto.getTipoDocumento());
    empleado.setNumeroDocumento(dto.getNumeroDocumento());
    empleado.setDireccion(direccionMapper.toEntity(dto.getDireccion()));
    
    // Establecer propiedades específicas de Empleado
    empleado.setTipoEmpleado(dto.getTipoEmpleado());

    // Mapear lista de contactos según su tipo
    if (dto.getContactos() != null && !dto.getContactos().isEmpty()) {
      List<nexora.proyectointegrador2.business.domain.entity.Contacto> contactos = dto.getContactos().stream()
          .map(contactoDTO -> {
            if (contactoDTO instanceof ContactoTelefonicoDTO contactoTelefonicoDTO) {
              ContactoTelefonico contacto = contactoTelefonicoMapper.toEntity(contactoTelefonicoDTO);
              contacto.setPersona(empleado);
              return contacto;
            } else if (contactoDTO instanceof ContactoCorreoElectronicoDTO contactoCorreoDTO) {
              ContactoCorreoElectronico contacto = contactoCorreoElectronicoMapper.toEntity(contactoCorreoDTO);
              contacto.setPersona(empleado);
              return contacto;
            }
            return null;
          })
          .filter(contacto -> contacto != null)
          .collect(Collectors.toList());
      empleado.setContactos(contactos);
    }

    // El usuario y la imagen de perfil se ignoran, deben ser seteados manualmente

    return empleado;
  }

  @Override
  public List<EmpleadoDTO> toDTOList(Collection<Empleado> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<Empleado> toEntityList(List<EmpleadoDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }

}

