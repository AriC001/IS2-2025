package com.practica.ej1b.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.practica.ej1b.business.persistence.repository.ProveedorRepositorio;
import com.practica.ej1b.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailScheduler {

  @Autowired
  private EmailService emailService;
  @Autowired
  private ProveedorRepositorio proveedorRepositorio;

  /**
   * Envía correos de publicidad a los proveedores el día 5 de cada mes a las 15:00
   * (Modificado para usar proveedores en lugar de empresas ya que Empresa no tiene email)
   * Cron: segundo minuto hora día mes día-semana
   * "0 0 15 5 * ?" = A las 15:00 del día 5 de cada mes
   * 
   * NOTA: Si deseas enviar a empresas, primero agrega el campo 'correoElectronico' 
   * a la entidad Empresa y actualiza este método.
   */
  @Scheduled(cron = "0 0 15 5 * ?")
  public void enviarCorreosPublicidad() {
    log.info("Iniciando envío de correos de publicidad...");
    
    try {
      // Enviamos a proveedores porque tienen correoElectronico heredado de Persona
      var proveedores = proveedorRepositorio.findAll();
      String htmlPublicidad = emailService.generarHtmlPublicidad();
      String asunto = "📢 Novedades y Promociones del Mes";
      
      int enviados = 0;
      for (var proveedor : proveedores) {
        try {
          if (proveedor.getCorreoElectronico() != null && !proveedor.getCorreoElectronico().isBlank()) {
            emailService.enviarCorreoHtml(
              proveedor.getCorreoElectronico(),
              asunto,
              htmlPublicidad
            );
            enviados++;
          }
        } catch (Exception e) {
          log.error("Error al enviar correo de publicidad a {} {}: {}", 
          proveedor.getNombre(), proveedor.getApellido(), e.getMessage());
        }
      }
      
      log.info("Correos de publicidad enviados: {} de {} destinatarios", enviados, proveedores.size());
    } catch (Exception e) {
      log.error("Error en el proceso de envío de correos de publicidad: {}", e.getMessage());
    }
  }

  /**
   * Envía saludos de fin de año a todos los proveedores el 31 de diciembre a las 15:00
   * Cron: "0 0 15 31 12 ?" = A las 15:00 del 31 de diciembre
   */
  @Scheduled(cron = "0 0 15 31 12 ?")
  public void enviarSaludosFinDeAnio() {
    log.info("Iniciando envío de saludos de fin de año a proveedores...");
      
    try {
      var proveedores = proveedorRepositorio.findAll();
      String htmlFinDeAnio = emailService.generarHtmlFinDeAnio();
      String asunto = "🎄 ¡Felices Fiestas y Próspero Año Nuevo! 🎆";
      
      int enviados = 0;
      for (var proveedor : proveedores) {
        try {
          if (proveedor.getCorreoElectronico() != null && !proveedor.getCorreoElectronico().isBlank()) {
            emailService.enviarCorreoHtml(
            proveedor.getCorreoElectronico(),
            asunto,
            htmlFinDeAnio
            );
            enviados++;
          }
        } catch (Exception e) {
          log.error("Error al enviar correo a proveedor {} {}: {}", 
          proveedor.getNombre(), proveedor.getApellido(), e.getMessage());
        }
      }
      log.info("Saludos de fin de año enviados: {} de {} proveedores", enviados, proveedores.size());
    } catch (Exception e) {
      log.error("Error en el proceso de envío de saludos de fin de año: {}", e.getMessage());
    }
  }

  /**
   * Método para pruebas - Se ejecuta cada minuto (comentar después de probar)
   * Descomenta este método solo para probar que los correos funcionan
   */
  
  // @Scheduled(cron = "0 * * * * ?")
  // public void enviarCorreoPrueba() {
  //     log.info("Enviando correo de prueba...");
  //     try {
  //         String htmlPrueba = emailService.generarHtmlFinDeAnio();
  //         emailService.enviarCorreoHtml(
  //             "juanimassacesi17@gmail.com", // Cambia esto por tu email
  //             "🧪 Correo de Prueba",
  //             htmlPrueba
  //         );
  //         log.info("Correo de prueba enviado exitosamente");
  //     } catch (Exception e) {
  //         log.error("Error al enviar correo de prueba: {}", e.getMessage());
  //     }
  // }
  

}
