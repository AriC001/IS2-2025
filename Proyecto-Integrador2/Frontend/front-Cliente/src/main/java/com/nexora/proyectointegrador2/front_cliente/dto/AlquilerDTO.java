package  com.nexora.proyectointegrador2.front_cliente.dto;

import java.util.Date;

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
  private Date fechaDesde;
  private Date fechaHasta;
  private DocumentoDTO documento;

  // Compatibility getters/setters: some backend implementations expect the nested
  // user to be named 'cliente'. Provide delegation methods so the serialized
  // JSON will include both 'usuario' and 'cliente' properties (client-side).
  public UsuarioDTO getCliente() {
    return this.usuario;
  }

  public void setCliente(UsuarioDTO cliente) {
    this.usuario = cliente;
  }
}
