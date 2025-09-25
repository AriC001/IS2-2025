package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.Promocion;
import com.sport.proyecto.servicios.PromocionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/promocion")
public class PromocionControlador {

  @Autowired
  private PromocionServicio promocionServicio;

  @GetMapping({"", "/"})
  public String listar(Model model) {
    model.addAttribute("promociones", promocionServicio.listarPromocionActiva());
    return "promociones"; // templates/promociones.html
  }

  @GetMapping("/nueva")
  public String nueva(Model model) {
    model.addAttribute("promocion", new Promocion());
    return "promocion-form"; // templates/promocion-form.html
  }

  @PostMapping("/crear")
  public String crear(@ModelAttribute Promocion promocion, RedirectAttributes ra) {
    try {
      promocionServicio.crearPromocion(promocion.getFechaEnvioPromocion(), promocion.getTitulo(),
        promocion.getTexto(), promocion.getUsuario().getId());
      ra.addFlashAttribute("msg", "Promoción creada correctamente.");
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
      return "redirect:/promocion/nueva";
    }
    return "redirect:/promocion";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable String id, Model model, RedirectAttributes ra) {
    try {
      Promocion p = promocionServicio.buscarPromocion(id);
      model.addAttribute("promocion", p);
      return "promocion-form";
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
      return "redirect:/promocion";
    }
  }

  @PostMapping("/actualizar/{id}")
  public String actualizar(@PathVariable String id, @ModelAttribute Promocion promocion, RedirectAttributes ra) {
    try {
      promocionServicio.modificarPromocion(id, promocion.getFechaEnvioPromocion(), promocion.getTitulo(),
        promocion.getTexto(), promocion.getUsuario().getId());
      ra.addFlashAttribute("msg", "Promoción actualizada.");
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/promocion";
  }

  @GetMapping("/eliminar/{id}")
  public String eliminar(@PathVariable String id, RedirectAttributes ra) {
    try {
      promocionServicio.eliminarPromocion(id);
      ra.addFlashAttribute("msg", "Promoción eliminada.");
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/promocion";
  }

  /*@GetMapping("/enviar/{id}")
  public String enviar(@PathVariable String id, RedirectAttributes ra) {
    try {
      promocionServicio.enviarPromocion(id);
      ra.addFlashAttribute("msg", "Promoción enviada a los socios.");
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/promocion";
  }*/

}
