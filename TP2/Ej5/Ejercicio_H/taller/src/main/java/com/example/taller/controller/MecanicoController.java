package com.example.taller.controller;



import com.example.taller.entity.Mecanico;
import com.example.taller.service.MecanicoService;
import com.example.taller.service.UsuarioService;
import com.example.taller.entity.Usuario;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mecanico")
public class MecanicoController extends BaseController<Mecanico, String> {
    private final UsuarioService usuarioService;

    public MecanicoController(MecanicoService service, UsuarioService usuarioService) {
        super(service);
        this.usuarioService = usuarioService;
        initController(new Mecanico(), "Listado de Mecánicos", "Editar Mecánico");
    }

    @Override
    protected void preAlta() throws com.example.taller.error.ErrorServicio {
        // pasar la lista de usuarios activos sin mecanico asignado para elegir uno
        List<Usuario> todos = usuarioService.listarActivos();
        List<Usuario> disponibles = todos.stream()
                .filter(u -> u.getMecanico() == null)
                .collect(Collectors.toList());
        this.model.addAttribute("usuarios", disponibles);
    }

    @Override
    protected void preModificacion() throws com.example.taller.error.ErrorServicio {
        List<Usuario> todos = usuarioService.listarActivos();
        // incluir usuarios sin mecanico o el usuario actualmente asignado al mecanico
        List<Usuario> candidatos = todos.stream()
                .filter(u -> u.getMecanico() == null || (this.entity != null && this.entity.getUsuario() != null && u.getId().equals(this.entity.getUsuario().getId())))
                .collect(Collectors.toList());
        this.model.addAttribute("usuarios", candidatos);
    }
}
