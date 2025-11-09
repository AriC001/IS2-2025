package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;

import nexora.proyectointegrador2.business.domain.entity.Vehiculo;
import nexora.proyectointegrador2.utils.dto.VehiculoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre Vehiculo (entidad) y VehiculoDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring", uses = {CaracteristicaVehiculoMapper.class})
public interface VehiculoMapper extends BaseMapper<Vehiculo, VehiculoDTO, String> {

  /**
   * Convierte Vehiculo a VehiculoDTO.
   * La CaracteristicaVehiculo se mapea como DTO completo.
   */
  @Override
  VehiculoDTO toDTO(Vehiculo entity);

  /**
   * Convierte VehiculoDTO a Vehiculo.
   * La CaracteristicaVehiculo se mapea desde su DTO.
   */
  @Override
  Vehiculo toEntity(VehiculoDTO dto);

}

