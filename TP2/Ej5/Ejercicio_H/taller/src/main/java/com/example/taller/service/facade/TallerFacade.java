package com.example.taller.service.facade;

import com.example.taller.entity.Cliente;
import com.example.taller.entity.HistorialArreglo;
import com.example.taller.entity.Mecanico;
import com.example.taller.entity.Vehiculo;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.example.taller.service.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import com.example.taller.error.*;
@Service
public class TallerFacade {

    private final VehiculoService vehiculoService;
    private final ClienteService clienteService;
    private final HistorialArregloService historialService;
    private final MecanicoService mecanicoService;

    public TallerFacade(VehiculoService vehiculoService,
                        ClienteService clienteService,
                        HistorialArregloService historialService,
                        MecanicoService mecanicoService) {
        this.vehiculoService = vehiculoService;
        this.clienteService = clienteService;
        this.historialService = historialService;
        this.mecanicoService = mecanicoService;
    }

    @Transactional
    public void registrarReparacion(String idVehiculo,
                                    String idMecanico,
                                    String detalle,
                                    String fechaArregloStr)
            throws ErrorServicio {


        Vehiculo vehiculo = vehiculoService.obtener(idVehiculo)
                .orElseThrow(() -> new ErrorServicio("Vehículo no encontrado"));
        Mecanico mecanico = mecanicoService.obtener(idMecanico)
                .orElseThrow(() -> new ErrorServicio("Mecánico no encontrado"));

        HistorialArreglo h = historialService.createEmpty();
        h.setVehiculo(vehiculo);
        h.setMecanico(mecanico);
        h.setDetalle(detalle);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime ldt = LocalDateTime.parse(fechaArregloStr, formatter);


            Date fechaComoDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

            h.setFecha(fechaComoDate);
        } catch (Exception e) {

            h.setFecha(new Date());
        }

        historialService.alta(h);
    }

    public List<HistorialArreglo> obtenerReparacionesCliente(String idCliente)
            throws ErrorServicio {
        Cliente cliente = clienteService.obtener(idCliente)
                .orElseThrow(() -> new ErrorServicio("Cliente no encontrado"));

        return historialService.listarActivos().stream()
                .filter(h -> h.getVehiculo() != null
                        && h.getVehiculo().getCliente() != null
                        && h.getVehiculo().getCliente().getId().equals(cliente.getId()))
                .toList();
    }
}