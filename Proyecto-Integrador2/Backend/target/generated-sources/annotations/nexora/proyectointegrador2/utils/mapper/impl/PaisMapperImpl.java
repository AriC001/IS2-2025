package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import nexora.proyectointegrador2.business.domain.entity.Pais;
import nexora.proyectointegrador2.utils.dto.PaisDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-09T19:26:39-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class PaisMapperImpl implements PaisMapper {

    @Override
    public List<PaisDTO> toDTOList(Collection<Pais> entities) {
        if ( entities == null ) {
            return null;
        }

        List<PaisDTO> list = new ArrayList<PaisDTO>( entities.size() );
        for ( Pais pais : entities ) {
            list.add( toDTO( pais ) );
        }

        return list;
    }

    @Override
    public List<Pais> toEntityList(List<PaisDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Pais> list = new ArrayList<Pais>( dtos.size() );
        for ( PaisDTO paisDTO : dtos ) {
            list.add( toEntity( paisDTO ) );
        }

        return list;
    }

    @Override
    public PaisDTO toDTO(Pais entity) {
        if ( entity == null ) {
            return null;
        }

        PaisDTO.PaisDTOBuilder paisDTO = PaisDTO.builder();

        paisDTO.nombre( entity.getNombre() );

        return paisDTO.build();
    }

    @Override
    public Pais toEntity(PaisDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Pais.PaisBuilder pais = Pais.builder();

        pais.nombre( dto.getNombre() );

        return pais.build();
    }
}
