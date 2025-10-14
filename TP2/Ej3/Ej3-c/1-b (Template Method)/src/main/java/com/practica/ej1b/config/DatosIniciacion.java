package com.practica.ej1b.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.practica.ej1b.business.domain.entity.Departamento;
import com.practica.ej1b.business.domain.entity.Localidad;
import com.practica.ej1b.business.domain.entity.Pais;
import com.practica.ej1b.business.domain.entity.Provincia;
import com.practica.ej1b.business.domain.entity.Usuario;
import com.practica.ej1b.business.logic.service.UsuarioServicio;
import com.practica.ej1b.business.persistence.repository.DepartamentoRepositorio;
import com.practica.ej1b.business.persistence.repository.LocalidadRepositorio;
import com.practica.ej1b.business.persistence.repository.PaisRepositorio;
import com.practica.ej1b.business.persistence.repository.ProvinciaRepositorio;
import com.practica.ej1b.business.persistence.repository.UsuarioRepositorio;

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
      Pais argentina = Pais.builder().nombre("Argentina").build();
      argentina.setEliminado(false);
      paisRepositorio.save(argentina);

      // PROVINCIAS
      Provincia mendoza = Provincia.builder().nombre("Mendoza").pais(argentina).build();
      mendoza.setEliminado(false);
      Provincia buenosAires = Provincia.builder().nombre("Buenos Aires").pais(argentina).build();
      buenosAires.setEliminado(false);
      provinciaRepositorio.save(mendoza);
      provinciaRepositorio.save(buenosAires);

      // DEPARTAMENTOS Mendoza
      Departamento guaymallen = Departamento.builder().nombre("Guaymallén").provincia(mendoza).build();
      guaymallen.setEliminado(false);
      Departamento godoyCruz = Departamento.builder().nombre("Godoy Cruz").provincia(mendoza).build();
      godoyCruz.setEliminado(false);
      departamentoRepositorio.save(guaymallen);
      departamentoRepositorio.save(godoyCruz);

      // DEPARTAMENTOS Buenos Aires
      Departamento laPlata = Departamento.builder().nombre("La Plata").provincia(buenosAires).build();
      laPlata.setEliminado(false);
      Departamento quilmes = Departamento.builder().nombre("Quilmes").provincia(buenosAires).build();
      quilmes.setEliminado(false);
      departamentoRepositorio.save(laPlata);
      departamentoRepositorio.save(quilmes);

      // LOCALIDADES Mendoza - Guaymallén
      Localidad villaNueva = Localidad.builder().nombre("Villa Nueva").codigoPostal("5521").departamento(guaymallen).build();
      villaNueva.setEliminado(false);
      localidadRepositorio.save(villaNueva);

      Localidad dorrego = Localidad.builder().nombre("Dorrego").codigoPostal("5519").departamento(guaymallen).build();
      dorrego.setEliminado(false);
      localidadRepositorio.save(dorrego);
      Localidad bermejo = Localidad.builder().nombre("Bermejo").codigoPostal("5523").departamento(guaymallen).build();
      bermejo.setEliminado(false);
      localidadRepositorio.save(bermejo);

      // LOCALIDADES Mendoza - Godoy Cruz
      Localidad godoyCruzCentro = Localidad.builder().nombre("Godoy Cruz Centro").codigoPostal("5501").departamento(godoyCruz).build();
      godoyCruzCentro.setEliminado(false);
      localidadRepositorio.save(godoyCruzCentro);
      Localidad sanFranciscoDelMonte = Localidad.builder().nombre("San Francisco del Monte").codigoPostal("5503").departamento(godoyCruz).build();
      sanFranciscoDelMonte.setEliminado(false);
      localidadRepositorio.save(sanFranciscoDelMonte);
      Localidad villaHipodromo = Localidad.builder().nombre("Villa Hipódromo").codigoPostal("5505").departamento(godoyCruz).build();
      villaHipodromo.setEliminado(false);
      localidadRepositorio.save(villaHipodromo);

      // LOCALIDADES Buenos Aires - La Plata
      Localidad laPlataCentro = Localidad.builder().nombre("La Plata Centro").codigoPostal("1900").departamento(laPlata).build();
      laPlataCentro.setEliminado(false);
      localidadRepositorio.save(laPlataCentro);
      Localidad tolosa = Localidad.builder().nombre("Tolosa").codigoPostal("1904").departamento(laPlata).build();
      tolosa.setEliminado(false);
      localidadRepositorio.save(tolosa);
      Localidad ringuelet = Localidad.builder().nombre("Ringuelet").codigoPostal("1906").departamento(laPlata).build();
      ringuelet.setEliminado(false);
      localidadRepositorio.save(ringuelet);

      // LOCALIDADES Buenos Aires - Quilmes
      Localidad quilmesCentro = Localidad.builder().nombre("Quilmes Centro").codigoPostal("1878").departamento(quilmes).build();
      quilmesCentro.setEliminado(false);
      localidadRepositorio.save(quilmesCentro);
      Localidad bernal = Localidad.builder().nombre("Bernal").codigoPostal("1876").departamento(quilmes).build();
      bernal.setEliminado(false);
      localidadRepositorio.save(bernal);
      Localidad ezpeleta = Localidad.builder().nombre("Ezpeleta").codigoPostal("1882").departamento(quilmes).build();
      ezpeleta.setEliminado(false);
      localidadRepositorio.save(ezpeleta);
    }


  }
}
