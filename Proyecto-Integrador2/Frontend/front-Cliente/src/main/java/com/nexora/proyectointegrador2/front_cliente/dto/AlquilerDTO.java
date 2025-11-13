package  com.nexora.proyectointegrador2.front_cliente.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlquilerDTO extends BaseDTO {

  private VehiculoDTO vehiculo ;
  private UsuarioDTO usuario;
  private ClienteDTO cliente; // Campo para almacenar el ClienteDTO del backend
  private Date fechaDesde;
  private Date fechaHasta;
  
  @Setter(AccessLevel.NONE) // Excluir del setter generado por Lombok
  @Getter
  private String estado; // Mapea desde estadoAlquiler del backend
  
  // Setter personalizado para manejar la conversión del enum a String
  // Usamos @JsonProperty directamente en el setter para que Jackson lo use
  @JsonProperty("estadoAlquiler")
  public void setEstado(Object estadoValue) {
    // Logging para debugging
    System.out.println("AlquilerDTO.setEstado llamado con valor: " + estadoValue + " (tipo: " + (estadoValue != null ? estadoValue.getClass().getName() : "null") + ")");
    
    if (estadoValue == null) {
      this.estado = null;
      System.out.println("Estado establecido como null");
    } else if (estadoValue instanceof String) {
      this.estado = (String) estadoValue;
      System.out.println("Estado establecido como String: " + this.estado);
    } else {
      // Si viene como enum u otro tipo, convertirlo a String usando toString()
      this.estado = estadoValue.toString();
      System.out.println("Estado convertido desde " + estadoValue.getClass().getName() + " a String: " + this.estado);
    }
  }
  
  // Getter con @JsonProperty para serialización
  @JsonProperty("estadoAlquiler")
  public String getEstadoAlquiler() {
    return this.estado;
  }
  
  private DocumentoDTO documento;

  // Compatibility getters/setters: some backend implementations expect the nested
  // user to be named 'cliente'. Provide delegation methods so the serialized
  // JSON will include both 'usuario' and 'cliente' properties (client-side).
  // NOTA: Este método ahora devuelve null si cliente es null, pero mantiene compatibilidad
  public UsuarioDTO getClienteAsUsuario() {
    return this.usuario;
  }

  public void setClienteAsUsuario(UsuarioDTO cliente) {
    this.usuario = cliente;
  }
}
