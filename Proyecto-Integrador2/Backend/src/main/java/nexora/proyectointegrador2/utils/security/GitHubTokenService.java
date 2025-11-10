package nexora.proyectointegrador2.utils.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Servicio simple para validar un access token de GitHub
 * Llama a https://api.github.com/user y devuelve el login si el token es válido
 */
@Service
public class GitHubTokenService {

  private static final Logger log = LoggerFactory.getLogger(GitHubTokenService.class);

  private final RestTemplate restTemplate;

  public GitHubTokenService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * Valida el token en GitHub. Si es válido devuelve el login (username) del usuario.
   * Si no es válido devuelve null.
   */
  public String validateTokenAndGetLogin(String token) {
    try {
      String url = "https://api.github.com/user";
      HttpHeaders headers = new HttpHeaders();
      // GitHub acepta "token <token>" o "Bearer <token>" — usamos Bearer
      headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
      HttpEntity<Void> entity = new HttpEntity<>(headers);

      ResponseEntity<JsonNode> resp = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
      if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
        JsonNode body = resp.getBody();
        String login = body.hasNonNull("login") ? body.get("login").asText() : null;
        log.debug("GitHub token valid for login={}", login);
        return login;
      }
    } catch (Exception e) {
      log.debug("GitHub token validation failed: {}", e.getMessage());
    }
    return null;
  }

}
