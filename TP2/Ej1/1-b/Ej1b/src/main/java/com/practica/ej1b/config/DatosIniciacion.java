package com.practica.ej1b.config;

import com.practica.ej1b.business.persistence.repository.UsuarioRepositorio;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import com.practica.ej1b.business.logic.service.UsuarioServicio;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.practica.ej1b.business.domain.entity.Usuario;
import com.practica.ej1b.business.persistence.repository.PaisRepositorio;
import com.practica.ej1b.business.persistence.repository.ProvinciaRepositorio;
import com.practica.ej1b.business.persistence.repository.DepartamentoRepositorio;
import com.practica.ej1b.business.persistence.repository.LocalidadRepositorio;
import com.practica.ej1b.business.domain.entity.Pais;
import com.practica.ej1b.business.domain.entity.Provincia;
import com.practica.ej1b.business.domain.entity.Departamento;
import com.practica.ej1b.business.domain.entity.Localidad;

@Configuration
public class DatosIniciacion implements CommandLineRunner {
  @Autowired
  private UsuarioServicio usuarioServicio;
  @Autowired
  private UsuarioRepositorio usuarioRepositorio;
  @Autowired
  private PasswordEncoder encoder;
  @Autowired
  private PaisRepositorio paisRepositorio;
  @Autowired
  private ProvinciaRepositorio provinciaRepositorio;
  @Autowired
  private DepartamentoRepositorio departamentoRepositorio;
  @Autowired
  private LocalidadRepositorio localidadRepositorio;

  @Override
  public void run(String... args) throws Exception {
    // Usuario de ejemplo
    if (usuarioServicio.buscarUsuarioPorNombre("admin") == null) {
      Usuario admin = new Usuario();
      admin.setNombreUsuario("admin");
      admin.setContrasenia(encoder.encode("admin123"));
      admin.setNombre("Administrador");
      admin.setApellido("Principal");
      admin.setCorreoElectronico("admin@gmail.com");
      admin.setTelefono("123456789");
      admin.setEliminado(false);
      usuarioRepositorio.save(admin);
    }

// ================== DATOS GEOGRÁFICOS =====================
    if (paisRepositorio.findByNombreAndEliminadoFalse("Argentina") == null) {
      // PAÍS
      Pais argentina = Pais.builder().nombre("Argentina").eliminado(false).build();
      paisRepositorio.save(argentina);

      // PROVINCIAS
      Provincia mendoza = Provincia.builder().nombre("Mendoza").pais(argentina).eliminado(false).build();
      Provincia buenosAires = Provincia.builder().nombre("Buenos Aires").pais(argentina).eliminado(false).build();
      provinciaRepositorio.save(mendoza);
      provinciaRepositorio.save(buenosAires);

      // DEPARTAMENTOS Mendoza
      Departamento guaymallen = Departamento.builder().nombre("Guaymallén").provincia(mendoza).eliminado(false).build();
      Departamento godoyCruz = Departamento.builder().nombre("Godoy Cruz").provincia(mendoza).eliminado(false).build();
      departamentoRepositorio.save(guaymallen);
      departamentoRepositorio.save(godoyCruz);

      // DEPARTAMENTOS Buenos Aires
      Departamento laPlata = Departamento.builder().nombre("La Plata").provincia(buenosAires).eliminado(false).build();
      Departamento quilmes = Departamento.builder().nombre("Quilmes").provincia(buenosAires).eliminado(false).build();
      departamentoRepositorio.save(laPlata);
      departamentoRepositorio.save(quilmes);

      // LOCALIDADES Mendoza - Guaymallén
      localidadRepositorio.save(Localidad.builder().nombre("Villa Nueva").codigoPostal("5521").departamento(guaymallen).eliminado(false).build());
      localidadRepositorio.save(Localidad.builder().nombre("Dorrego").codigoPostal("5519").departamento(guaymallen).eliminado(false).build());
      localidadRepositorio.save(Localidad.builder().nombre("Bermejo").codigoPostal("5523").departamento(guaymallen).eliminado(false).build());

      // LOCALIDADES Mendoza - Godoy Cruz
      localidadRepositorio.save(Localidad.builder().nombre("Godoy Cruz Centro").codigoPostal("5501").departamento(godoyCruz).eliminado(false).build());
      localidadRepositorio.save(Localidad.builder().nombre("San Francisco del Monte").codigoPostal("5503").departamento(godoyCruz).eliminado(false).build());
      localidadRepositorio.save(Localidad.builder().nombre("Villa Hipódromo").codigoPostal("5505").departamento(godoyCruz).eliminado(false).build());

      // LOCALIDADES Buenos Aires - La Plata
      localidadRepositorio.save(Localidad.builder().nombre("La Plata Centro").codigoPostal("1900").departamento(laPlata).eliminado(false).build());
      localidadRepositorio.save(Localidad.builder().nombre("Tolosa").codigoPostal("1904").departamento(laPlata).eliminado(false).build());
      localidadRepositorio.save(Localidad.builder().nombre("Ringuelet").codigoPostal("1906").departamento(laPlata).eliminado(false).build());

      // LOCALIDADES Buenos Aires - Quilmes
      localidadRepositorio.save(Localidad.builder().nombre("Quilmes Centro").codigoPostal("1878").departamento(quilmes).eliminado(false).build());
      localidadRepositorio.save(Localidad.builder().nombre("Bernal").codigoPostal("1876").departamento(quilmes).eliminado(false).build());
      localidadRepositorio.save(Localidad.builder().nombre("Ezpeleta").codigoPostal("1882").departamento(quilmes).eliminado(false).build());
    }


  }
}
