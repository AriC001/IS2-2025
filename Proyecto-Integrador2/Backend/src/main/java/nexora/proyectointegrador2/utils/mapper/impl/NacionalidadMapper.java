package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.Nacionalidad;
import nexora.proyectointegrador2.utils.dto.NacionalidadDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre Nacionalidad (entidad) y NacionalidadDTO.
 */
@Component
public class NacionalidadMapper implements BaseMapper<Nacionalidad, NacionalidadDTO, String> {

  @Override
  public NacionalidadDTO toDTO(Nacionalidad entity) {
    if (entity == null) {
      return null;
    }

    return NacionalidadDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .nombre(entity.getNombre())
        .build();
  }

  @Override
  public Nacionalidad toEntity(NacionalidadDTO dto) {
    if (dto == null) {
      return null;
    }

    Nacionalidad nacionalidad = new Nacionalidad();
    nacionalidad.setId(dto.getId());
    nacionalidad.setEliminado(dto.isEliminado());
    nacionalidad.setNombre(dto.getNombre());
    return nacionalidad;
  }

  @Override
  public List<NacionalidadDTO> toDTOList(Collection<Nacionalidad> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<Nacionalidad> toEntityList(List<NacionalidadDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }
}
