package com.practica.ej1b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practica.ej1b.business.domain.dto.ProvinciaDTO;
import com.practica.ej1b.business.domain.entity.Provincia;
import com.practica.ej1b.business.logic.service.PaisService;
import com.practica.ej1b.business.logic.service.ProvinciaService;

/**
 * Controlador para gestionar provincias.
 * Extiende BaseController para heredar operaciones CRUD comunes.
 */
@Controller
@RequestMapping("/provincia")
public class ProvinciaController extends BaseController<Provincia, String, ProvinciaDTO> {

    private final PaisService paisService;

    public ProvinciaController(ProvinciaService servicio, PaisService paisService) {
        super(servicio);
        this.paisService = paisService;
    }

    @Override
    protected String getNombreEntidad() {
        return "provincia";
    }

    @Override
    protected String getNombreEntidadPlural() {
        return "provincias";
    }

    @Override
    protected String getVistaBase() {
        return "provincia/";
    }

    @Override
    protected ProvinciaDTO crearDtoNuevo() {
        return new ProvinciaDTO();
    }

    @Override
    protected ProvinciaDTO entidadADto(Provincia entidad) {
        return ProvinciaDTO.fromEntity(entidad);
    }

    @Override
    protected void prepararDatosFormulario(Model model) throws Exception {
        // Cargar lista de países para el select en el formulario
        model.addAttribute("paises", paisService.listarActivos());
    }
}
