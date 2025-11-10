package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.Provincia;
import nexora.proyectointegrador2.utils.dto.ProvinciaDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre Provincia (entidad) y ProvinciaDTO.
 */
@Component
public class ProvinciaMapper implements BaseMapper<Provincia, ProvinciaDTO, String> {

  private final PaisMapper paisMapper;

  public ProvinciaMapper(PaisMapper paisMapper) {
    this.paisMapper = paisMapper;
  }

  @Override
  public ProvinciaDTO toDTO(Provincia entity) {
    if (entity == null) {
      return null;
    }

    return ProvinciaDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .nombre(entity.getNombre())
        .pais(paisMapper.toDTO(entity.getPais()))
        .build();
  }

  @Override
  public Provincia toEntity(ProvinciaDTO dto) {
    if (dto == null) {
      return null;
    }

    Provincia provincia = new Provincia();
    provincia.setId(dto.getId());
    provincia.setEliminado(dto.isEliminado());
    provincia.setNombre(dto.getNombre());
    provincia.setPais(paisMapper.toEntity(dto.getPais()));
    return provincia;
  }

  @Override
  public List<ProvinciaDTO> toDTOList(Collection<Provincia> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<Provincia> toEntityList(List<ProvinciaDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }
}
