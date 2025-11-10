package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import nexora.proyectointegrador2.business.domain.entity.Vehiculo;
import nexora.proyectointegrador2.utils.dto.VehiculoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-09T19:26:38-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class VehiculoMapperImpl implements VehiculoMapper {

    @Autowired
    private CaracteristicaVehiculoMapper caracteristicaVehiculoMapper;

    @Override
    public List<VehiculoDTO> toDTOList(Collection<Vehiculo> entities) {
        if ( entities == null ) {
            return null;
        }

        List<VehiculoDTO> list = new ArrayList<VehiculoDTO>( entities.size() );
        for ( Vehiculo vehiculo : entities ) {
            list.add( toDTO( vehiculo ) );
        }

        return list;
    }

    @Override
    public List<Vehiculo> toEntityList(List<VehiculoDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Vehiculo> list = new ArrayList<Vehiculo>( dtos.size() );
        for ( VehiculoDTO vehiculoDTO : dtos ) {
            list.add( toEntity( vehiculoDTO ) );
        }

        return list;
    }

    @Override
    public VehiculoDTO toDTO(Vehiculo entity) {
        if ( entity == null ) {
            return null;
        }

        VehiculoDTO.VehiculoDTOBuilder vehiculoDTO = VehiculoDTO.builder();

        vehiculoDTO.estadoVehiculo( entity.getEstadoVehiculo() );
        vehiculoDTO.patente( entity.getPatente() );
        vehiculoDTO.caracteristicaVehiculo( caracteristicaVehiculoMapper.toDTO( entity.getCaracteristicaVehiculo() ) );

        return vehiculoDTO.build();
    }

    @Override
    public Vehiculo toEntity(VehiculoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Vehiculo.VehiculoBuilder vehiculo = Vehiculo.builder();

        vehiculo.estadoVehiculo( dto.getEstadoVehiculo() );
        vehiculo.patente( dto.getPatente() );
        vehiculo.caracteristicaVehiculo( caracteristicaVehiculoMapper.toEntity( dto.getCaracteristicaVehiculo() ) );

        return vehiculo.build();
    }
}
