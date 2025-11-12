package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.DetalleFactura;
import nexora.proyectointegrador2.utils.dto.DetalleFacturaDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

@Component
public class DetalleFacturaMapper implements BaseMapper<DetalleFactura, DetalleFacturaDTO, String> {

  @Autowired
  private AlquilerMapper alquilerMapper;

  @Override
  public DetalleFacturaDTO toDTO(DetalleFactura entity) {
    if (entity == null) {
      return null;
    }

    DetalleFacturaDTO.DetalleFacturaDTOBuilder builder = DetalleFacturaDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .cantidad(entity.getCantidad())
        .subtotal(entity.getSubtotal());

    if (entity.getAlquiler() != null) {
      builder.alquiler(alquilerMapper.toDTO(entity.getAlquiler()));
    }

    return builder.build();
  }

  @Override
  public DetalleFactura toEntity(DetalleFacturaDTO dto) {
    if (dto == null) {
      return null;
    }

    DetalleFactura detalle = new DetalleFactura();
    detalle.setId(dto.getId());
    detalle.setEliminado(dto.isEliminado());
    detalle.setCantidad(dto.getCantidad());
    detalle.setSubtotal(dto.getSubtotal());
    
    if (dto.getAlquiler() != null) {
      detalle.setAlquiler(alquilerMapper.toEntity(dto.getAlquiler()));
    }

    return detalle;
  }

  @Override
  public List<DetalleFacturaDTO> toDTOList(Collection<DetalleFactura> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<DetalleFactura> toEntityList(List<DetalleFacturaDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }

}

