package com.example.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class ClienteController {

    @GetMapping("/clientes.xlsx")
    public void exportarExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=listado-clientes.xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Clientes");

            // Header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Listado General de Clientes");

            // Sample data
            Row row1 = sheet.createRow(2);
            String[] columnas = {"ID", "Nombre", "Apellido", "Email", "Teléfono", "Ciudad"};
            for (int i = 0; i < columnas.length; i++) {
                row1.createCell(i).setCellValue(columnas[i]);
            }

            workbook.write(response.getOutputStream());
        }
    }
}
