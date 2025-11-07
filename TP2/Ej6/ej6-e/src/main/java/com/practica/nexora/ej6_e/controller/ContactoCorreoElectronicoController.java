package com.practica.nexora.ej6_e.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practica.nexora.ej6_e.business.domain.dto.ContactoCorreoElectronicoDTO;
import com.practica.nexora.ej6_e.business.domain.entity.ContactoCorreoElectronico;
import com.practica.nexora.ej6_e.business.logic.service.ContactoCorreoElectronicoService;
import com.practica.nexora.ej6_e.business.logic.service.PersonaService;
import com.practica.nexora.ej6_e.utils.mapper.ContactoCorreoElectronicoMapper;
import com.practica.nexora.ej6_e.utils.mapper.PersonaMapper;

@Controller
@RequestMapping("/contactos/correos")
public class ContactoCorreoElectronicoController extends BaseController<ContactoCorreoElectronico, ContactoCorreoElectronicoDTO, Long> {

  private final PersonaService personaService;
  private final PersonaMapper personaMapper;

  public ContactoCorreoElectronicoController(
      ContactoCorreoElectronicoService contactoCorreoElectronicoService, 
      ContactoCorreoElectronicoMapper contactoCorreoElectronicoMapper,
      PersonaService personaService,
      PersonaMapper personaMapper) {
    super(contactoCorreoElectronicoService, contactoCorreoElectronicoMapper, "contactos/correos");
    this.personaService = personaService;
    this.personaMapper = personaMapper;
  }

  @Override
  protected ContactoCorreoElectronicoDTO createNewDTO() {
    ContactoCorreoElectronicoDTO dto = new ContactoCorreoElectronicoDTO();
    dto.setPersona(new com.practica.nexora.ej6_e.business.domain.dto.PersonaDTO());
    return dto;
  }

  @GetMapping("/nuevo")
  public String showCreateForm(Model model) {
    model.addAttribute("entity", createNewDTO());
    model.addAttribute("isEdit", false);
    model.addAttribute("personas", personaMapper.toDTOList(personaService.findAllActives()));
    return entityName + "/form";
  }

  @GetMapping("/{id}/editar")
  public String showEditForm(@PathVariable Long id, Model model) {
    try {
      ContactoCorreoElectronico entity = service.findById(id).orElseThrow(() -> new IllegalArgumentException("Contacto no encontrado"));
      model.addAttribute("entity", mapper.toDTO(entity));
      model.addAttribute("isEdit", true);
      model.addAttribute("personas", personaMapper.toDTOList(personaService.findAllActives()));
      return entityName + "/form";
    } catch (Exception ex) {
      model.addAttribute("error", "Error al cargar el contacto: " + ex.getMessage());
      return "redirect:/" + entityName;
    }
  }

  @Override
  public String create(ContactoCorreoElectronicoDTO dto, org.springframework.ui.Model model) {
    try {
      ContactoCorreoElectronico entity = mapper.toEntity(dto);
      
      // Buscar y asignar la Persona completa si se proporcionó un ID
      if (dto.getPersona() != null && dto.getPersona().getId() != null) {
        entity.setPersona(personaService.findById(dto.getPersona().getId())
            .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada")));
      }
      
      service.save(entity);
      return "redirect:/" + entityName;
    } catch (Exception ex) {
      model.addAttribute("error", "Error al crear contacto: " + ex.getMessage());
      model.addAttribute("entity", dto);
      model.addAttribute("isEdit", false);
      model.addAttribute("personas", personaMapper.toDTOList(personaService.findAllActives()));
      return entityName + "/form";
    }
  }

}
