package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import nexora.proyectointegrador2.business.domain.entity.Provincia;
import nexora.proyectointegrador2.utils.dto.ProvinciaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-09T19:26:39-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class ProvinciaMapperImpl implements ProvinciaMapper {

    @Autowired
    private PaisMapper paisMapper;

    @Override
    public List<ProvinciaDTO> toDTOList(Collection<Provincia> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ProvinciaDTO> list = new ArrayList<ProvinciaDTO>( entities.size() );
        for ( Provincia provincia : entities ) {
            list.add( toDTO( provincia ) );
        }

        return list;
    }

    @Override
    public List<Provincia> toEntityList(List<ProvinciaDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Provincia> list = new ArrayList<Provincia>( dtos.size() );
        for ( ProvinciaDTO provinciaDTO : dtos ) {
            list.add( toEntity( provinciaDTO ) );
        }

        return list;
    }

    @Override
    public ProvinciaDTO toDTO(Provincia entity) {
        if ( entity == null ) {
            return null;
        }

        ProvinciaDTO.ProvinciaDTOBuilder provinciaDTO = ProvinciaDTO.builder();

        provinciaDTO.nombre( entity.getNombre() );
        provinciaDTO.pais( paisMapper.toDTO( entity.getPais() ) );

        return provinciaDTO.build();
    }

    @Override
    public Provincia toEntity(ProvinciaDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Provincia.ProvinciaBuilder provincia = Provincia.builder();

        provincia.nombre( dto.getNombre() );
        provincia.pais( paisMapper.toEntity( dto.getPais() ) );

        return provincia.build();
    }
}
