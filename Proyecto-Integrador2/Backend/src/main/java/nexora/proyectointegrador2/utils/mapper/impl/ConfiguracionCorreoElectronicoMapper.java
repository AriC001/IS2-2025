package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;

import nexora.proyectointegrador2.business.domain.entity.ConfiguracionCorreoElectronico;
import nexora.proyectointegrador2.utils.dto.ConfiguracionCorreoElectronicoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre ConfiguracionCorreoElectronico (entidad) y ConfiguracionCorreoElectronicoDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring", uses = {EmpresaMapper.class})
public interface ConfiguracionCorreoElectronicoMapper extends BaseMapper<ConfiguracionCorreoElectronico, ConfiguracionCorreoElectronicoDTO, String> {

  /**
   * Convierte ConfiguracionCorreoElectronico a ConfiguracionCorreoElectronicoDTO.
   * La Empresa se mapea como DTO completo.
   */
  @Override
  ConfiguracionCorreoElectronicoDTO toDTO(ConfiguracionCorreoElectronico entity);

  /**
   * Convierte ConfiguracionCorreoElectronicoDTO a ConfiguracionCorreoElectronico.
   * La Empresa se mapea desde su DTO.
   */
  @Override
  ConfiguracionCorreoElectronico toEntity(ConfiguracionCorreoElectronicoDTO dto);

}

