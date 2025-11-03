package edu.egg.servicios;

import edu.egg.tinder.entidades.Foto;
import edu.egg.tinder.entidades.Mascota;
import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.enumeracion.Sexo;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.repositorios.MascotaRepositorio;
import edu.egg.tinder.repositorios.UsuarioRepositorio;
import edu.egg.tinder.servicios.FotoServicio;
import edu.egg.tinder.servicios.MascotaServicio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestMascotaServicio {

  // Simulaciones (mocks)
  @Mock
  private UsuarioRepositorio usuarioRepositorio;

  @Mock
  private MascotaRepositorio mascotaRepositorio;

  @Mock
  private FotoServicio fotoServicio;

  // Servicio real (pero con mocks inyectados)
  @InjectMocks
  private MascotaServicio mascotaServicio;

  private Usuario usuario;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this); // inicializa los mocks
    usuario = new Usuario();
    usuario.setId(1L);
  }

  @AfterEach
  public void afterEach() {
    System.out.println("Test completed.");
  }

  @Test
  void testCrearMascotaConUsuarioValido() throws ErrorServicio {
    // Datos de prueba
    String nombre = "Firulais";
    Sexo sexo = Sexo.MACHO;
    MultipartFile archivo = mock(MultipartFile.class);

    // Simulamos que el usuario existe y la foto se guarda
    when(usuarioRepositorio.findById(1L)).thenReturn(Optional.of(usuario));
    when(fotoServicio.guardar(archivo)).thenReturn(new Foto());

    // Ejecutamos el metodo real
    mascotaServicio.crearMascota(1L, nombre, sexo, archivo);

    // Verificamos que se haya guardado una mascota con esos datos
    verify(mascotaRepositorio).save(argThat(mascota ->
      mascota.getNombre().equals(nombre)
        && mascota.getSexo() == sexo
        && mascota.getUsuario() == usuario
        && mascota.isActivo()
        && mascota.getFoto() != null
    ));
  }

  @Test
  void testCrearMascotaConUsuarioInvalido() {
    // Simulamos que NO existe el usuario
    when(usuarioRepositorio.findById(1L)).thenReturn(Optional.empty());

    // El metodo debería lanzar un error
    assertThrows(ErrorServicio.class, () -> {
      mascotaServicio.crearMascota(1L, "Luna", Sexo.HEMBRA, mock(MultipartFile.class));
    });

  }
}