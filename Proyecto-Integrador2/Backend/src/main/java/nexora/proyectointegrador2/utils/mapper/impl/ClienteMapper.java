package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.Cliente;
import nexora.proyectointegrador2.utils.dto.ClienteDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre Cliente (entidad) y ClienteDTO.
 * Usa builders de Lombok para construir los objetos.
 */
@Component
public class ClienteMapper implements BaseMapper<Cliente, ClienteDTO, String> {

  @Autowired
  private DireccionMapper direccionMapper;

  @Autowired
  private NacionalidadMapper nacionalidadMapper;

  @Autowired
  private UsuarioMapper usuarioMapper;

  @Autowired
  private ImagenMapper imagenMapper;

  @Override
  public ClienteDTO toDTO(Cliente entity) {
    if (entity == null) {
      return null;
    }

    ClienteDTO.ClienteDTOBuilder builder = ClienteDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .nombre(entity.getNombre())
        .apellido(entity.getApellido())
        .fechaNacimiento(entity.getFechaNacimiento())
        .tipoDocumento(entity.getTipoDocumento())
        .numeroDocumento(entity.getNumeroDocumento())
        .direccionEstadia(entity.getDireccionEstadia())
        .direccion(direccionMapper.toDTO(entity.getDireccion()))
        .nacionalidad(nacionalidadMapper.toDTO(entity.getNacionalidad()))
        .usuario(usuarioMapper.toDTO(entity.getUsuario()))
        .imagenPerfil(imagenMapper.toDTO(entity.getImagenPerfil()));

    // No mapear contactos ya que están marcados con @JsonIgnore en PersonaDTO
    // y no son necesarios para el frontend. Esto evita problemas de serialización.
    // Los contactos se pueden obtener mediante endpoints específicos si se necesitan.

    return builder.build();
  }

  @Override
  public Cliente toEntity(ClienteDTO dto) {
    if (dto == null) {
      return null;
    }

    Cliente cliente = new Cliente();
    
    // Establecer propiedades de BaseEntity
    cliente.setId(dto.getId());
    cliente.setEliminado(dto.isEliminado());
    
    // Establecer propiedades heredadas de Persona
    cliente.setNombre(dto.getNombre());
    cliente.setApellido(dto.getApellido());
    cliente.setFechaNacimiento(dto.getFechaNacimiento());
    cliente.setTipoDocumento(dto.getTipoDocumento());
    cliente.setNumeroDocumento(dto.getNumeroDocumento());
    cliente.setUsuario(usuarioMapper.toEntity(dto.getUsuario()));
    cliente.setDireccion(direccionMapper.toEntity(dto.getDireccion()));
    cliente.setImagenPerfil(imagenMapper.toEntity(dto.getImagenPerfil()));
    
    // Establecer propiedades específicas de Cliente
    cliente.setDireccionEstadia(dto.getDireccionEstadia());
    cliente.setNacionalidad(nacionalidadMapper.toEntity(dto.getNacionalidad()));

    // No mapear contactos ya que están marcados con @JsonIgnore en PersonaDTO
    // Los contactos no vendrán en el DTO desde el frontend y deben manejarse
    // mediante endpoints específicos si se necesitan actualizar.

    // El usuario y la imagen de perfil se ignoran, deben ser seteados manualmente

    return cliente;
  }

  @Override
  public List<ClienteDTO> toDTOList(Collection<Cliente> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<Cliente> toEntityList(List<ClienteDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }

}

