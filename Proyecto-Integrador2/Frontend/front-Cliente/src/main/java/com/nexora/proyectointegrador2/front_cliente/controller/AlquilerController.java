package com.nexora.proyectointegrador2.front_cliente.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.util.Date;
import java.text.SimpleDateFormat;

import com.nexora.proyectointegrador2.front_cliente.business.logic.service.AlquilerService;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.DocumentoService;
import com.nexora.proyectointegrador2.front_cliente.dto.AlquilerDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.DocumentoDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.VehiculoDTO;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.UsuarioService;
import com.nexora.proyectointegrador2.front_cliente.dto.UsuarioDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.enums.TipoDocumentacion;
@Controller
@RequestMapping("/alquiler")
public class AlquilerController extends BaseController<AlquilerDTO, String> {

  private final AlquilerService alquilerervice;
  private final DocumentoService documentoService;
  private final UsuarioService usuarioService;

  public AlquilerController(AlquilerService alquilerervice, DocumentoService documentoService, UsuarioService usuarioService) {
    super(alquilerervice, "alquiler", "alquiler");
    this.alquilerervice = alquilerervice;
    this.documentoService = documentoService;
    this.usuarioService = usuarioService;
  }

  @Override
  protected AlquilerDTO createNewEntity() {
    return new AlquilerDTO();
  }

  /**
   * Maneja el POST del formulario de alquiler que incluye un archivo multipart.
   * Flujo:
   *  - valida sesión
   *  - parsea fechas y arma AlquilerDTO
   *  - crea el alquiler en la API (alquilerervice.create)
   *  - sube el documento asociado al alquiler (documentoService.uploadDocumento)
   *  - actualiza el alquiler con el DocumentoDTO retornado
   *  - muestra una vista de recibo/summary
   */
  @PostMapping(consumes = {"multipart/form-data"})
  public String crearConDocumento(@ModelAttribute AlquilerDTO alquilerDTO,
                                   @RequestParam("documentFile") MultipartFile documento,
                                   @RequestParam(value = "vehiculoId", required = false) String vehiculoId,
                                   @RequestParam(value = "fechaDesdeParam", required = false) String fechaDesdeStr,
                                   @RequestParam(value = "fechaHastaParam", required = false) String fechaHastaStr,
                                   Model model,
                                   HttpSession session) {
    // Verificar sesión
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }

    try {
      // Parsear fechas (formato yyyy-MM-dd)
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      if (fechaDesdeStr != null && !fechaDesdeStr.isBlank()) {
        Date desde = sdf.parse(fechaDesdeStr);
        alquilerDTO.setFechaDesde(desde);
      }
      if (fechaHastaStr != null && !fechaHastaStr.isBlank()) {
        Date hasta = sdf.parse(fechaHastaStr);
        alquilerDTO.setFechaHasta(hasta);
      }

      // Asociar usuario desde la sesión si existe (preferir obtener la versión completa
      // desde la API para asegurarnos que el cliente existe en el backend).
      Object usuObj = session.getAttribute("usuariosession");
      UsuarioDTO sessionUser = null;
      if (usuObj instanceof UsuarioDTO) {
        sessionUser = (UsuarioDTO) usuObj;
      }
      // Intentar obtener usuarioId previamente guardado en sesión por el auth handler
      Object usuarioIdAttr = session.getAttribute("usuarioId");
      String usuarioId = usuarioIdAttr != null ? usuarioIdAttr.toString() : (sessionUser != null ? sessionUser.getId() : null);
      if (usuarioId != null && !usuarioId.isBlank()) {
        try {
          UsuarioDTO fullUser = usuarioService.findById(usuarioId);
          alquilerDTO.setUsuario(fullUser);
        } catch (Exception e) {
          // Si no se pudo recuperar desde la API, fallback a lo que tengamos en sesión
          if (sessionUser != null) {
            alquilerDTO.setUsuario(sessionUser);
          }
        }
      } else if (sessionUser != null) {
        alquilerDTO.setUsuario(sessionUser);
      }

      // Asociar vehiculo por id si se proporcionó
      if (vehiculoId != null && !vehiculoId.isBlank()) {
        VehiculoDTO v = new VehiculoDTO();
        v.setId(vehiculoId);
        alquilerDTO.setVehiculo(v);
      }

      // Validar archivo
      if (documento == null || documento.isEmpty()) {
        model.addAttribute("error", "El documento es obligatorio");
        model.addAttribute("alquiler", alquilerDTO);
        return "alquiler/detail";
      }
      long maxBytes = 5L * 1024L * 1024L; // 5MB
      if (documento.getSize() > maxBytes) {
        model.addAttribute("error", "El archivo supera el tamaño máximo permitido (5MB)");
        model.addAttribute("alquiler", alquilerDTO);
        return "alquiler/detail";
      }
      String contentType = documento.getContentType();
      if (contentType == null) contentType = "";
      if (!(contentType.contains("pdf") || contentType.contains("msword") || contentType.contains("officedocument") || contentType.contains("word"))) {
        model.addAttribute("error", "Tipo de archivo no permitido. Use PDF o Word.");
        model.addAttribute("alquiler", alquilerDTO);
        return "alquiler/detail";
      }

      // Crear alquiler en la API
      AlquilerDTO creado = alquilerervice.create(alquilerDTO);

      // Subir documento usando el servicio especializado y asociarlo al alquiler
      try {
        // Usamos DNI por defecto si no se provee otro tipo en el formulario
        DocumentoDTO doc = documentoService.uploadDocumento(documento, TipoDocumentacion.DNI.name(), creado.getId());
        if (doc != null) {
          creado.setDocumento(doc);
          // Actualizar el alquiler para persistir la referencia al documento
          alquilerervice.update(creado.getId(), creado);
        }
      } catch (Exception e) {
        model.addAttribute("warning", "El alquiler fue creado pero no se pudo subir el documento: " + e.getMessage());
      }

      model.addAttribute("alquiler", creado);
      return "alquiler/receipt";
    } catch (Exception e) {
      logger.error("Error al crear alquiler: {}", e.getMessage(), e);
      model.addAttribute("error", "No se pudo crear la reserva: " + e.getMessage());
      model.addAttribute("alquiler", alquilerDTO);
      return "alquiler/detail";
    }
  }

  /**
   * Muestra la página de confirmación de alquiler (formulario) usando los
   * parámetros recibidos por query string. La plantilla `alquiler/detail.html`
   * lee los valores desde ${param.*}, pero aquí añadimos atributos de sesión
   * para el header y dejamos la vista mostrar los parámetros.
   */
  @GetMapping("/detail")
  public String showAlquilerDetail(@RequestParam(required = false) String id,
                                   @RequestParam(required = false) String fechaDesde,
                                   @RequestParam(required = false) String fechaHasta,
                                   @RequestParam(required = false) String total,
                                   @RequestParam(required = false) String basePrice,
                                   Model model,
                                   HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    // Exponer atributos comunes de sesión al modelo (nombreUsuario, rol)
    addSessionAttributesToModel(model, session);
    // No necesitamos poblar el modelo con los params porque la plantilla
    // usa ${param.*} para obtenerlos directamente. De todos modos, dejamos
    // un objeto vacío por compatibilidad con el formulario th:object si se usa.
    model.addAttribute("alquiler", createNewEntity());
    return "alquiler/detail";
  }

    
}
