package com.nexora.proyectointegrador2.front_cliente.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  // Optional: a fixed service JWT that the application can use to call the API
  // Useful during OAuth2 login when there is no user token yet
  @Value("${api.client.token:}")
  private String apiClientToken;

  @Bean
  public RestTemplate restTemplate(OAuth2AuthorizedClientService authorizedClientService) {
    RestTemplate restTemplate = new RestTemplate();

    List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

    // Interceptor that will add an Authorization: Bearer <token> header when possible.
    // Priority:
    // 1) api.client.token (application-level service token) if configured
    // 2) current authenticated user's OAuth2 access token (if present)
    interceptors.add(new ClientHttpRequestInterceptor() {
      @Override
      public ClientHttpResponse intercept(org.springframework.http.HttpRequest request, byte[] body,
          ClientHttpRequestExecution execution) throws IOException {
        try {
          if (apiClientToken != null && !apiClientToken.isBlank()) {
            request.getHeaders().setBearerAuth(apiClientToken.trim());
            return execution.execute(request, body);
          }

          Authentication auth = SecurityContextHolder.getContext().getAuthentication();
          if (auth instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2 = (OAuth2AuthenticationToken) auth;
            String registrationId = oauth2.getAuthorizedClientRegistrationId();
            String principalName = oauth2.getName();
            if (registrationId != null && principalName != null) {
              OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(registrationId,
                  principalName);
              if (client != null && client.getAccessToken() != null) {
                String tokenValue = client.getAccessToken().getTokenValue();
                if (tokenValue != null && !tokenValue.isBlank()) {
                  request.getHeaders().setBearerAuth(tokenValue);
                }
              }
            }
          }
        } catch (Exception e) {
          // don't fail requests if we couldn't resolve a token; proceed without auth header
        }
        return execution.execute(request, body);
      }
    });

    restTemplate.setInterceptors(interceptors);
    return restTemplate;
  }
}