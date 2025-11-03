package edu.egg.tinder.servicios;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Servicio para obtener y renovar automáticamente Access Tokens de Auth0.
 * Útil para llamadas backend-to-backend o scheduled jobs.
 */
@Service
public class Auth0TokenService {

    @Value("${spring.security.oauth2.client.provider.auth0.issuer-uri}")
    private String issuerUri;

    @Value("${spring.security.oauth2.client.registration.auth0.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.auth0.client-secret}")
    private String clientSecret;

    @Value("${auth0.audience}")
    private String audience;

    private String cachedAccessToken;
    private Instant tokenExpiryTime;

    /**
     * Obtiene un Access Token válido de Auth0.
     * Si el token está en caché y no ha expirado, lo devuelve.
     * Si está expirado o no existe, solicita uno nuevo.
     * 
     * @return Access Token válido
     */
    public String obtenerAccessToken() {
        if (tokenEsValido()) {
            return cachedAccessToken;
        }

        return solicitarNuevoToken();
    }

    /**
     * Solicita un nuevo Access Token de Auth0 usando client_credentials flow
     */
    @SuppressWarnings("null")
    private String solicitarNuevoToken() {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = issuerUri + "/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);
        body.put("audience", audience);
        body.put("grant_type", "client_credentials");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            
            if (response.getBody() != null) {
                cachedAccessToken = (String) response.getBody().get("access_token");
                Integer expiresIn = (Integer) response.getBody().get("expires_in");
                
                // Renovar 5 minutos antes de que expire
                tokenExpiryTime = Instant.now().plusSeconds(expiresIn - 300);
                
                return cachedAccessToken;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo Access Token de Auth0: " + e.getMessage(), e);
        }

        throw new RuntimeException("No se pudo obtener Access Token de Auth0");
    }

    /**
     * Verifica si el token actual es válido
     */
    private boolean tokenEsValido() {
        return cachedAccessToken != null 
            && tokenExpiryTime != null 
            && Instant.now().isBefore(tokenExpiryTime);
    }

    /**
     * Fuerza la renovación del token en el próximo uso
     */
    public void invalidarToken() {
        cachedAccessToken = null;
        tokenExpiryTime = null;
    }

    public Instant getTokenExpiryTime() {
        return tokenExpiryTime;
    }
}
