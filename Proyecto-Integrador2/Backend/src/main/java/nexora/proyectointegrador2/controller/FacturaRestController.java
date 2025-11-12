package nexora.proyectointegrador2.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Factura;
import nexora.proyectointegrador2.business.logic.service.FacturaService;
import nexora.proyectointegrador2.business.enums.TipoPago;
import nexora.proyectointegrador2.utils.dto.FacturaDTO;
import nexora.proyectointegrador2.utils.mapper.impl.FacturaMapper;

@RestController
@RequestMapping("api/v1/facturas")
public class FacturaRestController extends BaseRestController<Factura, FacturaDTO, String> {

  private final FacturaService facturaService;

  @Value("${facturas.dir:uploads/facturas_alquiler}")
  private String directorioFacturas;

  public FacturaRestController(FacturaService service, FacturaMapper mapper) {
    super(service, mapper);
    this.facturaService = service;
  }

  /**
   * Genera una factura para un alquiler.
   * Recibe el ID del alquiler y la forma de pago (solo efectivo desde front administrativo).
   */
  @PostMapping("/generar")
  public ResponseEntity<FacturaDTO> generarFactura(
      @RequestParam String alquilerId,
      @RequestParam(defaultValue = "EFECTIVO") TipoPago tipoPago,
      @RequestParam(required = false) String observacion) {
    try {
      Factura factura = facturaService.generarFacturaParaAlquiler(alquilerId, tipoPago, observacion);
      FacturaDTO facturaDTO = mapper.toDTO(factura);
      return ResponseEntity.status(HttpStatus.CREATED).body(facturaDTO);
    } catch (Exception e) {
      logger.error("Error al generar factura: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Obtiene una factura por el ID del alquiler.
   */
  @GetMapping("/alquiler/{alquilerId}")
  public ResponseEntity<FacturaDTO> obtenerFacturaPorAlquiler(@PathVariable String alquilerId) {
    try {
      Factura factura = facturaService.findByAlquilerId(alquilerId);
      if (factura == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
      FacturaDTO facturaDTO = mapper.toDTO(factura);
      return ResponseEntity.ok(facturaDTO);
    } catch (Exception e) {
      logger.error("Error al obtener factura por alquiler: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Descarga el PDF de una factura.
   */
  @GetMapping("/{id}/pdf")
  public ResponseEntity<byte[]> descargarPdf(@PathVariable String id) {
    try {
      Factura factura = facturaService.findById(id);
      String nombreArchivo = "factura_" + factura.getNumeroFactura() + "_" + 
          new java.text.SimpleDateFormat("yyyyMMdd").format(factura.getFechaFactura()) + ".pdf";
      
      Path pathArchivo = Paths.get(directorioFacturas, nombreArchivo);
      
      // Buscar el archivo con diferentes formatos de fecha si no se encuentra
      if (!Files.exists(pathArchivo)) {
        // Intentar buscar cualquier archivo que comience con factura_ y el número
        Path directorio = Paths.get(directorioFacturas);
        if (Files.exists(directorio)) {
          pathArchivo = Files.list(directorio)
              .filter(p -> p.getFileName().toString().startsWith("factura_" + factura.getNumeroFactura() + "_"))
              .findFirst()
              .orElse(null);
        }
      }

      if (pathArchivo == null || !Files.exists(pathArchivo)) {
        logger.warn("PDF de factura no encontrado: {}", nombreArchivo);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }

      byte[] pdfContent = Files.readAllBytes(pathArchivo);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_PDF);
      headers.setContentDispositionFormData("attachment", nombreArchivo);
      headers.setContentLength(pdfContent.length);

      return ResponseEntity.ok()
          .headers(headers)
          .body(pdfContent);

    } catch (IOException e) {
      logger.error("Error al leer PDF de factura: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    } catch (Exception e) {
      logger.error("Error al obtener factura para descargar PDF: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

}

