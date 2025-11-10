package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.Vehiculo;
import nexora.proyectointegrador2.utils.dto.VehiculoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre Vehiculo (entidad) y VehiculoDTO.
 */
@Component
public class VehiculoMapper implements BaseMapper<Vehiculo, VehiculoDTO, String> {

  private final CaracteristicaVehiculoMapper caracteristicaVehiculoMapper;

  public VehiculoMapper(CaracteristicaVehiculoMapper caracteristicaVehiculoMapper) {
    this.caracteristicaVehiculoMapper = caracteristicaVehiculoMapper;
  }

  @Override
  public VehiculoDTO toDTO(Vehiculo entity) {
    if (entity == null) {
      return null;
    }

    return VehiculoDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .estadoVehiculo(entity.getEstadoVehiculo())
        .patente(entity.getPatente())
        .caracteristicaVehiculo(caracteristicaVehiculoMapper.toDTO(entity.getCaracteristicaVehiculo()))
        .build();
  }

  @Override
  public Vehiculo toEntity(VehiculoDTO dto) {
    if (dto == null) {
      return null;
    }

    Vehiculo vehiculo = new Vehiculo();
    vehiculo.setId(dto.getId());
    vehiculo.setEliminado(dto.isEliminado());
    vehiculo.setEstadoVehiculo(dto.getEstadoVehiculo());
    vehiculo.setPatente(dto.getPatente());
    vehiculo.setCaracteristicaVehiculo(caracteristicaVehiculoMapper.toEntity(dto.getCaracteristicaVehiculo()));
    return vehiculo;
  }

  @Override
  public List<VehiculoDTO> toDTOList(Collection<Vehiculo> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<Vehiculo> toEntityList(List<VehiculoDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }
}
