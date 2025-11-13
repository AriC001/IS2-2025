package nexora.proyectointegrador2.business.logic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

  private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

  @Autowired(required = false)
  private JavaMailSender mailSender;

  /**
   * Envía un recordatorio de devolución por email al cliente.
   * @param emailCliente Email del cliente
   * @param modeloVehiculo Modelo del vehículo alquilado (ej: "Ford Focus 2020")
   * @param fechaDevolucion Fecha de devolución del vehículo
   * @param ubicacionEntrega Ubicación donde debe devolverse el vehículo
   * @param esDevolucionHoy true si la devolución es hoy, false si es mañana
   */
  public void enviarRecordatorioDevolucion(String emailCliente, String modeloVehiculo, 
                                           java.util.Date fechaDevolucion, String ubicacionEntrega,
                                           boolean esDevolucionHoy) throws Exception {
    logger.info("=== INICIANDO ENVÍO DE RECORDATORIO DE DEVOLUCIÓN ===");
    logger.info("Email destino: {}", emailCliente);
    logger.info("Modelo vehículo: {}", modeloVehiculo);
    logger.info("Fecha devolución: {}", fechaDevolucion);
    
    if (mailSender == null) {
      logger.error("JavaMailSender no está configurado, no se puede enviar el email");
      throw new Exception("JavaMailSender no está configurado. Verifique la configuración de Spring Mail.");
    }

    if (emailCliente == null || emailCliente.trim().isEmpty()) {
      logger.error("Email del cliente vacío, no se puede enviar el recordatorio");
      throw new Exception("Email del cliente vacío");
    }

    try {
      logger.info("Creando mensaje MIME...");
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

      logger.info("Configurando remitente: mycar.mdz@gmail.com");
      helper.setFrom("mycar.mdz@gmail.com", "My Car - Alquiler de Vehículos");
      
      logger.info("Configurando destinatario: {}", emailCliente);
      helper.setTo(emailCliente);
      
      String subject = "Recordatorio: Devolución de Vehículo - " + modeloVehiculo;
      logger.info("Asunto: {}", subject);
      helper.setSubject(subject);
      
      logger.info("Construyendo cuerpo del email...");
      String cuerpoEmail = construirCuerpoRecordatorio(modeloVehiculo, fechaDevolucion, ubicacionEntrega, esDevolucionHoy);
      helper.setText(cuerpoEmail, true);

      logger.info("Enviando email...");
      mailSender.send(message);
      logger.info("=== RECORDATORIO ENVIADO EXITOSAMENTE A: {} ===", emailCliente);

    } catch (jakarta.mail.AuthenticationFailedException e) {
      logger.error("=== ERROR DE AUTENTICACIÓN CON GMAIL ===");
      logger.error("Mensaje: {}", e.getMessage());
      throw new Exception("Error de autenticación con Gmail. Verifique la configuración de email.");
    } catch (jakarta.mail.MessagingException e) {
      logger.error("=== ERROR DE MENSAJERÍA ===");
      logger.error("Mensaje: {}", e.getMessage());
      throw new Exception("Error al enviar recordatorio: " + e.getMessage(), e);
    } catch (Exception e) {
      logger.error("=== ERROR GENERAL AL ENVIAR RECORDATORIO ===");
      logger.error("Tipo: {}", e.getClass().getName());
      logger.error("Mensaje: {}", e.getMessage());
      throw new Exception("Error al enviar recordatorio de devolución: " + e.getMessage(), e);
    }
  }

  /**
   * Construye el cuerpo del email de recordatorio de devolución.
   */
  private String construirCuerpoRecordatorio(String modeloVehiculo, java.util.Date fechaDevolucion, 
                                             String ubicacionEntrega, boolean esDevolucionHoy) {
    StringBuilder sb = new StringBuilder();
    sb.append("<html><body style='font-family: Arial, sans-serif;'>");
    sb.append("<div style='max-width: 600px; margin: 0 auto; padding: 20px;'>");
    sb.append("<h2 style='color: #333;'>Recordatorio de Devolución</h2>");
    sb.append("<p>Estimado cliente,</p>");
    
    if (esDevolucionHoy) {
      sb.append("<p>Le recordamos que <strong>hoy</strong> debe devolver el vehículo alquilado.</p>");
    } else {
      sb.append("<p>Le recordamos que <strong>mañana</strong> debe devolver el vehículo alquilado.</p>");
    }
    sb.append("<div style='background-color: #f5f5f5; padding: 15px; border-radius: 5px; margin: 20px 0;'>");
    sb.append("<h3 style='color: #555; margin-top: 0;'>Detalles del Alquiler:</h3>");
    sb.append("<ul style='list-style-type: none; padding-left: 0;'>");
    sb.append("<li style='margin: 10px 0;'><strong>Modelo:</strong> ").append(modeloVehiculo).append("</li>");
    sb.append("<li style='margin: 10px 0;'><strong>Fecha de Devolución:</strong> ")
        .append(new java.text.SimpleDateFormat("dd/MM/yyyy").format(fechaDevolucion)).append("</li>");
    sb.append("<li style='margin: 10px 0;'><strong>Ubicación de Entrega:</strong> ").append(ubicacionEntrega).append("</li>");
    sb.append("</ul>");
    sb.append("</div>");
    sb.append("<p>Por favor, asegúrese de devolver el vehículo en el lugar y horario acordados.</p>");
    sb.append("<p>Si tiene alguna consulta, no dude en contactarnos.</p>");
    sb.append("<p>Gracias por su preferencia.</p>");
    sb.append("<p>Saludos cordiales,<br><strong>MyCar - Alquiler de Vehículos</strong></p>");
    sb.append("</div>");
    sb.append("</body></html>");
    return sb.toString();
  }

}

