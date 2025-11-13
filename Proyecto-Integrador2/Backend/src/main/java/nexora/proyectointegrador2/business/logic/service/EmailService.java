package nexora.proyectointegrador2.business.logic.service;

import java.io.ByteArrayInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import nexora.proyectointegrador2.business.domain.entity.Factura;

@Service
public class EmailService {

  private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

  @Autowired(required = false)
  private JavaMailSender mailSender;

  /**
   * Envía la factura por email al cliente.
   */
  public void enviarFacturaPorEmail(String emailCliente, Factura factura, byte[] pdfFactura) throws Exception {
    logger.info("=== INICIANDO ENVÍO DE EMAIL ===");
    logger.info("Email destino: {}", emailCliente);
    logger.info("Factura número: {}", factura.getNumeroFactura());
    
    if (mailSender == null) {
      logger.error("JavaMailSender no está configurado, no se puede enviar el email");
      throw new Exception("JavaMailSender no está configurado. Verifique la configuración de Spring Mail.");
    }
    logger.info("JavaMailSender está configurado correctamente");

    if (emailCliente == null || emailCliente.trim().isEmpty()) {
      logger.error("Email del cliente vacío, no se puede enviar el email");
      throw new Exception("Email del cliente vacío");
    }

    try {
      logger.info("Creando mensaje MIME...");
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      logger.info("Configurando remitente: mycar.mdz@gmail.com");
      helper.setFrom("mycar.mdz@gmail.com", "My Car - Alquiler de Vehículos");
      
      logger.info("Configurando destinatario: {}", emailCliente);
      helper.setTo(emailCliente);
      
      String subject = "Factura de Alquiler - Número " + factura.getNumeroFactura();
      logger.info("Asunto: {}", subject);
      helper.setSubject(subject);
      
      logger.info("Construyendo cuerpo del email...");
      String cuerpoEmail = construirCuerpoEmail(factura);
      helper.setText(cuerpoEmail, true);

      // Adjuntar PDF
      String nombreArchivo = "factura_" + factura.getNumeroFactura() + ".pdf";
      logger.info("Adjuntando PDF: {} (tamaño: {} bytes)", nombreArchivo, pdfFactura.length);
      InputStreamSource attachment = new InputStreamSource() {
        @Override
        public java.io.InputStream getInputStream() {
          return new ByteArrayInputStream(pdfFactura);
        }
      };
      helper.addAttachment(nombreArchivo, attachment);

      logger.info("Enviando email...");
      mailSender.send(message);
      logger.info("=== EMAIL ENVIADO EXITOSAMENTE A: {} ===", emailCliente);

    } catch (jakarta.mail.AuthenticationFailedException e) {
      logger.error("=== ERROR DE AUTENTICACIÓN CON GMAIL ===");
      logger.error("Mensaje: {}", e.getMessage());
      logger.error("Causa: {}", e.getCause() != null ? e.getCause().getMessage() : "N/A");
      logger.error("=== SOLUCIÓN: Necesita generar una 'Contraseña de aplicación' en Gmail ===");
      logger.error("Pasos:");
      logger.error("1. Ir a https://myaccount.google.com/security");
      logger.error("2. Activar 'Verificación en 2 pasos'");
      logger.error("3. Ir a 'Contraseñas de aplicaciones'");
      logger.error("4. Generar una nueva contraseña para 'Correo'");
      logger.error("5. Usar esa contraseña en application.properties (spring.mail.password)");
      throw new Exception("Error de autenticación con Gmail. Verifique que esté usando una 'Contraseña de aplicación' en lugar de la contraseña normal. Revise los logs para más detalles.");
    } catch (jakarta.mail.MessagingException e) {
      logger.error("=== ERROR DE MENSAJERÍA ===");
      logger.error("Mensaje: {}", e.getMessage());
      logger.error("Causa: {}", e.getCause() != null ? e.getCause().getMessage() : "N/A");
      logger.error("Stack trace completo:", e);
      throw new Exception("Error al enviar email: " + e.getMessage() + (e.getCause() != null ? " - Causa: " + e.getCause().getMessage() : ""), e);
    } catch (Exception e) {
      logger.error("=== ERROR GENERAL AL ENVIAR EMAIL ===");
      logger.error("Tipo: {}", e.getClass().getName());
      logger.error("Mensaje: {}", e.getMessage());
      logger.error("Causa: {}", e.getCause() != null ? e.getCause().getMessage() : "N/A");
      logger.error("Stack trace completo:", e);
      throw new Exception("Error al enviar email de factura: " + e.getMessage(), e);
    }
  }

  /**
   * Construye el cuerpo del email con los detalles de la factura.
   */
  private String construirCuerpoEmail(Factura factura) {
    StringBuilder sb = new StringBuilder();
    sb.append("<html><body>");
    sb.append("<h2>Factura de Alquiler</h2>");
    sb.append("<p>Estimado cliente,</p>");
    sb.append("<p>Le enviamos la factura correspondiente a su alquiler:</p>");
    sb.append("<ul>");
    sb.append("<li><strong>Número de Factura:</strong> ").append(factura.getNumeroFactura()).append("</li>");
    sb.append("<li><strong>Fecha:</strong> ").append(new java.text.SimpleDateFormat("dd/MM/yyyy").format(factura.getFechaFactura())).append("</li>");
    sb.append("<li><strong>Total Pagado:</strong> $").append(String.format("%.2f", factura.getTotalPagado())).append("</li>");
    sb.append("<li><strong>Forma de Pago:</strong> ").append(factura.getFormaDePago().getTipoPago().name()).append("</li>");
    sb.append("</ul>");
    sb.append("<p>La factura en formato PDF se encuentra adjunta.</p>");
    sb.append("<p>Gracias por su preferencia.</p>");
    sb.append("<p>Saludos cordiales,<br>MyCar</p>");
    sb.append("</body></html>");
    return sb.toString();
  }

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

