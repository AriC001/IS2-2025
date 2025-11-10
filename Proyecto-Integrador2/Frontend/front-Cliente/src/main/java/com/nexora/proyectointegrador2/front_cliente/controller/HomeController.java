package  com.nexora.proyectointegrador2.front_cliente.controller;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
  /** 
  @GetMapping("/")
  public String home(HttpSession session) {
    // Si no hay sesión activa, redirigir al login
    if (session.getAttribute("token") == null) {
      return "redirect:/auth/login";
    }
    
    // Si hay sesión, redirigir al dashboard principal
    return "redirect:/dashboard";
  }

  @GetMapping("/dashboard")
  public String dashboard(HttpSession session, Model model) {
    // Verificar si hay sesión activa
    if (session.getAttribute("token") == null) {
      return "redirect:/auth/login";
    }
    
    // Pasar información del usuario al modelo para mostrar en la vista
    model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
    model.addAttribute("rol", session.getAttribute("rol"));
    
    // Mostrar dashboard principal (index.html)
    return "index";
  }
  **/


    // Landing pública
    @GetMapping("/")
    public String landing() {
        return "landing";  
    }

    // Dashboard protegido
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        if (session.getAttribute("token") == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
        model.addAttribute("rol", session.getAttribute("rol"));

        return "index";
    }

  
}

