package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.ContactoCorreoElectronico;
import nexora.proyectointegrador2.business.logic.service.ContactoCorreoElectronicoService;
import nexora.proyectointegrador2.utils.dto.ContactoCorreoElectronicoDTO;
import nexora.proyectointegrador2.utils.mapper.impl.ContactoCorreoElectronicoMapper;

@RestController
@RequestMapping("api/v1/contactos-correo-electronico")
public class ContactoCorreoElectronicoRestController extends BaseRestController<ContactoCorreoElectronico, ContactoCorreoElectronicoDTO, String> {
  public ContactoCorreoElectronicoRestController(ContactoCorreoElectronicoService service, ContactoCorreoElectronicoMapper mapper) {
    super(service, mapper);
  }
}

