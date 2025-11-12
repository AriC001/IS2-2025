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

}

