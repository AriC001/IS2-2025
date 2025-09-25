package com.sport.proyecto.controladores;


import com.sport.proyecto.entidades.Mensaje;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.enums.tipoMensaje;
import com.sport.proyecto.servicios.MensajeServicio;
import com.sport.proyecto.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mensaje")
public class MensajeControlador {

  @Autowired
  private MensajeServicio mensajeServicio;
  @Autowired
  private UsuarioServicio usuarioServicio;

  // Listado
  @GetMapping("")
  public String listar(Model model) {
    try{
      model.addAttribute("mensajes", mensajeServicio.listarMensajeActivo());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "views/mensajes";
  }

  // Form nuevo
  @GetMapping("/nuevo")
  public String nuevo(Model model) {
    model.addAttribute("mensaje", new Mensaje());
    model.addAttribute("tipos", tipoMensaje.values());
    model.addAttribute("usuarios", usuarioServicio.listarUsuarioActivo());
    return "views/mensaje-form";
  }

  // Crear
  @PostMapping("/crear")
  public String crear(@ModelAttribute Mensaje mensaje, RedirectAttributes ra) {
    try {
      mensajeServicio.crearMensaje(mensaje.getTitulo(), mensaje.getTexto(), mensaje.getTipoMensaje(), mensaje.getUsuario().getId());
      ra.addFlashAttribute("msg", "Mensaje creado correctamente.");
      return "redirect:/mensaje";
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
      return "redirect:/mensaje/nuevo";
    }
  }

  // Form editar
  @GetMapping("/editar/{id}")
  public String editar(@PathVariable String id, Model model, RedirectAttributes ra) {
    try {
      Mensaje mensaje = mensajeServicio.buscarMensaje(id);
      model.addAttribute("mensaje", mensaje);
      model.addAttribute("tipos", tipoMensaje.values());
      model.addAttribute("usuarios", usuarioServicio.listarUsuarioActivo());
      return "views/mensaje-form";
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
      return "redirect:/mensaje";
    }
  }

  // Actualizar
  @PostMapping("/actualizar/{id}")
  public String actualizar(@PathVariable String id, @ModelAttribute Mensaje mensaje, RedirectAttributes ra) {
    try {
      mensajeServicio.modificarMensaje(id, mensaje.getTitulo(), mensaje.getTexto(), mensaje.getTipoMensaje(), mensaje.getUsuario().getId());
      ra.addFlashAttribute("msg", "Mensaje actualizado.");
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/mensaje";
  }

  // Eliminar (lógica: baja)
  @GetMapping("/eliminar/{id}")
  public String eliminar(@PathVariable String id, RedirectAttributes ra) {
    try {
      mensajeServicio.eliminarMensaje(id);
      ra.addFlashAttribute("msg", "Mensaje eliminado.");
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/mensaje";
  }

  // Enviar (ejecuta envío a destinatarios)
  @GetMapping("/enviar/{id}")
  public String enviar(@PathVariable String id, RedirectAttributes ra) {
    try {
      mensajeServicio.enviarMensaje(id);
      ra.addFlashAttribute("msg", "Mensaje enviado.");
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/mensaje";
  }

  @ModelAttribute("usuariosession")
  public Usuario usuarioSession(HttpSession session) {
    return (Usuario) session.getAttribute("usuariosession");
  }

}
