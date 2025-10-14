package com.practica.ej1b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practica.ej1b.business.domain.dto.DireccionDTO;
import com.practica.ej1b.business.domain.entity.Direccion;
import com.practica.ej1b.business.logic.service.DepartamentoServicio;
import com.practica.ej1b.business.logic.service.DireccionServicio;
import com.practica.ej1b.business.logic.service.LocalidadServicio;
import com.practica.ej1b.business.logic.service.PaisService;
import com.practica.ej1b.business.logic.service.ProvinciaService;

/**
 * Controlador para gestionar direcciones.
 * Extiende BaseController para heredar operaciones CRUD comunes.
 */
@Controller
@RequestMapping("/direccion")
public class DireccionController extends BaseController<Direccion, String, DireccionDTO> {

    private final PaisService paisService;
    private final ProvinciaService provinciaService;
    private final DepartamentoServicio departamentoServicio;
    private final LocalidadServicio localidadServicio;

    public DireccionController(DireccionServicio servicio, PaisService paisService, 
                              ProvinciaService provinciaService, DepartamentoServicio departamentoServicio,
                              LocalidadServicio localidadServicio) {
        super(servicio);
        this.paisService = paisService;
        this.provinciaService = provinciaService;
        this.departamentoServicio = departamentoServicio;
        this.localidadServicio = localidadServicio;
    }

    @Override
    protected String getNombreEntidad() {
        return "direccion";
    }

    @Override
    protected String getNombreEntidadPlural() {
        return "direcciones";
    }

    @Override
    protected String getVistaBase() {
        return "direccion/";
    }

    @Override
    protected DireccionDTO crearDtoNuevo() {
        return new DireccionDTO();
    }

    @Override
    protected DireccionDTO entidadADto(Direccion entidad) {
        return DireccionDTO.fromEntity(entidad);
    }

    @Override
    protected void prepararDatosFormulario(Model model) throws Exception {
        // Cargar listas para los selects en cascada en el formulario
        model.addAttribute("paises", paisService.listarActivos());
        model.addAttribute("provincias", provinciaService.listarActivos());
        model.addAttribute("departamentos", departamentoServicio.listarActivos());
        model.addAttribute("localidades", localidadServicio.listarActivos());
    }
}
