package com.colmena.videojuegos;

import com.colmena.videojuegos.entities.Categoria;
import com.colmena.videojuegos.entities.Estudio;
import com.colmena.videojuegos.repositories.RepositorioCategoria;
import com.colmena.videojuegos.repositories.RepositorioEstudio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VideojuegosApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideojuegosApplication.class, args);
	}
	@Bean
	CommandLineRunner init(RepositorioCategoria categoriaRepositorio, RepositorioEstudio estudioRepositorio) {
		return args -> {
			if (categoriaRepositorio.count() == 0) {
				Categoria c = new Categoria();  // usa constructor por defecto
				c.setNombre("Accion");
				c.setActivo(true);
				categoriaRepositorio.save(c);
			}

			if (estudioRepositorio.count() == 0) {
				Estudio e = new Estudio();
				e.setNombre("Team Cherry");
				e.setActivo(true);
				estudioRepositorio.save(e);
			}
		};
	}


}
