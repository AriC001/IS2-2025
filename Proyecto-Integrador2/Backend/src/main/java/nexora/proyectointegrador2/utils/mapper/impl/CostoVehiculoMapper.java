package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.CostoVehiculo;
import nexora.proyectointegrador2.utils.dto.CostoVehiculoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre CostoVehiculo (entidad) y CostoVehiculoDTO.
 */
@Component
public class CostoVehiculoMapper implements BaseMapper<CostoVehiculo, CostoVehiculoDTO, String> {

  @Override
  public CostoVehiculoDTO toDTO(CostoVehiculo entity) {
    if (entity == null) {
      return null;
    }

    return CostoVehiculoDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .fechaDesde(entity.getFechaDesde())
        .fechaHasta(entity.getFechaHasta())
        .costo(entity.getCosto())
        .build();
  }

  @Override
  public CostoVehiculo toEntity(CostoVehiculoDTO dto) {
    if (dto == null) {
      return null;
    }

    CostoVehiculo costo = new CostoVehiculo();
    costo.setId(dto.getId());
    costo.setEliminado(dto.isEliminado());
    costo.setFechaDesde(dto.getFechaDesde());
    costo.setFechaHasta(dto.getFechaHasta());
    costo.setCosto(dto.getCosto());
    return costo;
  }

  @Override
  public List<CostoVehiculoDTO> toDTOList(Collection<CostoVehiculo> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<CostoVehiculo> toEntityList(List<CostoVehiculoDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }
}
