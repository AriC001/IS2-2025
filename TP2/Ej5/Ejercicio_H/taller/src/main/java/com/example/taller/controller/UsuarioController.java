package com.example.taller.controller;


import com.example.taller.entity.Usuario;
import com.example.taller.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioController extends BaseController<Usuario, String> {
    @Autowired
    public UsuarioController(UsuarioService service) {
        super(service);
        initController(new Usuario(), "Listado de Usuarios", "Editar Usuario");
    }
}