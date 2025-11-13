package nexora.proyectointegrador2.business.logic.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nexora.proyectointegrador2.business.domain.entity.Alquiler;
import nexora.proyectointegrador2.business.domain.entity.CaracteristicaVehiculo;
import nexora.proyectointegrador2.business.domain.entity.DetalleFactura;
import nexora.proyectointegrador2.business.domain.entity.Factura;
import nexora.proyectointegrador2.business.domain.entity.FormaDePago;
import nexora.proyectointegrador2.business.domain.entity.Vehiculo;
import nexora.proyectointegrador2.business.enums.EstadoAlquiler;
import nexora.proyectointegrador2.business.enums.EstadoFactura;
import nexora.proyectointegrador2.business.enums.TipoPago;
import nexora.proyectointegrador2.business.persistence.repository.AlquilerRepository;
import nexora.proyectointegrador2.business.persistence.repository.FacturaRepository;

@Service
public class FacturaService extends BaseService<Factura, String> {

  private static final Logger logger = LoggerFactory.getLogger(FacturaService.class);

  private final FacturaRepository facturaRepository;

  @Autowired
  private AlquilerService alquilerService;

  @Autowired
  private AlquilerRepository alquilerRepository;

  @Autowired
  private FormaDePagoService formaDePagoService;

  @Autowired
  private FacturaPdfService facturaPdfService;

  @Autowired
  private EmpresaService empresaService;

  @Value("${facturas.dir:uploads/facturas_alquiler}")
  private String directorioFacturas;

  public FacturaService(FacturaRepository repository) {
    super(repository);
    this.facturaRepository = repository;
  }

  /**
   * Genera una factura para un alquiler.
   * Calcula el costo total, crea la factura y detalle, y genera el PDF.
   */
  @Transactional
  public Factura generarFacturaParaAlquiler(String alquilerId, TipoPago tipoPago, String observacion) throws Exception {
    logger.info("Generando factura para alquiler: {}", alquilerId);

    // Validar que no exista ya una factura para este alquiler
    if (facturaRepository.findByAlquilerId(alquilerId).isPresent()) {
      throw new Exception("Ya existe una factura para este alquiler");
    }

    // Obtener alquiler con todas sus relaciones
    Alquiler alquiler = alquilerRepository.findByIdWithRelations(alquilerId)
        .orElseThrow(() -> new Exception("Alquiler no encontrado con ID: " + alquilerId));

    // Validar que el alquiler esté en estado PENDIENTE
    if (alquiler.getEstadoAlquiler() != EstadoAlquiler.PENDIENTE) {
      throw new Exception("Solo se pueden generar facturas para alquileres pendientes de pago");
    }

    // Calcular costo total: días de alquiler × costo diario del vehículo
    long diasAlquiler = calcularDiasAlquiler(alquiler.getFechaDesde(), alquiler.getFechaHasta());
    if (diasAlquiler <= 0) {
      throw new Exception("Las fechas del alquiler no son válidas");
    }

    Double costoDiario = obtenerCostoDiarioVehiculo(alquiler.getVehiculo());
    if (costoDiario == null || costoDiario <= 0) {
      throw new Exception("No se pudo obtener el costo diario del vehículo");
    }

    Double totalPagado = diasAlquiler * costoDiario;

    // Generar número de factura secuencial
    Long numeroFactura = generarNumeroFactura();

    // Crear FormaDePago
    FormaDePago formaDePago = FormaDePago.builder()
        .tipoPago(tipoPago)
        .observacion(observacion)
        .build();
    formaDePago.setEliminado(false);
    formaDePago = formaDePagoService.save(formaDePago);

    // Crear Factura
    Factura factura = Factura.builder()
        .numeroFactura(numeroFactura)
        .fechaFactura(new Date())
        .totalPagado(totalPagado)
        .estado(EstadoFactura.PAGADA)
        .formaDePago(formaDePago)
        .build();
    factura.setEliminado(false);

    // Crear DetalleFactura
    DetalleFactura detalle = DetalleFactura.builder()
        .cantidad((int) diasAlquiler)
        .subtotal(totalPagado)
        .alquiler(alquiler)
        .factura(factura)
        .build();
    detalle.setEliminado(false);

    factura.agregarDetalle(detalle);

    // Guardar factura (el detalle se guarda automáticamente por cascade)
    Factura facturaGuardada = save(factura);

    // Actualizar estado del alquiler a PAGADO
    alquiler.setEstadoAlquiler(EstadoAlquiler.PAGADO);
    alquilerService.update(alquiler.getId(), alquiler);

    logger.info("Factura {} creada exitosamente para alquiler {}", facturaGuardada.getNumeroFactura(), alquilerId);

    // Generar PDF
    try {
      byte[] pdfFactura = facturaPdfService.generarPdfFactura(facturaGuardada);
      
      // Guardar PDF en sistema de archivos
      facturaPdfService.guardarPdfEnDisco(pdfFactura, facturaGuardada);
      logger.info("PDF de factura guardado exitosamente");

    } catch (Exception e) {
      logger.error("Error al generar PDF de factura: {}", e.getMessage(), e);
      // No fallar la generación de factura si el PDF falla
    }

    return facturaGuardada;
  }

  /**
   * Calcula el número de días entre dos fechas.
   */
  private long calcularDiasAlquiler(Date fechaDesde, Date fechaHasta) {
    if (fechaDesde == null || fechaHasta == null) {
      return 0;
    }
    long diffInMillis = fechaHasta.getTime() - fechaDesde.getTime();
    return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
  }

  /**
   * Obtiene el costo diario del vehículo desde su característica.
   */
  private Double obtenerCostoDiarioVehiculo(Vehiculo vehiculo) {
    if (vehiculo == null || vehiculo.getCaracteristicaVehiculo() == null) {
      return null;
    }
    CaracteristicaVehiculo caracteristica = vehiculo.getCaracteristicaVehiculo();
    if (caracteristica.getCostoVehiculo() == null) {
      return null;
    }
    return caracteristica.getCostoVehiculo().getCosto();
  }

  /**
   * Genera el siguiente número de factura secuencial.
   */
  private Long generarNumeroFactura() {
    Long maxNumero = facturaRepository.findMaxNumeroFactura();
    if (maxNumero == null) {
      return 1L;
    }
    return maxNumero + 1;
  }

  /**
   * Busca una factura por el ID del alquiler.
   */
  public Factura findByAlquilerId(String alquilerId) throws Exception {
    return facturaRepository.findByAlquilerId(alquilerId)
        .orElse(null);
  }

  @Override
  protected void validar(Factura entity) throws Exception {
    if (entity.getNumeroFactura() == null) {
      throw new Exception("El número de factura es obligatorio");
    }
    if (entity.getFechaFactura() == null) {
      throw new Exception("La fecha de factura es obligatoria");
    }
    if (entity.getTotalPagado() == null || entity.getTotalPagado() <= 0) {
      throw new Exception("El total pagado debe ser mayor a 0");
    }
    if (entity.getEstado() == null) {
      throw new Exception("El estado de la factura es obligatorio");
    }
    if (entity.getFormaDePago() == null) {
      throw new Exception("La forma de pago es obligatoria");
    }
  }

}

