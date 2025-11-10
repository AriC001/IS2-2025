package com.practica.ej1b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practica.ej1b.business.domain.dto.DepartamentoDTO;
import com.practica.ej1b.business.domain.entity.Departamento;
import com.practica.ej1b.business.logic.service.DepartamentoServicio;
import com.practica.ej1b.business.logic.service.PaisService;
import com.practica.ej1b.business.logic.service.ProvinciaService;

/**
 * Controlador para gestionar departamentos.
 * Extiende BaseController para heredar operaciones CRUD comunes.
 */
@Controller
@RequestMapping("/departamento")
public class DepartamentoController extends BaseController<Departamento, String, DepartamentoDTO> {

    private final PaisService paisService;
    private final ProvinciaService provinciaService;

    public DepartamentoController(DepartamentoServicio servicio, PaisService paisService, ProvinciaService provinciaService) {
        super(servicio);
        this.paisService = paisService;
        this.provinciaService = provinciaService;
    }

    @Override
    protected String getNombreEntidad() {
        return "departamento";
    }

    @Override
    protected String getNombreEntidadPlural() {
        return "departamentos";
    }

    @Override
    protected String getVistaBase() {
        return "departamento/";
    }

    @Override
    protected DepartamentoDTO crearDtoNuevo() {
        return new DepartamentoDTO();
    }

    @Override
    protected DepartamentoDTO entidadADto(Departamento entidad) {
        return DepartamentoDTO.fromEntity(entidad);
    }

    @Override
    protected void prepararDatosFormulario(Model model) throws Exception {
        // Cargar listas para los selects en cascada en el formulario
        model.addAttribute("paises", paisService.listarActivos());
        model.addAttribute("provincias", provinciaService.listarActivos());
    }
}
