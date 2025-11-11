package com.nexora.proyecto.gestion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexora.proyecto.gestion.business.logic.service.CaracteristicaVehiculoService;
import com.nexora.proyecto.gestion.business.logic.service.CostoVehiculoService;
import com.nexora.proyecto.gestion.dto.CaracteristicaVehiculoDTO;
import com.nexora.proyecto.gestion.dto.ImagenDTO;
import com.nexora.proyecto.gestion.dto.enums.TipoImagen;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/caracteristicas-vehiculo")
public class CaracteristicaVehiculoController extends BaseController<CaracteristicaVehiculoDTO, String> {

  private final CostoVehiculoService costoVehiculoService;

  public CaracteristicaVehiculoController(CaracteristicaVehiculoService caracteristicaVehiculoService,
      CostoVehiculoService costoVehiculoService) {
    super(caracteristicaVehiculoService, "caracteristicaVehiculo", "caracteristicas-vehiculo");
    this.costoVehiculoService = costoVehiculoService;
  }

  @Override
  protected void prepareFormModel(Model model) {
    try {
      model.addAttribute("costosVehiculo", costoVehiculoService.findAllActives());
    } catch (Exception e) {
      logger.error("Error al cargar costos vehículo: {}", e.getMessage());
    }
  }

  @Override
  protected CaracteristicaVehiculoDTO createNewEntity() {
    return new CaracteristicaVehiculoDTO();
  }

  /**
   * Excluye el campo imagenVehiculo del binding automático porque se procesa manualmente
   * como MultipartFile en el método procesarImagen.
   */
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.setDisallowedFields("imagenVehiculo");
  }

  @PostMapping(consumes = "multipart/form-data")
  public String crear(CaracteristicaVehiculoDTO entity, 
      @RequestParam(value = "imagenVehiculo", required = false) MultipartFile archivoImagen,
      RedirectAttributes redirectAttributes, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      procesarImagen(entity, archivoImagen);
      service.create(entity);
      addSuccessMessage(redirectAttributes, "Característica de vehículo creada exitosamente");
      return "redirect:/" + entityPath;
    } catch (Exception e) {
      handleException(e, redirectAttributes, "crear");
      redirectAttributes.addFlashAttribute(entityPath.substring(0, entityPath.length() - 1), entity);
      return "redirect:/" + entityPath + "/nuevo";
    }
  }

  @PostMapping(value = "/{id}", consumes = "multipart/form-data")
  public String actualizar(@PathVariable String id, CaracteristicaVehiculoDTO entity,
      @RequestParam(value = "imagenVehiculo", required = false) MultipartFile archivoImagen,
      RedirectAttributes redirectAttributes, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      // Cargar la entidad existente para preservar la imagen si no se envía una nueva
      CaracteristicaVehiculoDTO entidadExistente = service.findById(id);
      if (entidadExistente != null && entidadExistente.getImagenVehiculo() != null) {
        // Preservar la imagen existente temporalmente
        entity.setImagenVehiculo(entidadExistente.getImagenVehiculo());
      }
      procesarImagen(entity, archivoImagen);
      service.update(id, entity);
      addSuccessMessage(redirectAttributes, "Característica de vehículo actualizada exitosamente");
      return "redirect:/" + entityPath;
    } catch (Exception e) {
      handleException(e, redirectAttributes, "actualizar");
      return "redirect:/" + entityPath + "/" + id + "/editar";
    }
  }

  private void procesarImagen(CaracteristicaVehiculoDTO entity, MultipartFile archivoImagen) throws Exception {
    if (entity == null) {
      logger.warn("Entity es null, no se puede procesar imagen");
      return;
    }

    if (archivoImagen != null && !archivoImagen.isEmpty()) {
      // Hay archivo: crear nueva imagen
      logger.info("Procesando imagen: nombre={}, tamaño={} bytes, tipo={}", 
          archivoImagen.getOriginalFilename(), archivoImagen.getSize(), archivoImagen.getContentType());
      ImagenDTO imagen = new ImagenDTO();
      String nombre = StringUtils.hasText(archivoImagen.getOriginalFilename()) ? archivoImagen.getOriginalFilename()
          : archivoImagen.getName();
      imagen.setNombre(nombre);
      imagen.setMime(archivoImagen.getContentType());
      byte[] contenido = archivoImagen.getBytes();
      imagen.setContenido(contenido);
      imagen.setTipoImagen(TipoImagen.VEHICULO);
      // Asegurar que el ID sea null para nueva imagen
      imagen.setId(null);
      imagen.setEliminado(false);
      entity.setImagenVehiculo(imagen);
      logger.info("ImagenDTO creada: nombre={}, tamaño contenido={} bytes, mime={}", 
          imagen.getNombre(), 
          imagen.getContenido() != null ? imagen.getContenido().length : 0, 
          imagen.getMime());
    } else {
      logger.debug("No se recibió archivo de imagen");
      // No hay archivo: mantener la imagen existente si tiene ID válido, sino establecer como null
      ImagenDTO imagenActual = entity.getImagenVehiculo();
      if (imagenActual != null) {
        String imagenId = imagenActual.getId();
        // Si el ID es null o vacío, establecer como null
        if (imagenId == null || imagenId.trim().isEmpty()) {
          logger.debug("Imagen sin ID válido, estableciendo como null");
          entity.setImagenVehiculo(null);
        } else {
          logger.debug("Imagen con ID válido: {}, manteniendo imagen existente", imagenId);
          // Mantener la imagen existente (ya está establecida desde entidadExistente)
        }
      }
    }
  }

}

