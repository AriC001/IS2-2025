package nexora.proyectointegrador2.business.logic.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.utils.dto.ReporteAlquilerDTO;
import nexora.proyectointegrador2.utils.dto.ReporteRecaudacionDTO;

@Service
public class ReporteExcelService {

  private static final Logger logger = LoggerFactory.getLogger(ReporteExcelService.class);
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
  private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

  /**
   * Genera un archivo Excel con el reporte de alquileres con factura.
   */
  public byte[] generarReporteAlquileresExcel(List<ReporteAlquilerDTO> datos) throws Exception {
    logger.info("Generando Excel de reporte de alquileres con factura. Registros: {}", datos.size());

    try (Workbook workbook = new XSSFWorkbook()) {
      Sheet sheet = workbook.createSheet("Alquileres con Factura");

      // Estilos
      CellStyle headerStyle = workbook.createCellStyle();
      Font headerFont = workbook.createFont();
      headerFont.setBold(true);
      headerFont.setFontHeightInPoints((short) 12);
      headerStyle.setFont(headerFont);
      headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      headerStyle.setBorderBottom(BorderStyle.THIN);
      headerStyle.setBorderTop(BorderStyle.THIN);
      headerStyle.setBorderLeft(BorderStyle.THIN);
      headerStyle.setBorderRight(BorderStyle.THIN);
      headerStyle.setAlignment(HorizontalAlignment.CENTER);

      CellStyle dataStyle = workbook.createCellStyle();
      dataStyle.setBorderBottom(BorderStyle.THIN);
      dataStyle.setBorderTop(BorderStyle.THIN);
      dataStyle.setBorderLeft(BorderStyle.THIN);
      dataStyle.setBorderRight(BorderStyle.THIN);

      CellStyle currencyStyle = workbook.createCellStyle();
      currencyStyle.cloneStyleFrom(dataStyle);
      currencyStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("$#,##0.00"));

      // Título
      Row titleRow = sheet.createRow(0);
      Cell titleCell = titleRow.createCell(0);
      titleCell.setCellValue("Reporte de Alquileres con Factura");
      Font titleFont = workbook.createFont();
      titleFont.setBold(true);
      titleFont.setFontHeightInPoints((short) 16);
      CellStyle titleStyle = workbook.createCellStyle();
      titleStyle.setFont(titleFont);
      titleCell.setCellStyle(titleStyle);

      // Fecha de generación
      Row dateRow = sheet.createRow(1);
      Cell dateCell = dateRow.createCell(0);
      dateCell.setCellValue("Generado el: " + dateTimeFormat.format(new java.util.Date()));

      // Encabezados
      int rowNum = 3;
      Row headerRow = sheet.createRow(rowNum++);
      String[] headers = { "Cliente", "Documento", "Patente", "Modelo", "Marca", "Fecha Desde", "Fecha Hasta", "Monto Pagado", "Número Factura", "Fecha Factura" };
      
      for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
        cell.setCellStyle(headerStyle);
      }

      // Datos
      for (ReporteAlquilerDTO dato : datos) {
        Row row = sheet.createRow(rowNum++);
        
        int colNum = 0;
        row.createCell(colNum++).setCellValue(dato.getNombreCompletoCliente());
        row.createCell(colNum++).setCellValue(dato.getDocumentoCliente() != null ? dato.getDocumentoCliente() : "");
        row.createCell(colNum++).setCellValue(dato.getPatenteVehiculo() != null ? dato.getPatenteVehiculo() : "");
        row.createCell(colNum++).setCellValue(dato.getModeloVehiculo() != null ? dato.getModeloVehiculo() : "");
        row.createCell(colNum++).setCellValue(dato.getMarcaVehiculo() != null ? dato.getMarcaVehiculo() : "");
        row.createCell(colNum++).setCellValue(dato.getFechaDesde() != null ? dateFormat.format(dato.getFechaDesde()) : "");
        row.createCell(colNum++).setCellValue(dato.getFechaHasta() != null ? dateFormat.format(dato.getFechaHasta()) : "");
        
        Cell montoCell = row.createCell(colNum++);
        montoCell.setCellValue(dato.getMontoPagado() != null ? dato.getMontoPagado() : 0.0);
        montoCell.setCellStyle(currencyStyle);
        
        row.createCell(colNum++).setCellValue(dato.getNumeroFactura() != null ? dato.getNumeroFactura().toString() : "");
        row.createCell(colNum++).setCellValue(dato.getFechaFactura() != null ? dateFormat.format(dato.getFechaFactura()) : "");

        // Aplicar estilo a todas las celdas
        for (int i = 0; i < headers.length; i++) {
          Cell cell = row.getCell(i);
          if (cell != null && cell.getCellStyle() != currencyStyle) {
            cell.setCellStyle(dataStyle);
          }
        }
      }

      // Total
      rowNum++;
      Row totalRow = sheet.createRow(rowNum++);
      Cell totalLabelCell = totalRow.createCell(0);
      totalLabelCell.setCellValue("Total de registros: " + datos.size());
      totalLabelCell.setCellStyle(headerStyle);

      double total = datos.stream()
          .mapToDouble(d -> d.getMontoPagado() != null ? d.getMontoPagado() : 0.0)
          .sum();

      Cell totalValueCell = totalRow.createCell(7);
      totalValueCell.setCellValue(total);
      totalValueCell.setCellStyle(currencyStyle);

      // Ajustar ancho de columnas
      for (int i = 0; i < headers.length; i++) {
        sheet.autoSizeColumn(i);
        sheet.setColumnWidth(i, Math.min(sheet.getColumnWidth(i) + 1000, 15000));
      }

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      workbook.write(baos);
      return baos.toByteArray();
    } catch (IOException e) {
      logger.error("Error al generar Excel de reporte de alquileres", e);
      throw new Exception("Error al generar Excel: " + e.getMessage(), e);
    }
  }

  /**
   * Genera un archivo Excel con el reporte de recaudación por modelo.
   */
  public byte[] generarReporteRecaudacionExcel(List<ReporteRecaudacionDTO> datos) throws Exception {
    logger.info("Generando Excel de reporte de recaudación por modelo. Registros: {}", datos.size());

    try (Workbook workbook = new XSSFWorkbook()) {
      Sheet sheet = workbook.createSheet("Recaudación por Modelo");

      // Estilos
      CellStyle headerStyle = workbook.createCellStyle();
      Font headerFont = workbook.createFont();
      headerFont.setBold(true);
      headerFont.setFontHeightInPoints((short) 12);
      headerStyle.setFont(headerFont);
      headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      headerStyle.setBorderBottom(BorderStyle.THIN);
      headerStyle.setBorderTop(BorderStyle.THIN);
      headerStyle.setBorderLeft(BorderStyle.THIN);
      headerStyle.setBorderRight(BorderStyle.THIN);
      headerStyle.setAlignment(HorizontalAlignment.CENTER);

      CellStyle dataStyle = workbook.createCellStyle();
      dataStyle.setBorderBottom(BorderStyle.THIN);
      dataStyle.setBorderTop(BorderStyle.THIN);
      dataStyle.setBorderLeft(BorderStyle.THIN);
      dataStyle.setBorderRight(BorderStyle.THIN);

      CellStyle currencyStyle = workbook.createCellStyle();
      currencyStyle.cloneStyleFrom(dataStyle);
      currencyStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("$#,##0.00"));

      // Título
      Row titleRow = sheet.createRow(0);
      Cell titleCell = titleRow.createCell(0);
      titleCell.setCellValue("Reporte de Recaudación por Modelo de Vehículo");
      Font titleFont = workbook.createFont();
      titleFont.setBold(true);
      titleFont.setFontHeightInPoints((short) 16);
      CellStyle titleStyle = workbook.createCellStyle();
      titleStyle.setFont(titleFont);
      titleCell.setCellStyle(titleStyle);

      // Fecha de generación
      Row dateRow = sheet.createRow(1);
      Cell dateCell = dateRow.createCell(0);
      dateCell.setCellValue("Generado el: " + dateTimeFormat.format(new java.util.Date()));

      // Encabezados
      int rowNum = 3;
      Row headerRow = sheet.createRow(rowNum++);
      String[] headers = { "Marca", "Modelo", "Total Recaudado", "Cantidad de Alquileres" };
      
      for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
        cell.setCellStyle(headerStyle);
      }

      // Datos
      for (ReporteRecaudacionDTO dato : datos) {
        Row row = sheet.createRow(rowNum++);
        
        row.createCell(0).setCellValue(dato.getMarcaVehiculo() != null ? dato.getMarcaVehiculo() : "");
        row.createCell(1).setCellValue(dato.getModeloVehiculo() != null ? dato.getModeloVehiculo() : "");
        
        Cell montoCell = row.createCell(2);
        montoCell.setCellValue(dato.getTotalRecaudado() != null ? dato.getTotalRecaudado() : 0.0);
        montoCell.setCellStyle(currencyStyle);
        
        row.createCell(3).setCellValue(dato.getCantidadAlquileres() != null ? dato.getCantidadAlquileres() : 0);

        // Aplicar estilo a todas las celdas
        for (int i = 0; i < headers.length; i++) {
          Cell cell = row.getCell(i);
          if (cell != null && cell.getCellStyle() != currencyStyle) {
            cell.setCellStyle(dataStyle);
          }
        }
      }

      // Total
      rowNum++;
      Row totalRow = sheet.createRow(rowNum++);
      Cell totalLabelCell = totalRow.createCell(0);
      totalLabelCell.setCellValue("Total general");
      totalLabelCell.setCellStyle(headerStyle);

      double total = datos.stream()
          .mapToDouble(d -> d.getTotalRecaudado() != null ? d.getTotalRecaudado() : 0.0)
          .sum();

      long totalAlquileres = datos.stream()
          .mapToLong(d -> d.getCantidadAlquileres() != null ? d.getCantidadAlquileres() : 0L)
          .sum();

      Cell totalValueCell = totalRow.createCell(2);
      totalValueCell.setCellValue(total);
      totalValueCell.setCellStyle(currencyStyle);

      Cell totalAlquileresCell = totalRow.createCell(3);
      totalAlquileresCell.setCellValue(totalAlquileres);
      totalAlquileresCell.setCellStyle(dataStyle);

      // Ajustar ancho de columnas
      for (int i = 0; i < headers.length; i++) {
        sheet.autoSizeColumn(i);
        sheet.setColumnWidth(i, Math.min(sheet.getColumnWidth(i) + 1000, 15000));
      }

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      workbook.write(baos);
      return baos.toByteArray();
    } catch (IOException e) {
      logger.error("Error al generar Excel de reporte de recaudación", e);
      throw new Exception("Error al generar Excel: " + e.getMessage(), e);
    }
  }
}

