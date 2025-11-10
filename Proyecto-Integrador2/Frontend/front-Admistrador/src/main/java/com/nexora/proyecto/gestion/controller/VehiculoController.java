package com.nexora.proyecto.gestion.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexora.proyecto.gestion.business.logic.service.VehiculoService;
import com.nexora.proyecto.gestion.dto.CaracteristicaVehiculoDTO;
import com.nexora.proyecto.gestion.dto.CostoVehiculoDTO;
import com.nexora.proyecto.gestion.dto.ImagenDTO;
import com.nexora.proyecto.gestion.dto.VehiculoDTO;
import com.nexora.proyecto.gestion.dto.enums.TipoImagen;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController extends BaseController<VehiculoDTO, String> {

  public VehiculoController(VehiculoService vehiculoService) {
    super(vehiculoService, "vehiculo", "vehiculos");
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setLenient(false);
    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
  }

  @Override
  protected VehiculoDTO createNewEntity() {
    return initializeVehiculo(new VehiculoDTO());
  }

  /**
   * Inicializa las propiedades anidadas del vehículo para evitar NullPointerException.
   */
  private VehiculoDTO initializeVehiculo(VehiculoDTO vehiculo) {
    if (vehiculo.getCaracteristicaVehiculo() == null) {
      CaracteristicaVehiculoDTO caracteristica = new CaracteristicaVehiculoDTO();
      caracteristica.setCostoVehiculo(new CostoVehiculoDTO());
      caracteristica.setImagenVehiculo(new ImagenDTO());
      vehiculo.setCaracteristicaVehiculo(caracteristica);
    } else {
      if (vehiculo.getCaracteristicaVehiculo().getCostoVehiculo() == null) {
        vehiculo.getCaracteristicaVehiculo().setCostoVehiculo(new CostoVehiculoDTO());
      }
      if (vehiculo.getCaracteristicaVehiculo().getImagenVehiculo() == null) {
        vehiculo.getCaracteristicaVehiculo().setImagenVehiculo(new ImagenDTO());
      }
    }
    return vehiculo;
  }

  @Override
  protected void prepareFormModel(Model model) {
    Object vehiculoAttr = model.getAttribute("vehiculo");
    if (vehiculoAttr instanceof VehiculoDTO vehiculo) {
      initializeVehiculo(vehiculo);
    }
  }

  @Override
  @PostMapping
  public String crear(VehiculoDTO entity, RedirectAttributes redirectAttributes, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      procesarImagen(entity);
      service.create(entity);
      addSuccessMessage(redirectAttributes, "Vehículo creado exitosamente");
      return "redirect:/" + entityPath;
    } catch (Exception e) {
      handleException(e, redirectAttributes, "crear");
      redirectAttributes.addFlashAttribute(entityPath.substring(0, entityPath.length() - 1), entity);
      return "redirect:/" + entityPath + "/nuevo";
    }
  }

  @Override
  @PostMapping("/{id}")
  public String actualizar(@PathVariable String id, VehiculoDTO entity, RedirectAttributes redirectAttributes,
      HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      procesarImagen(entity);
      service.update(id, entity);
      addSuccessMessage(redirectAttributes, "Vehículo actualizado exitosamente");
      return "redirect:/" + entityPath;
    } catch (Exception e) {
      handleException(e, redirectAttributes, "actualizar");
      return "redirect:/" + entityPath + "/" + id + "/editar";
    }
  }

  private void procesarImagen(VehiculoDTO entity) throws Exception {
    if (entity == null) {
      return;
    }
    if (entity.getCaracteristicaVehiculo() == null) {
      entity.setCaracteristicaVehiculo(new CaracteristicaVehiculoDTO());
    }

    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes == null) {
      return;
    }
    HttpServletRequest request = attributes.getRequest();
    if (!(request instanceof MultipartHttpServletRequest multipartRequest)) {
      return;
    }
    MultipartFile archivo = multipartRequest.getFile("imagenVehiculo");
    if (archivo != null && !archivo.isEmpty()) {
      ImagenDTO imagen = entity.getCaracteristicaVehiculo().getImagenVehiculo();
      if (imagen == null) {
        imagen = new ImagenDTO();
      }
      String nombre = StringUtils.hasText(archivo.getOriginalFilename()) ? archivo.getOriginalFilename()
          : archivo.getName();
      imagen.setNombre(nombre);
      imagen.setMime(archivo.getContentType());
      imagen.setContenido(archivo.getBytes());
      imagen.setTipoImagen(TipoImagen.VEHICULO);
      entity.getCaracteristicaVehiculo().setImagenVehiculo(imagen);
    } else if (entity.getCaracteristicaVehiculo().getImagenVehiculo() != null
        && !StringUtils.hasText(entity.getCaracteristicaVehiculo().getImagenVehiculo().getId())) {
      entity.getCaracteristicaVehiculo().setImagenVehiculo(null);
    }
  }

  /**
   * Sobrescribe el método para editar para asegurar que las propiedades anidadas estén inicializadas.
   */
  @Override
  @GetMapping("/{id}/editar")
  public String mostrarFormularioEditar(@PathVariable String id, Model model, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      addSessionAttributesToModel(model, session);
      VehiculoDTO vehiculo = service.findById(id);
      initializeVehiculo(vehiculo);
      model.addAttribute(entityName, vehiculo);
      try {
        prepareFormModel(model);
      } catch (Exception e) {
        logger.error("Error al preparar modelo del formulario: {}", e.getMessage(), e);
        addErrorToModel(model, "Error al cargar datos del formulario: " + e.getMessage());
      }
      return entityPath + "/form";
    } catch (Exception e) {
      logger.error("Error al cargar {} para editar: {}", entityName, e.getMessage(), e);
      return "redirect:/" + entityPath + "?error=" + e.getMessage();
    }
  }
}
