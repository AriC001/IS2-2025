package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import nexora.proyectointegrador2.business.domain.entity.ConfiguracionCorreoElectronico;
import nexora.proyectointegrador2.utils.dto.ConfiguracionCorreoElectronicoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-09T19:26:38-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class ConfiguracionCorreoElectronicoMapperImpl implements ConfiguracionCorreoElectronicoMapper {

    @Autowired
    private EmpresaMapper empresaMapper;

    @Override
    public List<ConfiguracionCorreoElectronicoDTO> toDTOList(Collection<ConfiguracionCorreoElectronico> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ConfiguracionCorreoElectronicoDTO> list = new ArrayList<ConfiguracionCorreoElectronicoDTO>( entities.size() );
        for ( ConfiguracionCorreoElectronico configuracionCorreoElectronico : entities ) {
            list.add( toDTO( configuracionCorreoElectronico ) );
        }

        return list;
    }

    @Override
    public List<ConfiguracionCorreoElectronico> toEntityList(List<ConfiguracionCorreoElectronicoDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<ConfiguracionCorreoElectronico> list = new ArrayList<ConfiguracionCorreoElectronico>( dtos.size() );
        for ( ConfiguracionCorreoElectronicoDTO configuracionCorreoElectronicoDTO : dtos ) {
            list.add( toEntity( configuracionCorreoElectronicoDTO ) );
        }

        return list;
    }

    @Override
    public ConfiguracionCorreoElectronicoDTO toDTO(ConfiguracionCorreoElectronico entity) {
        if ( entity == null ) {
            return null;
        }

        ConfiguracionCorreoElectronicoDTO.ConfiguracionCorreoElectronicoDTOBuilder configuracionCorreoElectronicoDTO = ConfiguracionCorreoElectronicoDTO.builder();

        configuracionCorreoElectronicoDTO.smtp( entity.getSmtp() );
        configuracionCorreoElectronicoDTO.puerto( entity.getPuerto() );
        configuracionCorreoElectronicoDTO.email( entity.getEmail() );
        configuracionCorreoElectronicoDTO.clave( entity.getClave() );
        configuracionCorreoElectronicoDTO.tls( entity.isTls() );
        configuracionCorreoElectronicoDTO.empresa( empresaMapper.toDTO( entity.getEmpresa() ) );

        return configuracionCorreoElectronicoDTO.build();
    }

    @Override
    public ConfiguracionCorreoElectronico toEntity(ConfiguracionCorreoElectronicoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ConfiguracionCorreoElectronico.ConfiguracionCorreoElectronicoBuilder configuracionCorreoElectronico = ConfiguracionCorreoElectronico.builder();

        configuracionCorreoElectronico.smtp( dto.getSmtp() );
        configuracionCorreoElectronico.puerto( dto.getPuerto() );
        configuracionCorreoElectronico.email( dto.getEmail() );
        configuracionCorreoElectronico.clave( dto.getClave() );
        configuracionCorreoElectronico.tls( dto.isTls() );
        configuracionCorreoElectronico.empresa( empresaMapper.toEntity( dto.getEmpresa() ) );

        return configuracionCorreoElectronico.build();
    }
}
