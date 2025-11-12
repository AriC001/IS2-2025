package nexora.proyectointegrador2.business.logic.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Alquiler;
import nexora.proyectointegrador2.business.domain.entity.Cliente;
import nexora.proyectointegrador2.business.domain.entity.DetalleFactura;
import nexora.proyectointegrador2.business.domain.entity.Empresa;
import nexora.proyectointegrador2.business.domain.entity.Factura;
import nexora.proyectointegrador2.business.domain.entity.Vehiculo;

@Service
public class FacturaPdfService {

  private static final Logger logger = LoggerFactory.getLogger(FacturaPdfService.class);

  @Value("${facturas.dir:uploads/facturas_alquiler}")
  private String directorioFacturas;

  @Autowired
  private EmpresaService empresaService;

  @Autowired
  private nexora.proyectointegrador2.business.persistence.repository.EmpresaRepository empresaRepository;

  /**
   * Genera el PDF de una factura con formato profesional.
   */
  public byte[] generarPdfFactura(Factura factura) throws Exception {
    logger.info("Generando PDF para factura número: {}", factura.getNumeroFactura());

    try (PDDocument document = new PDDocument()) {
      PDPage page = new PDPage();
      document.addPage(page);

      try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
        float margin = 50;
        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();
        float yPosition = pageHeight - margin;
        float lineHeight = 14;
        float spacing = 10;

        // Obtener empresa MyCar
        Empresa empresa = obtenerEmpresaMyCar();
        
        // Obtener datos del alquiler
        DetalleFactura detalle = factura.getDetalles().isEmpty() ? null : factura.getDetalles().get(0);
        Alquiler alquiler = detalle != null ? detalle.getAlquiler() : null;
        Cliente cliente = alquiler != null ? alquiler.getCliente() : null;
        Vehiculo vehiculo = alquiler != null ? alquiler.getVehiculo() : null;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        // ========== ENCABEZADO ==========
        // Nombre de la empresa (izquierda, grande)
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 20);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText(empresa != null ? empresa.getNombre() : "MyCar");
        contentStream.endText();
        yPosition -= lineHeight * 1.5f;

        // Datos de la empresa (izquierda)
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        if (empresa != null) {
          if (empresa.getDireccion() != null) {
            String direccionCompleta = construirDireccionCompleta(empresa.getDireccion());
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(direccionCompleta);
            contentStream.endText();
            yPosition -= lineHeight;
          }
          
          contentStream.beginText();
          contentStream.newLineAtOffset(margin, yPosition);
          contentStream.showText("Teléfono: " + empresa.getTelefono());
          contentStream.endText();
          yPosition -= lineHeight;

          contentStream.beginText();
          contentStream.newLineAtOffset(margin, yPosition);
          contentStream.showText("Email: " + empresa.getEmail());
          contentStream.endText();
          yPosition -= lineHeight * 2;
        }

        // Datos de la factura (derecha)
        float rightMargin = pageWidth - margin - 150;
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
        contentStream.beginText();
        contentStream.newLineAtOffset(rightMargin, pageHeight - margin - lineHeight);
        contentStream.showText("FACTURA");
        contentStream.endText();
        
        float facturaY = pageHeight - margin - lineHeight * 2.5f;
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(rightMargin, facturaY);
        contentStream.showText("Nº de factura: " + factura.getNumeroFactura());
        contentStream.endText();
        facturaY -= lineHeight;
        
        contentStream.beginText();
        contentStream.newLineAtOffset(rightMargin, facturaY);
        contentStream.showText("Fecha: " + dateFormat.format(factura.getFechaFactura()));
        contentStream.endText();
        facturaY -= lineHeight;
        
        if (alquiler != null) {
          contentStream.beginText();
          contentStream.newLineAtOffset(rightMargin, facturaY);
          contentStream.showText("Nº de pedido: " + alquiler.getId());
          contentStream.endText();
          facturaY -= lineHeight;
        }
        
        contentStream.beginText();
        contentStream.newLineAtOffset(rightMargin, facturaY);
        contentStream.showText("Fecha vencimiento: " + dateFormat.format(factura.getFechaFactura()));
        contentStream.endText();

        yPosition -= spacing * 2;

        // ========== DATOS DEL CLIENTE ==========
        // Línea separadora
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(pageWidth - margin, yPosition);
        contentStream.stroke();
        yPosition -= spacing * 2;

        // Título "Facturación" y "Envío" (aunque ambos son el mismo cliente)
        float clientLeftX = margin;
        float clientRightX = pageWidth / 2 + margin / 2;
        
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
        contentStream.beginText();
        contentStream.newLineAtOffset(clientLeftX, yPosition);
        contentStream.showText("Facturación:");
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(clientRightX, yPosition);
        contentStream.showText("Envío:");
        contentStream.endText();
        yPosition -= lineHeight * 1.2f;

        // Datos del cliente (repetidos en ambas columnas)
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        if (cliente != null) {
          String nombreCompleto = cliente.getNombre() + " " + cliente.getApellido();
          contentStream.beginText();
          contentStream.newLineAtOffset(clientLeftX, yPosition);
          contentStream.showText(nombreCompleto);
          contentStream.endText();
          
          contentStream.beginText();
          contentStream.newLineAtOffset(clientRightX, yPosition);
          contentStream.showText(nombreCompleto);
          contentStream.endText();
          yPosition -= lineHeight;

          if (cliente.getDireccion() != null) {
            String direccionCliente = construirDireccionCompleta(cliente.getDireccion());
            contentStream.beginText();
            contentStream.newLineAtOffset(clientLeftX, yPosition);
            contentStream.showText(direccionCliente);
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.newLineAtOffset(clientRightX, yPosition);
            contentStream.showText(direccionCliente);
            contentStream.endText();
            yPosition -= lineHeight;
          }

          String documento = cliente.getTipoDocumento() + ": " + cliente.getNumeroDocumento();
          contentStream.beginText();
          contentStream.newLineAtOffset(clientLeftX, yPosition);
          contentStream.showText(documento);
          contentStream.endText();
          
          contentStream.beginText();
          contentStream.newLineAtOffset(clientRightX, yPosition);
          contentStream.showText(documento);
          contentStream.endText();
        }
        yPosition -= spacing * 2;

        // ========== TABLA DE DETALLES ==========
        // Línea separadora
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(pageWidth - margin, yPosition);
        contentStream.stroke();
        yPosition -= spacing;

        // Encabezados de la tabla
        float col1X = margin; // CANT.
        float col2X = margin + 50; // DESCRIPCIÓN
        float col3X = pageWidth - margin - 100; // PRECIO UNITARIO
        float col4X = pageWidth - margin - 50; // IMPORTE

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 9);
        contentStream.beginText();
        contentStream.newLineAtOffset(col1X, yPosition);
        contentStream.showText("CANT.");
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(col2X, yPosition);
        contentStream.showText("DESCRIPCIÓN");
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(col3X, yPosition);
        contentStream.showText("PRECIO UNITARIO");
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(col4X, yPosition);
        contentStream.showText("IMPORTE");
        contentStream.endText();
        yPosition -= lineHeight;

        // Línea debajo de encabezados
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(pageWidth - margin, yPosition);
        contentStream.stroke();
        yPosition -= spacing;

        // Fila de datos
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        if (detalle != null && vehiculo != null && vehiculo.getCaracteristicaVehiculo() != null) {
          // Cantidad
          contentStream.beginText();
          contentStream.newLineAtOffset(col1X, yPosition);
          contentStream.showText(String.valueOf(detalle.getCantidad()));
          contentStream.endText();
          
          // Descripción
          String descripcion = "Alquiler de vehículo " + vehiculo.getCaracteristicaVehiculo().getMarca() + 
              " " + vehiculo.getCaracteristicaVehiculo().getModelo() + 
              " (" + vehiculo.getPatente() + ")";
          if (alquiler != null) {
            descripcion += " - " + dateFormat.format(alquiler.getFechaDesde()) + 
                " al " + dateFormat.format(alquiler.getFechaHasta());
          }
          contentStream.beginText();
          contentStream.newLineAtOffset(col2X, yPosition);
          contentStream.showText(descripcion);
          contentStream.endText();
          
          // Precio unitario (costo diario)
          Double costoDiario = vehiculo.getCaracteristicaVehiculo().getCostoVehiculo() != null ? 
              vehiculo.getCaracteristicaVehiculo().getCostoVehiculo().getCosto() : 0.0;
          String precioUnitario = "$" + String.format("%.2f", costoDiario);
          contentStream.beginText();
          contentStream.newLineAtOffset(col3X, yPosition);
          contentStream.showText(precioUnitario);
          contentStream.endText();
          
          // Importe
          String importe = "$" + String.format("%.2f", detalle.getSubtotal());
          contentStream.beginText();
          contentStream.newLineAtOffset(col4X, yPosition);
          contentStream.showText(importe);
          contentStream.endText();
        }
        yPosition -= lineHeight * 1.5f;

        // Línea debajo de la fila
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(pageWidth - margin, yPosition);
        contentStream.stroke();
        yPosition -= spacing * 2;

        // ========== TOTALES ==========
        float totalRightX = pageWidth - margin - 50;
        
        // Subtotal
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        contentStream.beginText();
        contentStream.newLineAtOffset(totalRightX - 80, yPosition);
        contentStream.showText("Subtotal:");
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(totalRightX, yPosition);
        contentStream.showText("$" + String.format("%.2f", factura.getTotalPagado()));
        contentStream.endText();
        yPosition -= lineHeight;

        // IVA (0% ya que no tenemos IVA en el sistema)
        contentStream.beginText();
        contentStream.newLineAtOffset(totalRightX - 80, yPosition);
        contentStream.showText("IVA (0.0%):");
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(totalRightX, yPosition);
        contentStream.showText("$0.00");
        contentStream.endText();
        yPosition -= lineHeight * 1.5f;

        // Línea separadora antes del total
        contentStream.moveTo(totalRightX - 100, yPosition);
        contentStream.lineTo(pageWidth - margin, yPosition);
        contentStream.stroke();
        yPosition -= spacing;

        // TOTAL
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(totalRightX - 80, yPosition);
        contentStream.showText("TOTAL:");
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(totalRightX, yPosition);
        contentStream.showText("$" + String.format("%.2f", factura.getTotalPagado()));
        contentStream.endText();
        yPosition -= spacing * 2;

        // Forma de pago
        if (factura.getFormaDePago() != null) {
          contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
          contentStream.beginText();
          contentStream.newLineAtOffset(margin, yPosition);
          contentStream.showText("Forma de Pago: " + factura.getFormaDePago().getTipoPago().name().replace("_", " "));
          contentStream.endText();
          if (factura.getFormaDePago().getObservacion() != null && !factura.getFormaDePago().getObservacion().trim().isEmpty()) {
            yPosition -= lineHeight;
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Observación: " + factura.getFormaDePago().getObservacion());
            contentStream.endText();
          }
        }

        // Pie de página
        yPosition = margin + 30;
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Gracias por su preferencia.");
        contentStream.endText();
      }

      // Guardar PDF en ByteArrayOutputStream
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      document.save(baos);
      return baos.toByteArray();

    } catch (IOException e) {
      logger.error("Error al generar PDF de factura: {}", e.getMessage(), e);
      throw new Exception("Error al generar PDF de factura: " + e.getMessage(), e);
    }
  }

  /**
   * Guarda el PDF en el sistema de archivos.
   */
  public String guardarPdfEnDisco(byte[] pdfContent, Factura factura) throws Exception {
    try {
      Path uploadPath = Paths.get(directorioFacturas);
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
        logger.info("Directorio de facturas creado: {}", uploadPath.toAbsolutePath());
      }

      String nombreArchivo = "factura_" + factura.getNumeroFactura() + "_" + 
          new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".pdf";
      Path destino = uploadPath.resolve(nombreArchivo);

      Files.write(destino, pdfContent);
      logger.info("PDF guardado en: {}", destino.toAbsolutePath());

      return destino.toString();
    } catch (IOException e) {
      logger.error("Error al guardar PDF en disco: {}", e.getMessage(), e);
      throw new Exception("Error al guardar PDF en disco: " + e.getMessage(), e);
    }
  }

  /**
   * Obtiene la empresa MyCar.
   */
  private Empresa obtenerEmpresaMyCar() {
    try {
      return empresaRepository.findByEmailAndEliminadoFalse("mycar.mdz@gmail.com")
          .orElse(null);
    } catch (Exception e) {
      logger.warn("Error al obtener empresa MyCar: {}", e.getMessage());
      return null;
    }
  }

  /**
   * Construye una dirección completa como string.
   */
  private String construirDireccionCompleta(nexora.proyectointegrador2.business.domain.entity.Direccion direccion) {
    if (direccion == null) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    if (direccion.getCalle() != null) {
      sb.append(direccion.getCalle());
    }
    if (direccion.getNumero() != null) {
      sb.append(" ").append(direccion.getNumero());
    }
    if (direccion.getBarrio() != null && !direccion.getBarrio().trim().isEmpty()) {
      sb.append(", ").append(direccion.getBarrio());
    }
    if (direccion.getLocalidad() != null && direccion.getLocalidad().getNombre() != null) {
      sb.append(", ").append(direccion.getLocalidad().getNombre());
    }
    return sb.toString();
  }

}

