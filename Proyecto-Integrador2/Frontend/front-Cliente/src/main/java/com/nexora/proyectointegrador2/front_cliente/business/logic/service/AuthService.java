package com.nexora.proyectointegrador2.front_cliente.business.logic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyectointegrador2.front_cliente.dto.AuthResponseDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.LoginRequestDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.RegisterRequestDTO;
@Service
public class AuthService {

  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

  private final RestTemplate restTemplate;

  @Value("${api.base.url}")
  private String baseUrl;

  public AuthService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public AuthResponseDTO login(LoginRequestDTO loginRequest) throws Exception {
    try {
      String url = baseUrl + "/auth/login";
      
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      
      HttpEntity<LoginRequestDTO> request = new HttpEntity<>(loginRequest, headers);
      
      logger.debug("Enviando petición de login a: {}", url);
      
      ResponseEntity<AuthResponseDTO> response = restTemplate.postForEntity(
          url, 
          request, 
          AuthResponseDTO.class
      );
      
      AuthResponseDTO authResponse = response.getBody();
      
      if (authResponse != null && authResponse.getToken() != null) {
        logger.info("Login exitoso para usuario: {}", loginRequest.getNombreUsuario());
        return authResponse;
      } else {
        throw new Exception("Respuesta de autenticación inválida");
      }
      
    } catch (HttpClientErrorException.Unauthorized e) {
      logger.warn("Credenciales inválidas para usuario: {}", loginRequest.getNombreUsuario());
      throw new Exception("Usuario o contraseña incorrectos");
    } catch (HttpClientErrorException e) {
      logger.error("Error del cliente en login: {} - {}", e.getStatusCode(), e.getMessage());
      throw new Exception("Error al intentar iniciar sesión: " + e.getStatusCode());
    } catch (Exception e) {
      logger.error("Error inesperado en login: {}", e.getMessage());
      throw new Exception("Error al conectar con el servidor de autenticación");
    }
  }
   public AuthResponseDTO register(RegisterRequestDTO registerRequest) throws Exception {
    try {
      String url = baseUrl + "/auth/registro";
      
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      
      HttpEntity<RegisterRequestDTO> request = new HttpEntity<>(registerRequest, headers);
      
      logger.debug("Enviando petición de registro a: {}", url);
      
      ResponseEntity<AuthResponseDTO> response = restTemplate.postForEntity(
          url, 
          request, 
          AuthResponseDTO.class
      );
      
      AuthResponseDTO authResponse = response.getBody();
      
        logger.info("Registro exitoso para usuario: {}", registerRequest.getNombreUsuario());
        return authResponse;
     
    } catch (HttpClientErrorException.Conflict e) {
      logger.warn("Usuario ya existe: {}", registerRequest.getNombreUsuario());
      throw new Exception("El nombre de usuario ya está en uso");
    } catch (HttpClientErrorException.BadRequest e) {
      logger.warn("Datos de registro inválidos: {}", e.getMessage());
      throw new Exception("Datos de registro inválidos");
    } catch (HttpClientErrorException e) {
      logger.error("Error del cliente en registro: {} - {}", e.getStatusCode(), e.getMessage());
      throw new Exception("Error al intentar registrarse: " + e.getStatusCode());
    } catch (Exception e) {
      logger.error("Error inesperado en registro: {}", e.getMessage());
      throw new Exception("Error al conectar con el servidor de registro");
    }
  }

}
