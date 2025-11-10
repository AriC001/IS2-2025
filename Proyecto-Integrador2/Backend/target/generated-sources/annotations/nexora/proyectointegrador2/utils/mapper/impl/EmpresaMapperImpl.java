package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import nexora.proyectointegrador2.business.domain.entity.Empresa;
import nexora.proyectointegrador2.utils.dto.EmpresaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-09T21:37:14-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class EmpresaMapperImpl implements EmpresaMapper {

    @Autowired
    private DireccionMapper direccionMapper;

    @Override
    public List<EmpresaDTO> toDTOList(Collection<Empresa> entities) {
        if ( entities == null ) {
            return null;
        }

        List<EmpresaDTO> list = new ArrayList<EmpresaDTO>( entities.size() );
        for ( Empresa empresa : entities ) {
            list.add( toDTO( empresa ) );
        }

        return list;
    }

    @Override
    public List<Empresa> toEntityList(List<EmpresaDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Empresa> list = new ArrayList<Empresa>( dtos.size() );
        for ( EmpresaDTO empresaDTO : dtos ) {
            list.add( toEntity( empresaDTO ) );
        }

        return list;
    }

    @Override
    public EmpresaDTO toDTO(Empresa entity) {
        if ( entity == null ) {
            return null;
        }

        EmpresaDTO.EmpresaDTOBuilder empresaDTO = EmpresaDTO.builder();

        empresaDTO.nombre( entity.getNombre() );
        empresaDTO.telefono( entity.getTelefono() );
        empresaDTO.email( entity.getEmail() );
        empresaDTO.direccion( direccionMapper.toDTO( entity.getDireccion() ) );

        return empresaDTO.build();
    }

    @Override
    public Empresa toEntity(EmpresaDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Empresa.EmpresaBuilder empresa = Empresa.builder();

        empresa.nombre( dto.getNombre() );
        empresa.telefono( dto.getTelefono() );
        empresa.email( dto.getEmail() );
        empresa.direccion( direccionMapper.toEntity( dto.getDireccion() ) );

        return empresa.build();
    }
}
