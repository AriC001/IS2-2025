package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import nexora.proyectointegrador2.business.domain.entity.CaracteristicaVehiculo;
import nexora.proyectointegrador2.utils.dto.CaracteristicaVehiculoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-10T00:14:38-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class CaracteristicaVehiculoMapperImpl implements CaracteristicaVehiculoMapper {

    @Autowired
    private ImagenMapper imagenMapper;
    @Autowired
    private CostoVehiculoMapper costoVehiculoMapper;

    @Override
    public List<CaracteristicaVehiculoDTO> toDTOList(Collection<CaracteristicaVehiculo> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CaracteristicaVehiculoDTO> list = new ArrayList<CaracteristicaVehiculoDTO>( entities.size() );
        for ( CaracteristicaVehiculo caracteristicaVehiculo : entities ) {
            list.add( toDTO( caracteristicaVehiculo ) );
        }

        return list;
    }

    @Override
    public List<CaracteristicaVehiculo> toEntityList(List<CaracteristicaVehiculoDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<CaracteristicaVehiculo> list = new ArrayList<CaracteristicaVehiculo>( dtos.size() );
        for ( CaracteristicaVehiculoDTO caracteristicaVehiculoDTO : dtos ) {
            list.add( toEntity( caracteristicaVehiculoDTO ) );
        }

        return list;
    }

    @Override
    public CaracteristicaVehiculoDTO toDTO(CaracteristicaVehiculo entity) {
        if ( entity == null ) {
            return null;
        }

        CaracteristicaVehiculoDTO.CaracteristicaVehiculoDTOBuilder caracteristicaVehiculoDTO = CaracteristicaVehiculoDTO.builder();

        caracteristicaVehiculoDTO.marca( entity.getMarca() );
        caracteristicaVehiculoDTO.modelo( entity.getModelo() );
        caracteristicaVehiculoDTO.cantidadPuerta( entity.getCantidadPuerta() );
        caracteristicaVehiculoDTO.cantidadAsiento( entity.getCantidadAsiento() );
        caracteristicaVehiculoDTO.anio( entity.getAnio() );
        caracteristicaVehiculoDTO.cantidadTotalVehiculo( entity.getCantidadTotalVehiculo() );
        caracteristicaVehiculoDTO.cantidadVehiculoDisponible( entity.getCantidadVehiculoDisponible() );
        caracteristicaVehiculoDTO.imagenVehiculo( imagenMapper.toDTO( entity.getImagenVehiculo() ) );
        caracteristicaVehiculoDTO.costoVehiculo( costoVehiculoMapper.toDTO( entity.getCostoVehiculo() ) );

        return caracteristicaVehiculoDTO.build();
    }

    @Override
    public CaracteristicaVehiculo toEntity(CaracteristicaVehiculoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CaracteristicaVehiculo.CaracteristicaVehiculoBuilder caracteristicaVehiculo = CaracteristicaVehiculo.builder();

        caracteristicaVehiculo.marca( dto.getMarca() );
        caracteristicaVehiculo.modelo( dto.getModelo() );
        caracteristicaVehiculo.cantidadPuerta( dto.getCantidadPuerta() );
        caracteristicaVehiculo.cantidadAsiento( dto.getCantidadAsiento() );
        caracteristicaVehiculo.anio( dto.getAnio() );
        caracteristicaVehiculo.cantidadTotalVehiculo( dto.getCantidadTotalVehiculo() );
        caracteristicaVehiculo.cantidadVehiculoDisponible( dto.getCantidadVehiculoDisponible() );
        caracteristicaVehiculo.imagenVehiculo( imagenMapper.toEntity( dto.getImagenVehiculo() ) );
        caracteristicaVehiculo.costoVehiculo( costoVehiculoMapper.toEntity( dto.getCostoVehiculo() ) );

        return caracteristicaVehiculo.build();
    }
}
