package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import nexora.proyectointegrador2.business.domain.entity.Departamento;
import nexora.proyectointegrador2.business.domain.entity.Localidad;
import nexora.proyectointegrador2.utils.dto.LocalidadDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-09T19:26:39-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class LocalidadMapperImpl implements LocalidadMapper {

    @Override
    public List<LocalidadDTO> toDTOList(Collection<Localidad> entities) {
        if ( entities == null ) {
            return null;
        }

        List<LocalidadDTO> list = new ArrayList<LocalidadDTO>( entities.size() );
        for ( Localidad localidad : entities ) {
            list.add( toDTO( localidad ) );
        }

        return list;
    }

    @Override
    public List<Localidad> toEntityList(List<LocalidadDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Localidad> list = new ArrayList<Localidad>( dtos.size() );
        for ( LocalidadDTO localidadDTO : dtos ) {
            list.add( toEntity( localidadDTO ) );
        }

        return list;
    }

    @Override
    public LocalidadDTO toDTO(Localidad entity) {
        if ( entity == null ) {
            return null;
        }

        LocalidadDTO.LocalidadDTOBuilder localidadDTO = LocalidadDTO.builder();

        localidadDTO.departamentoId( entityDepartamentoId( entity ) );
        localidadDTO.nombre( entity.getNombre() );
        localidadDTO.codigoPostal( entity.getCodigoPostal() );

        return localidadDTO.build();
    }

    @Override
    public Localidad toEntity(LocalidadDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Localidad.LocalidadBuilder localidad = Localidad.builder();

        localidad.nombre( dto.getNombre() );
        localidad.codigoPostal( dto.getCodigoPostal() );

        return localidad.build();
    }

    private String entityDepartamentoId(Localidad localidad) {
        Departamento departamento = localidad.getDepartamento();
        if ( departamento == null ) {
            return null;
        }
        return departamento.getId();
    }
}
