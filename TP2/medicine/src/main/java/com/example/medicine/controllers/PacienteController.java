package com.example.medicine.controllers;
import lombok.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.medicine.services.PacienteServicio;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import com.example.medicine.model.Paciente;
import com.example.medicine.model.Usuario;
import com.example.medicine.model.FotoPaciente;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {
    @Autowired
    private PacienteServicio pacienteServicio;


    @PostMapping("/actualizar/{id}")
    public String actualizarPaciente(
            @PathVariable String id,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String documento,
            @RequestParam MultipartFile archivo,
            Model model,
            HttpSession session
    ) throws Exception {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if(login==null){
            return "index";
        }
        Optional <Paciente> pacienteOpt = pacienteServicio.findById(id);
        if (!pacienteOpt.isPresent()) {
            throw new Exception("No se encontró el paciente solicitado");
        }
        Paciente paciente = pacienteOpt.get();
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setDocumento(documento);
        if (archivo != null && !archivo.isEmpty()) {
            FotoPaciente fotoExistente = paciente.getFoto();
            if (fotoExistente != null) {
                fotoExistente.setMime(archivo.getContentType());
                fotoExistente.setNombre(archivo.getName());
                fotoExistente.setContenido(archivo.getBytes());
            } else {
                FotoPaciente nuevaFoto = new FotoPaciente();
                nuevaFoto.setMime(archivo.getContentType());
                nuevaFoto.setNombre(archivo.getName());
                nuevaFoto.setContenido(archivo.getBytes());
                paciente.setFoto(nuevaFoto);
                nuevaFoto.setPaciente(paciente);
            }
        }
        pacienteServicio.actualizar(paciente);
        return "redirect:/pacientes";
    }
    @PostMapping("/crear")
    public String crearPaciente(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String documento,
            @RequestParam MultipartFile archivo,
            Model model, HttpSession session
    ) throws Exception {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if(login==null){
            return "index";
        }
        Paciente paciente = new Paciente();
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setDocumento(documento);

        if (archivo != null && !archivo.isEmpty()) {
            FotoPaciente foto = new FotoPaciente();
            foto.setMime(archivo.getContentType());
            foto.setNombre(archivo.getOriginalFilename()); // ⚠️ este es el correcto, no getName()
            foto.setContenido(archivo.getBytes());
            foto.setPaciente(paciente);
            paciente.setFoto(foto);
        }

        pacienteServicio.crear(paciente);
        return "redirect:/pacientes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPaciente(@PathVariable String id, Model model, HttpSession session) throws Exception {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if(login==null){
            return "index";
        }
        Optional <Paciente> pacienteOpt = pacienteServicio.findById(id);
        if (!pacienteOpt.isPresent()) {
            throw new Exception("No se encontró el paciente solicitado");
        }
        Paciente paciente = pacienteOpt.get();
        pacienteServicio.eliminar(paciente);
        return "redirect:/pacientes";
    }

    @GetMapping("")
    public String listarPacientes(Model model,HttpSession session) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if(login==null){
            return "index";
        }
        // La lista de pacientes se añade al modelo bajo el nombre "pacientes"
        List<Paciente> pacientes = pacienteServicio.listarTodos();
        model.addAttribute("pacientes", pacientes);
        

       return "views/pacientes";
    }
    @GetMapping("/nuevo")
    public String mostrarFormularioCreacion(Model model) {
       
        return "views/formPaciente"; // Ruta a formulario.html
    }
    @GetMapping("/foto/{id}")
    public ResponseEntity<byte[]> obtenerFoto(@PathVariable String id) {
        Optional<Paciente> pacienteOpt = pacienteServicio.findById(id);

        if (pacienteOpt.isPresent()) {
            // ACCESO DIRECTO a la única foto
            FotoPaciente foto = pacienteOpt.get().getFoto(); 

            if (foto != null && foto.getContenido() != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.valueOf(foto.getMime()));
                
                return new ResponseEntity<>(foto.getContenido(), headers, HttpStatus.OK);
            }
        }

        // Retorna 404 si el paciente o la foto no existe
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
    }
    @GetMapping("/modificar/{id}") 
    public String mostrarFormularioModificacion(@PathVariable String id, Model model,HttpSession session) throws Exception {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if(login==null){
            return "index";
        }
        
        Optional<Paciente> pacienteOpt = pacienteServicio.findById(id);
        
        if (!pacienteOpt.isPresent()) {
         
            throw new Exception("No se encontró el paciente solicitado para modificar");
        }
        
  
        model.addAttribute("paciente", pacienteOpt.get());
        
    
        return "views/formPaciente"; 
    }
  

}
