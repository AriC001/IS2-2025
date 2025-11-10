package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.Departamento;
import nexora.proyectointegrador2.utils.dto.DepartamentoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre Departamento (entidad) y DepartamentoDTO.
 */
@Component
public class DepartamentoMapper implements BaseMapper<Departamento, DepartamentoDTO, String> {

  private final ProvinciaMapper provinciaMapper;

  public DepartamentoMapper(ProvinciaMapper provinciaMapper) {
    this.provinciaMapper = provinciaMapper;
  }

  @Override
  public DepartamentoDTO toDTO(Departamento entity) {
    if (entity == null) {
      return null;
    }

    return DepartamentoDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .nombre(entity.getNombre())
        .provincia(provinciaMapper.toDTO(entity.getProvincia()))
        .build();
  }

  @Override
  public Departamento toEntity(DepartamentoDTO dto) {
    if (dto == null) {
      return null;
    }

    Departamento departamento = new Departamento();
    departamento.setId(dto.getId());
    departamento.setEliminado(dto.isEliminado());
    departamento.setNombre(dto.getNombre());
    departamento.setProvincia(provinciaMapper.toEntity(dto.getProvincia()));
    return departamento;
  }

  @Override
  public List<DepartamentoDTO> toDTOList(Collection<Departamento> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<Departamento> toEntityList(List<DepartamentoDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }
}
