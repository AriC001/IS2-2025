package com.example.medicine.services;



import java.security.MessageDigest;
import java.util.Date;
import java.util.regex.Pattern;
import com.example.medicine.errors.ErrorServicio;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class UtilServicio {

  // Expresión regular para validar correos electrónicos comunes
  private static final String EMAIL_REGEX ="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
  private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

  //Arreglo para encriptar
  private static final char[] HEXADECIMAL = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  public static String encriptarClave(String textoEncriptar) throws ErrorServicio {

    try {

      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] bytes = md.digest(textoEncriptar.getBytes());
      StringBuilder sb = new StringBuilder(2 * bytes.length);

      for (int i = 0; i < bytes.length; i++) {
        int low = (int) (bytes[i] & 0x0f);
        int high = (int) ((bytes[i] & 0xf0) >> 4);
        sb.append(HEXADECIMAL[high]);
        sb.append(HEXADECIMAL[low]);
      }

      return sb.toString();

    } catch (Exception e) {
      e.printStackTrace();
      throw new ErrorServicio("Error de sistema");
    }
  }
  public static boolean validarClave(String claveIngresada, String claveEncriptada) throws ErrorServicio {
    String claveEncriptadaIngresada = encriptarClave(claveIngresada);
    return claveEncriptadaIngresada.equals(claveEncriptada);
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
