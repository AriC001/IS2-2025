package com.example.taller;

import com.example.taller.entity.Rol;
import com.example.taller.entity.Usuario;
import com.example.taller.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Component that runs at application startup and ensures an ADMIN user exists.
 * Idempotent: if a user with nombreUsuario 'admin' already exists, it does nothing.
 */
@Component
public class DataInitiator implements CommandLineRunner {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		try {
			String adminUser = "admin";
			if (usuarioRepository.findByNombreUsuario(adminUser).isPresent()) {
				System.out.println("[DataInitiator] Admin user already exists, skipping creation.");
				return;
			}

			Usuario admin = new Usuario();
			admin.setNombreUsuario(adminUser);
			admin.setClave(passwordEncoder.encode("admin123")); // default password; recommend to change in prod
			admin.setRol(Rol.ADMIN);
			admin.setEliminado(false);

			usuarioRepository.save(admin);
			System.out.println("[DataInitiator] Created default admin user 'admin'.");
		} catch (Exception ex) {
			System.err.println("[DataInitiator] Error creating admin user: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
