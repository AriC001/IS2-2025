package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.Pais;
import nexora.proyectointegrador2.utils.dto.PaisDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre Pais (entidad) y PaisDTO.
 * Usa builders de Lombok para construir los objetos.
 */
@Component
public class PaisMapper implements BaseMapper<Pais, PaisDTO, String> {

  @Override
  public PaisDTO toDTO(Pais entity) {
    if (entity == null) {
      return null;
    }

    return PaisDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .nombre(entity.getNombre())
        .build();
  }

  @Override
  public Pais toEntity(PaisDTO dto) {
    if (dto == null) {
      return null;
    }

    Pais pais = new Pais();
    pais.setId(dto.getId());
    pais.setEliminado(dto.isEliminado());
    pais.setNombre(dto.getNombre());
    return pais;
  }

  @Override
  public List<PaisDTO> toDTOList(Collection<Pais> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<Pais> toEntityList(List<PaisDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }
}
