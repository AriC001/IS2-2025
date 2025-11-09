package nexora.proyectointegrador2.utils.mapper.impl;

import org.mapstruct.Mapper;

import nexora.proyectointegrador2.business.domain.entity.Documento;
import nexora.proyectointegrador2.utils.dto.DocumentoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper para convertir entre Documento (entidad) y DocumentoDTO.
 * MapStruct genera automáticamente la implementación.
 */
@Mapper(componentModel = "spring")
public interface DocumentoMapper extends BaseMapper<Documento, DocumentoDTO, String> {

  /**
   * Convierte Documento a DocumentoDTO.
   */
  @Override
  DocumentoDTO toDTO(Documento entity);

  /**
   * Convierte DocumentoDTO a Documento.
   */
  @Override
  Documento toEntity(DocumentoDTO dto);

}

