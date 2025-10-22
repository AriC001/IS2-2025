package com.practica.ej1b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practica.ej1b.business.domain.dto.LocalidadDTO;
import com.practica.ej1b.business.domain.entity.Localidad;
import com.practica.ej1b.business.logic.service.DepartamentoServicio;
import com.practica.ej1b.business.logic.service.LocalidadServicio;
import com.practica.ej1b.business.logic.service.PaisService;
import com.practica.ej1b.business.logic.service.ProvinciaService;

/**
 * Controlador para gestionar localidades.
 * Extiende BaseController para heredar operaciones CRUD comunes.
 */
@Controller
@RequestMapping("/localidad")
public class LocalidadController extends BaseController<Localidad, String, LocalidadDTO> {

    private final PaisService paisService;
    private final ProvinciaService provinciaService;
    private final DepartamentoServicio departamentoServicio;

    public LocalidadController(LocalidadServicio servicio, PaisService paisService, 
                              ProvinciaService provinciaService, DepartamentoServicio departamentoServicio) {
        super(servicio);
        this.paisService = paisService;
        this.provinciaService = provinciaService;
        this.departamentoServicio = departamentoServicio;
    }

    @Override
    protected String getNombreEntidad() {
        return "localidad";
    }

    @Override
    protected String getNombreEntidadPlural() {
        return "localidades";
    }

    @Override
    protected String getVistaBase() {
        return "localidad/";
    }

    @Override
    protected LocalidadDTO crearDtoNuevo() {
        return new LocalidadDTO();
    }

    @Override
    protected LocalidadDTO entidadADto(Localidad entidad) {
        return LocalidadDTO.fromEntity(entidad);
    }

    @Override
    protected void prepararDatosFormulario(Model model) throws Exception {
        // Cargar listas para los selects en cascada en el formulario
        model.addAttribute("paises", paisService.listarActivos());
        model.addAttribute("provincias", provinciaService.listarActivos());
        model.addAttribute("departamentos", departamentoServicio.listarActivos());
    }
}
