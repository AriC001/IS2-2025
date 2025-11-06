package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import nexora.proyectointegrador2.business.domain.entity.Localidad;
import nexora.proyectointegrador2.utils.dto.LocalidadDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre Localidad (entidad) y LocalidadDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring")
public interface LocalidadMapper extends BaseMapper<Localidad, LocalidadDTO, String> {

  /**
   * Convierte Localidad a LocalidadDTO.
   * El Departamento se mapea solo su ID.
   */
  @Override
  @Mapping(source = "departamento.id", target = "departamentoId")
  LocalidadDTO toDTO(Localidad entity);

  /**
   * Convierte LocalidadDTO a Localidad.
   * El departamento se ignora, debe ser seteado manualmente o desde el servicio.
   */
  @Override
  @Mapping(target = "departamento", ignore = true)
  Localidad toEntity(LocalidadDTO dto);

}
