package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import nexora.proyectointegrador2.business.domain.entity.Provincia;
import nexora.proyectointegrador2.utils.dto.ProvinciaDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre Provincia (entidad) y ProvinciaDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring")
public interface ProvinciaMapper extends BaseMapper<Provincia, ProvinciaDTO, String> {

  /**
   * Convierte Provincia a ProvinciaDTO.
   * El Pais se mapea solo su ID.
   */
  @Override
  @Mapping(source = "pais.id", target = "paisId")
  ProvinciaDTO toDTO(Provincia entity);

  /**
   * Convierte ProvinciaDTO a Provincia.
   * El paisId se ignora, debe ser seteado manualmente o desde el servicio.
   */
  @Override
  @Mapping(target = "pais", ignore = true)
  Provincia toEntity(ProvinciaDTO dto);

}
