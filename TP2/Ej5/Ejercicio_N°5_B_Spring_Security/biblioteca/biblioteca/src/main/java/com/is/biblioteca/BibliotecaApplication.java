package com.is.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.is.biblioteca.business.logic.error.ErrorServiceException;

@SpringBootApplication
public class BibliotecaApplication {

	public static void main(String[] args) throws ErrorServiceException {
		SpringApplication.run(BibliotecaApplication.class, args);
		
	}

}
