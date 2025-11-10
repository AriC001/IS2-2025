package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.Localidad;
import nexora.proyectointegrador2.utils.dto.LocalidadDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre Localidad (entidad) y LocalidadDTO.
 */
@Component
public class LocalidadMapper implements BaseMapper<Localidad, LocalidadDTO, String> {

  private final DepartamentoMapper departamentoMapper;

  public LocalidadMapper(DepartamentoMapper departamentoMapper) {
    this.departamentoMapper = departamentoMapper;
  }

  @Override
  public LocalidadDTO toDTO(Localidad entity) {
    if (entity == null) {
      return null;
    }

    return LocalidadDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .nombre(entity.getNombre())
        .codigoPostal(entity.getCodigoPostal())
        .departamento(departamentoMapper.toDTO(entity.getDepartamento()))
        .build();
  }

  @Override
  public Localidad toEntity(LocalidadDTO dto) {
    if (dto == null) {
      return null;
    }

    Localidad localidad = new Localidad();
    localidad.setId(dto.getId());
    localidad.setEliminado(dto.isEliminado());
    localidad.setNombre(dto.getNombre());
    localidad.setCodigoPostal(dto.getCodigoPostal());
    localidad.setDepartamento(departamentoMapper.toEntity(dto.getDepartamento()));
    return localidad;
  }

  @Override
  public List<LocalidadDTO> toDTOList(Collection<Localidad> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<Localidad> toEntityList(List<LocalidadDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }
}
