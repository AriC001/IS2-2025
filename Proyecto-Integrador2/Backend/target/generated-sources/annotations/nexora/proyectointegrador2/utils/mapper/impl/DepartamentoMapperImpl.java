package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import nexora.proyectointegrador2.business.domain.entity.Departamento;
import nexora.proyectointegrador2.utils.dto.DepartamentoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-10T00:14:38-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class DepartamentoMapperImpl implements DepartamentoMapper {

    @Autowired
    private ProvinciaMapper provinciaMapper;

    @Override
    public List<DepartamentoDTO> toDTOList(Collection<Departamento> entities) {
        if ( entities == null ) {
            return null;
        }

        List<DepartamentoDTO> list = new ArrayList<DepartamentoDTO>( entities.size() );
        for ( Departamento departamento : entities ) {
            list.add( toDTO( departamento ) );
        }

        return list;
    }

    @Override
    public List<Departamento> toEntityList(List<DepartamentoDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Departamento> list = new ArrayList<Departamento>( dtos.size() );
        for ( DepartamentoDTO departamentoDTO : dtos ) {
            list.add( toEntity( departamentoDTO ) );
        }

        return list;
    }

    @Override
    public DepartamentoDTO toDTO(Departamento entity) {
        if ( entity == null ) {
            return null;
        }

        DepartamentoDTO.DepartamentoDTOBuilder departamentoDTO = DepartamentoDTO.builder();

        departamentoDTO.nombre( entity.getNombre() );
        departamentoDTO.provincia( provinciaMapper.toDTO( entity.getProvincia() ) );

        return departamentoDTO.build();
    }

    @Override
    public Departamento toEntity(DepartamentoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Departamento.DepartamentoBuilder departamento = Departamento.builder();

        departamento.nombre( dto.getNombre() );
        departamento.provincia( provinciaMapper.toEntity( dto.getProvincia() ) );

        return departamento.build();
    }
}
