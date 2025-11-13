package nexora.proyectointegrador2.business.logic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.CaracteristicaVehiculo;
import nexora.proyectointegrador2.business.domain.entity.CostoVehiculo;
import nexora.proyectointegrador2.business.domain.entity.Imagen;
import nexora.proyectointegrador2.business.persistence.repository.CaracteristicaVehiculoRepository;

@Service
public class CaracteristicaVehiculoService extends BaseService<CaracteristicaVehiculo, String> {

  private static final Logger logger = LoggerFactory.getLogger(CaracteristicaVehiculoService.class);

  @Autowired
  private ImagenService imagenService;

  @Autowired
  private CostoVehiculoService costoVehiculoService;

  public CaracteristicaVehiculoService(CaracteristicaVehiculoRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(CaracteristicaVehiculo entity) throws Exception {
    if (entity.getMarca() == null || entity.getMarca().trim().isEmpty()) {
      throw new Exception("La marca es obligatoria");
    }
    if (entity.getModelo() == null || entity.getModelo().trim().isEmpty()) {
      throw new Exception("El modelo es obligatorio");
    }
    if (entity.getAnio() == null || entity.getAnio() < 1900 || entity.getAnio() > 2100) {
      throw new Exception("El año debe estar entre 1900 y 2100");
    }
    if (entity.getCantidadPuerta() == null || entity.getCantidadPuerta() <= 0) {
      throw new Exception("La cantidad de puertas debe ser mayor a 0");
    }
    if (entity.getCantidadAsiento() == null || entity.getCantidadAsiento() <= 0) {
      throw new Exception("La cantidad de asientos debe ser mayor a 0");
    }
    if (entity.getCostoVehiculo() == null) {
      throw new Exception("El costo del vehículo es obligatorio");
    }
  }

  @Override
  protected void preAlta(CaracteristicaVehiculo entity) throws Exception {
    // Manejar imagenVehiculo
    if (entity.getImagenVehiculo() != null) {
      String imagenId = entity.getImagenVehiculo().getId();
      // Si el ID es null o cadena vacía, tratar como nueva imagen
      if (imagenId == null || imagenId.trim().isEmpty()) {
        // Verificar si la imagen tiene contenido válido antes de guardarla
        byte[] contenido = entity.getImagenVehiculo().getContenido();
        if (contenido != null && contenido.length > 0) {
          // Validar tipo de imagen antes de guardar
          validarTipoImagen(entity.getImagenVehiculo());
          Imagen imagenGuardada = imagenService.save(entity.getImagenVehiculo());
          entity.setImagenVehiculo(imagenGuardada);
          logger.info("Imagen guardada exitosamente con ID: {}", imagenGuardada.getId());
        } else {
          // Si no tiene contenido, establecer como null
          entity.setImagenVehiculo(null);
        }
      } else {
        // Si tiene ID válido, buscar la imagen existente
        Imagen imagenExistente = imagenService.findById(imagenId);
        entity.setImagenVehiculo(imagenExistente);
      }
    } 

    // Manejar costoVehiculo
    if (entity.getCostoVehiculo() != null) {
      String costoId = entity.getCostoVehiculo().getId();
      if (costoId == null || costoId.trim().isEmpty()) {
        CostoVehiculo costoGuardado = costoVehiculoService.save(entity.getCostoVehiculo());
        entity.setCostoVehiculo(costoGuardado);
      } else {
        CostoVehiculo costoExistente = costoVehiculoService.findById(costoId);
        entity.setCostoVehiculo(costoExistente);
      }
    }
  }

  @Override
  protected void preUpdate(String id, CaracteristicaVehiculo entity) throws Exception {
    // Manejar imagenVehiculo
    if (entity.getImagenVehiculo() != null) {
      String imagenId = entity.getImagenVehiculo().getId();
      // Si el ID es null o cadena vacía, tratar como nueva imagen
      if (imagenId == null || imagenId.trim().isEmpty()) {
        // Verificar si la imagen tiene contenido válido antes de guardarla
        if (entity.getImagenVehiculo().getContenido() != null && entity.getImagenVehiculo().getContenido().length > 0) {
          // Validar tipo de imagen antes de guardar
          validarTipoImagen(entity.getImagenVehiculo());
          Imagen imagenGuardada = imagenService.save(entity.getImagenVehiculo());
          entity.setImagenVehiculo(imagenGuardada);
        } else {
          // Si no tiene contenido, mantener la imagen existente si existe
          CaracteristicaVehiculo caracteristicaExistente = findById(id);
          entity.setImagenVehiculo(caracteristicaExistente.getImagenVehiculo());
        }
      } else {
        // Si tiene ID válido, verificar si cambió
        CaracteristicaVehiculo caracteristicaExistente = findById(id);
        if (caracteristicaExistente.getImagenVehiculo() == null ||
            !caracteristicaExistente.getImagenVehiculo().getId().equals(imagenId)) {
          Imagen imagenExistente = imagenService.findById(imagenId);
          entity.setImagenVehiculo(imagenExistente);
        }
      }
    }

    // Manejar costoVehiculo
    if (entity.getCostoVehiculo() != null) {
      String costoId = entity.getCostoVehiculo().getId();
      if (costoId == null || costoId.trim().isEmpty()) {
        CostoVehiculo costoGuardado = costoVehiculoService.save(entity.getCostoVehiculo());
        entity.setCostoVehiculo(costoGuardado);
      } else {
        CaracteristicaVehiculo caracteristicaExistente = findById(id);
        if (caracteristicaExistente.getCostoVehiculo() == null ||
            !caracteristicaExistente.getCostoVehiculo().getId().equals(costoId)) {
          CostoVehiculo costoExistente = costoVehiculoService.findById(costoId);
          entity.setCostoVehiculo(costoExistente);
        }
      }
    }
  }

  /**
   * Valida que la imagen sea de tipo JPG o PNG.
   */
  private void validarTipoImagen(Imagen imagen) throws Exception {
    if (imagen == null) {
      return;
    }
    
    // Validar tipo MIME
    if (imagen.getMime() != null && !imagen.getMime().trim().isEmpty()) {
      String mimeType = imagen.getMime().toLowerCase().trim();
      if (!mimeType.equals("image/jpeg") && !mimeType.equals("image/jpg") && !mimeType.equals("image/png")) {
        throw new Exception("El tipo de archivo debe ser JPG o PNG. Tipo recibido: " + imagen.getMime());
      }
    }
    
    // Validar extensión del archivo
    if (imagen.getNombre() != null && !imagen.getNombre().trim().isEmpty()) {
      String nombreArchivo = imagen.getNombre().toLowerCase();
      if (!nombreArchivo.endsWith(".jpg") && !nombreArchivo.endsWith(".jpeg") && !nombreArchivo.endsWith(".png")) {
        throw new Exception("La extensión del archivo debe ser .jpg, .jpeg o .png. Archivo recibido: " + imagen.getNombre());
      }
    }
  }

}
