package com.practica.ej1b.business.logic.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.practica.ej1b.business.domain.dto.UsuarioDTO;
import com.practica.ej1b.business.domain.entity.Usuario;
import com.practica.ej1b.business.persistence.repository.UsuarioRepositorio;

import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar usuarios.
 * Implementa Template Method mediante BaseService.
 * Gestiona conversión de UsuarioDTO a Usuario y encriptación de contraseñas.
 */
@Service
public class UsuarioServicio extends BaseService<Usuario, String, UsuarioDTO> {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioServicio(UsuarioRepositorio repositorio, PasswordEncoder passwordEncoder) {
        super(repositorio);
        this.usuarioRepositorio = repositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected String getNombreEntidad() {
        return "Usuario";
    }

    /**
     * Valida los datos del usuario.
     * Hook method que verifica campos obligatorios.
     */
    @Override
    protected void validar(Usuario usuario) throws Exception {
        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().trim().isEmpty()) {
            throw new Exception("El nombre de usuario es obligatorio");
        }
        
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre es obligatorio");
        }
        
        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
            throw new Exception("El apellido es obligatorio");
        }
        
        if (usuario.getCorreoElectronico() == null || usuario.getCorreoElectronico().trim().isEmpty()) {
            throw new Exception("El correo electrónico es obligatorio");
        }
    }

    /**
     * Convierte un UsuarioDTO a una entidad Usuario para crear.
     * Hook method que implementa la conversión DTO -> Entidad.
     * 
     * @param dto el UsuarioDTO con los datos
     * @return la entidad Usuario creada desde el DTO
     * @throws Exception si ocurre un error en la conversión
     */
    @Override
    protected Usuario dtoAEntidad(UsuarioDTO dto) throws Exception {
        // Verificar que no exista un usuario con el mismo nombreUsuario
        Usuario existente = usuarioRepositorio.findUsuarioByNombreUsuarioAndEliminadoFalse(dto.getNombreUsuario());
        if (existente != null) {
            throw new Exception("Ya existe un usuario con el nombre de usuario: " + dto.getNombreUsuario());
        }
        
        // Crear nueva entidad Usuario
        Usuario usuario = new Usuario();
        
        // Mapear campos de PersonaDTO (heredados)
        usuario.setId(dto.getId());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setTelefono(dto.getTelefono());
        usuario.setCorreoElectronico(dto.getCorreoElectronico());
        
        // Mapear campos específicos de UsuarioDTO
        usuario.setNombreUsuario(dto.getNombreUsuario());
        
        // Encriptar contraseña
        if (dto.getContrasenia() != null && !dto.getContrasenia().trim().isEmpty()) {
            usuario.setContrasenia(passwordEncoder.encode(dto.getContrasenia()));
        } else {
            throw new Exception("La contraseña es obligatoria al crear un usuario");
        }
        
        return usuario;
    }

    /**
     * Mapea los datos de un UsuarioDTO a una entidad Usuario existente para modificar.
     * Hook method que actualiza los campos de la entidad desde el DTO.
     * 
     * @param usuarioExistente la entidad Usuario existente en la BD
     * @param dto el UsuarioDTO con los nuevos datos
     * @return la entidad Usuario con los datos actualizados
     * @throws Exception si ocurre un error en el mapeo
     */
    @Override
    protected Usuario mapearDatosDto(Usuario usuarioExistente, UsuarioDTO dto) throws Exception {
        // Verificar si se está cambiando el nombreUsuario y si ya existe
        if (!usuarioExistente.getNombreUsuario().equals(dto.getNombreUsuario())) {
            Usuario existente = usuarioRepositorio.findUsuarioByNombreUsuarioAndEliminadoFalse(dto.getNombreUsuario());
            if (existente != null) {
                throw new Exception("Ya existe un usuario con el nombre de usuario: " + dto.getNombreUsuario());
            }
        }
        
        // Mapear campos de PersonaDTO (heredados)
        usuarioExistente.setNombre(dto.getNombre());
        usuarioExistente.setApellido(dto.getApellido());
        usuarioExistente.setTelefono(dto.getTelefono());
        usuarioExistente.setCorreoElectronico(dto.getCorreoElectronico());
        
        // Mapear campos específicos de UsuarioDTO
        usuarioExistente.setNombreUsuario(dto.getNombreUsuario());
        
        // Solo actualizar contraseña si se proporciona una nueva (no nula y no vacía)
        if (dto.getContrasenia() != null && !dto.getContrasenia().trim().isEmpty()) {
            usuarioExistente.setContrasenia(passwordEncoder.encode(dto.getContrasenia()));
        }
        // Si la contraseña es null o vacía, mantener la contraseña existente
        
        return usuarioExistente;
    }
    
    /**
     * Busca un usuario por su nombre de usuario.
     * 
     * @param nombreUsuario el nombre de usuario a buscar
     * @return el usuario encontrado
     * @throws Exception si no se encuentra el usuario
     */
    @Transactional
    public Usuario buscarPorNombreUsuario(String nombreUsuario) throws Exception {
        Usuario usuario = usuarioRepositorio.findUsuarioByNombreUsuarioAndEliminadoFalse(nombreUsuario);
        if (usuario == null) {
            return null;
        }
        return usuario;
    }

    /**
     * Busca un usuario por su nombre de usuario (método legacy).
     * 
     * @param nombreUsuario el nombre de usuario
     * @return el usuario encontrado
     * @throws Exception si no se encuentra el usuario
     */
    @Transactional
    public Usuario buscarUsuarioPorNombre(String nombreUsuario) throws Exception {
        return buscarPorNombreUsuario(nombreUsuario);
    }
    
    /**
     * Cambia la contraseña de un usuario.
     * 
     * @param usuarioId el ID del usuario
     * @param contraseniaActual la contraseña actual del usuario
     * @param nuevaContrasenia la nueva contraseña
     * @return el usuario actualizado
     * @throws Exception si la contraseña actual es incorrecta o hay un error
     */
    @Transactional
    public Usuario cambiarContrasenia(String usuarioId, String contraseniaActual, String nuevaContrasenia) throws Exception {
        // Buscar el usuario
        Usuario usuario = buscar(usuarioId);
        
        // Verificar la contraseña actual
        if (!passwordEncoder.matches(contraseniaActual, usuario.getContrasenia())) {
            throw new Exception("La contraseña actual es incorrecta");
        }
        
        // Validar la nueva contraseña
        if (nuevaContrasenia == null || nuevaContrasenia.trim().isEmpty()) {
            throw new Exception("La nueva contraseña no puede estar vacía");
        }
        
        if (nuevaContrasenia.length() < 6) {
            throw new Exception("La contraseña debe tener al menos 6 caracteres");
        }
        
        // Actualizar la contraseña
        usuario.setContrasenia(passwordEncoder.encode(nuevaContrasenia));
        return usuarioRepositorio.save(usuario);
    }
}
