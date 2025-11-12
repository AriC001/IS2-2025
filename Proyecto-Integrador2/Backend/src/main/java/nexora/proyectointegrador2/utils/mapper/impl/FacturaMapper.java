package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.Factura;
import nexora.proyectointegrador2.utils.dto.FacturaDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

@Component
public class FacturaMapper implements BaseMapper<Factura, FacturaDTO, String> {

  @Autowired
  private FormaDePagoMapper formaDePagoMapper;

  @Autowired
  private DetalleFacturaMapper detalleFacturaMapper;

  @Override
  public FacturaDTO toDTO(Factura entity) {
    if (entity == null) {
      return null;
    }

    FacturaDTO.FacturaDTOBuilder builder = FacturaDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .numeroFactura(entity.getNumeroFactura())
        .fechaFactura(entity.getFechaFactura())
        .totalPagado(entity.getTotalPagado())
        .estado(entity.getEstado());

    if (entity.getFormaDePago() != null) {
      builder.formaDePago(formaDePagoMapper.toDTO(entity.getFormaDePago()));
    }

    if (entity.getDetalles() != null && !entity.getDetalles().isEmpty()) {
      builder.detalles(detalleFacturaMapper.toDTOList(entity.getDetalles()));
    }

    return builder.build();
  }

  @Override
  public Factura toEntity(FacturaDTO dto) {
    if (dto == null) {
      return null;
    }

    Factura factura = new Factura();
    factura.setId(dto.getId());
    factura.setEliminado(dto.isEliminado());
    factura.setNumeroFactura(dto.getNumeroFactura());
    factura.setFechaFactura(dto.getFechaFactura());
    factura.setTotalPagado(dto.getTotalPagado());
    factura.setEstado(dto.getEstado());

    if (dto.getFormaDePago() != null) {
      factura.setFormaDePago(formaDePagoMapper.toEntity(dto.getFormaDePago()));
    }

    if (dto.getDetalles() != null && !dto.getDetalles().isEmpty()) {
      factura.setDetalles(detalleFacturaMapper.toEntityList(dto.getDetalles()));
    }

    return factura;
  }

  @Override
  public List<FacturaDTO> toDTOList(Collection<Factura> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<Factura> toEntityList(List<FacturaDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }

}

