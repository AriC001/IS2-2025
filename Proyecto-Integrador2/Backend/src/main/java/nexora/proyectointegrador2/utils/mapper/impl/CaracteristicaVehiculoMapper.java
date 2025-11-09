package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;

import nexora.proyectointegrador2.business.domain.entity.CaracteristicaVehiculo;
import nexora.proyectointegrador2.utils.dto.CaracteristicaVehiculoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre CaracteristicaVehiculo (entidad) y CaracteristicaVehiculoDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring", uses = {ImagenMapper.class, CostoVehiculoMapper.class})
public interface CaracteristicaVehiculoMapper extends BaseMapper<CaracteristicaVehiculo, CaracteristicaVehiculoDTO, String> {

  /**
   * Convierte CaracteristicaVehiculo a CaracteristicaVehiculoDTO.
   * La Imagen y CostoVehiculo se mapean como DTOs completos.
   */
  @Override
  CaracteristicaVehiculoDTO toDTO(CaracteristicaVehiculo entity);

  /**
   * Convierte CaracteristicaVehiculoDTO a CaracteristicaVehiculo.
   * La Imagen y CostoVehiculo se mapean desde sus DTOs.
   */
  @Override
  CaracteristicaVehiculo toEntity(CaracteristicaVehiculoDTO dto);

}

