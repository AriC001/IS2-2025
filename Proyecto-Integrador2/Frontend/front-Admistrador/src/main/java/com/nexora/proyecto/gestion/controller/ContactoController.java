package com.nexora.proyecto.gestion.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexora.proyecto.gestion.business.logic.service.ContactoCorreoElectronicoService;
import com.nexora.proyecto.gestion.business.logic.service.ContactoTelefonicoService;
import com.nexora.proyecto.gestion.dto.ContactoCorreoElectronicoDTO;
import com.nexora.proyecto.gestion.dto.ContactoTelefonicoDTO;
import com.nexora.proyecto.gestion.dto.enums.TipoContacto;
import com.nexora.proyecto.gestion.dto.enums.TipoTelefono;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/contactos")
public class ContactoController {

  private final ContactoTelefonicoService contactoTelefonicoService;
  private final ContactoCorreoElectronicoService contactoCorreoElectronicoService;

  public ContactoController(ContactoTelefonicoService contactoTelefonicoService,
                            ContactoCorreoElectronicoService contactoCorreoElectronicoService) {
    this.contactoTelefonicoService = contactoTelefonicoService;
    this.contactoCorreoElectronicoService = contactoCorreoElectronicoService;
  }

  @GetMapping
  public String listar(Model model, HttpSession session) {
    if (session.getAttribute("token") == null) {
      return "redirect:/auth/login";
    }

    try {
      model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
      model.addAttribute("rol", session.getAttribute("rol"));

      // Obtener todos los contactos
      List<ContactoTelefonicoDTO> telefonos = contactoTelefonicoService.findAllActives();
      List<ContactoCorreoElectronicoDTO> correos = contactoCorreoElectronicoService.findAllActives();

      // Crear lista unificada para mostrar
      List<ContactoUnificado> contactosUnificados = new ArrayList<>();
      telefonos.forEach(t -> contactosUnificados.add(new ContactoUnificado(t.getId(), "TELEFONO", t.getTelefono(), 
          t.getTipoContacto(), t.getTipoTelefono(), t.getObservacion(), null)));
      correos.forEach(c -> contactosUnificados.add(new ContactoUnificado(c.getId(), "CORREO", c.getEmail(), 
          c.getTipoContacto(), null, c.getObservacion(), null)));

      model.addAttribute("contactos", contactosUnificados);

      return "contactos/list";
    } catch (Exception e) {
      model.addAttribute("error", "Error al cargar contactos: " + e.getMessage());
      return "contactos/list";
    }
  }

  @PostMapping
  public String guardar(ContactoFormDTO form, RedirectAttributes redirectAttributes, HttpSession session) {
    if (session.getAttribute("token") == null) {
      return "redirect:/auth/login";
    }

    try {
      if ("TELEFONO".equals(form.getCanal())) {
        ContactoTelefonicoDTO contacto = new ContactoTelefonicoDTO();
        if (form.getId() != null && !form.getId().isEmpty()) {
          contacto = contactoTelefonicoService.findById(form.getId());
        }
        contacto.setTelefono(form.getTelefono());
        contacto.setTipoTelefono(form.getTipoTelefono());
        contacto.setTipoContacto(form.getTipoContacto());
        contacto.setObservacion(form.getObservacion());

        if (contacto.getId() == null || contacto.getId().isEmpty()) {
          contactoTelefonicoService.create(contacto);
          redirectAttributes.addFlashAttribute("success", "Contacto telefónico creado exitosamente");
        } else {
          contactoTelefonicoService.update(contacto.getId(), contacto);
          redirectAttributes.addFlashAttribute("success", "Contacto telefónico actualizado exitosamente");
        }
      } else if ("CORREO".equals(form.getCanal())) {
        ContactoCorreoElectronicoDTO contacto = new ContactoCorreoElectronicoDTO();
        if (form.getId() != null && !form.getId().isEmpty()) {
          contacto = contactoCorreoElectronicoService.findById(form.getId());
        }
        contacto.setEmail(form.getEmail());
        contacto.setTipoContacto(form.getTipoContacto());
        contacto.setObservacion(form.getObservacion());

        if (contacto.getId() == null || contacto.getId().isEmpty()) {
          contactoCorreoElectronicoService.create(contacto);
          redirectAttributes.addFlashAttribute("success", "Contacto de correo creado exitosamente");
        } else {
          contactoCorreoElectronicoService.update(contacto.getId(), contacto);
          redirectAttributes.addFlashAttribute("success", "Contacto de correo actualizado exitosamente");
        }
      }

      return "redirect:/contactos";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al guardar contacto: " + e.getMessage());
      return "redirect:/contactos";
    }
  }

  @PostMapping("/{canal}/{id}/eliminar")
  public String eliminar(@PathVariable String canal, @PathVariable String id, 
                         RedirectAttributes redirectAttributes, HttpSession session) {
    if (session.getAttribute("token") == null) {
      return "redirect:/auth/login";
    }

    try {
      if ("TELEFONO".equals(canal)) {
        contactoTelefonicoService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Contacto telefónico eliminado exitosamente");
      } else if ("CORREO".equals(canal)) {
        contactoCorreoElectronicoService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Contacto de correo eliminado exitosamente");
      }
      return "redirect:/contactos";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al eliminar contacto: " + e.getMessage());
      return "redirect:/contactos";
    }
  }

  @GetMapping("/nuevo")
  public String mostrarFormularioNuevo(Model model, HttpSession session) {
    if (session.getAttribute("token") == null) {
      return "redirect:/auth/login";
    }

    try {
      model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
      model.addAttribute("rol", session.getAttribute("rol"));
      model.addAttribute("contactoForm", new ContactoFormDTO());
      model.addAttribute("tiposContacto", TipoContacto.values());
      model.addAttribute("tiposTelefono", TipoTelefono.values());
      return "contactos/form";
    } catch (Exception e) {
      model.addAttribute("error", "Error al cargar formulario: " + e.getMessage());
      return "contactos/form";
    }
  }

  @GetMapping("/{id}/editar")
  public String mostrarFormularioEditar(@PathVariable String id, 
                                        @RequestParam String canal,
                                        Model model, HttpSession session) {
    if (session.getAttribute("token") == null) {
      return "redirect:/auth/login";
    }

    try {
      model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
      model.addAttribute("rol", session.getAttribute("rol"));
      model.addAttribute("tiposContacto", TipoContacto.values());
      model.addAttribute("tiposTelefono", TipoTelefono.values());

      ContactoFormDTO contactoForm = new ContactoFormDTO();
      if ("TELEFONO".equals(canal)) {
        ContactoTelefonicoDTO contacto = contactoTelefonicoService.findById(id);
        contactoForm.setId(contacto.getId());
        contactoForm.setCanal("TELEFONO");
        contactoForm.setTelefono(contacto.getTelefono());
        contactoForm.setTipoTelefono(contacto.getTipoTelefono());
        contactoForm.setTipoContacto(contacto.getTipoContacto());
        contactoForm.setObservacion(contacto.getObservacion());
      } else if ("CORREO".equals(canal)) {
        ContactoCorreoElectronicoDTO contacto = contactoCorreoElectronicoService.findById(id);
        contactoForm.setId(contacto.getId());
        contactoForm.setCanal("CORREO");
        contactoForm.setEmail(contacto.getEmail());
        contactoForm.setTipoContacto(contacto.getTipoContacto());
        contactoForm.setObservacion(contacto.getObservacion());
      }
      model.addAttribute("contactoForm", contactoForm);
      return "contactos/form";
    } catch (Exception e) {
      model.addAttribute("error", "Error al cargar contacto: " + e.getMessage());
      return "redirect:/contactos";
    }
  }

  // Clase auxiliar para mostrar contactos unificados en la lista
  public static class ContactoUnificado {
    private String id;
    private String canal;
    private String valorPrincipal;
    private TipoContacto tipoContacto;
    private TipoTelefono tipoTelefono;
    private String observacion;
    private String personaId;

    public ContactoUnificado(String id, String canal, String valorPrincipal, TipoContacto tipoContacto,
                            TipoTelefono tipoTelefono, String observacion, String personaId) {
      this.id = id;
      this.canal = canal;
      this.valorPrincipal = valorPrincipal;
      this.tipoContacto = tipoContacto;
      this.tipoTelefono = tipoTelefono;
      this.observacion = observacion;
      this.personaId = personaId;
    }

    // Getters
    public String getId() { return id; }
    public String getCanal() { return canal; }
    public String getValorPrincipal() { return valorPrincipal; }
    public TipoContacto getTipoContacto() { return tipoContacto; }
    public TipoTelefono getTipoTelefono() { return tipoTelefono; }
    public String getObservacion() { return observacion; }
    public String getPersonaId() { return personaId; }
  }

  // DTO para el formulario
  public static class ContactoFormDTO {
    private String id;
    private String canal;
    private String telefono;
    private String email;
    private TipoTelefono tipoTelefono;
    private TipoContacto tipoContacto;
    private String observacion;
    private String personaId;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCanal() { return canal; }
    public void setCanal(String canal) { this.canal = canal; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public TipoTelefono getTipoTelefono() { return tipoTelefono; }
    public void setTipoTelefono(TipoTelefono tipoTelefono) { this.tipoTelefono = tipoTelefono; }
    public TipoContacto getTipoContacto() { return tipoContacto; }
    public void setTipoContacto(TipoContacto tipoContacto) { this.tipoContacto = tipoContacto; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public String getPersonaId() { return personaId; }
    public void setPersonaId(String personaId) { this.personaId = personaId; }
  }
}

