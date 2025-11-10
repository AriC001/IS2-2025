package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import nexora.proyectointegrador2.business.domain.entity.CostoVehiculo;
import nexora.proyectointegrador2.utils.dto.CostoVehiculoDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-10T00:14:38-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class CostoVehiculoMapperImpl implements CostoVehiculoMapper {

    @Override
    public List<CostoVehiculoDTO> toDTOList(Collection<CostoVehiculo> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CostoVehiculoDTO> list = new ArrayList<CostoVehiculoDTO>( entities.size() );
        for ( CostoVehiculo costoVehiculo : entities ) {
            list.add( toDTO( costoVehiculo ) );
        }

        return list;
    }

    @Override
    public List<CostoVehiculo> toEntityList(List<CostoVehiculoDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<CostoVehiculo> list = new ArrayList<CostoVehiculo>( dtos.size() );
        for ( CostoVehiculoDTO costoVehiculoDTO : dtos ) {
            list.add( toEntity( costoVehiculoDTO ) );
        }

        return list;
    }

    @Override
    public CostoVehiculoDTO toDTO(CostoVehiculo entity) {
        if ( entity == null ) {
            return null;
        }

        CostoVehiculoDTO.CostoVehiculoDTOBuilder costoVehiculoDTO = CostoVehiculoDTO.builder();

        costoVehiculoDTO.fechaDesde( entity.getFechaDesde() );
        costoVehiculoDTO.fechaHasta( entity.getFechaHasta() );
        costoVehiculoDTO.costo( entity.getCosto() );

        return costoVehiculoDTO.build();
    }

    @Override
    public CostoVehiculo toEntity(CostoVehiculoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CostoVehiculo.CostoVehiculoBuilder costoVehiculo = CostoVehiculo.builder();

        costoVehiculo.fechaDesde( dto.getFechaDesde() );
        costoVehiculo.fechaHasta( dto.getFechaHasta() );
        costoVehiculo.costo( dto.getCosto() );

        return costoVehiculo.build();
    }
}
