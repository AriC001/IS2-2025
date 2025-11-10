package com.example.taller.controller;



import com.example.taller.entity.Cliente;
import com.example.taller.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/cliente")
public class ClienteController extends BaseController<Cliente, String> {

    public ClienteController(ClienteService service) {
        super(service);
        initController(new Cliente(), "Listado de Clientes", "Editar Cliente");
    }
}