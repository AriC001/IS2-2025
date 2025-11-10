package com.practica.ej1b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.practica.ej1b.service.MigracionService;
import com.practica.ej1b.service.MigracionService.ResultadoMigracion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/migracion")
@RequiredArgsConstructor
@Slf4j
public class MigracionController {

    private final MigracionService migracionService;

    @GetMapping
    public String mostrarFormulario(Model model) {
        return "migracion/migracion-form";
    }

    @PostMapping("/procesar")
    public String procesarArchivo(@RequestParam("archivo") MultipartFile archivo,
                                  RedirectAttributes redirectAttributes) {
        try {
            // Validar que se haya seleccionado un archivo
            if (archivo.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar un archivo");
                return "redirect:/migracion";
            }

            // Validar que sea un archivo .txt
            String nombreArchivo = archivo.getOriginalFilename();
            if (nombreArchivo == null || !nombreArchivo.toLowerCase().endsWith(".txt")) {
                redirectAttributes.addFlashAttribute("error", "El archivo debe ser un archivo de texto (.txt)");
                return "redirect:/migracion";
            }

            // Procesar el archivo
            ResultadoMigracion resultado = migracionService.procesarArchivoMigracion(archivo);

            // Preparar mensajes de resultado
            if (resultado.getErrores().isEmpty()) {
                redirectAttributes.addFlashAttribute("success", 
                    String.format("Migración completada exitosamente. Se importaron %d proveedores", 
                    resultado.getExitosos()));
            } else {
                redirectAttributes.addFlashAttribute("warning", 
                    String.format("Migración completada con advertencias. Exitosos: %d, Errores: %d", 
                    resultado.getExitosos(), resultado.getErrores().size()));
                redirectAttributes.addFlashAttribute("errores", resultado.getErrores());
            }

            log.info("Migración completada: {} exitosos, {} errores", 
                    resultado.getExitosos(), resultado.getErrores().size());

        } catch (Exception e) {
            log.error("Error al procesar archivo de migración: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", 
                "Error al procesar el archivo: " + e.getMessage());
        }

        return "redirect:/migracion";
    }
}
