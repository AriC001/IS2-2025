package com.practica.nexora.ej6_e.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practica.nexora.ej6_e.business.domain.dto.UsuarioDTO;
import com.practica.nexora.ej6_e.business.domain.entity.Usuario;
import com.practica.nexora.ej6_e.business.logic.service.UsuarioService;
import com.practica.nexora.ej6_e.utils.mapper.UsuarioMapper;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController extends BaseController<Usuario, UsuarioDTO, Long> {

  public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
    super(usuarioService, usuarioMapper, "usuarios");
  }

  @Override
  protected UsuarioDTO createNewDTO() {
    return new UsuarioDTO();
  }

}
