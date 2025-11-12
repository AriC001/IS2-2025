package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.FormaDePago;
import nexora.proyectointegrador2.utils.dto.FormaDePagoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

@Component
public class FormaDePagoMapper implements BaseMapper<FormaDePago, FormaDePagoDTO, String> {

  @Override
  public FormaDePagoDTO toDTO(FormaDePago entity) {
    if (entity == null) {
      return null;
    }

    return FormaDePagoDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .tipoPago(entity.getTipoPago())
        .observacion(entity.getObservacion())
        .build();
  }

  @Override
  public FormaDePago toEntity(FormaDePagoDTO dto) {
    if (dto == null) {
      return null;
    }

    FormaDePago formaDePago = new FormaDePago();
    formaDePago.setId(dto.getId());
    formaDePago.setEliminado(dto.isEliminado());
    formaDePago.setTipoPago(dto.getTipoPago());
    formaDePago.setObservacion(dto.getObservacion());
    return formaDePago;
  }

  @Override
  public List<FormaDePagoDTO> toDTOList(Collection<FormaDePago> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<FormaDePago> toEntityList(List<FormaDePagoDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }

}

