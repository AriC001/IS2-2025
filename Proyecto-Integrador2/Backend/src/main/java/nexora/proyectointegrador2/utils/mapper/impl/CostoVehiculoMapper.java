package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;

import nexora.proyectointegrador2.business.domain.entity.CostoVehiculo;
import nexora.proyectointegrador2.utils.dto.CostoVehiculoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre CostoVehiculo (entidad) y CostoVehiculoDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring")
public interface CostoVehiculoMapper extends BaseMapper<CostoVehiculo, CostoVehiculoDTO, String> {

  /**
   * Convierte CostoVehiculo a CostoVehiculoDTO.
   */
  @Override
  CostoVehiculoDTO toDTO(CostoVehiculo entity);

  /**
   * Convierte CostoVehiculoDTO a CostoVehiculo.
   */
  @Override
  CostoVehiculo toEntity(CostoVehiculoDTO dto);

}

