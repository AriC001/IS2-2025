package com.sport.proyecto.controladores;
import com.sport.proyecto.entidades.*;
import com.sport.proyecto.servicios.SucursalServicio;
import com.sport.proyecto.servicios.DepartamentoServicio;
import com.sport.proyecto.servicios.DireccionServicio;
import com.sport.proyecto.servicios.EmpresaServicio;
import com.sport.proyecto.servicios.LocalidadServicio;
import com.sport.proyecto.servicios.PaisServicio;
import com.sport.proyecto.servicios.ProvinciaServicio;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ubicacion")
public class UbicacionControlador {

    @Autowired
    private ProvinciaServicio provinciaServicio;

    @Autowired
    private DepartamentoServicio departamentoServicio;

    @Autowired
    private LocalidadServicio localidadServicio;

    @GetMapping("/provincias/{paisId}")
    @ResponseBody
    public List<Provincia> getProvinciasPorPais(@PathVariable String paisId) {
        return provinciaServicio.buscarProvinciaPorPais(paisId);
    }

    @GetMapping("/departamentos/{provinciaId}")
    @ResponseBody
    public List<Departamento> getDepartamentosPorProvincia(@PathVariable String provinciaId) {
        return departamentoServicio.buscarDepartamentoPorProvincia(provinciaId);
    }

    @GetMapping("/localidades/{departamentoId}")
    @ResponseBody
    public List<Localidad> getLocalidadesPorDepartamento(@PathVariable String departamentoId) {
        return localidadServicio.buscarLocalidadPorDepartamento(departamentoId);
    }
}
