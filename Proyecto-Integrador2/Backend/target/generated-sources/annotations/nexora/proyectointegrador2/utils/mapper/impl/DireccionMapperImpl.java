package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import nexora.proyectointegrador2.business.domain.entity.Direccion;
import nexora.proyectointegrador2.utils.dto.DireccionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-09T19:26:38-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class DireccionMapperImpl implements DireccionMapper {

    @Autowired
    private LocalidadMapper localidadMapper;

    @Override
    public List<DireccionDTO> toDTOList(Collection<Direccion> entities) {
        if ( entities == null ) {
            return null;
        }

        List<DireccionDTO> list = new ArrayList<DireccionDTO>( entities.size() );
        for ( Direccion direccion : entities ) {
            list.add( toDTO( direccion ) );
        }

        return list;
    }

    @Override
    public List<Direccion> toEntityList(List<DireccionDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Direccion> list = new ArrayList<Direccion>( dtos.size() );
        for ( DireccionDTO direccionDTO : dtos ) {
            list.add( toEntity( direccionDTO ) );
        }

        return list;
    }

    @Override
    public DireccionDTO toDTO(Direccion entity) {
        if ( entity == null ) {
            return null;
        }

        DireccionDTO.DireccionDTOBuilder direccionDTO = DireccionDTO.builder();

        direccionDTO.calle( entity.getCalle() );
        direccionDTO.numero( entity.getNumero() );
        direccionDTO.barrio( entity.getBarrio() );
        direccionDTO.manzanaPiso( entity.getManzanaPiso() );
        direccionDTO.casaDepartamento( entity.getCasaDepartamento() );
        direccionDTO.referencia( entity.getReferencia() );
        direccionDTO.localidad( localidadMapper.toDTO( entity.getLocalidad() ) );

        return direccionDTO.build();
    }

    @Override
    public Direccion toEntity(DireccionDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Direccion.DireccionBuilder direccion = Direccion.builder();

        direccion.calle( dto.getCalle() );
        direccion.numero( dto.getNumero() );
        direccion.barrio( dto.getBarrio() );
        direccion.manzanaPiso( dto.getManzanaPiso() );
        direccion.casaDepartamento( dto.getCasaDepartamento() );
        direccion.referencia( dto.getReferencia() );
        direccion.localidad( localidadMapper.toEntity( dto.getLocalidad() ) );

        return direccion.build();
    }
}
