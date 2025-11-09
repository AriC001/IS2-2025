package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.ConfiguracionCorreoElectronico;
import nexora.proyectointegrador2.business.logic.service.ConfiguracionCorreoElectronicoService;
import nexora.proyectointegrador2.utils.dto.ConfiguracionCorreoElectronicoDTO;
import nexora.proyectointegrador2.utils.mapper.impl.ConfiguracionCorreoElectronicoMapper;

@RestController
@RequestMapping("api/v1/configuraciones-correo")
public class ConfiguracionCorreoElectronicoRestController extends BaseRestController<ConfiguracionCorreoElectronico, ConfiguracionCorreoElectronicoDTO, String> {
  public ConfiguracionCorreoElectronicoRestController(ConfiguracionCorreoElectronicoService service, ConfiguracionCorreoElectronicoMapper mapper) {
    super(service, mapper);
  }
}

