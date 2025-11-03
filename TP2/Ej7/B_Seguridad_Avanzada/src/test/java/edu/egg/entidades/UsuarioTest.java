package edu.egg.entidades;


import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.entidades.Zona;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioTest {
  private Usuario usuario;
  private Zona zona;

  @BeforeEach
  public void beforeEach() {
    usuario = new Usuario();
    zona = new Zona();
    System.out.println("Starting a test...");
  }

  @AfterEach
  public void afterEach() {
    System.out.println("Test completed.");
  }

  @Test
  public void testSettersAndGetters() {
    usuario.setNombre("Juan");
    usuario.setApellido("Massacesi");
    usuario.setMail("juan@gmail.com");

    assertEquals("Juan", usuario.getNombre());
    assertEquals("Massacesi", usuario.getApellido());
    assertEquals("juan@gmail.com", usuario.getMail());
  }

  @Test
  public void testSettersAndZonaRelation() {
    zona.setNombre("Norte");
    zona.setDescripcion("Zona norte");
    zona.setActivo(true);

    usuario.setNombre("Ana");
    usuario.setMail("ana@email.com");
    usuario.setZona(zona);

    assertEquals("Ana", usuario.getNombre());
    assertEquals("ana@email.com", usuario.getMail());
    assertEquals(zona, usuario.getZona());

    assertTrue(zona.isActivo());
  }
}
