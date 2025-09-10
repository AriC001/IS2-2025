package com.tpa.entidades;

import com.tpa.servicios.RectanguloServicio;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RectanguloTest {
  RectanguloServicio rs;

  @BeforeAll
  public static void beforeAll() throws Exception {
    System.out.println("Inicio de pruebas unitarias");
  }

  @AfterAll
  public static void afterAll() throws Exception {
    System.out.println("Fin de pruebas unitarias");
  }

  @BeforeEach
  public void beforeEach() throws Exception {
    rs = new RectanguloServicio();
    System.out.println("Inicio de un test");
  }

  @AfterEach
  public void afterEach() throws Exception {
    System.out.println("Fin de un test");
  }

  @Test
  public void deberiaInicializarConColor(){
    assertNotNull(new Rectangulo(2.0,3.0).getColor());
  }

  @Test
  public void deberiaCalcularArea(){
    Assertions.assertEquals(100, rs.calcularArea(new Rectangulo(10.0,10.0)),0);
    Assertions.assertEquals(20, rs.calcularArea(new Rectangulo(4.0,5.0)),0);
    Assertions.assertEquals(1, rs.calcularArea(new Rectangulo(1.0,1.0)),0);
  }

  @Test
  public void deberiaCalcularPerimetro(){
    Assertions.assertEquals(40, rs.calcularPerimetro(new Rectangulo(10.0,10.0)),0);
    Assertions.assertEquals(18, rs.calcularPerimetro(new Rectangulo(4.0,5.0)),0);
    Assertions.assertEquals(4, rs.calcularPerimetro(new Rectangulo(1.0,1.0)),0);
  }

  @Test
  public void activarODesactivar( ){
    Rectangulo r = new Rectangulo(2.0,3.0);
    Assertions.assertTrue(r.isActivo());
    r.setActivo(false);
    Assertions.assertFalse(r.isActivo());
  }
}
