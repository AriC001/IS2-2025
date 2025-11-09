package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;

import nexora.proyectointegrador2.business.domain.entity.Provincia;
import nexora.proyectointegrador2.utils.dto.ProvinciaDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre Provincia (entidad) y ProvinciaDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring", uses = {PaisMapper.class})
public interface ProvinciaMapper extends BaseMapper<Provincia, ProvinciaDTO, String> {

  /**
   * Convierte Provincia a ProvinciaDTO.
   * El Pais se mapea como DTO completo.
   */
  @Override
  ProvinciaDTO toDTO(Provincia entity);

  /**
   * Convierte ProvinciaDTO a Provincia.
   * El Pais se mapea desde su DTO.
   */
  @Override
  Provincia toEntity(ProvinciaDTO dto);

}
