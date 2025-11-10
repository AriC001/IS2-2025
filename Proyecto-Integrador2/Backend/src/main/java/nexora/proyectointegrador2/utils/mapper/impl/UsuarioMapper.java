package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.Usuario;
import nexora.proyectointegrador2.utils.dto.UsuarioDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre Usuario (entidad) y UsuarioDTO.
 */
@Component
public class UsuarioMapper implements BaseMapper<Usuario, UsuarioDTO, String> {

  @Override
  public UsuarioDTO toDTO(Usuario entity) {
    if (entity == null) {
      return null;
    }

    return UsuarioDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .nombreUsuario(entity.getNombreUsuario())
        .rol(entity.getRol())
        .build();
  }

  @Override
  public Usuario toEntity(UsuarioDTO dto) {
    if (dto == null) {
      return null;
    }

    Usuario usuario = new Usuario();
    usuario.setId(dto.getId());
    usuario.setEliminado(dto.isEliminado());
    usuario.setNombreUsuario(dto.getNombreUsuario());
    usuario.setClave(dto.getClave());
    usuario.setRol(dto.getRol());
    return usuario;
  }

  @Override
  public List<UsuarioDTO> toDTOList(Collection<Usuario> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<Usuario> toEntityList(List<UsuarioDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }
}
