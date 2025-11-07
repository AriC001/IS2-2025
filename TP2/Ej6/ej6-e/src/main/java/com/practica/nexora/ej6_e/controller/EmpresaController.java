package com.practica.nexora.ej6_e.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practica.nexora.ej6_e.business.domain.dto.EmpresaDTO;
import com.practica.nexora.ej6_e.business.domain.entity.Empresa;
import com.practica.nexora.ej6_e.business.logic.service.EmpresaService;
import com.practica.nexora.ej6_e.utils.mapper.EmpresaMapper;

@Controller
@RequestMapping("/empresas")
public class EmpresaController extends BaseController<Empresa, EmpresaDTO, Long> {

  public EmpresaController(EmpresaService empresaService, EmpresaMapper empresaMapper) {
    super(empresaService, empresaMapper, "empresas");
  }

  @Override
  protected EmpresaDTO createNewDTO() {
    return new EmpresaDTO();
  }

}
