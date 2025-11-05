package nexora.proyectointegrador2.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexora.proyectointegrador2.business.enums.TipoImagen;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "imagen")
public class Imagen extends BaseEntity<String> {

  @Column(nullable = false, length = 100)
  private String nombre;

  @Column(length = 50)
  private String mime;

  @Lob
  @Column(nullable = false, columnDefinition = "LONGBLOB")
  private byte[] contenido;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private TipoImagen tipoImagen;
  
}
