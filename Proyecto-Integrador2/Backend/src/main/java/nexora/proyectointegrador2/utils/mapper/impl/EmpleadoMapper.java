package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.ContactoCorreoElectronico;
import nexora.proyectointegrador2.business.domain.entity.ContactoTelefonico;
import nexora.proyectointegrador2.business.domain.entity.Empleado;
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

    // Mapear contacto según su tipo
    if (entity.getContacto() != null) {
      if (entity.getContacto() instanceof ContactoTelefonico contactoTelefonico) {
        builder.contacto(contactoTelefonicoMapper.toDTO(contactoTelefonico));
      } else if (entity.getContacto() instanceof ContactoCorreoElectronico contactoCorreo) {
        builder.contacto(contactoCorreoElectronicoMapper.toDTO(contactoCorreo));
      }
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

    // El contacto se maneja en EmpleadoService.preAlta()/preUpdate()
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

