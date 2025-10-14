package com.practica.ej1b;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Ej1bApplication {

  public static void main(String[] args) {
    SpringApplication.run(Ej1bApplication.class, args);
  }

}
