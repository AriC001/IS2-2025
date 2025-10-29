package com.example.medicine.controllers;
import lombok.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.medicine.model.Medico;
import com.example.medicine.model.FotoPaciente;
import com.example.medicine.model.Paciente;
import com.example.medicine.model.Usuario;
import com.example.medicine.services.MedicoServicio;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/medicos")
@Controller
@RequiredArgsConstructor
public class MedicoController {
    @Autowired
    private MedicoServicio medicoServicio;

    @GetMapping("")
    public String listarMedicos(Model model) {
        model.addAttribute("medicos", medicoServicio.listarMedicos());
        return "views/medicos";
    }

    @PostMapping("/crear")
    public String crearMedico(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String documento,
            Model model,
            HttpSession session
    ) throws Exception {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if(login==null){
            return "index";
        }
        Medico medico = new Medico();
        medico.setNombre(nombre);
        medico.setApellido(apellido);
        medico.setDocumento(documento);
        medicoServicio.crear(medico);
        return "redirect:/medicos";
    }

    @GetMapping("/nuevo")
    public String nuevoMedico() {
        return "views/formMedico";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarMedico(
            @PathVariable String id,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String documento,
            Model model,
            HttpSession session
    )  throws Exception {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if(login==null){
            return "index";
        }
        Medico medico = medicoServicio.buscarMedico(id);
        medico.setNombre(nombre);
        medico.setApellido(apellido);
        medico.setDocumento(documento);
        medicoServicio.actualizar(medico);
        return "redirect:/medicos";
    }

    @GetMapping("/modificar/{id}") // <--- ESTE MÉTODO ES EL QUE FALTA
    public String mostrarFormularioModificacion(@PathVariable String id, Model model, HttpSession session) throws Exception {
        Medico medico = medicoServicio.buscarMedico(id);
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if(login==null){
            return "index";
        }
        if (medico == null) {
            // En lugar de una excepción cruda, podrías redirigir con un mensaje de error.
            throw new Exception("No se encontró el médico solicitado para modificar");
        }

        // 1. Añade el médico al modelo para que el formulario se prellene
        model.addAttribute("medico", medico);

        // 2. Retorna la vista del formulario (asumo que se llama formMedico.html)
        return "views/formMedico";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMedico(@PathVariable String id, Model model, HttpSession session) throws Exception {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if(login==null){
            return "index";
        }
        Medico medico = medicoServicio.buscarMedico(id);
        if (medico == null) {
            throw new Exception("No se encontró el médico solicitado");
        }
        medicoServicio.eliminar(medico);
        return "redirect:/medicos";
    }
}
