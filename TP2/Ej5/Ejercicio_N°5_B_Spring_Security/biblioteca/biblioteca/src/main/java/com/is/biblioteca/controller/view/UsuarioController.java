package com.is.biblioteca.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.is.biblioteca.business.domain.entity.Usuario;
import com.is.biblioteca.business.logic.error.ErrorServiceException;
import com.is.biblioteca.business.logic.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	//////////////////////////////////////////
	//////////////////////////////////////////
	////////////// VIEW: LOGIN ///////////////
	//////////////////////////////////////////
	//////////////////////////////////////////

	@GetMapping("/login")
	public String login(@RequestParam(required = false) String error, ModelMap modelo) {

		if (error != null) {
			modelo.put("error", "Usuario o Contraseña invalidos!");
		}

		return "login.html";
	}

	//@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@PostMapping("/login")
	public String inicio(@RequestParam String email, @RequestParam(value = "password") String clave,
			HttpSession session, ModelMap modelo) {

		try {

			/*
			 * El usuario se loguea mediante la clase UserDatail que se
			 * implementa en el paquete biblioteca.security.
			 * 
				//Usuario usuario = usuarioService.login(email, clave);
				//session.setAttribute("usuariosession", usuario);
			 * 
             */
			
			Usuario usuario = (Usuario)session.getAttribute("usuariosession");
			
			if (usuario.getRol().toString().equals("ADMIN")) {
				return "redirect:/admin/dashboard";
			}

			return "inicio.html";

		} catch (Exception e) {
			e.printStackTrace();
			modelo.put("error", e.getMessage());
			return "login.html";
		}
	}

	@GetMapping("/logout")
	public String salir(HttpSession session) {
		session.setAttribute("usuariosession", null);
		return "index.html";
	}

	//////////////////////////////////////////
	//////////////////////////////////////////
	//////////// VIEW: CREAR USUARIO /////////
	//////////////////////////////////////////
	//////////////////////////////////////////

	@GetMapping("/registrar")
	public String irEditAlta() {
		return "registro.html";
	}

	@PostMapping("/registro")
	public String aceptarEditAlta(@RequestParam(required = false) String nombre, 
								   @RequestParam(required = false) String email,
								   @RequestParam(required = false) String password, 
								   @RequestParam(required = false) String password2, 
								   ModelMap modelo) {

		try {
			
			// Debug: imprimir valores recibidos
			System.out.println("Nombre recibido: [" + nombre + "]");
			System.out.println("Email recibido: [" + email + "]");
			System.out.println("Password recibido: [" + (password != null ? "***" : "null") + "]");
			System.out.println("Password2 recibido: [" + (password2 != null ? "***" : "null") + "]");
			usuarioService.crearUsuario(nombre, email, password, password2);

			modelo.put("exito", "Usuario registrado correctamente!");

			return "index.html";

		} catch (ErrorServiceException ex) {

			modelo.put("error", ex.getMessage());
			modelo.put("nombre", nombre);
			modelo.put("email", email);

			return "registro.html";
		}

	}

	//////////////////////////////////////////
	//////////////////////////////////////////
	///////// VIEW: MODIFICAR USUARIO //////// 
	//////////////////////////////////////////
	//////////////////////////////////////////

	@GetMapping("/perfil")
	public String irEditModificar(ModelMap modelo, HttpSession session) {

		Usuario usuario = (Usuario) session.getAttribute("usuariosession");
		modelo.put("usuario", usuario);

		return "usuario_modificar.html";
	}
	
	@GetMapping("/perfil/{id}")
	public String irEditModificar(ModelMap modelo, @PathVariable String id) {

	  try {	
		  
		Usuario usuario = usuarioService.buscarUsuario(id);
		modelo.put("usuario", usuario);
		
		return "usuario_modificar.html";
		
	  } catch (ErrorServiceException e) {
          modelo.put("error", e.getMessage());
          return "usuario_list";
      } catch (Exception e) {
          modelo.put("error", "Error de Sistemas");
          return "usuario_list";
      }	
	}

	@PostMapping("/perfil/{id}")
	public String irEditModificar(@PathVariable String id, @RequestParam String nombre,
			@RequestParam String email, @RequestParam String password, @RequestParam String password2,
			ModelMap modelo) throws ErrorServiceException {

		try {

			usuarioService.modificarUsuario(id, nombre, email, password, password2);

			return "redirect:/regresoPage";

		} catch (ErrorServiceException ex) {
			
			Usuario usuario = usuarioService.buscarUsuario(id);
			modelo.put("usuario", usuario);
			
			modelo.put("error", ex.getMessage());
			modelo.put("nombre", nombre);
			modelo.put("email", email);

			return "usuario_modificar.html";
		}

	}

}
