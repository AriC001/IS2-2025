package nexora.proyectointegrador2.utils.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nexora.proyectointegrador2.business.enums.TipoContacto;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ContactoTelefonicoDTO.class),
    @JsonSubTypes.Type(value = ContactoCorreoElectronicoDTO.class)
})
public abstract class ContactoDTO extends BaseDTO {

  private TipoContacto tipoContacto;
  private String observacion;
  private String personaId; // ID de la persona (Cliente o Empleado) asociada al contacto

}
