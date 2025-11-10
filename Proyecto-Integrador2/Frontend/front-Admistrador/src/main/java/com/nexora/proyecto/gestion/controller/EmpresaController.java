package com.nexora.proyecto.gestion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexora.proyecto.gestion.business.logic.service.DireccionService;
import com.nexora.proyecto.gestion.business.logic.service.EmpresaService;
import com.nexora.proyecto.gestion.business.logic.service.LocalidadService;
import com.nexora.proyecto.gestion.dto.DireccionDTO;
import com.nexora.proyecto.gestion.dto.EmpresaDTO;
import com.nexora.proyecto.gestion.dto.LocalidadDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/empresas")
public class EmpresaController extends BaseController<EmpresaDTO, String> {

  private final LocalidadService localidadService;
  private final DireccionService direccionService;

  public EmpresaController(EmpresaService empresaService, LocalidadService localidadService,
      DireccionService direccionService) {
    super(empresaService, "empresa", "empresas");
    this.localidadService = localidadService;
    this.direccionService = direccionService;
  }

  @Override
  protected EmpresaDTO createNewEntity() {
    EmpresaDTO empresa = new EmpresaDTO();
    if (empresa.getDireccion() == null) {
      DireccionDTO direccion = new DireccionDTO();
      direccion.setLocalidad(new LocalidadDTO());
      empresa.setDireccion(direccion);
    }
    return empresa;
  }

  @Override
  protected void prepareFormModel(Model model) {
    try {
      EmpresaDTO empresa = (EmpresaDTO) model.getAttribute("empresa");
      if (empresa != null) {
        if (empresa.getDireccion() == null) {
          DireccionDTO direccion = new DireccionDTO();
          direccion.setLocalidad(new LocalidadDTO());
          empresa.setDireccion(direccion);
        } else if (empresa.getDireccion().getLocalidad() == null) {
          empresa.getDireccion().setLocalidad(new LocalidadDTO());
        }
      }
      model.addAttribute("direcciones", direccionService.findAllActives());
      model.addAttribute("localidades", localidadService.findAllActives());
    } catch (Exception e) {
      logger.error("Error al preparar modelo del formulario: {}", e.getMessage());
    }
  }

  @Override
  @PostMapping
  public String crear(EmpresaDTO entity, RedirectAttributes redirectAttributes, HttpSession session) {
    sanitizeEmpresa(entity);
    return super.crear(entity, redirectAttributes, session);
  }

  @Override
  @PostMapping("/{id}")
  public String actualizar(@PathVariable String id, EmpresaDTO entity, RedirectAttributes redirectAttributes,
      HttpSession session) {
    sanitizeEmpresa(entity);
    return super.actualizar(id, entity, redirectAttributes, session);
  }

  private void sanitizeEmpresa(EmpresaDTO entity) {
    if (entity.getDireccion() != null && !StringUtils.hasText(entity.getDireccion().getId())) {
      entity.setDireccion(null);
    }
  }
}

