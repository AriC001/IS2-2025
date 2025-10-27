package com.is.biblioteca.controller.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.is.biblioteca.business.domain.entity.Libro;
import com.is.biblioteca.business.domain.entity.Prestamo;
import com.is.biblioteca.business.domain.entity.Usuario;
import com.is.biblioteca.business.logic.service.LibroService;
import com.is.biblioteca.business.logic.service.PrestamoService;
import com.is.biblioteca.business.logic.service.UsuarioService;

@Controller
@RequestMapping("/prestamo")
public class PrestamoController {

  @Autowired
  private PrestamoService prestamoService;

  @Autowired
  private LibroService libroService;

  @Autowired
  private UsuarioService usuarioService;

  //////////////////////////////////////////
  //////////////////////////////////////////
  ///////////// VIEW: LISTAR PRÉSTAMOS /////
  //////////////////////////////////////////
  //////////////////////////////////////////

  @GetMapping("/listar")
  public String listarPrestamos(ModelMap modelo) {
    try {
      List<Prestamo> prestamos = prestamoService.listarPrestamos();
      modelo.addAttribute("prestamos", prestamos);
    } catch (Exception e) {
      modelo.addAttribute("error", "Error al cargar la lista de préstamos: " + e.getMessage());
    }
    return "prestamo_list.html";
  }

  //////////////////////////////////////////
  //////////////////////////////////////////
  ///////// VIEW: MIS PRÉSTAMOS (USER) /////
  //////////////////////////////////////////
  //////////////////////////////////////////

  @GetMapping("/mis-prestamos")
  public String misPrestamos(ModelMap modelo) {
    try {
      // Obtener el usuario autenticado
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      String email = auth.getName(); // El username es el email
      
      // Buscar el usuario por email
      Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);
      
      if (usuario != null) {
        List<Prestamo> prestamos = prestamoService.listarPrestamosPorUsuario(usuario.getId());
        modelo.addAttribute("prestamos", prestamos);
        modelo.addAttribute("esUsuario", true); // Para ocultar columnas de admin
      } else {
        modelo.addAttribute("error", "Usuario no encontrado");
      }
    } catch (Exception e) {
      modelo.addAttribute("error", "Error al cargar sus préstamos: " + e.getMessage());
    }
    return "prestamo_list.html";
  }

  //////////////////////////////////////////
  //////////////////////////////////////////
  ///////////// VIEW: CREAR PRÉSTAMO ///////
  //////////////////////////////////////////
  //////////////////////////////////////////

  @GetMapping("/registrar")
  public String irEditAlta(ModelMap modelo) {
    try {
      List<Libro> libros = libroService.listarLibro();
      List<Usuario> usuarios = usuarioService.listarUsuario();

      modelo.addAttribute("libros", libros);
      modelo.addAttribute("usuarios", usuarios);
    } catch (Exception e) {
      modelo.addAttribute("error", e.getMessage());
    }
    return "prestamo_form.html";
  }

  @PostMapping("/registro")
  public String registro(ModelMap modelo,
                        @RequestParam String idLibro,
                        @RequestParam String idUsuario,
                        @RequestParam String fechaPrestamo,
                        @RequestParam String fechaDevolucion) {
    try {
      // Cargar listas para el formulario
      List<Libro> libros = libroService.listarLibro();
      List<Usuario> usuarios = usuarioService.listarUsuario();
      modelo.addAttribute("libros", libros);
      modelo.addAttribute("usuarios", usuarios);

      // Buscar el libro y el usuario
      Libro libro = libroService.buscarLibro(idLibro);
      Usuario usuario = usuarioService.buscarUsuario(idUsuario);

      // Parsear las fechas
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      Date fechaPrest = formatter.parse(fechaPrestamo);
      Date fechaDev = formatter.parse(fechaDevolucion);

      // Crear el préstamo
      prestamoService.crearPrestamo(usuario, libro, fechaPrest, fechaDev);

      modelo.addAttribute("exito", "Préstamo registrado exitosamente");

    } catch (Exception e) {
      modelo.addAttribute("error", "Error al registrar el préstamo: " + e.getMessage());
      
      // Recargar listas en caso de error
      try {
        List<Libro> libros = libroService.listarLibro();
        List<Usuario> usuarios = usuarioService.listarUsuario();
        modelo.addAttribute("libros", libros);
        modelo.addAttribute("usuarios", usuarios);
      } catch (Exception ex) {
        // Si falla la recarga de listas, solo mostramos el error
      }
    }

    return "prestamo_list.html";
  }

  //////////////////////////////////////////
  //////////////////////////////////////////
  ///////////// VIEW: MODIFICAR PRÉSTAMO ///
  //////////////////////////////////////////
  //////////////////////////////////////////

  @GetMapping("/modificar/{id}")
  public String irEditModificar(@PathVariable String id, ModelMap modelo) {
    try {
      Prestamo prestamo = prestamoService.buscarPrestamoPorId(id);
      List<Libro> libros = libroService.listarLibro();
      List<Usuario> usuarios = usuarioService.listarUsuario();

      modelo.addAttribute("prestamo", prestamo);
      modelo.addAttribute("libros", libros);
      modelo.addAttribute("usuarios", usuarios);
    } catch (Exception e) {
      modelo.addAttribute("error", e.getMessage());
    }

    return "prestamo_modificar.html";
  }

  @PostMapping("/modificar/{id}")
  public String modificarPrestamo(@PathVariable String id,
                                 ModelMap modelo,
                                 @RequestParam String idLibro,
                                 @RequestParam String idUsuario,
                                 @RequestParam String fechaPrestamo,
                                 @RequestParam String fechaDevolucion) {
    try {
      // Buscar el libro y el usuario
      Libro libro = libroService.buscarLibro(idLibro);
      Usuario usuario = usuarioService.buscarUsuario(idUsuario);

      // Parsear las fechas
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      Date fechaPrest = formatter.parse(fechaPrestamo);
      Date fechaDev = formatter.parse(fechaDevolucion);

      // Actualizar el préstamo
      prestamoService.actualizarPrestamo(id, usuario, libro, fechaPrest, fechaDev);

      modelo.addAttribute("exito", "Préstamo modificado exitosamente");
      
      // Recargar datos
      Prestamo prestamo = prestamoService.buscarPrestamoPorId(id);
      List<Libro> libros = libroService.listarLibro();
      List<Usuario> usuarios = usuarioService.listarUsuario();
      
      modelo.addAttribute("prestamo", prestamo);
      modelo.addAttribute("libros", libros);
      modelo.addAttribute("usuarios", usuarios);

    } catch (Exception e) {
      modelo.addAttribute("error", "Error al modificar el préstamo: " + e.getMessage());
      
      try {
        Prestamo prestamo = prestamoService.buscarPrestamoPorId(id);
        List<Libro> libros = libroService.listarLibro();
        List<Usuario> usuarios = usuarioService.listarUsuario();
        
        modelo.addAttribute("prestamo", prestamo);
        modelo.addAttribute("libros", libros);
        modelo.addAttribute("usuarios", usuarios);
      } catch (Exception ex) {
        // Si falla la recarga de datos, solo mostramos el error
      }
    }

    return "prestamo_list.html";
  }

  //////////////////////////////////////////
  //////////////////////////////////////////
  ///////////// VIEW: ELIMINAR PRÉSTAMO ////
  //////////////////////////////////////////
  //////////////////////////////////////////

  @GetMapping("/eliminar/{id}")
  public String eliminarPrestamo(@PathVariable String id, ModelMap modelo) {
    try {
      prestamoService.eliminarPrestamo(id);
      modelo.addAttribute("exito", "Préstamo eliminado exitosamente");
    } catch (Exception e) {
      modelo.addAttribute("error", "Error al eliminar el préstamo: " + e.getMessage());
    }
    
    // Recargar lista
    try {
      List<Prestamo> prestamos = prestamoService.listarPrestamos();
      modelo.addAttribute("prestamos", prestamos);
    } catch (Exception e) {
      modelo.addAttribute("error", "Error al cargar la lista de préstamos");
    }
    
    return "prestamo_list.html";
  }
}
