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

import com.practica.ej1b.business.domain.dto.EmpresaDTO;
import com.practica.ej1b.business.domain.entity.Empresa;
import com.practica.ej1b.business.logic.service.DepartamentoServicio;
import com.practica.ej1b.business.logic.service.EmpresaServicio;
import com.practica.ej1b.business.logic.service.LocalidadServicio;
import com.practica.ej1b.business.logic.service.PaisService;
import com.practica.ej1b.business.logic.service.ProvinciaService;
import com.practica.ej1b.service.ExcelService;

/**
 * Controlador para gestionar empresas.
 * Extiende BaseController para heredar operaciones CRUD comunes.
 */
@Controller
@RequestMapping("/empresa")
public class EmpresaController extends BaseController<Empresa, String, EmpresaDTO> {

    private final PaisService paisService;
    private final ProvinciaService provinciaService;
    private final DepartamentoServicio departamentoServicio;
    private final LocalidadServicio localidadServicio;
    private final ExcelService excelService;
    private final EmpresaServicio empresaServicio;

    public EmpresaController(EmpresaServicio servicio, PaisService paisService, 
                            ProvinciaService provinciaService, DepartamentoServicio departamentoServicio,
                            LocalidadServicio localidadServicio, ExcelService excelService) {
        super(servicio);
        this.paisService = paisService;
        this.provinciaService = provinciaService;
        this.departamentoServicio = departamentoServicio;
        this.localidadServicio = localidadServicio;
        this.excelService = excelService;
        this.empresaServicio = servicio;
    }

    @Override
    protected String getNombreEntidad() {
        return "empresa";
    }

    @Override
    protected String getNombreEntidadPlural() {
        return "empresas";
    }

    @Override
    protected String getVistaBase() {
        return "empresa/";
    }

    @Override
    protected EmpresaDTO crearDtoNuevo() {
        return EmpresaDTO.builder().build();
    }

    @Override
    protected EmpresaDTO entidadADto(Empresa entidad) {
        return EmpresaDTO.fromEntity(entidad);
    }

    @Override
    protected void prepararDatosFormulario(Model model) throws Exception {
        // Cargar listas para los selects de la dirección en el formulario
        model.addAttribute("paises", paisService.listarActivos());
        model.addAttribute("provincias", provinciaService.listarActivos());
        model.addAttribute("departamentos", departamentoServicio.listarActivos());
        model.addAttribute("localidades", localidadServicio.listarActivos());
    }

    @GetMapping("/descargar-excel")
    public ResponseEntity<byte[]> descargarExcel() {
        try {
            List<Empresa> empresas = new java.util.ArrayList<>(empresaServicio.listarActivos());
            byte[] excelBytes = excelService.generarExcelEmpresas(empresas);
            
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("empresas_%s.xlsx", fecha);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", filename);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
