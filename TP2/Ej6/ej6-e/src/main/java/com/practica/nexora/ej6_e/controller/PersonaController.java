package com.practica.nexora.ej6_e.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practica.nexora.ej6_e.business.domain.dto.PersonaDTO;
import com.practica.nexora.ej6_e.business.domain.entity.Persona;
import com.practica.nexora.ej6_e.business.logic.service.PersonaService;
import com.practica.nexora.ej6_e.utils.mapper.PersonaMapper;

@Controller
@RequestMapping("/personas")
public class PersonaController extends BaseController<Persona, PersonaDTO, Long> {

  public PersonaController(PersonaService personaService, PersonaMapper personaMapper) {
    super(personaService, personaMapper, "personas");
  }

  @Override
  protected PersonaDTO createNewDTO() {
    return new PersonaDTO();
  }

}
