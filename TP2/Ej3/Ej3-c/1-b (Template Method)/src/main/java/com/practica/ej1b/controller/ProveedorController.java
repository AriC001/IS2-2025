package com.practica.ej1b.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practica.ej1b.business.domain.dto.ProveedorDTO;
import com.practica.ej1b.business.domain.entity.Proveedor;
import com.practica.ej1b.business.logic.service.DepartamentoServicio;
import com.practica.ej1b.business.logic.service.LocalidadServicio;
import com.practica.ej1b.business.logic.service.PaisService;
import com.practica.ej1b.business.logic.service.ProveedorServicio;
import com.practica.ej1b.business.logic.service.ProvinciaService;
import com.practica.ej1b.service.PdfService;

/**
 * Controlador para gestionar proveedores.
 * Extiende BaseController para heredar operaciones CRUD comunes.
 */
@Controller
@RequestMapping("/proveedor")
public class ProveedorController extends BaseController<Proveedor, String, ProveedorDTO> {

    private final PaisService paisService;
    private final ProvinciaService provinciaService;
    private final DepartamentoServicio departamentoServicio;
    private final LocalidadServicio localidadServicio;
    private final ProveedorServicio proveedorServicio;
    private final PdfService pdfService;

    public ProveedorController(ProveedorServicio servicio, PaisService paisService, 
                              ProvinciaService provinciaService, DepartamentoServicio departamentoServicio,
                              LocalidadServicio localidadServicio, PdfService pdfService) {
        super(servicio);
        this.proveedorServicio = servicio;
        this.paisService = paisService;
        this.provinciaService = provinciaService;
        this.departamentoServicio = departamentoServicio;
        this.localidadServicio = localidadServicio;
        this.pdfService = pdfService;
    }

    @Override
    protected String getNombreEntidad() {
        return "proveedor";
    }

    @Override
    protected String getNombreEntidadPlural() {
        return "proveedores";
    }

    @Override
    protected String getVistaBase() {
        return "proveedor/";
    }

    @Override
    protected ProveedorDTO crearDtoNuevo() {
        return new ProveedorDTO();
    }

    @Override
    protected ProveedorDTO entidadADto(Proveedor entidad) {
        return ProveedorDTO.fromEntity(entidad);
    }

    @Override
    protected void prepararDatosFormulario(Model model) throws Exception {
        // Cargar listas para los selects de la dirección en el formulario
        model.addAttribute("paises", paisService.listarActivos());
        model.addAttribute("provincias", provinciaService.listarActivos());
        model.addAttribute("departamentos", departamentoServicio.listarActivos());
        model.addAttribute("localidades", localidadServicio.listarActivos());
    }

    /**
     * Descarga el listado de proveedores en formato PDF
     * GET /proveedor/descargar-pdf
     */
    @GetMapping("/descargar-pdf")
    public ResponseEntity<byte[]> descargarPdf() {
        try {
            List<Proveedor> proveedores = new java.util.ArrayList<>(proveedorServicio.listarActivos());
            byte[] pdfBytes = pdfService.generarPdfProveedores(proveedores);
            
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("proveedores_%s.pdf", fecha);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            // En caso de error, retornar error 500
            return ResponseEntity.internalServerError().build();
        }
    }
}
