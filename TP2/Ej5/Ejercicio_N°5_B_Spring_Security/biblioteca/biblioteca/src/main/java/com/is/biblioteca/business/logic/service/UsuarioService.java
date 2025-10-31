package com.is.biblioteca.business.logic.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.is.biblioteca.business.domain.entity.Usuario;
import com.is.biblioteca.business.domain.enumeration.Rol;
import com.is.biblioteca.business.logic.error.ErrorServiceException;
import com.is.biblioteca.business.persistence.repository.UsuarioRepository;

import jakarta.persistence.NoResultException;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
    private UsuarioRepository repository;


	@Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> opt = repository.buscarUsuarioPorEmail(email);
        if(!opt.isPresent()){
                throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
        }else{
            Usuario usuario = opt.get();
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
        
            // La sesión se maneja ahora en CustomAuthenticationSuccessHandler
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        }
    }
	
    public void validar(String nombre, String email, String clave, String confirmacion) throws ErrorServiceException {
      
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServiceException("Debe indicar el nombre");
        }
        
        if (email == null || email.trim().isEmpty()) {
            throw new ErrorServiceException("Debe indicar el Email");
        }
        
        if (clave == null || clave.trim().isEmpty()) {
            throw new ErrorServiceException("Debe indicar la clave");
        }
        
        if (confirmacion == null || confirmacion.trim().isEmpty()) {
            throw new ErrorServiceException("Debe indicar la confirmación de clave");
        }
        
        if (!clave.trim().equals(confirmacion.trim())) {
            throw new ErrorServiceException("La clave debe ser igual a su confirmación");
        }

    }
    
    @Transactional
    public Usuario crearUsuario(String nombre, String email, String clave, String confirmacion) throws ErrorServiceException {

      try {	
    	  
        validar(nombre, email, clave, confirmacion);
        validarEmailDuplicado(email);
        
        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID().toString());
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setRol(Rol.USER);
        usuario.setPassword(passwordEncoder.encode(clave));
        usuario.setEliminado(false);
        
        return repository.save(usuario);
        
      }catch(ErrorServiceException e) {  
		   throw e;  
	  }catch(Exception e) {
		   e.printStackTrace();
		   throw new ErrorServiceException("Error de Sistemas");
	  } 
    }

    private void validarEmailDuplicado(String email) throws ErrorServiceException {
        Optional<Usuario> opt = repository.buscarUsuarioPorEmail(email);
        if (opt.isPresent()) {
            throw new ErrorServiceException("Email ya registrado");
        }
    }

    @Transactional
    public Usuario modificarUsuario(String idUsuario, String nombre, String email, String clave, String confirmacion) throws ErrorServiceException {

    	try {
    		
    		validar(nombre, email, clave, confirmacion);
    		
            Usuario usuario = buscarUsuario(idUsuario);
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            //usuario.setRol(Rol.USER);
            usuario.setPassword(passwordEncoder.encode(clave));
            
            return repository.save(usuario);
            
    	}catch(ErrorServiceException e) {  
  		   throw e;  
  	  	}catch(Exception e) {
  		   e.printStackTrace();
  		   throw new ErrorServiceException("Error de Sistemas");
  	  	} 
    }
    
    @Transactional
    public void eliminarUsuario(String idUsuario) throws ErrorServiceException {

    	 try {		

            Usuario usuario = buscarUsuario(idUsuario);
            usuario.setEliminado(true);

            repository.save(usuario);
            
          }catch(ErrorServiceException e) {  
    		   throw e;  
    	  }catch(Exception e) {
    		   e.printStackTrace();
    		   throw new ErrorServiceException("Error de Sistemas");
    	  } 

    }

    @Transactional
    public Usuario findOrCreateOAuthUser(String provider, String providerId, String username, String name, String email) {
        System.out.println("HOLAAA 1");
            try {
                // 1) Buscar por provider+providerId (asociación preferida)
                Optional<Usuario> found = repository.findByProviderAndProviderId(provider, providerId);
                if (found.isPresent()) {
                    Usuario u = found.get();
                    boolean changed = false;
                    if (name != null && !name.equals(u.getNombre())) { u.setNombre(name); changed = true; }
                    if (email != null && u.getEmail()== null) { u.setEmail(email); changed = true; }
                    if (changed) { repository.save(u); }
                    return u;
                }

                // 2) Si no existe, intentar buscar por email (si se recibió) y asociar
                if (email != null && !email.trim().isEmpty()) {
                    Optional<Usuario> byEmail = repository.buscarUsuarioPorEmail(email);
                    if (byEmail.isPresent()) {
                        Usuario existing = byEmail.get();
                        // asociar provider info para futuros logins
                        existing.setProvider(provider);
                        existing.setProviderId(providerId);
                        return repository.save(existing);
                    }
                }

                // 3) Si no existe por provider ni por email, crear un usuario nuevo
                Usuario u = new Usuario();
                u.setId(username != null ? username : provider + "_" + providerId);
                u.setNombre(name != null ? name : username);
                u.setEmail(email);
                u.setProvider(provider);
                u.setProviderId(providerId);
                u.setRol(Rol.USER);
                u.setEliminado(false);
                return repository.save(u);
            } catch (Exception ex) {
                ex.printStackTrace();
                // en caso de error, lanzar unchecked para que el flujo de login falle y se pueda ver el error
                throw new RuntimeException("Error al crear/obtener usuario OAuth", ex);
            }
    }
    
    @Transactional
    public void cambiarRol(String idUsuario) throws ErrorServiceException {

    	 try {		

            Usuario usuario = buscarUsuario(idUsuario);
            
            if(usuario.getRol() == Rol.ADMIN)
              usuario.setRol(Rol.USER);
            else
              usuario.setRol(Rol.ADMIN);

            repository.save(usuario);
            
          }catch(ErrorServiceException e) {  
    		   throw e;  
    	  }catch(Exception e) {
    		   e.printStackTrace();
    		   throw new ErrorServiceException("Error de Sistemas");
    	  } 

    }
    
    @Transactional(readOnly=true)
    public Usuario buscarUsuario(String idUsuario) throws ErrorServiceException {
        System.out.println("HOLAAA 2");
    	try {
            
            if (idUsuario == null || idUsuario.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el usuario");
            }

            Optional<Usuario> optional = repository.findById(idUsuario);
            Usuario usuario = null;
            if (optional.isPresent()) {
            	usuario= optional.get();
    			if (usuario == null || usuario.isEliminado()){
                    throw new ErrorServiceException("No se encuentra el usuario indicado");
                }
    		}
            
            return usuario;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }
    
    public Usuario buscarUsuarioPorEmail (String email) throws ErrorServiceException {
        System.out.println("HOLAAA 3");
    	try {	
    		
    		if (email == null || email.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el email");
            }
    		
    		Optional<Usuario> opt = repository.buscarUsuarioPorEmail(email);
            if(opt.isPresent()){
                Usuario u = opt.get();
                return u;
            }else{
                 throw new ErrorServiceException("Error de sistema");
            }
        
    	 } catch (ErrorServiceException ex) {  
             throw ex;
         } catch (Exception ex) {
             ex.printStackTrace();
             throw new ErrorServiceException("Error de sistema");
         }
    }

    public Usuario buscarUsuarioPorNombre (String nombre) throws ErrorServiceException {
    	
    	try {	
    		
    		if (nombre == null || nombre.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }
    		
    		return repository.buscarUsuarioPorNombre(nombre);
        
    	 } catch (ErrorServiceException ex) {  
             throw ex;
         } catch (Exception ex) {
             ex.printStackTrace();
             throw new ErrorServiceException("Error de sistema");
         }
    }

    public List<Usuario> listarUsuario()throws ErrorServiceException {
 
	  try { 
		
        return repository.findAll();

      }catch(Exception e) {
   	   e.printStackTrace();
   	   throw new ErrorServiceException("Error de Sistemas");
      }
        
    }
    

    public Usuario login(String email, String clave) throws ErrorServiceException {
    	
    	try {
    		
    		if (email == null || email.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el usuario");
            }

            if (clave == null || clave.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar la clave");
            }
            
            Usuario usuario = null; 
            try {
             usuario = repository.buscarUsuarioPorEmailYClave(email, clave);
            } catch (NoResultException ex) {
            	throw new ErrorServiceException("No existe usuario para el correo y clave indicado");
            }
    		
            return usuario;
            
    	}catch(ErrorServiceException e) {  
         	throw e;  
        }catch(Exception e) {
         	e.printStackTrace();
         	throw new ErrorServiceException("Error de Sistemas");
        } 
    }

    

}
