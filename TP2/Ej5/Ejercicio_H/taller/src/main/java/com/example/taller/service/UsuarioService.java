package com.example.taller.service;


import com.example.taller.entity.Rol;
import com.example.taller.entity.Usuario;
import com.example.taller.error.ErrorServicio;
import com.example.taller.repository.MecanicoRepository;
import com.example.taller.repository.BaseRepository;
import com.example.taller.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService extends BaseService<Usuario, String> {

    public UsuarioService(BaseRepository<Usuario, String> repository) {
        super(repository);
    }

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    protected Usuario createEmpty() {
        Usuario u = new Usuario();
        u.setEliminado(false);
        return u;
    }

    @Override
    protected void validar(Usuario entidad) throws ErrorServicio {
        if (entidad == null) throw new ErrorServicio("Usuario nulo");
        if (entidad.getNombreUsuario() == null || entidad.getNombreUsuario().isBlank())
            throw new ErrorServicio("Nombre de usuario es obligatorio");
        if (entidad.getClave() == null || entidad.getClave().isBlank())
            throw new ErrorServicio("Clave es obligatoria");
        if (entidad.getClave().length() < 6) {
            throw new ErrorServicio("La clave debe tener al menos 6 caracteres");
        }
    }

    @Transactional
    public void registrar(String clave, String nombre, String clave2) throws ErrorServicio {
        if (clave == null || clave.isBlank() || clave2 == null || clave2.isBlank()) {
            throw new ErrorServicio("Las claves no pueden estar vacías");
        }
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves deben coincidir");
        }
    // Validaciones básicas ya implementadas en validar(entidad)
        if (usuarioRepository.findByNombreUsuario(nombre).isPresent()) {
            throw new ErrorServicio("El nombre de usuario ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombre);
        usuario.setClave(passwordEncoder.encode(clave));
        usuario.setRol(Rol.USER);
        usuario.setEliminado(false);

        validar(usuario);

        usuarioRepository.save(usuario);
    }

    // Nota: la lógica de UserDetails se centraliza ahora en CustomUserDetailsService.
}
