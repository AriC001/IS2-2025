package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.CaracteristicaVehiculo;
import nexora.proyectointegrador2.utils.dto.CaracteristicaVehiculoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre CaracteristicaVehiculo (entidad) y su DTO.
 */
@Component
public class CaracteristicaVehiculoMapper implements BaseMapper<CaracteristicaVehiculo, CaracteristicaVehiculoDTO, String> {

  private final ImagenMapper imagenMapper;
  private final CostoVehiculoMapper costoVehiculoMapper;

  public CaracteristicaVehiculoMapper(ImagenMapper imagenMapper, CostoVehiculoMapper costoVehiculoMapper) {
    this.imagenMapper = imagenMapper;
    this.costoVehiculoMapper = costoVehiculoMapper;
  }

  @Override
  public CaracteristicaVehiculoDTO toDTO(CaracteristicaVehiculo entity) {
    if (entity == null) {
      return null;
    }

    return CaracteristicaVehiculoDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .marca(entity.getMarca())
        .modelo(entity.getModelo())
        .cantidadPuerta(entity.getCantidadPuerta())
        .cantidadAsiento(entity.getCantidadAsiento())
        .anio(entity.getAnio())
        .cantidadTotalVehiculo(entity.getCantidadTotalVehiculo())
        .cantidadVehiculoDisponible(entity.getCantidadVehiculoDisponible())
        .imagenVehiculo(imagenMapper.toDTO(entity.getImagenVehiculo()))
        .costoVehiculo(costoVehiculoMapper.toDTO(entity.getCostoVehiculo()))
        .build();
  }

  @Override
  public CaracteristicaVehiculo toEntity(CaracteristicaVehiculoDTO dto) {
    if (dto == null) {
      return null;
    }

    CaracteristicaVehiculo caracteristica = new CaracteristicaVehiculo();
    caracteristica.setId(dto.getId());
    caracteristica.setEliminado(dto.isEliminado());
    caracteristica.setMarca(dto.getMarca());
    caracteristica.setModelo(dto.getModelo());
    caracteristica.setCantidadPuerta(dto.getCantidadPuerta());
    caracteristica.setCantidadAsiento(dto.getCantidadAsiento());
    caracteristica.setAnio(dto.getAnio());
    // cantidadTotalVehiculo y cantidadVehiculoDisponible son campos calculados (@Formula)
    // No se deben mapear desde el DTO, se calculan automáticamente en la base de datos
    caracteristica.setImagenVehiculo(imagenMapper.toEntity(dto.getImagenVehiculo()));
    caracteristica.setCostoVehiculo(costoVehiculoMapper.toEntity(dto.getCostoVehiculo()));
    return caracteristica;
  }

  @Override
  public List<CaracteristicaVehiculoDTO> toDTOList(Collection<CaracteristicaVehiculo> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<CaracteristicaVehiculo> toEntityList(List<CaracteristicaVehiculoDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }
}
