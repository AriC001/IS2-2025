package com.practica.ej2b.business.logic.adapter.impl;

import org.springframework.stereotype.Component;

import com.practica.ej2b.business.domain.dto.DocumentoDTO;
import com.practica.ej2b.business.domain.entity.Documento;
import com.practica.ej2b.business.logic.adapter.EntityDTOAdapter;

/**
 * Patrón Adapter para convertir entre DocumentoDTO y Documento Entity.
 * Utiliza el patrón Builder para construir los objetos.
 */
@Component
public class DocumentoAdapter implements EntityDTOAdapter<Documento, DocumentoDTO> {

    /**
     * Convierte una entidad Documento a DocumentoDTO.
     * 
     * @param documento entidad a convertir
     * @return DocumentoDTO correspondiente
     */
    @Override
    public DocumentoDTO toDTO(Documento documento) {
        if (documento == null) {
            return null;
        }

        DocumentoDTO dto = DocumentoDTO.builder()
                .nombreArchivo(documento.getNombreArchivo())
                .rutaArchivo(documento.getRutaArchivo())
                .tipoContenido(documento.getTipoContenido())
                .build();
        
        dto.setId(documento.getId());
        dto.setEliminado(documento.isEliminado());
        
        return dto;
    }

    /**
     * Convierte un DocumentoDTO a entidad Documento.
     * 
     * @param dto DTO a convertir
     * @return Documento correspondiente
     */
    @Override
    public Documento toEntity(DocumentoDTO dto) {
        if (dto == null) {
            return null;
        }

        Documento documento = Documento.builder()
                .nombreArchivo(dto.getNombreArchivo())
                .rutaArchivo(dto.getRutaArchivo())
                .tipoContenido(dto.getTipoContenido())
                .build();
        
        documento.setId(dto.getId());
        documento.setEliminado(dto.isEliminado());
        
        return documento;
    }
}
