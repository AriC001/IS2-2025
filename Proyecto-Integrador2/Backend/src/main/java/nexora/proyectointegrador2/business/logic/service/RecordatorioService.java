package nexora.proyectointegrador2.business.logic.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.Alquiler;
import nexora.proyectointegrador2.business.domain.entity.Empresa;
import nexora.proyectointegrador2.business.domain.entity.Direccion;
import nexora.proyectointegrador2.business.domain.entity.ContactoCorreoElectronico;
import nexora.proyectointegrador2.business.persistence.repository.AlquilerRepository;
import nexora.proyectointegrador2.business.persistence.repository.EmpresaRepository;
import nexora.proyectointegrador2.business.persistence.repository.ContactoCorreoElectronicoRepository;

@Service
public class RecordatorioService {

  private static final Logger logger = LoggerFactory.getLogger(RecordatorioService.class);

  @Autowired
  private AlquilerRepository alquilerRepository;

  @Autowired
  private EmailService emailService;

  @Autowired
  private EmpresaRepository empresaRepository;

  @Autowired
  private ContactoCorreoElectronicoRepository contactoCorreoElectronicoRepository;

  /**
   * Tarea programada que se ejecuta diariamente a las 9:00 AM.
   * Busca alquileres con fecha de devolución mañana y hoy, y envía recordatorios.
   * Maneja tanto alquileres de varios días como alquileres de corta duración (mismo día).
   */
  @Scheduled(cron = "0 0 9 * * ?") // Se ejecuta todos los días a las 9:00 AM
  public void enviarRecordatoriosDevolucion() {
    enviarRecordatoriosDevolucionManual();
  }

  /**
   * Método público para ejecutar manualmente el proceso de recordatorios.
   * Útil para pruebas y ejecución manual desde endpoints.
   */
  public void enviarRecordatoriosDevolucionManual() {
    logger.info("=== INICIANDO PROCESO DE RECORDATORIOS DE DEVOLUCIÓN ===");
    
    try {
      // Calcular fecha de hoy y mañana
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      
      // Fecha de hoy (limpiar hora)
      Calendar calHoy = Calendar.getInstance();
      calHoy.setTime(calendar.getTime());
      calHoy.set(Calendar.HOUR_OF_DAY, 0);
      calHoy.set(Calendar.MINUTE, 0);
      calHoy.set(Calendar.SECOND, 0);
      calHoy.set(Calendar.MILLISECOND, 0);
      Date fechaHoyLimpia = calHoy.getTime();
      
      // Fecha de mañana
      calendar.add(Calendar.DAY_OF_MONTH, 1);
      Calendar calMañana = Calendar.getInstance();
      calMañana.setTime(calendar.getTime());
      calMañana.set(Calendar.HOUR_OF_DAY, 0);
      calMañana.set(Calendar.MINUTE, 0);
      calMañana.set(Calendar.SECOND, 0);
      calMañana.set(Calendar.MILLISECOND, 0);
      Date fechaMañanaLimpia = calMañana.getTime();
      
      // Buscar alquileres con devolución mañana
      Collection<Alquiler> alquileresMañana = alquilerRepository.findAlquileresConDevolucionMañana(fechaMañanaLimpia);
      logger.info("Se encontraron {} alquileres con devolución mañana", alquileresMañana.size());
      
      // Buscar alquileres con devolución hoy (para alquileres de corta duración)
      Collection<Alquiler> alquileresHoy = alquilerRepository.findAlquileresConDevolucionMañana(fechaHoyLimpia);
      logger.info("Se encontraron {} alquileres con devolución hoy", alquileresHoy.size());
      
      if (alquileresMañana.isEmpty() && alquileresHoy.isEmpty()) {
        logger.info("No hay alquileres con devolución hoy o mañana. Proceso finalizado.");
        return;
      }

      // Obtener ubicación de entrega (dirección de la empresa MyCar)
      String ubicacionEntrega = obtenerUbicacionEntrega();
      
      int exitosos = 0;
      int fallidos = 0;
      
      // Enviar recordatorios para alquileres de mañana
      for (Alquiler alquiler : alquileresMañana) {
        try {
          String emailCliente = obtenerEmailCliente(alquiler);
          if (emailCliente == null || emailCliente.trim().isEmpty()) {
            logger.warn("No se encontró email para el cliente del alquiler ID: {}", alquiler.getId());
            fallidos++;
            continue;
          }
          
          String modeloVehiculo = construirModeloVehiculo(alquiler);
          emailService.enviarRecordatorioDevolucion(
              emailCliente,
              modeloVehiculo,
              alquiler.getFechaHasta(),
              ubicacionEntrega,
              false // false = devolución mañana
          );
          
          exitosos++;
          logger.info("Recordatorio enviado exitosamente al cliente: {} (Alquiler ID: {}, devolución mañana)", 
              emailCliente, alquiler.getId());
          
        } catch (Exception e) {
          fallidos++;
          logger.error("Error al enviar recordatorio para alquiler ID {}: {}", 
              alquiler.getId(), e.getMessage(), e);
        }
      }
      
      // Enviar recordatorios para alquileres de hoy (corta duración)
      for (Alquiler alquiler : alquileresHoy) {
        try {
          String emailCliente = obtenerEmailCliente(alquiler);
          if (emailCliente == null || emailCliente.trim().isEmpty()) {
            logger.warn("No se encontró email para el cliente del alquiler ID: {}", alquiler.getId());
            fallidos++;
            continue;
          }
          
          String modeloVehiculo = construirModeloVehiculo(alquiler);
          emailService.enviarRecordatorioDevolucion(
              emailCliente,
              modeloVehiculo,
              alquiler.getFechaHasta(),
              ubicacionEntrega,
              true // true = devolución hoy
          );
          
          exitosos++;
          logger.info("Recordatorio enviado exitosamente al cliente: {} (Alquiler ID: {}, devolución hoy)", 
              emailCliente, alquiler.getId());
          
        } catch (Exception e) {
          fallidos++;
          logger.error("Error al enviar recordatorio para alquiler ID {}: {}", 
              alquiler.getId(), e.getMessage(), e);
        }
      }
      
      logger.info("=== PROCESO DE RECORDATORIOS FINALIZADO ===");
      logger.info("Recordatorios enviados exitosamente: {}", exitosos);
      logger.info("Recordatorios fallidos: {}", fallidos);
      
    } catch (Exception e) {
      logger.error("Error general en el proceso de recordatorios: {}", e.getMessage(), e);
    }
  }

  /**
   * Obtiene la ubicación de entrega (dirección de la empresa MyCar).
   */
  private String obtenerUbicacionEntrega() {
    try {
      // Buscar empresa MyCar
      var empresaOpt = empresaRepository.findByEmailAndEliminadoFalse("mycar.mdz@gmail.com");
      
      if (empresaOpt.isPresent()) {
        Empresa empresa = empresaOpt.get();
        if (empresa.getDireccion() != null) {
          return construirDireccionCompleta(empresa.getDireccion());
        }
      }
      
      // Si no se encuentra, usar una dirección por defecto
      logger.warn("No se encontró dirección de la empresa MyCar, usando dirección por defecto");
      return "San Martín 1234, Centro, Mendoza";
      
    } catch (Exception e) {
      logger.error("Error al obtener ubicación de entrega: {}", e.getMessage(), e);
      return "San Martín 1234, Centro, Mendoza"; // Dirección por defecto
    }
  }

  /**
   * Construye una dirección completa a partir de una entidad Direccion.
   */
  private String construirDireccionCompleta(Direccion direccion) {
    StringBuilder sb = new StringBuilder();
    sb.append(direccion.getCalle());
    if (direccion.getNumero() != null && !direccion.getNumero().trim().isEmpty()) {
      sb.append(" ").append(direccion.getNumero());
    }
    if (direccion.getBarrio() != null && !direccion.getBarrio().trim().isEmpty()) {
      sb.append(", ").append(direccion.getBarrio());
    }
    if (direccion.getLocalidad() != null && direccion.getLocalidad().getNombre() != null) {
      sb.append(", ").append(direccion.getLocalidad().getNombre());
    }
    return sb.toString();
  }

  /**
   * Obtiene el email del cliente del alquiler.
   */
  private String obtenerEmailCliente(Alquiler alquiler) {
    try {
      if (alquiler.getCliente() == null) {
        logger.warn("El alquiler ID {} no tiene cliente asociado", alquiler.getId());
        return null;
      }
      
      // Intentar obtener email de los contactos cargados
      if (alquiler.getCliente().getContactos() != null && !alquiler.getCliente().getContactos().isEmpty()) {
        for (var contacto : alquiler.getCliente().getContactos()) {
          if (contacto instanceof ContactoCorreoElectronico) {
            ContactoCorreoElectronico contactoEmail = 
                (ContactoCorreoElectronico) contacto;
            if (!contactoEmail.isEliminado() && contactoEmail.getEmail() != null) {
              return contactoEmail.getEmail();
            }
          }
        }
      }
      
      // Si no se encontró en los contactos cargados, buscar en el repositorio
      var contactoOpt = contactoCorreoElectronicoRepository
          .findByPersonaIdAndEliminadoFalse(alquiler.getCliente().getId());
      
      if (contactoOpt.isPresent()) {
        return contactoOpt.get().getEmail();
      }
      
      logger.warn("No se encontró email para el cliente ID: {}", alquiler.getCliente().getId());
      return null;
      
    } catch (Exception e) {
      logger.error("Error al obtener email del cliente: {}", e.getMessage(), e);
      return null;
    }
  }

  /**
   * Construye el modelo completo del vehículo (marca + modelo + año).
   */
  private String construirModeloVehiculo(Alquiler alquiler) {
    try {
      if (alquiler.getVehiculo() == null || alquiler.getVehiculo().getCaracteristicaVehiculo() == null) {
        return "Vehículo";
      }
      
      var caracteristica = alquiler.getVehiculo().getCaracteristicaVehiculo();
      StringBuilder sb = new StringBuilder();
      
      if (caracteristica.getMarca() != null) {
        sb.append(caracteristica.getMarca());
      }
      if (caracteristica.getModelo() != null) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(caracteristica.getModelo());
      }
      if (caracteristica.getAnio() != null) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(caracteristica.getAnio());
      }
      
      return sb.length() > 0 ? sb.toString() : "Vehículo";
      
    } catch (Exception e) {
      logger.error("Error al construir modelo del vehículo: {}", e.getMessage(), e);
      return "Vehículo";
    }
  }
}

