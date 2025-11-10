package com.practica.ej1b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practica.ej1b.business.domain.dto.PaisDTO;
import com.practica.ej1b.business.domain.entity.Pais;
import com.practica.ej1b.business.logic.service.PaisService;

/**
 * Controlador para gestionar países.
 * Extiende BaseController para heredar operaciones CRUD comunes.
 */
@Controller
@RequestMapping("/pais")
public class PaisController extends BaseController<Pais, String, PaisDTO> {

    public PaisController(PaisService servicio) {
        super(servicio);
    }

    @Override
    protected String getNombreEntidad() {
        return "pais";
    }

    @Override
    protected String getNombreEntidadPlural() {
        return "paises";
    }

    @Override
    protected String getVistaBase() {
        return "pais/";
    }

    @Override
    protected PaisDTO crearDtoNuevo() {
        return new PaisDTO();
    }

    @Override
    protected PaisDTO entidadADto(Pais entidad) {
        return PaisDTO.fromEntity(entidad);
    }
}
