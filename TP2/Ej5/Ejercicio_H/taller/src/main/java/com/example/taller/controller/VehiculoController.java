package com.example.taller.controller;


import com.example.taller.entity.Vehiculo;
import com.example.taller.error.ErrorServicio;
import com.example.taller.service.VehiculoService;
import com.example.taller.service.ClienteService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vehiculo")
public class VehiculoController extends BaseController<Vehiculo, String> {

    private final ClienteService clienteService;

    public VehiculoController(VehiculoService service, ClienteService clienteService) {
        super(service);
        this.clienteService = clienteService;
        initController(new Vehiculo(), "Listado de Vehículos", "Editar Vehículo");
    }

    @Override
    protected void preAlta() throws ErrorServicio {
        this.model.addAttribute("clientes", clienteService.listarActivos());
    }

    @Override
    protected void preModificacion() throws ErrorServicio {
        this.model.addAttribute("clientes", clienteService.listarActivos());
    }
}