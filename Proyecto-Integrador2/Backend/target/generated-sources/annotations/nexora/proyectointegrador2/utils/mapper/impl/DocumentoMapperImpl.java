package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import nexora.proyectointegrador2.business.domain.entity.Documento;
import nexora.proyectointegrador2.utils.dto.DocumentoDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-09T21:37:14-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class DocumentoMapperImpl implements DocumentoMapper {

    @Override
    public List<DocumentoDTO> toDTOList(Collection<Documento> entities) {
        if ( entities == null ) {
            return null;
        }

        List<DocumentoDTO> list = new ArrayList<DocumentoDTO>( entities.size() );
        for ( Documento documento : entities ) {
            list.add( toDTO( documento ) );
        }

        return list;
    }

    @Override
    public List<Documento> toEntityList(List<DocumentoDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Documento> list = new ArrayList<Documento>( dtos.size() );
        for ( DocumentoDTO documentoDTO : dtos ) {
            list.add( toEntity( documentoDTO ) );
        }

        return list;
    }

    @Override
    public DocumentoDTO toDTO(Documento entity) {
        if ( entity == null ) {
            return null;
        }

        DocumentoDTO.DocumentoDTOBuilder documentoDTO = DocumentoDTO.builder();

        documentoDTO.tipoDocumento( entity.getTipoDocumento() );
        documentoDTO.observacion( entity.getObservacion() );
        documentoDTO.pathArchivo( entity.getPathArchivo() );
        documentoDTO.nombreArchivo( entity.getNombreArchivo() );
        documentoDTO.mimeType( entity.getMimeType() );
        documentoDTO.fechaCarga( entity.getFechaCarga() );

        return documentoDTO.build();
    }

    @Override
    public Documento toEntity(DocumentoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Documento.DocumentoBuilder documento = Documento.builder();

        documento.tipoDocumento( dto.getTipoDocumento() );
        documento.observacion( dto.getObservacion() );
        documento.pathArchivo( dto.getPathArchivo() );
        documento.nombreArchivo( dto.getNombreArchivo() );
        documento.mimeType( dto.getMimeType() );
        documento.fechaCarga( dto.getFechaCarga() );

        return documento.build();
    }
}
