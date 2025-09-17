package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_persona", discriminatorType = DiscriminatorType.STRING)
@Table(name = "persona")
public abstract class Persona implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Column
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String clave;

    @Column(nullable = false)
    private boolean eliminado;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
//Persona
//-nombre: string
//-apellido: string
//-fechaNacimiento:Date
//-tipoDocumento:Enum
//-numeroDocumento:String
//-telefono:String
//-correoElectronico:String
//-eliminado: boolean
//--
//+ buscarPersona(id:String):Persona
//+ eliminarPersona(id:String):void