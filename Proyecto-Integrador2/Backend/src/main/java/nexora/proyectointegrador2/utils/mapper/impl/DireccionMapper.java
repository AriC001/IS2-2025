package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.Direccion;
import nexora.proyectointegrador2.utils.dto.DireccionDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre Direccion (entidad) y DireccionDTO.
 */
@Component
public class DireccionMapper implements BaseMapper<Direccion, DireccionDTO, String> {

  private final LocalidadMapper localidadMapper;

  public DireccionMapper(LocalidadMapper localidadMapper) {
    this.localidadMapper = localidadMapper;
  }

  @Override
  public DireccionDTO toDTO(Direccion entity) {
    if (entity == null) {
      return null;
    }

    return DireccionDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .calle(entity.getCalle())
        .numero(entity.getNumero())
        .barrio(entity.getBarrio())
        .manzanaPiso(entity.getManzanaPiso())
        .casaDepartamento(entity.getCasaDepartamento())
        .referencia(entity.getReferencia())
        .localidad(localidadMapper.toDTO(entity.getLocalidad()))
        .build();
  }

  @Override
  public Direccion toEntity(DireccionDTO dto) {
    if (dto == null) {
      return null;
    }

    Direccion direccion = new Direccion();
    direccion.setId(dto.getId());
    direccion.setEliminado(dto.isEliminado());
    direccion.setCalle(dto.getCalle());
    direccion.setNumero(dto.getNumero());
    direccion.setBarrio(dto.getBarrio());
    direccion.setManzanaPiso(dto.getManzanaPiso());
    direccion.setCasaDepartamento(dto.getCasaDepartamento());
    direccion.setReferencia(dto.getReferencia());
    direccion.setLocalidad(localidadMapper.toEntity(dto.getLocalidad()));
    return direccion;
  }

  @Override
  public List<DireccionDTO> toDTOList(Collection<Direccion> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<Direccion> toEntityList(List<DireccionDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }
}
