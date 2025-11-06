package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import nexora.proyectointegrador2.business.domain.entity.Departamento;
import nexora.proyectointegrador2.utils.dto.DepartamentoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre Departamento (entidad) y DepartamentoDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring")
public interface DepartamentoMapper extends BaseMapper<Departamento, DepartamentoDTO, String> {

  /**
   * Convierte Departamento a DepartamentoDTO.
   * La Provincia se mapea solo su ID.
   */
  @Override
  @Mapping(source = "provincia.id", target = "provinciaId")
  DepartamentoDTO toDTO(Departamento entity);

  /**
   * Convierte DepartamentoDTO a Departamento.
   * La provincia se ignora, debe ser seteada manualmente o desde el servicio.
   */
  @Override
  @Mapping(target = "provincia", ignore = true)
  Departamento toEntity(DepartamentoDTO dto);

}
