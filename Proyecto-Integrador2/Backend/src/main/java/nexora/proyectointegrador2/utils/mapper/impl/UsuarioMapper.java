package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import nexora.proyectointegrador2.business.domain.entity.Usuario;
import nexora.proyectointegrador2.utils.dto.UsuarioDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre Usuario (entidad) y UsuarioDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring")
public interface UsuarioMapper extends BaseMapper<Usuario, UsuarioDTO, String> {

  /**
   * Convierte Usuario a UsuarioDTO.
   * La clave no se incluye en el DTO por seguridad.
   */
  @Override
  @Mapping(target = "clave", ignore = true)
  UsuarioDTO toDTO(Usuario entity);

  /**
   * Convierte UsuarioDTO a Usuario.
   */
  @Override
  Usuario toEntity(UsuarioDTO dto);

}
