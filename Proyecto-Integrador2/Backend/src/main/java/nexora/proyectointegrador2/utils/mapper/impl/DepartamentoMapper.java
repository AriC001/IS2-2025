package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;

import nexora.proyectointegrador2.business.domain.entity.Departamento;
import nexora.proyectointegrador2.utils.dto.DepartamentoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre Departamento (entidad) y DepartamentoDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring", uses = {ProvinciaMapper.class})
public interface DepartamentoMapper extends BaseMapper<Departamento, DepartamentoDTO, String> {

  /**
   * Convierte Departamento a DepartamentoDTO.
   * La Provincia se mapea como DTO completo.
   */
  @Override
  DepartamentoDTO toDTO(Departamento entity);

  /**
   * Convierte DepartamentoDTO a Departamento.
   * La Provincia se mapea desde su DTO.
   */
  @Override
  Departamento toEntity(DepartamentoDTO dto);

}
