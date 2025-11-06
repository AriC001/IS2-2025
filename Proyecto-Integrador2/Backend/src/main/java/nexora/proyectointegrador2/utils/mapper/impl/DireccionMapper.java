package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import nexora.proyectointegrador2.business.domain.entity.Direccion;
import nexora.proyectointegrador2.utils.dto.DireccionDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre Direccion (entidad) y DireccionDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring")
public interface DireccionMapper extends BaseMapper<Direccion, DireccionDTO, String> {

  /**
   * Convierte Direccion a DireccionDTO.
   * La Localidad se mapea solo su ID.
   */
  @Override
  @Mapping(source = "localidad.id", target = "localidadId")
  DireccionDTO toDTO(Direccion entity);

  /**
   * Convierte DireccionDTO a Direccion.
   * La localidad se ignora, debe ser seteada manualmente o desde el servicio.
   */
  @Override
  @Mapping(target = "localidad", ignore = true)
  Direccion toEntity(DireccionDTO dto);

}
