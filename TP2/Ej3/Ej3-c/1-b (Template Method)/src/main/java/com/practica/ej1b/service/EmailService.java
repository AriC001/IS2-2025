package com.practica.ej1b.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.empresa.nombre:Nexora}")
    private String empresaNombre;

    @Value("${app.empresa.email}")
    private String empresaEmail;

    /**
     * Envía un correo HTML con un botón que enlaza a la URL especificada
     */
    public void enviarCorreoHtml(String destinatario, String asunto, String contenidoHtml) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setFrom(empresaEmail);
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(contenidoHtml, true); // true indica que es HTML

            mailSender.send(mensaje);
            log.info("Correo enviado exitosamente a: {}", destinatario);
        } catch (MessagingException e) {
            log.error("Error al enviar correo a {}: {}", destinatario, e.getMessage());
            throw new RuntimeException("Error al enviar el correo", e);
        }
    }

    /**
     * Genera HTML para correo de publicidad
     */
    public String generarHtmlPublicidad() {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Publicidad %s</title>
                    <style>
                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            background-color: #f4f4f4;
                            margin: 0;
                            padding: 0;
                        }
                        .container {
                            max-width: 600px;
                            margin: 40px auto;
                            background-color: white;
                            border-radius: 10px;
                            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
                            overflow: hidden;
                        }
                        .header {
                            background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                            color: white;
                            padding: 40px 20px;
                            text-align: center;
                        }
                        .header h1 {
                            margin: 0;
                            font-size: 32px;
                        }
                        .content {
                            padding: 40px 30px;
                            line-height: 1.6;
                            color: #333;
                        }
                        .content p {
                            font-size: 16px;
                            margin-bottom: 20px;
                        }
                        .btn-container {
                            text-align: center;
                            padding: 20px 0;
                        }
                        .btn {
                            display: inline-block;
                            padding: 15px 40px;
                            background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                            color: white;
                            text-decoration: none;
                            border-radius: 50px;
                            font-weight: bold;
                            font-size: 16px;
                            transition: transform 0.3s ease;
                            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
                        }
                        .btn:hover {
                            transform: translateY(-2px);
                            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
                        }
                        .footer {
                            background-color: #f8f9fa;
                            padding: 20px;
                            text-align: center;
                            font-size: 12px;
                            color: #666;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>🎉 Novedades de %s</h1>
                        </div>
                        <div class="content">
                            <p>¡Hola!</p>
                            <p>Te damos la bienvenida a nuestro boletín mensual de novedades. Este mes tenemos promociones especiales y nuevos servicios que te encantarán.</p>
                            <p>Visita nuestra página para conocer más sobre nuestros servicios y las últimas actualizaciones de la Facultad de Ingeniería.</p>
                            <div class="btn-container">
                                <a href="https://www.uncuyo.edu.ar/" target="_blank" class="btn">
                                    🔗 Visitar Sitio Web
                                </a>
                            </div>
                            <p>No te pierdas nuestras ofertas exclusivas y mantente informado sobre todo lo que sucede en la universidad.</p>
                        </div>
                        <div class="footer">
                            <p>&copy; 2025 %s. Todos los derechos reservados.</p>
                            <p>Este es un correo automático, por favor no responder.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(empresaNombre, empresaNombre, empresaNombre);
    }

    /**
     * Genera HTML para correo de fin de año
     */
    public String generarHtmlFinDeAnio() {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Felices Fiestas</title>
                    <style>
                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            background-color: #f4f4f4;
                            margin: 0;
                            padding: 0;
                        }
                        .container {
                            max-width: 600px;
                            margin: 40px auto;
                            background-color: white;
                            border-radius: 10px;
                            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
                            overflow: hidden;
                        }
                        .header {
                            background: linear-gradient(135deg, #c94b4b 0%%, #4b134f 100%%);
                            color: white;
                            padding: 40px 20px;
                            text-align: center;
                        }
                        .header h1 {
                            margin: 0;
                            font-size: 36px;
                            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
                        }
                        .content {
                            padding: 40px 30px;
                            line-height: 1.6;
                            color: #333;
                        }
                        .content p {
                            font-size: 16px;
                            margin-bottom: 20px;
                        }
                        .highlight {
                            background-color: #fff3cd;
                            padding: 20px;
                            border-left: 4px solid #ffc107;
                            margin: 20px 0;
                            border-radius: 5px;
                        }
                        .btn-container {
                            text-align: center;
                            padding: 20px 0;
                        }
                        .btn {
                            display: inline-block;
                            padding: 15px 40px;
                            background: linear-gradient(135deg, #c94b4b 0%%, #4b134f 100%%);
                            color: white;
                            text-decoration: none;
                            border-radius: 50px;
                            font-weight: bold;
                            font-size: 16px;
                            transition: transform 0.3s ease;
                            box-shadow: 0 4px 15px rgba(201, 75, 75, 0.4);
                        }
                        .btn:hover {
                            transform: translateY(-2px);
                            box-shadow: 0 6px 20px rgba(201, 75, 75, 0.6);
                        }
                        .footer {
                            background-color: #f8f9fa;
                            padding: 20px;
                            text-align: center;
                            font-size: 12px;
                            color: #666;
                        }
                        .decorative {
                            text-align: center;
                            font-size: 40px;
                            margin: 20px 0;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>🎄 ¡Felices Fiestas! 🎆</h1>
                        </div>
                        <div class="content">
                            <div class="decorative">🎁 ⭐ 🎊</div>
                            <p><strong>Estimado Proveedor:</strong></p>
                            <p>Al finalizar este año, queremos tomarnos un momento para agradecerle por su confianza y colaboración durante todo este tiempo.</p>
                            <div class="highlight">
                                <p><strong>Su apoyo ha sido fundamental para nuestro crecimiento y éxito.</strong></p>
                            </div>
                            <p>Les deseamos unas felices fiestas junto a sus seres queridos y un próspero año nuevo lleno de éxitos y oportunidades.</p>
                            <p>Esperamos seguir contando con su valiosa colaboración el próximo año.</p>
                            <div class="btn-container">
                                <a href="https://www.uncuyo.edu.ar/" target="_blank" class="btn">
                                    🔗 Visitar Nuestro Sitio
                                </a>
                            </div>
                            <p><em>¡Que el próximo año esté lleno de bendiciones para usted y su familia!</em></p>
                            <div class="decorative">🥂 🎉 ✨</div>
                        </div>
                        <div class="footer">
                            <p><strong>%s</strong></p>
                            <p>&copy; 2025 Todos los derechos reservados.</p>
                            <p>Este es un correo automático, por favor no responder.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(empresaNombre);
    }
}
