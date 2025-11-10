package com.tpa.entidades;

import org.junit.jupiter.api.*;
import org.springframework.test.annotation.Repeat;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CalculadoraTest {
  Calculadora calc;

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
    calc = new Calculadora();
    System.out.println("Inicio de un test");
  }

  @AfterEach
  public void afterEach() throws Exception {
    System.out.println("Fin de un test");
  }

  @Test
  @Order(1)
  public void testSumar() {
    Integer a=2, b=3, esperado=5, resultado=0;
    resultado = calc.sumar(a, b);
    System.out.println("Resultado: " + resultado);

  }

  @Test
  @Order(2)
  public void testDividir(){
    Double a=2.0, b=3.0, esperado=0.6666666666666666, resultado=0.0;
    try {
      resultado = calc.dividir(a, b);
    }catch (Exception e){
      throw new RuntimeException(e);
    }

    Assertions.assertEquals(esperado,resultado);

    System.out.println("Resultado: " + resultado);

  }

  @Order(3)
  @RepeatedTest(value = 5)
  public void testDividirCheckException(){
    Double a=2.0, b=0.0, esperado=0.6666666666666666, resultado=0.0;
    try {
      Exception ex = assertThrows(Exception.class ,() -> {
        calc.dividir(a, b);
      });

      Assertions.assertEquals("Denominador no puede ser nulo o cero", ex.getMessage());

    }catch (Exception e){
      throw new RuntimeException(e);
    }

    System.out.println("Resultado: " + resultado);

  }
}
