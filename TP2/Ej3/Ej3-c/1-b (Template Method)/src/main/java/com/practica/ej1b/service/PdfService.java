package com.practica.ej1b.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.practica.ej1b.business.domain.entity.Proveedor;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PdfService {

  public byte[] generarPdfProveedores(List<Proveedor> proveedores) {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      Document document = new Document(PageSize.A4.rotate());
      PdfWriter.getInstance(document, baos);
      document.open();
      
      // Título
      Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
      Paragraph titulo = new Paragraph("Listado de Proveedores", fontTitulo);
      titulo.setAlignment(Element.ALIGN_CENTER);
      titulo.setSpacingAfter(20);
      document.add(titulo);
      
      // Tabla
      PdfPTable table = new PdfPTable(6);
      table.setWidthPercentage(100);
      table.setWidths(new float[]{2f, 2f, 2f, 2f, 2f, 3f});
      
      // Headers con estilo
      Font fontHeader = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
      String[] headers = {"Nombre", "Apellido", "CUIT", "Teléfono", "Email", "Dirección"};
      for (String header : headers) {
        PdfPCell cell = new PdfPCell(new Phrase(header, fontHeader));
        cell.setBackgroundColor(new BaseColor(52, 73, 94));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8);
        table.addCell(cell);
      }
      
      // Datos
      for (Proveedor p : proveedores) {
        table.addCell(p.getNombre() != null ? p.getNombre() : "-");
        table.addCell(p.getApellido() != null ? p.getApellido() : "-");
        table.addCell(p.getCuit() != null ? p.getCuit() : "-");
        table.addCell(p.getTelefono() != null ? p.getTelefono() : "-");
        table.addCell(p.getCorreoElectronico() != null ? p.getCorreoElectronico() : "-");
        
        // Dirección
        String dir = "-";
        if (p.getDireccion() != null) {
            dir = (p.getDireccion().getCalle() != null ? p.getDireccion().getCalle() : "") + 
                  (p.getDireccion().getNumeracion() != null ? " " + p.getDireccion().getNumeracion() : "");
        }
        table.addCell(dir);
      }
      
      document.add(table);
      
      // Total
      Paragraph total = new Paragraph("Total: " + proveedores.size() + " proveedores", 
          new Font(Font.FontFamily.HELVETICA, 10));
      total.setAlignment(Element.ALIGN_RIGHT);
      total.setSpacingBefore(10);
      document.add(total);
      
      document.close();
      log.info("PDF generado con {} proveedores", proveedores.size());
      return baos.toByteArray();
      
    } catch (Exception e) {
      log.error("Error al generar PDF: {}", e.getMessage());
      throw new RuntimeException("Error al generar el PDF", e);
    }
  }
}
