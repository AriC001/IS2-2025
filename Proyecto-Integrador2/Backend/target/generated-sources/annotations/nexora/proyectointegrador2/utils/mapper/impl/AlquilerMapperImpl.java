package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import nexora.proyectointegrador2.business.domain.entity.Alquiler;
import nexora.proyectointegrador2.utils.dto.AlquilerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-10T00:14:38-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class AlquilerMapperImpl implements AlquilerMapper {

    @Autowired
    private ClienteMapper clienteMapper;
    @Autowired
    private VehiculoMapper vehiculoMapper;
    @Autowired
    private DocumentoMapper documentoMapper;

    @Override
    public List<AlquilerDTO> toDTOList(Collection<Alquiler> entities) {
        if ( entities == null ) {
            return null;
        }

        List<AlquilerDTO> list = new ArrayList<AlquilerDTO>( entities.size() );
        for ( Alquiler alquiler : entities ) {
            list.add( toDTO( alquiler ) );
        }

        return list;
    }

    @Override
    public List<Alquiler> toEntityList(List<AlquilerDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Alquiler> list = new ArrayList<Alquiler>( dtos.size() );
        for ( AlquilerDTO alquilerDTO : dtos ) {
            list.add( toEntity( alquilerDTO ) );
        }

        return list;
    }

    @Override
    public AlquilerDTO toDTO(Alquiler entity) {
        if ( entity == null ) {
            return null;
        }

        AlquilerDTO.AlquilerDTOBuilder alquilerDTO = AlquilerDTO.builder();

        alquilerDTO.fechaDesde( entity.getFechaDesde() );
        alquilerDTO.fechaHasta( entity.getFechaHasta() );
        alquilerDTO.cliente( clienteMapper.toDTO( entity.getCliente() ) );
        alquilerDTO.vehiculo( vehiculoMapper.toDTO( entity.getVehiculo() ) );
        alquilerDTO.documento( documentoMapper.toDTO( entity.getDocumento() ) );

        return alquilerDTO.build();
    }

    @Override
    public Alquiler toEntity(AlquilerDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Alquiler.AlquilerBuilder alquiler = Alquiler.builder();

        alquiler.fechaDesde( dto.getFechaDesde() );
        alquiler.fechaHasta( dto.getFechaHasta() );
        alquiler.vehiculo( vehiculoMapper.toEntity( dto.getVehiculo() ) );
        alquiler.cliente( clienteMapper.toEntity( dto.getCliente() ) );
        alquiler.documento( documentoMapper.toEntity( dto.getDocumento() ) );

        return alquiler.build();
    }
}
