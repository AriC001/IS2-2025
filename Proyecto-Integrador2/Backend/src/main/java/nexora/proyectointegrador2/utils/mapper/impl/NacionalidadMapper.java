package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;

import nexora.proyectointegrador2.business.domain.entity.Nacionalidad;
import nexora.proyectointegrador2.utils.dto.NacionalidadDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre Nacionalidad (entidad) y NacionalidadDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring")
public interface NacionalidadMapper extends BaseMapper<Nacionalidad, NacionalidadDTO, String> {

  @Override
  NacionalidadDTO toDTO(Nacionalidad entity);

  @Override
  Nacionalidad toEntity(NacionalidadDTO dto);

}
