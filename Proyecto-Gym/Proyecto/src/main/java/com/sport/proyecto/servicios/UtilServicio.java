package com.sport.proyecto.servicios;

import java.security.MessageDigest;
import java.util.Date;
import java.util.regex.Pattern;

import com.sport.proyecto.errores.ErrorServicio;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class UtilServicio {

  // Expresión regular para validar correos electrónicos comunes
  private static final String EMAIL_REGEX ="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
  private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

  // Instancia de BCryptPasswordEncoder para encriptar contraseñas
  private static final BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

  public static String encriptarClave(String textoEncriptar) throws ErrorServicio {
    try {
      // Usar BCrypt para encriptar la contraseña
      return bcryptEncoder.encode(textoEncriptar);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ErrorServicio("Error de sistema al encriptar contraseña");
    }
  }

  public static boolean verificarClave(String clavePlana, String claveEncriptada) throws ErrorServicio {
    try {
      return bcryptEncoder.matches(clavePlana, claveEncriptada);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ErrorServicio("Error de sistema al verificar contraseña");
    }
  }

  public static boolean esEmailValido(String email) {
    if (email == null) return false;
    return EMAIL_PATTERN.matcher(email).matches();
  }


  public static String generateHmacSHA256(String key, String data) throws Exception {
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
    sha256_HMAC.init(secretKey);

    byte[] hash = sha256_HMAC.doFinal(data.getBytes());
    StringBuilder result = new StringBuilder();
    for (byte b : hash) {
      result.append(String.format("%02x", b));
    }
    return result.toString();
  }
}
