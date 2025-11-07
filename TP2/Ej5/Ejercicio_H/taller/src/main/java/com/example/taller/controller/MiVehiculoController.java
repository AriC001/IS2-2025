package com.example.taller.controller;

import com.example.taller.dto.UsuarioSafeDTO;
import com.example.taller.entity.Cliente;
import com.example.taller.entity.Vehiculo;
import com.example.taller.error.ErrorServicio;
import com.example.taller.repository.ClienteRepository;
import com.example.taller.repository.VehiculoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MiVehiculoController {

    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;

    public MiVehiculoController(VehiculoRepository vehiculoRepository, ClienteRepository clienteRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping("/mis-vehiculos")
    public String misVehiculos(Model model, HttpSession session) {
        UsuarioSafeDTO usuario = (UsuarioSafeDTO) session.getAttribute("usuariosession");
        if (usuario == null) {
            return "redirect:/login";
        }

        // find vehicles whose cliente.nombre equals the username (best-effort since Cliente may not be linked to Usuario)
        List<Vehiculo> todos = vehiculoRepository.findAll();
        List<Vehiculo> propios = todos.stream()
                .filter(v -> v.getCliente() != null && usuario.getNombreUsuario().equals(v.getCliente().getNombre()))
                .collect(Collectors.toList());

        model.addAttribute("vehiculos", propios);
        model.addAttribute("usuario", usuario);
        return "views/mis_vehiculos";
    }

    @PostMapping("/mis-vehiculos/add")
    public String addVehiculo(@RequestParam String patente,
                              @RequestParam String marca,
                              @RequestParam String modelo,
                              HttpSession session,
                              Model model) throws ErrorServicio {
        UsuarioSafeDTO usuario = (UsuarioSafeDTO) session.getAttribute("usuariosession");
        if (usuario == null) {
            return "redirect:/login";
        }

        // create or find Cliente matching the username
        Cliente cliente = clienteRepository.findAll().stream()
                .filter(c -> usuario.getNombreUsuario().equals(c.getNombre()))
                .findFirst().orElse(null);

        if (cliente == null) {
            cliente = new Cliente();
            cliente.setNombre(usuario.getNombreUsuario());
            cliente.setApellido("");
            cliente.setEliminado(false);
            cliente = clienteRepository.save(cliente);
        }

        Vehiculo v = new Vehiculo();
        v.setPatente(patente);
        v.setMarca(marca);
        v.setModelo(modelo);
        v.setCliente(cliente);
        v.setEliminado(false);

        vehiculoRepository.save(v);

        return "redirect:/mis-vehiculos";
    }
}
