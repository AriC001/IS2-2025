package nexora.proyectointegrador2.business.logic.service;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import nexora.proyectointegrador2.business.domain.entity.Alquiler;
import nexora.proyectointegrador2.business.domain.entity.Cliente;
import nexora.proyectointegrador2.business.domain.entity.Documento;
import nexora.proyectointegrador2.business.domain.entity.Vehiculo;
import nexora.proyectointegrador2.business.persistence.repository.AlquilerRepository;
import nexora.proyectointegrador2.utils.dto.AlquilerDTO;

@Service
public class AlquilerService extends BaseService<Alquiler, String> {

  private static final Logger logger = LoggerFactory.getLogger(AlquilerService.class);
  
  private final AlquilerRepository alquilerRepository;

  @Autowired
  private ClienteService clienteService;

  @Autowired
  private VehiculoService vehiculoService;

  @Autowired
  private DocumentoService documentoService;

  public AlquilerService(AlquilerRepository repository) {
    super(repository);
    this.alquilerRepository = repository;
  }

  /**
   * Sobrescribe findAllActives para usar el método que carga las relaciones.
   * Esto evita problemas de lazy loading al listar alquileres.
   */
  @Override
  public java.util.Collection<Alquiler> findAllActives() throws Exception {
    return alquilerRepository.findAllActivesWithRelations();
  }

  @Override
  protected void validar(Alquiler entity) throws Exception {
    if (entity.getFechaDesde() == null) {
      throw new Exception("La fecha desde es obligatoria");
    }
    if (entity.getFechaHasta() == null) {
      throw new Exception("La fecha hasta es obligatoria");
    }
    if (entity.getFechaDesde().after(entity.getFechaHasta())) {
      throw new Exception("La fecha desde no puede ser posterior a la fecha hasta");
    }
    if (entity.getCliente() == null) {
      throw new Exception("El cliente es obligatorio");
    }
    if (entity.getVehiculo() == null) {
      throw new Exception("El vehículo es obligatorio");
    }
    
    // Validar que el vehículo esté disponible en el rango de fechas
    // (Esta validación se puede mejorar con consultas más complejas)
  }

  @Override
  protected void preAlta(Alquiler entity) throws Exception {
    if (entity.getCliente() != null && entity.getCliente().getId() == null) {
      Cliente clienteGuardado = clienteService.save(entity.getCliente());
      entity.setCliente(clienteGuardado);
    } else if (entity.getCliente() != null && entity.getCliente().getId() != null) {
      Cliente clienteExistente = clienteService.findById(entity.getCliente().getId());
      entity.setCliente(clienteExistente);
    }

    if (entity.getVehiculo() != null && entity.getVehiculo().getId() == null) {
      Vehiculo vehiculoGuardado = vehiculoService.save(entity.getVehiculo());
      entity.setVehiculo(vehiculoGuardado);
    } else if (entity.getVehiculo() != null && entity.getVehiculo().getId() != null) {
      Vehiculo vehiculoExistente = vehiculoService.findById(entity.getVehiculo().getId());
      entity.setVehiculo(vehiculoExistente);
    }

    if (entity.getDocumento() != null && entity.getDocumento().getId() == null) {
      Documento documentoGuardado = documentoService.save(entity.getDocumento());
      entity.setDocumento(documentoGuardado);
    } else if (entity.getDocumento() != null && entity.getDocumento().getId() != null) {
      Documento documentoExistente = documentoService.findById(entity.getDocumento().getId());
      entity.setDocumento(documentoExistente);
    }
  }

  @Override
  protected void preUpdate(String id, Alquiler entity) throws Exception {
    if (entity.getCliente() != null && entity.getCliente().getId() == null) {
      Cliente clienteGuardado = clienteService.save(entity.getCliente());
      entity.setCliente(clienteGuardado);
    } else if (entity.getCliente() != null && entity.getCliente().getId() != null) {
      Alquiler alquilerExistente = findById(id);
      if (alquilerExistente.getCliente() == null ||
          !alquilerExistente.getCliente().getId().equals(entity.getCliente().getId())) {
        Cliente clienteExistente = clienteService.findById(entity.getCliente().getId());
        entity.setCliente(clienteExistente);
      }
    }

    if (entity.getVehiculo() != null && entity.getVehiculo().getId() == null) {
      Vehiculo vehiculoGuardado = vehiculoService.save(entity.getVehiculo());
      entity.setVehiculo(vehiculoGuardado);
    } else if (entity.getVehiculo() != null && entity.getVehiculo().getId() != null) {
      Alquiler alquilerExistente = findById(id);
      if (alquilerExistente.getVehiculo() == null ||
          !alquilerExistente.getVehiculo().getId().equals(entity.getVehiculo().getId())) {
        Vehiculo vehiculoExistente = vehiculoService.findById(entity.getVehiculo().getId());
        entity.setVehiculo(vehiculoExistente);
      }
    }

    if (entity.getDocumento() != null && entity.getDocumento().getId() == null) {
      // Si se está creando un nuevo documento, eliminar el anterior si existe
      Alquiler alquilerExistente = findById(id);
      if (alquilerExistente.getDocumento() != null) {
        Documento documentoAnterior = alquilerExistente.getDocumento();
        // Eliminar archivo físico
        if (documentoAnterior.getPathArchivo() != null && !documentoAnterior.getPathArchivo().trim().isEmpty()) {
          try {
            documentoService.eliminarArchivoFisico(documentoAnterior.getPathArchivo());
          } catch (Exception e) {
            logger.warn("No se pudo eliminar el archivo físico del documento anterior: {}", e.getMessage());
          }
        }
        // Eliminar registro de la base de datos
        try {
          documentoService.delete(documentoAnterior.getId());
        } catch (Exception e) {
          logger.warn("No se pudo eliminar el documento anterior de la base de datos: {}", e.getMessage());
        }
      }
      // Crear el nuevo documento
      Documento documentoGuardado = documentoService.save(entity.getDocumento());
      entity.setDocumento(documentoGuardado);
    } else if (entity.getDocumento() != null && entity.getDocumento().getId() != null) {
      Alquiler alquilerExistente = findById(id);
      if (alquilerExistente.getDocumento() == null ||
          !alquilerExistente.getDocumento().getId().equals(entity.getDocumento().getId())) {
        Documento documentoExistente = documentoService.findById(entity.getDocumento().getId());
        entity.setDocumento(documentoExistente);
      }
    }
  }

  /**
   * Procesa el documento del alquiler: guarda el archivo en disco y rellena los campos del documento en el DTO.
   * Este método se llama desde el controller antes de convertir el DTO a entidad.
   */
  public void procesarDocumentoConArchivo(AlquilerDTO dto, MultipartFile archivoDocumento) throws Exception {
    if (dto == null) {
      throw new Exception("El alquiler no puede ser null");
    }

    // Asegurar que el documento esté inicializado
    if (dto.getDocumento() == null) {
      dto.setDocumento(new nexora.proyectointegrador2.utils.dto.DocumentoDTO());
    }

    // Validar que el documento tenga tipoDocumento (obligatorio)
    if (dto.getDocumento().getTipoDocumento() == null) {
      throw new Exception("El tipo de documento es obligatorio");
    }

    // Validar que el archivo esté presente
    if (archivoDocumento == null || archivoDocumento.isEmpty()) {
      throw new Exception("El archivo del documento es obligatorio");
    }

    // Validar tipo de contenido (PDF o WORD)
    String contentType = archivoDocumento.getContentType();
    if (contentType == null) {
      throw new Exception("No se pudo determinar el tipo de contenido del archivo");
    }
    
    boolean esPdf = contentType.equals("application/pdf");
    boolean esWord = contentType.equals("application/msword") || 
                     contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    
    if (!esPdf && !esWord) {
      throw new Exception("Solo se permiten archivos PDF o WORD. Tipo recibido: " + contentType);
    }

    // Obtener datos del cliente y vehículo
    if (dto.getCliente() == null || dto.getCliente().getId() == null) {
      throw new Exception("El cliente es obligatorio");
    }
    if (dto.getVehiculo() == null || dto.getVehiculo().getId() == null) {
      throw new Exception("El vehículo es obligatorio");
    }

    // Obtener cliente y vehículo desde la base de datos
    // findById puede lanzar excepción si no existe, la capturamos y damos un mensaje más claro
    Cliente cliente;
    try {
      cliente = clienteService.findById(dto.getCliente().getId());
    } catch (Exception e) {
      throw new Exception("Cliente no encontrado con ID: " + dto.getCliente().getId() + ". " + e.getMessage(), e);
    }
    
    Vehiculo vehiculo;
    try {
      vehiculo = vehiculoService.findById(dto.getVehiculo().getId());
    } catch (Exception e) {
      throw new Exception("Vehículo no encontrado con ID: " + dto.getVehiculo().getId() + ". " + e.getMessage(), e);
    }

    // Generar información para el nombre del archivo
    String nombreCliente = (cliente.getNombre() + " " + cliente.getApellido()).trim();
    String marca = null;
    String modelo = null;
    if (vehiculo.getCaracteristicaVehiculo() != null) {
      marca = vehiculo.getCaracteristicaVehiculo().getMarca();
      modelo = vehiculo.getCaracteristicaVehiculo().getModelo();
    }
    // Si no hay marca/modelo, usar la patente como fallback
    if (marca == null || modelo == null) {
      marca = vehiculo.getPatente() != null ? vehiculo.getPatente() : "vehiculo";
      modelo = "";
    }

    // Guardar archivo en disco usando DocumentoService
    // Retorna: [0] = Path completo, [1] = Nombre del archivo, [2] = MimeType
    Object[] resultadoGuardado = documentoService.guardarArchivoEnDisco(archivoDocumento, nombreCliente, marca, modelo);
    Path rutaArchivo = (Path) resultadoGuardado[0];
    String nombreArchivo = (String) resultadoGuardado[1];
    String mimeType = (String) resultadoGuardado[2];

    // Rellenar campos del documento en el DTO
    dto.getDocumento().setPathArchivo(rutaArchivo.toString());
    dto.getDocumento().setNombreArchivo(nombreArchivo);
    dto.getDocumento().setMimeType(mimeType);
  }

}

