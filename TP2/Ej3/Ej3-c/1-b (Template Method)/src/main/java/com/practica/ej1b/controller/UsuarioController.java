package com.practica.ej1b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.practica.ej1b.business.domain.dto.UsuarioDTO;
import com.practica.ej1b.business.domain.entity.Usuario;
import com.practica.ej1b.business.logic.service.UsuarioServicio;

import jakarta.servlet.http.HttpSession;

/**
 * Controlador para gestionar usuarios.
 * Extiende BaseController para heredar operaciones CRUD comunes.
 */
@Controller
@RequestMapping("/usuario")
public class UsuarioController extends BaseController<Usuario, String, UsuarioDTO> {

    public UsuarioController(UsuarioServicio servicio) {
        super(servicio);
    }

    @Override
    protected String getNombreEntidad() {
        return "usuario";
    }

    @Override
    protected String getNombreEntidadPlural() {
        return "usuarios";
    }

    @Override
    protected String getVistaBase() {
        return "usuario/";
    }

    @Override
    protected UsuarioDTO crearDtoNuevo() {
        return new UsuarioDTO();
    }

    @Override
    protected UsuarioDTO entidadADto(Usuario entidad) {
        return UsuarioDTO.fromEntity(entidad);
    }
    
    /**
     * Muestra el formulario de cambio de contraseña
     */
    @GetMapping("/cambiar-contrasenia")
    public String mostrarFormularioCambioContrasenia(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        return "usuario/password";
    }
    
    /**
     * Procesa el cambio de contraseña
     */
    @PostMapping("/actualizarContraseña/{id}")
    public String actualizarContrasenia(
            @PathVariable String id,
            @RequestParam String contraseniaActual,
            @RequestParam String nuevaContrasenia,
            @RequestParam String nuevaConstraseniaConfirmada,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            
            // Verificar que el usuario esté logueado
            if (usuario == null || !usuario.getId().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "No tiene permisos para realizar esta acción");
                return "redirect:/login";
            }
            
            // Validar que las nuevas contraseñas coincidan
            if (!nuevaContrasenia.equals(nuevaConstraseniaConfirmada)) {
                redirectAttributes.addFlashAttribute("error", "Las contraseñas nuevas no coinciden");
                return "redirect:/usuario/cambiar-contrasenia";
            }
            
            // Cambiar contraseña usando el servicio
            UsuarioServicio usuarioServicio = (UsuarioServicio) servicio;
            Usuario usuarioActualizado = usuarioServicio.cambiarContrasenia(id, contraseniaActual, nuevaContrasenia);
            
            // Actualizar usuario en sesión
            session.setAttribute("usuario", usuarioActualizado);
            
            redirectAttributes.addFlashAttribute("success", "Contraseña actualizada correctamente");
            return "redirect:/usuario/cambiar-contrasenia";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuario/cambiar-contrasenia";
        }
    }
    
    /**
     * Cierra la sesión del usuario y redirige al login
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // Invalidar la sesión
        if (session != null) {
            session.invalidate();
        }
        
        redirectAttributes.addFlashAttribute("success", "Sesión cerrada correctamente");
        return "redirect:/login";
    }
}
