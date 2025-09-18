package com.sport.proyecto.entidades;

import com.sport.proyecto.enums.tipoDocumento;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "persona")
public abstract class Persona implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String nombre;

    @Column
    private String apellido;

    @Column(nullable = false, unique = true)
    private String correoElectronico;

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

    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

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