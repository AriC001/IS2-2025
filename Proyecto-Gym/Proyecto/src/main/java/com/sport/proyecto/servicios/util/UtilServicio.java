package com.sport.proyecto.servicios.util;

import com.sport.proyecto.errores.ErrorServicio;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.regex.Pattern;

@Service
public class UtilServicio {

  // Expresión regular para validar correos electrónicos comunes
  private static final String EMAIL_REGEX ="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
  private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

  //Arreglo para encriptar
  private static final char[] HEXADECIMAL = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  public String encriptar(String textoEncriptar) throws ErrorServicio {

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

  public static boolean esEmailValido(String email) {
    if (email == null) return false;
    return EMAIL_PATTERN.matcher(email).matches();
  }
}