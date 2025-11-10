package nexora.proyectointegrador2.utils.mapper.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import nexora.proyectointegrador2.business.domain.entity.ConfiguracionCorreoElectronico;
import nexora.proyectointegrador2.utils.dto.ConfiguracionCorreoElectronicoDTO;
import nexora.proyectointegrador2.utils.mapper.BaseMapper;

/**
 * Mapper manual para convertir entre ConfiguracionCorreoElectronico (entidad) y su DTO.
 */
@Component
public class ConfiguracionCorreoElectronicoMapper implements BaseMapper<ConfiguracionCorreoElectronico, ConfiguracionCorreoElectronicoDTO, String> {

  private final EmpresaMapper empresaMapper;

  public ConfiguracionCorreoElectronicoMapper(EmpresaMapper empresaMapper) {
    this.empresaMapper = empresaMapper;
  }

  @Override
  public ConfiguracionCorreoElectronicoDTO toDTO(ConfiguracionCorreoElectronico entity) {
    if (entity == null) {
      return null;
    }

    return ConfiguracionCorreoElectronicoDTO.builder()
        .id(entity.getId())
        .eliminado(entity.isEliminado())
        .smtp(entity.getSmtp())
        .puerto(entity.getPuerto())
        .email(entity.getEmail())
        .clave(entity.getClave())
        .tls(entity.isTls())
        .empresa(empresaMapper.toDTO(entity.getEmpresa()))
        .build();
  }

  @Override
  public ConfiguracionCorreoElectronico toEntity(ConfiguracionCorreoElectronicoDTO dto) {
    if (dto == null) {
      return null;
    }

    ConfiguracionCorreoElectronico configuracion = new ConfiguracionCorreoElectronico();
    configuracion.setId(dto.getId());
    configuracion.setEliminado(dto.isEliminado());
    configuracion.setSmtp(dto.getSmtp());
    configuracion.setPuerto(dto.getPuerto());
    configuracion.setEmail(dto.getEmail());
    configuracion.setClave(dto.getClave());
    configuracion.setTls(dto.isTls());
    configuracion.setEmpresa(empresaMapper.toEntity(dto.getEmpresa()));
    return configuracion;
  }

  @Override
  public List<ConfiguracionCorreoElectronicoDTO> toDTOList(Collection<ConfiguracionCorreoElectronico> entities) {
    if (entities == null) {
      return null;
    }
    return entities.stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<ConfiguracionCorreoElectronico> toEntityList(List<ConfiguracionCorreoElectronicoDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    return dtos.stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }
}
