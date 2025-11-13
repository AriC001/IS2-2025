package nexora.proyectointegrador2.business.logic.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.utils.dto.ReporteAlquilerDTO;
import nexora.proyectointegrador2.utils.dto.ReporteRecaudacionDTO;

@Service
public class ReportePdfService {

  private static final Logger logger = LoggerFactory.getLogger(ReportePdfService.class);
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
  private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

  /**
   * Genera un PDF con el reporte de alquileres con factura.
   */
  public byte[] generarReporteAlquileresPdf(List<ReporteAlquilerDTO> datos) throws Exception {
    logger.info("Generando PDF de reporte de alquileres con factura. Registros: {}", datos.size());

    PDPageContentStream contentStream = null;
    try (PDDocument document = new PDDocument()) {
      PDPage page = new PDPage();
      document.addPage(page);
      contentStream = new PDPageContentStream(document, page);
        PDType1Font fontTitle = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font fontHeader = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font fontNormal = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        PDType1Font fontSmall = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        float margin = 50;
        float yPosition = 750;
        float lineHeight = 15;
        float tableTop = yPosition - 40;

        // Título
        contentStream.beginText();
        contentStream.setFont(fontTitle, 18);
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Reporte de Alquileres con Factura");
        contentStream.endText();

        yPosition -= 30;

        // Fecha de generación
        contentStream.beginText();
        contentStream.setFont(fontSmall, 10);
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Generado el: " + dateTimeFormat.format(new java.util.Date()));
        contentStream.endText();

        yPosition -= 30;

        // Encabezados de tabla - Ajustados para que quepan en la página (ancho útil ~495 puntos)
        float[] columnWidths = { 70, 70, 60, 70, 60, 60, 60, 70 };
        float tableWidth = 0;
        for (float width : columnWidths) {
          tableWidth += width;
        }

        float xPosition = margin;
        String[] headers = { "Cliente", "Documento", "Patente", "Modelo", "Marca", "Desde", "Hasta", "Monto" };

        contentStream.setFont(fontHeader, 8);
        for (int i = 0; i < headers.length; i++) {
          contentStream.beginText();
          contentStream.newLineAtOffset(xPosition, yPosition);
          contentStream.showText(headers[i]);
          contentStream.endText();
          xPosition += columnWidths[i];
        }

        yPosition -= lineHeight + 2;
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(margin + tableWidth, yPosition);
        contentStream.stroke();
        yPosition -= 8;

        // Datos
        contentStream.setFont(fontSmall, 7);
        for (ReporteAlquilerDTO dato : datos) {
          if (yPosition < 50) {
            contentStream.close();
            page = new PDPage();
            document.addPage(page);
            contentStream = new PDPageContentStream(document, page);
            yPosition = 750;
            contentStream.setFont(fontSmall, 7);
          }

          xPosition = margin;
          // Truncar texto largo para que quepa en las columnas
          String nombreCompleto = dato.getNombreCompletoCliente();
          if (nombreCompleto != null && nombreCompleto.length() > 15) {
            nombreCompleto = nombreCompleto.substring(0, 15) + "...";
          }
          
          String[] rowData = {
              nombreCompleto != null ? nombreCompleto : "",
              dato.getDocumentoCliente() != null ? dato.getDocumentoCliente() : "",
              dato.getPatenteVehiculo() != null ? dato.getPatenteVehiculo() : "",
              dato.getModeloVehiculo() != null && dato.getModeloVehiculo().length() > 10 
                  ? dato.getModeloVehiculo().substring(0, 10) + "..." 
                  : (dato.getModeloVehiculo() != null ? dato.getModeloVehiculo() : ""),
              dato.getMarcaVehiculo() != null ? dato.getMarcaVehiculo() : "",
              dato.getFechaDesde() != null ? dateFormat.format(dato.getFechaDesde()) : "",
              dato.getFechaHasta() != null ? dateFormat.format(dato.getFechaHasta()) : "",
              dato.getMontoPagado() != null ? String.format("$%.2f", dato.getMontoPagado()) : "$0.00"
          };

          for (int i = 0; i < rowData.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(xPosition, yPosition);
            // Truncar texto si es muy largo para la columna
            String text = rowData[i];
            if (text.length() > 15 && i != 7) { // No truncar montos
              text = text.substring(0, Math.min(15, text.length())) + "...";
            }
            contentStream.showText(text);
            contentStream.endText();
            xPosition += columnWidths[i];
          }

          yPosition -= lineHeight + 2;
        }

        // Total
        yPosition -= 8;
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(margin + tableWidth, yPosition);
        contentStream.stroke();
        yPosition -= lineHeight + 5;

        double total = datos.stream()
            .mapToDouble(d -> d.getMontoPagado() != null ? d.getMontoPagado() : 0.0)
            .sum();

        contentStream.setFont(fontHeader, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Total de registros: " + datos.size() + " | Total recaudado: $" + String.format("%.2f", total));
        contentStream.endText();
      
      // Cerrar el contentStream manualmente antes de guardar el documento
      if (contentStream != null) {
        contentStream.close();
        contentStream = null;
      }

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      document.save(baos);
      return baos.toByteArray();
    } catch (IOException e) {
      logger.error("Error al generar PDF de reporte de alquileres", e);
      throw new Exception("Error al generar PDF: " + e.getMessage(), e);
    } finally {
      // Asegurar que el contentStream se cierre incluso si hay un error
      if (contentStream != null) {
        try {
          contentStream.close();
        } catch (IOException e) {
          logger.warn("Error al cerrar contentStream", e);
        }
      }
    }
  }

  /**
   * Genera un PDF con el reporte de recaudación por modelo.
   */
  public byte[] generarReporteRecaudacionPdf(List<ReporteRecaudacionDTO> datos) throws Exception {
    logger.info("Generando PDF de reporte de recaudación por modelo. Registros: {}", datos.size());

    PDPageContentStream contentStream = null;
    try (PDDocument document = new PDDocument()) {
      PDPage page = new PDPage();
      document.addPage(page);
      contentStream = new PDPageContentStream(document, page);
        PDType1Font fontTitle = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font fontHeader = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font fontNormal = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        PDType1Font fontSmall = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        float margin = 50;
        float yPosition = 750;
        float lineHeight = 15;
        float tableTop = yPosition - 40;

        // Título
        contentStream.beginText();
        contentStream.setFont(fontTitle, 18);
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Reporte de Recaudación por Modelo de Vehículo");
        contentStream.endText();

        yPosition -= 30;

        // Fecha de generación
        contentStream.beginText();
        contentStream.setFont(fontSmall, 10);
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Generado el: " + dateTimeFormat.format(new java.util.Date()));
        contentStream.endText();

        yPosition -= 30;

        // Encabezados de tabla - Ajustados para que quepan bien
        float[] columnWidths = { 110, 120, 150, 100 };
        float tableWidth = 0;
        for (float width : columnWidths) {
          tableWidth += width;
        }

        float xPosition = margin;
        String[] headers = { "Marca", "Modelo", "Total Recaudado", "Cant. Alquileres" };

        contentStream.setFont(fontHeader, 10);
        for (int i = 0; i < headers.length; i++) {
          contentStream.beginText();
          contentStream.newLineAtOffset(xPosition, yPosition);
          contentStream.showText(headers[i]);
          contentStream.endText();
          xPosition += columnWidths[i];
        }

        yPosition -= lineHeight + 2;
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(margin + tableWidth, yPosition);
        contentStream.stroke();
        yPosition -= 8;

        // Datos
        contentStream.setFont(fontNormal, 10);
        for (ReporteRecaudacionDTO dato : datos) {
          if (yPosition < 50) {
            contentStream.close();
            page = new PDPage();
            document.addPage(page);
            contentStream = new PDPageContentStream(document, page);
            yPosition = 750;
            contentStream.setFont(fontNormal, 10);
          }

          xPosition = margin;
          // Truncar texto largo si es necesario
          String modelo = dato.getModeloVehiculo();
          if (modelo != null && modelo.length() > 15) {
            modelo = modelo.substring(0, 15) + "...";
          }
          
          String[] rowData = {
              dato.getMarcaVehiculo() != null ? dato.getMarcaVehiculo() : "",
              modelo != null ? modelo : "",
              dato.getTotalRecaudado() != null ? String.format("$%.2f", dato.getTotalRecaudado()) : "$0.00",
              dato.getCantidadAlquileres() != null ? dato.getCantidadAlquileres().toString() : "0"
          };

          for (int i = 0; i < rowData.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(xPosition, yPosition);
            contentStream.showText(rowData[i]);
            contentStream.endText();
            xPosition += columnWidths[i];
          }

          yPosition -= lineHeight + 2;
        }

        // Total
        yPosition -= 8;
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(margin + tableWidth, yPosition);
        contentStream.stroke();
        yPosition -= lineHeight + 5;

        double total = datos.stream()
            .mapToDouble(d -> d.getTotalRecaudado() != null ? d.getTotalRecaudado() : 0.0)
            .sum();

        long totalAlquileres = datos.stream()
            .mapToLong(d -> d.getCantidadAlquileres() != null ? d.getCantidadAlquileres() : 0L)
            .sum();

        contentStream.setFont(fontHeader, 11);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Total general: $" + String.format("%.2f", total) + " | Total alquileres: " + totalAlquileres);
        contentStream.endText();
      
      // Cerrar el contentStream manualmente antes de guardar el documento
      if (contentStream != null) {
        contentStream.close();
        contentStream = null;
      }

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      document.save(baos);
      return baos.toByteArray();
    } catch (IOException e) {
      logger.error("Error al generar PDF de reporte de recaudación", e);
      throw new Exception("Error al generar PDF: " + e.getMessage(), e);
    } finally {
      // Asegurar que el contentStream se cierre incluso si hay un error
      if (contentStream != null) {
        try {
          contentStream.close();
        } catch (IOException e) {
          logger.warn("Error al cerrar contentStream", e);
        }
      }
    }
  }
}

