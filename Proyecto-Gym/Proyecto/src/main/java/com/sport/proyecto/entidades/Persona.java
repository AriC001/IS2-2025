package com.sport.proyecto.entidades;

import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.sport.proyecto.enums.*;
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
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column
    private String nombre;

    @Column
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String clave;

    @Column
    private String telefono;

    @Column
    private tipoDocumento tipoDocumento;

    @Column(unique = true)
    private String numeroDocumento;

    @Column
    private Date fechaNacimiento;

    @Column(nullable = false)
    private boolean eliminado;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "direccion_id")
    private Direccion direccion;
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