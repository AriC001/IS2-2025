package com.practica.ej1b.service;

import java.io.ByteArrayOutputStream;
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
import org.springframework.stereotype.Service;

import com.practica.ej1b.business.domain.entity.Empresa;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExcelService {

  public byte[] generarExcelEmpresas(List<Empresa> empresas) {
    try (Workbook workbook = new XSSFWorkbook();
      ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      
      Sheet sheet = workbook.createSheet("Empresas");
      int rowNum = 0;
      
      // Título
      Row titleRow = sheet.createRow(rowNum++);
      Cell titleCell = titleRow.createCell(0);
      titleCell.setCellValue("LISTADO DE EMPRESAS");
      CellStyle titleStyle = workbook.createCellStyle();
      Font titleFont = workbook.createFont();
      titleFont.setBold(true);
      titleFont.setFontHeightInPoints((short) 14);
      titleStyle.setFont(titleFont);
      titleStyle.setAlignment(HorizontalAlignment.CENTER);
      titleCell.setCellStyle(titleStyle);
      sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
      
      rowNum++; // Fila vacía
      
      // Headers
      Row headerRow = sheet.createRow(rowNum++);
      String[] headers = {"Razón Social", "Dirección", "Localidad", "Provincia", "País", "Código Postal"};
      
      CellStyle headerStyle = workbook.createCellStyle();
      Font headerFont = workbook.createFont();
      headerFont.setBold(true);
      headerFont.setColor(IndexedColors.WHITE.getIndex());
      headerStyle.setFont(headerFont);
      headerStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
      headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      headerStyle.setAlignment(HorizontalAlignment.CENTER);
      headerStyle.setBorderBottom(BorderStyle.THIN);
      headerStyle.setBorderTop(BorderStyle.THIN);
      headerStyle.setBorderLeft(BorderStyle.THIN);
      headerStyle.setBorderRight(BorderStyle.THIN);
      
      for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
        cell.setCellStyle(headerStyle);
      }
      
      // Estilo para datos
      CellStyle dataStyle = workbook.createCellStyle();
      dataStyle.setBorderBottom(BorderStyle.THIN);
      dataStyle.setBorderTop(BorderStyle.THIN);
      dataStyle.setBorderLeft(BorderStyle.THIN);
      dataStyle.setBorderRight(BorderStyle.THIN);
      
      // Datos
      for (Empresa e : empresas) {
        Row row = sheet.createRow(rowNum++);
        
        Cell c0 = row.createCell(0);
        c0.setCellValue(e.getRazonSocial() != null ? e.getRazonSocial() : "-");
        c0.setCellStyle(dataStyle);
        
        Cell c1 = row.createCell(1);
        String dir = "-";
        if (e.getDireccion() != null) {
          dir = (e.getDireccion().getCalle() != null ? e.getDireccion().getCalle() : "") + 
                (e.getDireccion().getNumeracion() != null ? " " + e.getDireccion().getNumeracion() : "");
        }
        c1.setCellValue(dir);
        c1.setCellStyle(dataStyle);
        
        Cell c2 = row.createCell(2);
        c2.setCellValue(e.getDireccion() != null && e.getDireccion().getLocalidad() != null ? 
          e.getDireccion().getLocalidad().getNombre() : "-");
        c2.setCellStyle(dataStyle);
        
        Cell c3 = row.createCell(3);
        c3.setCellValue(e.getDireccion() != null && 
          e.getDireccion().getLocalidad() != null && 
          e.getDireccion().getLocalidad().getDepartamento() != null &&
          e.getDireccion().getLocalidad().getDepartamento().getProvincia() != null ? 
          e.getDireccion().getLocalidad().getDepartamento().getProvincia().getNombre() : "-");
        c3.setCellStyle(dataStyle);
        
        Cell c4 = row.createCell(4);
        c4.setCellValue(e.getDireccion() != null && 
          e.getDireccion().getLocalidad() != null && 
          e.getDireccion().getLocalidad().getDepartamento() != null &&
          e.getDireccion().getLocalidad().getDepartamento().getProvincia() != null &&
          e.getDireccion().getLocalidad().getDepartamento().getProvincia().getPais() != null ? 
          e.getDireccion().getLocalidad().getDepartamento().getProvincia().getPais().getNombre() : "-");
        c4.setCellStyle(dataStyle);
        
        Cell c5 = row.createCell(5);
        c5.setCellValue(e.getDireccion() != null && 
          e.getDireccion().getLocalidad() != null &&
          e.getDireccion().getLocalidad().getCodigoPostal() != null ? 
          e.getDireccion().getLocalidad().getCodigoPostal() : "-");
        c5.setCellStyle(dataStyle);
      }
      
      // Total
      rowNum++;
      Row totalRow = sheet.createRow(rowNum);
      Cell totalCell = totalRow.createCell(0);
      totalCell.setCellValue("Total: " + empresas.size() + " empresas");
      Font totalFont = workbook.createFont();
      totalFont.setBold(true);
      CellStyle totalStyle = workbook.createCellStyle();
      totalStyle.setFont(totalFont);
      totalCell.setCellStyle(totalStyle);
      sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rowNum, rowNum, 0, 5));
      
      // Auto-ajustar columnas
      for (int i = 0; i < 6; i++) {
        sheet.autoSizeColumn(i);
      }
      
      workbook.write(baos);
      log.info("Excel generado con {} empresas", empresas.size());
      return baos.toByteArray();
      
    } catch (Exception e) {
      log.error("Error al generar Excel: {}", e.getMessage());
      throw new RuntimeException("Error al generar el Excel", e);
    }
  }
}
