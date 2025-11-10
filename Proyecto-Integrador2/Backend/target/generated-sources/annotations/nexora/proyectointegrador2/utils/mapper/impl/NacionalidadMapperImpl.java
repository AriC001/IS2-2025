package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import nexora.proyectointegrador2.business.domain.entity.Nacionalidad;
import nexora.proyectointegrador2.utils.dto.NacionalidadDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-10T00:14:38-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class NacionalidadMapperImpl implements NacionalidadMapper {

    @Override
    public List<NacionalidadDTO> toDTOList(Collection<Nacionalidad> entities) {
        if ( entities == null ) {
            return null;
        }

        List<NacionalidadDTO> list = new ArrayList<NacionalidadDTO>( entities.size() );
        for ( Nacionalidad nacionalidad : entities ) {
            list.add( toDTO( nacionalidad ) );
        }

        return list;
    }

    @Override
    public List<Nacionalidad> toEntityList(List<NacionalidadDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Nacionalidad> list = new ArrayList<Nacionalidad>( dtos.size() );
        for ( NacionalidadDTO nacionalidadDTO : dtos ) {
            list.add( toEntity( nacionalidadDTO ) );
        }

        return list;
    }

    @Override
    public NacionalidadDTO toDTO(Nacionalidad entity) {
        if ( entity == null ) {
            return null;
        }

        NacionalidadDTO.NacionalidadDTOBuilder nacionalidadDTO = NacionalidadDTO.builder();

        nacionalidadDTO.nombre( entity.getNombre() );

        return nacionalidadDTO.build();
    }

    @Override
    public Nacionalidad toEntity(NacionalidadDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Nacionalidad.NacionalidadBuilder nacionalidad = Nacionalidad.builder();

        nacionalidad.nombre( dto.getNombre() );

        return nacionalidad.build();
    }
}
