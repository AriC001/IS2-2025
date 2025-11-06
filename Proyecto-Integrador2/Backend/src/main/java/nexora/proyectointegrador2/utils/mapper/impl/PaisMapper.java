package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;

import nexora.proyectointegrador2.business.domain.entity.Pais;
import nexora.proyectointegrador2.utils.dto.PaisDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre Pais (entidad) y PaisDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring")
public interface PaisMapper extends BaseMapper<Pais, PaisDTO, String> {

  @Override
  PaisDTO toDTO(Pais entity);

  @Override
  Pais toEntity(PaisDTO dto);

}
