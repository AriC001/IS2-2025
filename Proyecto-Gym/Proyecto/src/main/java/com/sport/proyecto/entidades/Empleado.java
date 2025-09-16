package com.sport.proyecto.entidades;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("EMPLEADO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Empleado extends Persona {

    @Enumerated(EnumType.STRING)
    private com.sport.proyecto.enums.tipoEmpleado tipoEmpleado;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
