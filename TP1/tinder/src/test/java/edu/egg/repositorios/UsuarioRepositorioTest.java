package edu.egg.repositorios;

import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.repositorios.UsuarioRepositorio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


public class UsuarioRepositorioTest {

  @Mock
  private UsuarioRepositorio usuarioRepositorio;

  private Usuario usuario;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    usuario = Usuario.builder()
      .nombre("Juan")
      .apellido("Perez")
      .mail("jp@gmail.com")
      .build();
    System.out.println("Starting a test...");
  }

  @AfterEach
  public void afterEach() {
    System.out.println("Test completed.");
  }

  @Test
  public void testCrearUsuario() {
    // simulamos que save devuelve el mismo usuario con ID asignado
    when(usuarioRepositorio.save(usuario)).thenReturn(usuario);

    // when
    Usuario usuarioGuardado = usuarioRepositorio.save(usuario);

    // then
    assertThat(usuarioGuardado).isNotNull();
    assertThat(usuarioGuardado.getNombre()).isEqualTo("Juan");

    verify(usuarioRepositorio, times(1)).save(usuario);
  }

  @Test
  public void testListarUsuarios() {
    // given
    Usuario usuario1 = Usuario.builder()
      .nombre("Lucas")
      .apellido("Massacesi")
      .mail("lm17@gmail.com")
      .build();

    when(usuarioRepositorio.findAll()).thenReturn(List.of(usuario, usuario1));

    // when
    List<Usuario> usuarios = usuarioRepositorio.findAll();

    // then
    assertThat(usuarios).isNotNull();
    assertThat(usuarios.size()).isEqualTo(2);
    assertThat(usuarios.get(0).getNombre()).isEqualTo("Juan");
    assertThat(usuarios.get(1).getNombre()).isEqualTo("Lucas");
  }
}
