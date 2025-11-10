package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.Alquiler;
import nexora.proyectointegrador2.utils.dto.AlquilerDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre Alquiler (entidad) y AlquilerDTO.
 */
@Component
public class AlquilerMapper implements BaseMapper<Alquiler, AlquilerDTO, String> {

  private final ClienteMapper clienteMapper;
  private final VehiculoMapper vehiculoMapper;
  private final DocumentoMapper documentoMapper;

  public AlquilerMapper(
      ClienteMapper clienteMapper,
      VehiculoMapper vehiculoMapper,
      DocumentoMapper documentoMapper) {
    this.clienteMapper = clienteMapper;
    this.vehiculoMapper = vehiculoMapper;
    this.documentoMapper = documentoMapper;
  }

  @Override
  public AlquilerDTO toDTO(Alquiler entity) {
    if (entity == null) {
      return null;
    }

    return AlquilerDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .fechaDesde(entity.getFechaDesde())
        .fechaHasta(entity.getFechaHasta())
        .cliente(clienteMapper.toDTO(entity.getCliente()))
        .vehiculo(vehiculoMapper.toDTO(entity.getVehiculo()))
        .documento(documentoMapper.toDTO(entity.getDocumento()))
        .build();
  }

  @Override
  public Alquiler toEntity(AlquilerDTO dto) {
    if (dto == null) {
      return null;
    }

    Alquiler alquiler = new Alquiler();
    alquiler.setId(dto.getId());
    alquiler.setEliminado(dto.isEliminado());
    alquiler.setFechaDesde(dto.getFechaDesde());
    alquiler.setFechaHasta(dto.getFechaHasta());
    alquiler.setCliente(clienteMapper.toEntity(dto.getCliente()));
    alquiler.setVehiculo(vehiculoMapper.toEntity(dto.getVehiculo()));
    alquiler.setDocumento(documentoMapper.toEntity(dto.getDocumento()));
    return alquiler;
  }

  @Override
  public List<AlquilerDTO> toDTOList(Collection<Alquiler> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<Alquiler> toEntityList(List<AlquilerDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }
}
