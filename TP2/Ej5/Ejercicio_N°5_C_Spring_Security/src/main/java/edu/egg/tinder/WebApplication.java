package edu.egg.tinder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import edu.egg.tinder.entidades.Zona;
import edu.egg.tinder.repositorios.ZonaRepositorio;

@SpringBootApplication
public class WebApplication {

    //@Autowired
    //private UsuarioServicio usuarioServicio;

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
	@Bean
	public CommandLineRunner init(ZonaRepositorio zonaRepositorio) {
		return args -> {
			if (zonaRepositorio.count() == 0) {
				Zona z = Zona.builder()
						.nombre("Godoy Cruz")
						.descripcion("zona de mendoza")
						.activo(true)
						.build();
				zonaRepositorio.save(z);
			}
		};
	}

  //  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
  //      auth.userDetailsService(usuarioServicio).passwordEncoder(new BCryptPasswordEncoder());
  //  }

}
