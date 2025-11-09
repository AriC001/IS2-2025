package nexora.proyectointegrador2.utils.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import nexora.proyectointegrador2.business.enums.RolUsuario;

@Component
public class JwtUtil {

  private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  public String generateToken(String usuarioId, String nombreUsuario, RolUsuario rol) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("usuarioId", usuarioId);
    claims.put("rol", rol.name());
    return createToken(claims, nombreUsuario);
  }

  private String createToken(Map<String, Object> claims, String subject) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);

    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSignKey())
        .compact();
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public String extractUsuarioId(String token) {
    return extractClaim(token, claims -> claims.get("usuarioId", String.class));
  }

  public String extractRol(String token) {
    return extractClaim(token, claims -> claims.get("rol", String.class));
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSignKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Boolean validateToken(String token, String username) {
    try {
      final String extractedUsername = extractUsername(token);
      return (extractedUsername.equals(username) && !isTokenExpired(token));
    } catch (MalformedJwtException e) {
      logger.error("Token JWT mal formado: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("Token JWT expirado: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("Token JWT no soportado: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string está vacío: {}", e.getMessage());
    }
    return false;
  }

  private SecretKey getSignKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

}
