package com.is.biblioteca.controller.view;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.is.biblioteca.business.domain.entity.Usuario;
import com.is.biblioteca.business.logic.service.InicioAplicacionService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class InicioController {

	@Autowired
   	private InicioAplicacionService inicioAplicacionService;
	
    @GetMapping("/")
	public String index() {
    	
       //Creo el usuario por defecto	
 	   //try {	
 		//inicioAplicacionService.iniciarAplicacion();
 	   //}catch(Exception e) {}	
    	
	   return "index.html";
	}
    
    @GetMapping("/regresoPage")
	public String regreso(HttpSession session) {
    	
    	Usuario usuario = (Usuario) session.getAttribute("usuariosession");
 
    	if (usuario != null) {
				if (usuario.getRol().toString().equals("ADMIN")) {
					return "redirect:/admin/dashboard";
				}else {
					return "inicio.html";
				}
    	}else {
    	  return "redirect:/index.html";
    	}
    }
}
