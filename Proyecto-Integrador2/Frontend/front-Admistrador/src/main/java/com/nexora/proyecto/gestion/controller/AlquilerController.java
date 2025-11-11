package com.nexora.proyecto.gestion.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexora.proyecto.gestion.business.logic.service.AlquilerService;
import com.nexora.proyecto.gestion.business.logic.service.ClienteService;
import com.nexora.proyecto.gestion.business.logic.service.VehiculoService;
import com.nexora.proyecto.gestion.dto.AlquilerDTO;
import com.nexora.proyecto.gestion.dto.ClienteDTO;
import com.nexora.proyecto.gestion.dto.DocumentoDTO;
import com.nexora.proyecto.gestion.dto.VehiculoDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/alquileres")
public class AlquilerController extends BaseController<AlquilerDTO, String> {

  private final ClienteService clienteService;
  private final VehiculoService vehiculoService;

  public AlquilerController(AlquilerService alquilerService, ClienteService clienteService, VehiculoService vehiculoService) {
    super(alquilerService, "alquiler", "alquileres");
    this.clienteService = clienteService;
    this.vehiculoService = vehiculoService;
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setLenient(false);
    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
  }

  @Override
  protected AlquilerDTO createNewEntity() {
    AlquilerDTO alquiler = new AlquilerDTO();
    // Inicializar objetos anidados para evitar NullPointerException en Thymeleaf
    alquiler.setCliente(new ClienteDTO());
    alquiler.setVehiculo(new VehiculoDTO());
    alquiler.setDocumento(new DocumentoDTO());
    return alquiler;
  }

  @Override
  protected void prepareFormModel(Model model) {
    try {
      
      model.addAttribute("clientes", clienteService.findAllActives());
      model.addAttribute("vehiculos", vehiculoService.findAllActives());
    } catch (Exception e) {
      logger.error("Error al preparar modelo del formulario: {}", e.getMessage());
    }
  }

  @PostMapping(consumes = "multipart/form-data")
  public String crear(AlquilerDTO entity, @RequestParam(value = "archivoDocumento", required = false) MultipartFile archivoDocumento, RedirectAttributes redirectAttributes, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      // Validar que el documento tenga tipoDocumento (obligatorio)
      if (entity.getDocumento() == null || entity.getDocumento().getTipoDocumento() == null) {
        throw new Exception("El tipo de documento es obligatorio");
      }

      // Validar que el archivo esté presente
      if (archivoDocumento == null || archivoDocumento.isEmpty()) {
        throw new Exception("El archivo del documento es obligatorio");
      }

      // El backend se encarga de procesar el documento y guardar el archivo
      AlquilerService alquilerService = (AlquilerService) service;
      alquilerService.createWithDocument(entity, archivoDocumento);
      
      addSuccessMessage(redirectAttributes, "Alquiler creado exitosamente");
      return "redirect:/" + entityPath;
    } catch (Exception e) {
      handleException(e, redirectAttributes, "crear");
      redirectAttributes.addFlashAttribute(entityPath.substring(0, entityPath.length() - 1), entity);
      return "redirect:/" + entityPath + "/nuevo";
    }
  }

  @PostMapping(value = "/{id}", consumes = "multipart/form-data")
  public String actualizar(@PathVariable String id, AlquilerDTO entity, @RequestParam(value = "archivoDocumento", required = false) MultipartFile archivoDocumento, RedirectAttributes redirectAttributes, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      // Validar que el documento tenga tipoDocumento (obligatorio)
      if (entity.getDocumento() == null || entity.getDocumento().getTipoDocumento() == null) {
        throw new Exception("El tipo de documento es obligatorio");
      }

      // Validar que el archivo esté presente
      if (archivoDocumento == null || archivoDocumento.isEmpty()) {
        throw new Exception("El archivo del documento es obligatorio");
      }

      // El backend se encarga de procesar el documento y guardar el archivo
      AlquilerService alquilerService = (AlquilerService) service;
      alquilerService.updateWithDocument(id, entity, archivoDocumento);
      
      addSuccessMessage(redirectAttributes, "Alquiler actualizado exitosamente");
      return "redirect:/" + entityPath;
    } catch (Exception e) {
      handleException(e, redirectAttributes, "actualizar");
      return "redirect:/" + entityPath + "/" + id + "/editar";
    }
  }

}

