package com.sport.proyecto.entidades;

import com.sport.proyecto.enums.tipoEmpleado;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Empleado extends Persona {

    @Enumerated(EnumType.STRING)
    private tipoEmpleado tipoEmpleado;
}
