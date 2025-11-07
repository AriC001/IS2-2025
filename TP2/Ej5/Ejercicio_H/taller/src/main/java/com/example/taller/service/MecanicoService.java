package com.example.taller.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.taller.entity.Mecanico;
import java.util.Optional;
import com.example.taller.entity.Usuario;
import com.example.taller.entity.Rol;
import com.example.taller.error.ErrorServicio;
import com.example.taller.repository.MecanicoRepository;
import com.example.taller.repository.UsuarioRepository;


@Service
public class MecanicoService extends BaseService<Mecanico, String> {

    private final MecanicoRepository mecanicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MecanicoService(MecanicoRepository repository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.mecanicoRepository = repository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Mecanico> findByUsuarioId(String usuarioId) {
        return mecanicoRepository.findByUsuarioId(usuarioId);
    }

    @Override
    protected Mecanico createEmpty() {
        Mecanico m = new Mecanico();
        m.setEliminado(false);
        return m;
    }

    @Override
    protected void preAlta(Mecanico entidad) throws ErrorServicio {
        System.out.println("[MecanicoService.preAlta] entidad before preAlta: " + entidad + " usuario: " + entidad.getUsuario());
        // If the form supplied a nested usuario without id, create it here
        Usuario u = entidad.getUsuario();
        if (u != null && (u.getId() == null || u.getId().isBlank())) {
            // validate username
            if (u.getNombreUsuario() == null || u.getNombreUsuario().isBlank()) {
                throw new ErrorServicio("Nombre de usuario para el mecánico es obligatorio");
            }
            if (u.getClave() == null || u.getClave().isBlank()) {
                throw new ErrorServicio("Clave para el usuario del mecánico es obligatoria");
            }
            if (usuarioRepository.findByNombreUsuario(u.getNombreUsuario()).isPresent()) {
                throw new ErrorServicio("El nombre de usuario ya existe");
            }

            // prepare and save user (without mecanico linkage yet)
            Usuario nuevo = new Usuario();
            nuevo.setNombreUsuario(u.getNombreUsuario());
            nuevo.setClave(passwordEncoder.encode(u.getClave()));
            nuevo.setRol(Rol.USER);
            nuevo.setEliminado(false);
            Usuario guardado = usuarioRepository.save(nuevo);

            System.out.println("[MecanicoService.preAlta] created Usuario id=" + guardado.getId());
            // set saved user into mecanico entity so when mecanico is saved it links to the user
            entidad.setUsuario(guardado);
        }
    }

    @Override
    protected void postAlta(Mecanico entidad) throws ErrorServicio {
        // After mecanico saved, ensure usuario.mecanico points back to mecanico (bidirectional sync)
        System.out.println("[MecanicoService.postAlta] entidad after save: " + entidad + " usuario: " + entidad.getUsuario());
        Usuario u = entidad.getUsuario();
        if (u != null) {
            // reload to avoid transient state
            usuarioRepository.findById(u.getId()).ifPresent(usuario -> {
                usuario.setMecanico(entidad);
                usuarioRepository.save(usuario);
                System.out.println("[MecanicoService.postAlta] linked Usuario id=" + usuario.getId() + " -> mecanico id=" + entidad.getId());
            });
        }
    }

    @Override
    protected void preModificacion(Mecanico entidad) throws ErrorServicio {
        // If admin provides a nested new usuario while editing, create it similarly
        Usuario u = entidad.getUsuario();
        if (u != null && (u.getId() == null || u.getId().isBlank())) {
            if (u.getNombreUsuario() == null || u.getNombreUsuario().isBlank()) {
                throw new ErrorServicio("Nombre de usuario para el mecánico es obligatorio");
            }
            if (u.getClave() == null || u.getClave().isBlank()) {
                throw new ErrorServicio("Clave para el usuario del mecánico es obligatoria");
            }
            if (usuarioRepository.findByNombreUsuario(u.getNombreUsuario()).isPresent()) {
                throw new ErrorServicio("El nombre de usuario ya existe");
            }

            Usuario nuevo = new Usuario();
            nuevo.setNombreUsuario(u.getNombreUsuario());
            nuevo.setClave(passwordEncoder.encode(u.getClave()));
            nuevo.setRol(Rol.USER);
            nuevo.setEliminado(false);
            Usuario guardado = usuarioRepository.save(nuevo);
            entidad.setUsuario(guardado);
        }
    }

    @Override
    protected void validar(Mecanico entidad) throws ErrorServicio {
        if (entidad == null) throw new ErrorServicio("Mecánico nulo");
        if (entidad.getLegajo() == null || entidad.getLegajo().isBlank())
            throw new ErrorServicio("Legajo es obligatorio");
    }
}