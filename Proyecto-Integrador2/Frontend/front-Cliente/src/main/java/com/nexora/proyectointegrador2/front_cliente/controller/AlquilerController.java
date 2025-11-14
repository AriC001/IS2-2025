package com.nexora.proyectointegrador2.front_cliente.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import com.nexora.proyectointegrador2.front_cliente.business.logic.service.AlquilerService;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.DocumentoService;
import com.nexora.proyectointegrador2.front_cliente.dto.AlquilerDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.DocumentoDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.VehiculoDTO;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.UsuarioService;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.VehiculoService;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.ClienteService;
import com.nexora.proyectointegrador2.front_cliente.dto.UsuarioDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.ClienteDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.FacturaDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.enums.TipoDocumentacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/mis-alquileres")
public class AlquilerController extends BaseController<AlquilerDTO, String> {

  private static final Logger logger = LoggerFactory.getLogger(AlquilerController.class);
  
  private final AlquilerService alquilerervice;
  private final DocumentoService documentoService;
  private final UsuarioService usuarioService;
  private final VehiculoService vehiculoService;
  private final ClienteService clienteService;
  
  @Autowired
  private RestTemplate restTemplate;
  
  @Value("${api.base.url}")
  private String baseUrl;

  public AlquilerController(AlquilerService alquilerervice, DocumentoService documentoService, UsuarioService usuarioService, VehiculoService vehiculoService, ClienteService clienteService) {
    super(alquilerervice, "alquiler", "alquileres");
    this.alquilerervice = alquilerervice;
    this.documentoService = documentoService;
    this.usuarioService = usuarioService;
    this.vehiculoService = vehiculoService;
    this.clienteService = clienteService;
  }

  @Override
  protected AlquilerDTO createNewEntity() {
    return new AlquilerDTO();
  }

  /**
   * Sobrescribe el método listar() de BaseController para evitar conflicto.
   * Mapea a /mis-alquileres/list para que no entre en conflicto con alquileres().
   */
  @Override
  @GetMapping("/list")
  public String listar(Model model, HttpSession session) {
    return alquileres(null, null, model, session);
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

  /**
   * GET PARA LISTA DE ALQUILERES
   * Muestra solo los alquileres asociados al usuario logueado
   * Ruta: /mis-alquileres
   */
  @GetMapping
  public String alquileres(
    @RequestParam(required = false) String fechaDesde,
    @RequestParam(required = false) String fechaHasta,
    Model model,
    HttpSession session
  ) {
    // Validar sesión (usa método protegido del BaseController)
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }

    try {
      // Añadir atributos de sesión comunes al modelo
      addSessionAttributesToModel(model, session);

      // Obtener usuarioId desde la sesión (preferido)
      Object usuarioIdAttr = session.getAttribute("usuarioId");
      String usuarioId = null;
      
      if (usuarioIdAttr != null) {
        usuarioId = usuarioIdAttr.toString();
      } else {
        // Fallback: intentar obtener desde usuariosession
        Object usuObj = session.getAttribute("usuariosession");
        if (usuObj instanceof UsuarioDTO) {
          UsuarioDTO sessionUser = (UsuarioDTO) usuObj;
          usuarioId = sessionUser.getId();
        }
      }

      // Obtener lista de alquileres filtrados por usuario
      List<AlquilerDTO> alquileres;
      
      if (usuarioId != null && !usuarioId.isBlank()) {
        logger.debug("Buscando alquileres para usuarioId: {}", usuarioId);
        try {
          alquileres = alquilerervice.findByUsuarioId(usuarioId);
          logger.info("Se encontraron {} alquileres para usuarioId: {}", alquileres.size(), usuarioId);
          // Logging para debugging del estado
          for (AlquilerDTO a : alquileres) {
            logger.debug("Alquiler {} - Estado después de recibir del backend: {}", a.getId(), a.getEstado());
          }
        } catch (Exception e) {
          logger.error("Error al buscar alquileres para usuarioId {}: {}", usuarioId, e.getMessage(), e);
          throw e; // Re-lanzar para que sea capturado por el catch general
        }
      } else {
        // Si no hay usuarioId, retornar lista vacía
        alquileres = List.of();
        logger.warn("No se pudo obtener usuarioId de la sesión para listar alquileres");
      }

      // Enriquecer los alquileres con el detalle del vehículo y cliente
      // (por si el backend devuelve solo el id)
      for (AlquilerDTO a : alquileres) {
        // Preservar el estado antes de enriquecer
        String estadoPreservado = a.getEstado();
        logger.debug("Estado del alquiler {} antes de enriquecer: {}", a.getId(), estadoPreservado);
        
        // Enriquecer vehículo
        if (a.getVehiculo() != null && a.getVehiculo().getId() != null) {
          try {
            VehiculoDTO vehiculo = vehiculoService.findById(a.getVehiculo().getId());
            a.setVehiculo(vehiculo);
            // Restaurar estado después de enriquecer
            if (estadoPreservado != null) {
              a.setEstado(estadoPreservado);
            }
          } catch (Exception ex) {
            logger.warn("No se pudo obtener el vehículo del alquiler {}: {}", a.getId(), ex.getMessage());
          }
        }
        
        // Enriquecer cliente si no está completo o si solo viene el ID
        if (a.getCliente() != null && a.getCliente().getId() != null) {
          // Si el cliente ya tiene nombre y apellido, está completo
          if (a.getCliente().getNombre() == null || a.getCliente().getApellido() == null) {
            try {
              ClienteDTO cliente = clienteService.findById(a.getCliente().getId());
              a.setCliente(cliente);
              // Restaurar estado después de enriquecer
              if (estadoPreservado != null) {
                a.setEstado(estadoPreservado);
              }
              logger.debug("Cliente enriquecido para alquiler {}: {} {}", a.getId(), cliente.getNombre(), cliente.getApellido());
            } catch (Exception ex) {
              logger.warn("No se pudo obtener el cliente del alquiler {}: {}", a.getId(), ex.getMessage());
            }
          }
        } else if (a.getUsuario() != null && a.getUsuario().getId() != null) {
          // Si no hay cliente pero hay usuario, intentar obtener el cliente por usuarioId
          try {
            ClienteDTO cliente = clienteService.findByNombreUsuario(a.getUsuario().getNombreUsuario());
            if (cliente != null) {
              a.setCliente(cliente);
              // Restaurar estado después de enriquecer
              if (estadoPreservado != null) {
                a.setEstado(estadoPreservado);
              }
              logger.debug("Cliente obtenido por nombreUsuario para alquiler {}: {} {}", a.getId(), cliente.getNombre(), cliente.getApellido());
            }
          } catch (Exception ex) {
            logger.warn("No se pudo obtener el cliente por nombreUsuario {} para alquiler {}: {}", 
                a.getUsuario().getNombreUsuario(), a.getId(), ex.getMessage());
          }
        }
        
        // Verificar estado final
        logger.debug("Estado del alquiler {} después de enriquecer: {}", a.getId(), a.getEstado());
      }

      // Añadir la lista al modelo con el nombre esperado por la vista
      model.addAttribute("alquileres", alquileres);

      // Añadir filtros al modelo (por consistencia)
      model.addAttribute("fechaDesde", fechaDesde);
      model.addAttribute("fechaHasta", fechaHasta);

      return "alquiler/list";

    } catch (Exception e) {
      logger.error("Error al obtener alquileres: {}", e.getMessage(), e);
      model.addAttribute("error", "Error al obtener alquileres: " + e.getMessage());
      model.addAttribute("alquileres", List.of());
      return "alquiler/list";
    }
  }
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
    
    // Crear un AlquilerDTO y cargar el vehículo si se proporciona el id
    AlquilerDTO alquiler = createNewEntity();
    
    if (id != null && !id.trim().isEmpty()) {
      try {
        // Cargar el vehículo desde el backend
        VehiculoDTO vehiculo = vehiculoService.findById(id);
        if (vehiculo != null) {
          alquiler.setVehiculo(vehiculo);
          logger.debug("Vehículo cargado para alquiler: {}", vehiculo.getId());
        } else {
          logger.warn("No se encontró vehículo con ID: {}", id);
        }
      } catch (Exception e) {
        logger.error("Error al cargar vehículo con ID {}: {}", id, e.getMessage(), e);
        // Continuar con el alquiler vacío, el template manejará el caso null
      }
    }
    
    model.addAttribute("alquiler", alquiler);
    return "alquiler/detail";
  }


  /**
   * Muestra el detalle de un alquiler específico
   * Valida que el alquiler pertenezca al usuario logueado
   */
  @GetMapping("/{id}")
  public String detalle(@PathVariable("id") String id, Model model, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }

    try {
      addSessionAttributesToModel(model, session);

      // Obtener usuarioId desde la sesión
      Object usuarioIdAttr = session.getAttribute("usuarioId");
      String usuarioId = null;
      
      if (usuarioIdAttr != null) {
        usuarioId = usuarioIdAttr.toString();
      } else {
        // Fallback: intentar obtener desde usuariosession
        Object usuObj = session.getAttribute("usuariosession");
        if (usuObj instanceof UsuarioDTO) {
          UsuarioDTO sessionUser = (UsuarioDTO) usuObj;
          usuarioId = sessionUser.getId();
        }
      }

      // Obtener el alquiler por ID
      AlquilerDTO alquiler = alquilerervice.findById(id);
      
      if (alquiler == null) {
        logger.warn("Alquiler con ID {} no encontrado", id);
        addErrorToModel(model, "Alquiler no encontrado");
        return "alquiler/list";
      }

      // Validar que el alquiler pertenezca al usuario logueado
      if (usuarioId != null && alquiler.getUsuario() != null) {
        String alquilerUsuarioId = alquiler.getUsuario().getId();
        if (!usuarioId.equals(alquilerUsuarioId)) {
          logger.warn("Intento de acceso a alquiler {} por usuario {} que no es el propietario", id, usuarioId);
          addErrorToModel(model, "No tiene permiso para ver este alquiler");
          model.addAttribute("alquileres", List.of());
          return "alquiler/list";
        }
      }

      // Preservar el estado antes de enriquecer
      String estadoPreservado = alquiler.getEstado();
      logger.debug("Estado del alquiler {} antes de enriquecer: {}", id, estadoPreservado);
      
      // Enriquecer con datos del vehículo si es necesario
      if (alquiler.getVehiculo() != null && alquiler.getVehiculo().getId() != null) {
        try {
          VehiculoDTO vehiculo = vehiculoService.findById(alquiler.getVehiculo().getId());
          alquiler.setVehiculo(vehiculo);
          // Restaurar estado después de enriquecer
          if (estadoPreservado != null) {
            alquiler.setEstado(estadoPreservado);
          }
        } catch (Exception ex) {
          logger.warn("No se pudo obtener el vehículo del alquiler {}: {}", id, ex.getMessage());
        }
      }
      
      // Verificar estado final
      logger.debug("Estado del alquiler {} después de enriquecer: {}", id, alquiler.getEstado());

      model.addAttribute("alquiler", alquiler);
      return "alquiler/detalle";

    } catch (Exception e) {
      logger.error("Error al obtener alquiler {}: {}", id, e.getMessage(), e);
      addErrorToModel(model, "Error al obtener el alquiler: " + e.getMessage());
      model.addAttribute("alquileres", List.of());
      return "alquiler/list";
    }
  }

  /**
   * Descarga el PDF de la factura asociada a un alquiler
   */
  @GetMapping("/{id}/factura")
  public ResponseEntity<byte[]> descargarFactura(@PathVariable("id") String id, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    try {
      // Validar que el alquiler pertenezca al usuario logueado
      Object usuarioIdAttr = session.getAttribute("usuarioId");
      String usuarioId = null;
      
      if (usuarioIdAttr != null) {
        usuarioId = usuarioIdAttr.toString();
      } else {
        Object usuObj = session.getAttribute("usuariosession");
        if (usuObj instanceof UsuarioDTO) {
          UsuarioDTO sessionUser = (UsuarioDTO) usuObj;
          usuarioId = sessionUser.getId();
        }
      }

      AlquilerDTO alquiler = alquilerervice.findById(id);
      
      if (alquiler == null) {
        logger.warn("Alquiler con ID {} no encontrado", id);
        return ResponseEntity.notFound().build();
      }

      // Validar que el alquiler pertenezca al usuario
      if (usuarioId != null && alquiler.getUsuario() != null) {
        String alquilerUsuarioId = alquiler.getUsuario().getId();
        if (!usuarioId.equals(alquilerUsuarioId)) {
          logger.warn("Intento de descarga de factura de alquiler {} por usuario {} que no es el propietario", id, usuarioId);
          return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
      }

      // Validar que el alquiler esté pagado
      if (alquiler.getEstado() == null || !alquiler.getEstado().equals("PAGADO")) {
        logger.warn("Intento de descargar factura de alquiler {} que no está pagado. Estado: {}", id, alquiler.getEstado());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }

      // Intentar obtener la factura por alquilerId (similar a como lo hace front-admin)
      String facturaUrl = baseUrl + "/api/v1/facturas/alquiler/" + id;
      logger.debug("Obteniendo factura desde: {}", facturaUrl);
      
      org.springframework.http.HttpEntity<?> requestEntity = new org.springframework.http.HttpEntity<>(null);
      org.springframework.http.HttpMethod method = org.springframework.http.HttpMethod.GET;
      
      String facturaId = null;
      
      try {
        // Obtener la factura usando FacturaDTO directamente (como en front-admin)
        ResponseEntity<FacturaDTO> facturaResponse = restTemplate.exchange(
            facturaUrl,
            method,
            requestEntity,
            FacturaDTO.class
        );

        if (facturaResponse.getStatusCode().is2xxSuccessful() && facturaResponse.getBody() != null) {
          FacturaDTO factura = facturaResponse.getBody();
          facturaId = factura.getId();
          if (facturaId != null && !facturaId.isBlank()) {
            logger.debug("Factura encontrada con ID: {}", facturaId);
          } else {
            logger.warn("La factura no tiene un ID válido");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(("Error: La factura no tiene un ID válido.").getBytes());
          }
        } else {
          logger.warn("No se encontró factura para el alquiler {}. Status: {}", id, facturaResponse.getStatusCode());
          return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(("No se encontró una factura asociada a este alquiler.").getBytes());
        }
      } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
        logger.warn("No se encontró factura para el alquiler {}", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(("No se encontró una factura asociada a este alquiler.").getBytes());
      } catch (org.springframework.web.client.HttpServerErrorException e) {
        logger.error("Error 500 del servidor al obtener factura: {}", e.getResponseBodyAsString());
        // Si el backend devuelve 500, puede ser que no exista la factura o haya un error en el mapper
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
            .body(("Error del servidor al obtener la factura. Es posible que el alquiler no tenga una factura asociada o haya un problema en el servidor.").getBytes());
      }

      // Si tenemos el ID de la factura, descargar el PDF
      if (facturaId != null && !facturaId.isBlank()) {
        String pdfUrl = baseUrl + "/api/v1/facturas/" + facturaId + "/pdf";
        logger.debug("Descargando PDF de factura desde: {}", pdfUrl);
        
        try {
          ResponseEntity<byte[]> pdfResponse = restTemplate.exchange(
              pdfUrl,
              method,
              requestEntity,
              byte[].class
          );

          if (pdfResponse.getStatusCode().is2xxSuccessful() && pdfResponse.getBody() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "factura_alquiler_" + id + ".pdf");
            headers.setContentLength(pdfResponse.getBody().length);
            
            logger.info("Factura descargada exitosamente para alquiler {}", id);
            return new ResponseEntity<>(pdfResponse.getBody(), headers, HttpStatus.OK);
          } else {
            logger.warn("No se pudo descargar el PDF de la factura. Status: {}", pdfResponse.getStatusCode());
            return ResponseEntity.status(pdfResponse.getStatusCode())
                .body(("No se pudo descargar el PDF de la factura.").getBytes());
          }
        } catch (org.springframework.web.client.HttpServerErrorException e) {
          logger.error("Error 500 del servidor al descargar PDF: {}", e.getResponseBodyAsString());
          return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
              .body(("Error del servidor al descargar el PDF de la factura.").getBytes());
        }
      } else {
        logger.error("No se pudo obtener el ID de la factura");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(("No se pudo obtener el ID de la factura.").getBytes());
      }

    } catch (org.springframework.web.client.HttpClientErrorException e) {
      logger.error("Error HTTP (4xx) al descargar factura para alquiler {}: {} - {}", id, e.getStatusCode(), e.getResponseBodyAsString());
      return ResponseEntity.status(e.getStatusCode()).build();
    } catch (org.springframework.web.client.HttpServerErrorException e) {
      logger.error("Error HTTP (5xx) del servidor al descargar factura para alquiler {}: {} - {}", id, e.getStatusCode(), e.getResponseBodyAsString());
      // Si el backend devuelve 500, puede ser que no exista la factura o haya un error interno
      // Devolvemos un error más descriptivo
      return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
          .body(("Error del servidor al obtener la factura. Por favor, verifique que el alquiler tenga una factura asociada.").getBytes());
    } catch (Exception e) {
      logger.error("Error inesperado al descargar factura para alquiler {}: {}", id, e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

    
}
