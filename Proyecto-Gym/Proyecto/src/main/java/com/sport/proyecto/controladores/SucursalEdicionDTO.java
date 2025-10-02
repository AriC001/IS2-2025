package com.sport.proyecto.controladores;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SucursalEdicionDTO {
    private String id;
    private String nombre;
    private String empresaId;

    // Dirección
    private String direccionId;
    private String paisId;
    private String provinciaId;
    private String departamentoId;
    private String localidadId;
    private String calle;
    private String numeracion;
    private String barrio;
    private String casaDepartamento;
    private String manzanaPiso;
    private String latitud;
    private String longitud;
    private String referencia;
}

