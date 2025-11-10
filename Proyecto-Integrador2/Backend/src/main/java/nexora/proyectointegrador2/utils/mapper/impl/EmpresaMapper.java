package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;

import nexora.proyectointegrador2.business.domain.entity.Empresa;
import nexora.proyectointegrador2.utils.dto.EmpresaDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre Empresa (entidad) y EmpresaDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring", uses = {DireccionMapper.class})
public interface EmpresaMapper extends BaseMapper<Empresa, EmpresaDTO, String> {

  /**
   * Convierte Empresa a EmpresaDTO.
   * La Dirección se mapea como DTO completo.
   */
  @Override
  EmpresaDTO toDTO(Empresa entity);

  /**
   * Convierte EmpresaDTO a Empresa.
   * La Dirección se mapea desde su DTO.
   */
  @Override
  Empresa toEntity(EmpresaDTO dto);

}

