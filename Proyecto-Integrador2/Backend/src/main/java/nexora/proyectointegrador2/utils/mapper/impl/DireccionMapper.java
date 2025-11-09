package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;

import nexora.proyectointegrador2.business.domain.entity.Direccion;
import nexora.proyectointegrador2.utils.dto.DireccionDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre Direccion (entidad) y DireccionDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring", uses = {LocalidadMapper.class})
public interface DireccionMapper extends BaseMapper<Direccion, DireccionDTO, String> {

  /**
   * Convierte Direccion a DireccionDTO.
   * La Localidad se mapea como DTO completo.
   */
  @Override
  DireccionDTO toDTO(Direccion entity);

  /**
   * Convierte DireccionDTO a Direccion.
   * La Localidad se mapea desde su DTO.
   */
  @Override
  Direccion toEntity(DireccionDTO dto);

}
