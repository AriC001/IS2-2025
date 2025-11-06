package com.example.taller.service;


import com.example.taller.entity.Rol;
import com.example.taller.entity.Usuario;
import com.example.taller.error.ErrorServicio;
import com.example.taller.repository.MecanicoRepository;
import com.example.taller.repository.BaseRepository;
import com.example.taller.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService extends BaseService<Usuario, String> implements UserDetailsService {

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuario(username).get();

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getNombreUsuario(), usuario.getClave(), permisos);
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
    }
}
