package edu.egg.servicios;

import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.entidades.Zona;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.servicios.UsuarioServicio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class TestUsuarioServicio {
  @Mock
  UsuarioServicio usuarioServicio;

  @BeforeEach
  public void beforeEach() {
    MockitoAnnotations.openMocks(this);
    usuarioServicio = new UsuarioServicio();
    System.out.println("Starting a test...");
  }

  @AfterEach
  public void afterEach() {
    System.out.println("Test completed.");
  }

  @Test
  void validarDatosCorrectos() {
    assertDoesNotThrow(() -> {
      usuarioServicio.validar("Juan", "Perez", "juan@mail.com", "clave123", "clave123");
    });
  }

  @Test
  void validarNombreNulo() {
    ErrorServicio ex = assertThrows(ErrorServicio.class, () -> {
      usuarioServicio.validar(null, "Perez", "juan@mail.com", "clave123", "clave123");
    });
    assertEquals("El nombre no puede ser nulo", ex.getMessage());
  }

  @Test
  void validarClavesDistintas() {
    ErrorServicio ex = assertThrows(ErrorServicio.class, () -> {
      usuarioServicio.validar("Juan", "Perez", "juan@mail.com", "clave123", "clave456");
    });
    assertEquals("Las contraseñas deben coincidir", ex.getMessage());
  }

}