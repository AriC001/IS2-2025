package nexora.proyectointegrador2.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexora.proyectointegrador2.business.enums.TipoDocumentacion;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "documento")
public class Documento extends BaseEntity<String> {

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_documento", nullable = false, length = 30)
  private TipoDocumentacion tipoDocumento;

  @Column(name = "observacion", length = 500)
  private String observacion;

  @Column(name = "path_archivo", nullable = false, length = 200)
  private String pathArchivo;

  @Column(name = "nombre_archivo", nullable = false, length = 100)
  private String nombreArchivo;

}
