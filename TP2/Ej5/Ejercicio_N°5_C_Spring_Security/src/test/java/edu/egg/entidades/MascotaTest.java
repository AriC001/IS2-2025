package edu.egg.entidades;

import edu.egg.tinder.entidades.Mascota;
import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.entidades.Zona;
import edu.egg.tinder.enumeracion.Sexo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MascotaTest {
  private Mascota mascota;
  private Usuario usuario;

  @BeforeEach
  public void beforeEach() {
    usuario = new Usuario();
    mascota = new Mascota();
    System.out.println("Starting a test...");
  }

  @AfterEach
  public void afterEach() {
    System.out.println("Test completed.");
  }

  @Test
  void testActivoPorDefectoEsTrue() {
    assertTrue(mascota.isActivo(), "Una nueva mascota debería estar activa por defecto");
  }

  @Test
  void testDesactivarCambiaActivoAFalso() {
    mascota.desactivar();
    assertFalse(mascota.isActivo(), "desactivar() debería poner activo en false");
  }

  @Test
  void testSettersAndGetters() {
    mascota.setNombre("Firulais");
    mascota.setSexo(Sexo.MACHO);

    assertEquals("Firulais", mascota.getNombre());
    assertEquals(Sexo.MACHO, mascota.getSexo());
  }

  @Test
  void testRelacionUsuario() {
    usuario.setNombre("Juan");

    mascota.setUsuario(usuario);

    assertEquals("Juan", mascota.getUsuario().getNombre());
  }
}
