package edu.egg.tinder.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.servicios.UsuarioServicio;
import edu.egg.tinder.servicios.ZonaServicio;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ZonaServicio zonaServicio;

    /////////////////////////////////////////////
    /////////////////////////////////////////////
    //////////////// VIEW: Login    /////////////
    /////////////////////////////////////////////
    /////////////////////////////////////////////

    @PostMapping("/registrar")
    public String registroUsuario(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam(required = false) MultipartFile archivo,
                                   @RequestParam String idZona, @RequestParam String clave1, @RequestParam String clave2, ModelMap modelo) throws ErrorServicio {
        try {
            usuarioServicio.registrar(archivo,nombre,apellido,mail,clave1,clave2);
            modelo.put("titulo", "Bienvenido al Tinder de Mascotas. ");
            modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria. ");
            return "exito";
        } catch (ErrorServicio e) {
        modelo.put("error", e.getMessage());
        modelo.put("nombre", nombre);
        modelo.put("apellido", apellido);
        modelo.put("mail", mail);
        modelo.put("clave1", clave1);
        modelo.put("clave2", clave2);
        // Volver a cargar la lista de zonas para que no falle el select
        modelo.put("zonas", zonaServicio.mostrarZonas());
            return "redirect:/registro";
        }
    }

    //@{/usuario/editar-perfil(id=__${session.usuariosession.id}__)}
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session,@RequestParam Long id, ModelMap model) throws ErrorServicio {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        model.put("perfil",login);
        model.put("zonas",zonaServicio.mostrarZonas());
        return "perfil";
        
    }

    @PostMapping("/actualizar-perfil")
    public String actualizarPerfil(@RequestParam Long id, @RequestParam String nombre,@RequestParam String apellido,@RequestParam String mail,@RequestParam(required = false) MultipartFile archivo,@RequestParam String clave1, @RequestParam String clave2) throws ErrorServicio {
        usuarioServicio.modificar(archivo,id,nombre,apellido,mail,clave1,clave2);
        return "redirect:/inicio";
    }

}
