package com.nexora.proyecto.gestion.controller;

import java.util.Collections;
import java.util.List;

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
    } catch (Exception e) {
      logger.error("Error al inicializar empresa en el modelo: {}", e.getMessage(), e);
    }

    // Cargar direcciones con manejo de errores específico
    try {
      List<DireccionDTO> direcciones = direccionService.findAllActives();
      logger.debug("Cargadas {} direcciones para el formulario de empresa", direcciones != null ? direcciones.size() : 0);
      model.addAttribute("direcciones", direcciones != null ? direcciones : Collections.emptyList());
    } catch (Exception e) {
      logger.error("Error al cargar direcciones para el formulario de empresa: {}", e.getMessage(), e);
      logger.error("Stack trace completo:", e);
      // Asegurar que siempre haya un atributo direcciones en el modelo, incluso si está vacío
      model.addAttribute("direcciones", Collections.emptyList());
    }

    // Cargar localidades con manejo de errores específico
    try {
      List<LocalidadDTO> localidades = localidadService.findAllActives();
      logger.debug("Cargadas {} localidades para el formulario de empresa", localidades != null ? localidades.size() : 0);
      model.addAttribute("localidades", localidades != null ? localidades : Collections.emptyList());
    } catch (Exception e) {
      logger.error("Error al cargar localidades para el formulario de empresa: {}", e.getMessage(), e);
      model.addAttribute("localidades", Collections.emptyList());
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

