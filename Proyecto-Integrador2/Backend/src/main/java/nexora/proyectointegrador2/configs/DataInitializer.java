package nexora.proyectointegrador2.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import nexora.proyectointegrador2.business.domain.entity.Usuario;
import nexora.proyectointegrador2.business.enums.RolUsuario;
import nexora.proyectointegrador2.business.persistence.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

  private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

  @Bean
  public CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, 
                                        PasswordEncoder passwordEncoder) {
    return args -> {
      // Verificar si ya existe un usuario admin
      if (usuarioRepository.findByNombreUsuario("admin").isEmpty()) {
        Usuario admin = Usuario.builder()
            .nombreUsuario("admin")
            .clave(passwordEncoder.encode("admin123"))
            .rol(RolUsuario.JEFE)
            .build();
        admin.setEliminado(false);
        
        usuarioRepository.save(admin);
        logger.info("✅ Usuario JEFE creado exitosamente");
        logger.info("   Usuario: admin");
        logger.info("   Contraseña: admin123");
        logger.info("   Rol: JEFE");
      } else {
        logger.info("✓ Usuario admin ya existe en la base de datos");
      }

      // Crear usuario administrativo si no existe
      if (usuarioRepository.findByNombreUsuario("operador").isEmpty()) {
        Usuario operador = Usuario.builder()
            .nombreUsuario("operador")
            .clave(passwordEncoder.encode("operador123"))
            .rol(RolUsuario.ADMINISTRATIVO)
            .build();
        operador.setEliminado(false);
        
        usuarioRepository.save(operador);
        logger.info("✅ Usuario ADMINISTRATIVO creado exitosamente");
        logger.info("   Usuario: operador");
        logger.info("   Contraseña: operador123");
        logger.info("   Rol: ADMINISTRATIVO");
      } else {
        logger.info("✓ Usuario operador ya existe en la base de datos");
      }
      
    };
  }

}
