package edu.egg.tinder;

import edu.egg.tinder.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApplication {

    //@Autowired
    //private UsuarioServicio usuarioServicio;

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

  //  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
  //      auth.userDetailsService(usuarioServicio).passwordEncoder(new BCryptPasswordEncoder());
  //  }

}
