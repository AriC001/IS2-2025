package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.Documento;
import nexora.proyectointegrador2.utils.dto.DocumentoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre Documento (entidad) y DocumentoDTO.
 */
@Component
public class DocumentoMapper implements BaseMapper<Documento, DocumentoDTO, String> {

  @Override
  public DocumentoDTO toDTO(Documento entity) {
    if (entity == null) {
      return null;
    }

    return DocumentoDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .tipoDocumento(entity.getTipoDocumento())
        .observacion(entity.getObservacion())
        .pathArchivo(entity.getPathArchivo())
        .nombreArchivo(entity.getNombreArchivo())
        .mimeType(entity.getMimeType())
        .build();
  }

  @Override
  public Documento toEntity(DocumentoDTO dto) {
    if (dto == null) {
      return null;
    }

    Documento documento = new Documento();
    documento.setId(dto.getId());
    documento.setEliminado(dto.isEliminado());
    documento.setTipoDocumento(dto.getTipoDocumento());
    documento.setObservacion(dto.getObservacion());
    documento.setPathArchivo(dto.getPathArchivo());
    documento.setNombreArchivo(dto.getNombreArchivo());
    documento.setMimeType(dto.getMimeType());
    return documento;
  }

  @Override
  public List<DocumentoDTO> toDTOList(Collection<Documento> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<Documento> toEntityList(List<DocumentoDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }
}
