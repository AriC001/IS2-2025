package com.nexora.proyecto.gestion.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.nexora.proyecto.gestion.business.logic.service.AlquilerService;
import com.nexora.proyecto.gestion.business.logic.service.ClienteService;
import com.nexora.proyecto.gestion.business.logic.service.VehiculoService;
import com.nexora.proyecto.gestion.dto.AlquilerDTO;
import com.nexora.proyecto.gestion.dto.ClienteDTO;
import com.nexora.proyecto.gestion.dto.DocumentoDTO;
import com.nexora.proyecto.gestion.dto.VehiculoDTO;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/alquileres")
public class AlquilerController extends BaseController<AlquilerDTO, String> {

    private static final Logger logger = LoggerFactory.getLogger(AlquilerController.class);
    private final ClienteService clienteService;
    private final VehiculoService vehiculoService;

    // URL base del backend REST (ajustar según tu entorno)
    private static final String DOCUMENTO_API_URL = "http://localhost:8080/api/v1/documentos/upload";

    public AlquilerController(AlquilerService alquilerService, ClienteService clienteService, VehiculoService vehiculoService) {
        super(alquilerService, "alquiler", "alquileres");
        this.clienteService = clienteService;
        this.vehiculoService = vehiculoService;
    }

    @Override
    protected AlquilerDTO createNewEntity() {
        AlquilerDTO alquiler = new AlquilerDTO();
        alquiler.setCliente(new ClienteDTO());
        alquiler.setVehiculo(new VehiculoDTO());
        alquiler.setDocumento(new DocumentoDTO());
        return alquiler;
    }

    @Override
    protected void prepareFormModel(Model model) {
        try {
            model.addAttribute("clientes", clienteService.findAllActives());
            model.addAttribute("vehiculos", vehiculoService.findAllActives());
        } catch (Exception e) {
            logger.error("Error al preparar modelo del formulario: {}", e.getMessage());
        }
    }

    @GetMapping("/{id}/documentos")
    public String mostrarFormularioDocumentos(@PathVariable("id") String alquilerId, Model model) {
        model.addAttribute("alquilerId", alquilerId);
        return "alquileres/subir-documento"; // plantilla Thymeleaf que crearás
    }

    @PostMapping("/{id}/documentos")
    public String subirDocumento(
            @PathVariable("id") String alquilerId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("tipoDocumento") String tipoDocumento,
            Model model) {

        try {
            DocumentoDTO documento = documentoService.uploadDocumento(file, tipoDocumento, alquilerId);
            model.addAttribute("mensaje", "Documento cargado exitosamente: " + documento.getNombreArchivo());
        } catch (Exception e) {
            logger.error("Error al subir documento: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al subir documento: " + e.getMessage());
        }

        return "redirect:/alquileres/" + alquilerId + "/documentos";
    }
}
