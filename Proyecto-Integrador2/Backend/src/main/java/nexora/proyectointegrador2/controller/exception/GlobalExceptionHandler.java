package nexora.proyectointegrador2.controller.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

/**
 * Manejador global de excepciones para todos los controladores.
 * Proporciona respuestas consistentes y estructuradas para diferentes tipos de errores.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Maneja errores de validación de Bean Validation (@Valid).
   * Se dispara cuando los datos del request body no cumplen las validaciones.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    logger.warn("Error de validación: {}", ex.getMessage());
    
    Map<String, String> fieldErrors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Valor inválido",
            (existing, replacement) -> existing // En caso de múltiples errores en el mismo campo, mantener el primero
        ));

    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", "Validation Error");
    response.put("message", "Error de validación en los datos enviados");
    response.put("fieldErrors", fieldErrors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * Maneja violaciones de constraints de validación.
   */
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {
    logger.warn("Error de constraint violation: {}", ex.getMessage());
    
    Map<String, String> violations = ex.getConstraintViolations()
        .stream()
        .collect(Collectors.toMap(
            violation -> violation.getPropertyPath().toString(),
            ConstraintViolation::getMessage
        ));

    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", "Constraint Violation");
    response.put("message", "Error en las restricciones de los datos");
    response.put("violations", violations);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * Maneja errores cuando una entidad no es encontrada.
   */
  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
    logger.warn("Entidad no encontrada: {}", ex.getMessage());
    
    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.NOT_FOUND.value());
    response.put("error", "Not Found");
    response.put("message", ex.getMessage() != null ? ex.getMessage() : "El recurso solicitado no fue encontrado");

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  /**
   * Maneja errores de tipo de argumento incorrecto (ej: pasar String cuando se espera Integer).
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, Object>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
    logger.warn("Error de tipo de argumento: {}", ex.getMessage());
    
    String message = String.format(
        "El parámetro '%s' debe ser de tipo %s",
        ex.getName(),
        ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
    );

    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", "Type Mismatch");
    response.put("message", message);
    response.put("parameter", ex.getName());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * Maneja IllegalArgumentException (argumentos inválidos).
   */
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
    logger.warn("Argumento ilegal: {}", ex.getMessage());
    
    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", "Invalid Argument");
    response.put("message", ex.getMessage() != null ? ex.getMessage() : "Argumento inválido");

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * Maneja IllegalStateException (estado inválido de la aplicación).
   */
  @ExceptionHandler(IllegalStateException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<Map<String, Object>> handleIllegalStateException(IllegalStateException ex) {
    logger.warn("Estado ilegal: {}", ex.getMessage());
    
    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.CONFLICT.value());
    response.put("error", "Conflict");
    response.put("message", ex.getMessage() != null ? ex.getMessage() : "Conflicto en el estado de la operación");

    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  /**
   * Maneja excepciones genéricas no capturadas por otros handlers.
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
    logger.error("Error interno del servidor", ex);
    
    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.put("error", "Internal Server Error");
    response.put("message", "Ha ocurrido un error inesperado. Por favor, contacte al administrador.");
    
    // Solo incluir detalles técnicos en desarrollo (puedes usar un profile de Spring)
    // response.put("details", ex.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  /**
   * Maneja NullPointerException.
   */
  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Map<String, Object>> handleNullPointerException(NullPointerException ex) {
    logger.error("NullPointerException capturado", ex);
    
    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.put("error", "Null Pointer Error");
    response.put("message", "Se ha producido un error de referencia nula. Por favor, contacte al administrador.");

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  /**
   * Maneja errores de acceso denegado o autenticación.
   * (Puedes expandir esto con Spring Security si lo usas)
   */
  @ExceptionHandler(SecurityException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<Map<String, Object>> handleSecurityException(SecurityException ex) {
    logger.warn("Error de seguridad: {}", ex.getMessage());
    
    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.FORBIDDEN.value());
    response.put("error", "Forbidden");
    response.put("message", "No tiene permisos para realizar esta operación");

    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
  }

}
