package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.ContactoTelefonico;
import nexora.proyectointegrador2.business.logic.service.ContactoTelefonicoService;
import nexora.proyectointegrador2.utils.dto.ContactoTelefonicoDTO;
import nexora.proyectointegrador2.utils.mapper.impl.ContactoTelefonicoMapper;

@RestController
@RequestMapping("api/v1/contactos-telefonicos")
public class ContactoTelefonicoRestController extends BaseRestController<ContactoTelefonico, ContactoTelefonicoDTO, String> {
  public ContactoTelefonicoRestController(ContactoTelefonicoService service, ContactoTelefonicoMapper mapper) {
    super(service, mapper);
  }
}

