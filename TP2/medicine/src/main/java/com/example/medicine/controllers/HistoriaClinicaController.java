package com.example.medicine.controllers;

import org.springframework.stereotype.Controller;

import com.example.medicine.services.HistoriaClinicaServicio;
import com.example.medicine.services.MedicoServicio;
import com.example.medicine.services.PacienteServicio;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import com.example.medicine.services.DetalleServicio;
import com.example.medicine.services.UsuarioServicio;
import com.example.medicine.model.Medico;
import com.example.medicine.model.Paciente;
import com.example.medicine.model.Usuario;
import com.example.medicine.errors.ErrorServicio;
import com.example.medicine.model.DetalleHistoriaClinica;
import com.example.medicine.model.HistoriaClinica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Controller
public class HistoriaClinicaController {
    @Autowired
    private HistoriaClinicaServicio historiaClinicaServicio;
    @Autowired
    private MedicoServicio medicoServicio;
    @Autowired
    private PacienteServicio pacienteServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private DetalleServicio detalleServicio;

    @RequestMapping("/historiasClinicas")
    @GetMapping("")
    public String historiasClinicas(Model model) {
        model.addAttribute("historias", historiaClinicaServicio.listarTodasLasHistoriasClinicas());
        return "views/historias";
    }
    @GetMapping("/historiasClinicas/nueva")
    public String nuevaHistoriaClinica(Model model, HttpSession session) throws ErrorServicio {
        try{
             Usuario login = (Usuario) session.getAttribute("usuariosession");
             if (login==null){
                System.out.print("Hay que iniciar sesión");
                ErrorServicio e = new ErrorServicio("Debe iniciar sesión");
                return "index";
            }
             model.addAttribute("historiaClinica", new HistoriaClinica());
             model.addAttribute("medicos", medicoServicio.listarMedicosActivos());
             model.addAttribute("pacientes", pacienteServicio.listarActivos());
            return "views/formHistoriaClinica";
        } catch (ErrorServicio e) {
            e = new ErrorServicio("Error al cargar el formulario de historia clínica");
            model.addAttribute("error", e);
            return "views/historias";
        }
    }

    @GetMapping("/historiasClinicas/ver/{id}")
    public String verHistoriaClinica(@PathVariable("id") String id, Model model, HttpSession session) throws Exception {
        HistoriaClinica historiaClinica = historiaClinicaServicio.buscarPorId(id).get();
        if (historiaClinica == null) {
            throw new Exception("Historia Clinica no encontrada");
        }
        model.addAttribute("historiaClinica", historiaClinica);
        model.addAttribute("detalleHistoria", historiaClinica.getDetallesHistoriaClinica());
        model.addAttribute("medicos", historiaClinica.getMedicos());
        model.addAttribute("paciente", historiaClinica.getPaciente());
        return "views/verHistoria";
    }
    @GetMapping("historiasClinicas/editar/{id}")
    public String editar(@PathVariable String id, Model model) {
        HistoriaClinica historiaClinica = historiaClinicaServicio.buscarPorId(id).get();
        model.addAttribute("historiaClinica", historiaClinica);
        model.addAttribute("detalleHistoria", historiaClinica.getDetallesHistoriaClinica());
        model.addAttribute("medicos", medicoServicio.listarMedicosActivos());
        model.addAttribute("pacientes", pacienteServicio.listarActivos());
        return "views/modificarHistoriaClinica";
    }

    
    @PostMapping("/historiasClinicas/crear")
    public String crearHistoria(
        @RequestParam String pacienteId,
        @RequestParam("detalles[]") List<String> detalles,
        @RequestParam("fechas[]") List<LocalDate> fechas,
        @RequestParam("medicoIds[]") List<String> medicoIds,
        HttpSession session,
        ModelMap model
    ) throws ErrorServicio{
        try{
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            List<Medico> medicos =new ArrayList<>();
            System.out.println("Paciente: " + pacienteId);
            System.out.println("Detalles: " + detalles);
            System.out.println("Fechas: " + fechas);
            System.out.println("Médicos: " + medicoIds);
            System.out.println("Tamaño detalles: " + detalles.size());
            System.out.println("Tamaño fechas: " + fechas.size());
            System.out.println("Tamaño médicos: " + medicoIds.size());

        
            for (int i = 0; i < medicoIds.size(); i++) {
                Medico medico = medicoServicio.buscarMedico(medicoIds.get(i));
                medicos.add(medico);
                System.out.println("Medicos");

            }
            HistoriaClinica historiaClinica = new HistoriaClinica();
            historiaClinica.setUsuario(login);
            List<DetalleHistoriaClinica> d=historiaClinica.crearDetalleHistoriaClinica(detalles, fechas,medicos);
            historiaClinica.setPaciente(pacienteServicio.findById(pacienteId).get());
            historiaClinica.setDetallesHistoriaClinica(d);
            // Guardar la historia clínica y sus detalles
            historiaClinicaServicio.validar(historiaClinica);
            historiaClinicaServicio.guardar(historiaClinica);
            return "redirect:/historiasClinicas";
        }catch (ErrorServicio e){
            model.put("error",e.getMessage());
            return "views/formHistoriaClinica";
        }
    }
   
        // === Guardar los cambios de los detalles ===
    @PostMapping("/historiasClinicas/guardarDetalles/{id}")
    public String guardarDetalles(
            @PathVariable("id") String id,
            @ModelAttribute("detalles") DetalleFormWrapper detallesWrapper) {

        HistoriaClinica historiaClinica = historiaClinicaServicio.buscarPorId(id).get();
        List<DetalleHistoriaClinica> detalles = detallesWrapper.getDetalles();

        for (DetalleHistoriaClinica detalle : detalles) {
            detalle.setHistoriaClinica(historiaClinica);
            detalleServicio.guardar(detalle);
        }

        return "redirect:/historiasClinicas";
    }

    @GetMapping("/historiasClinicas/eliminar/{id}")
    public String eliminar(@PathVariable String id,
                        RedirectAttributes redirectAttributes,
                        HttpSession session) {
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            if (login == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Debe iniciar sesión.");
                return "redirect:/login";
            }

            // Buscamos la historia clínica existente
            HistoriaClinica hClinica = historiaClinicaServicio.buscarPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Historia clínica no encontrada."));

            // Marcamos los detalles como eliminados
            if (hClinica.getDetallesHistoriaClinica() != null) {
                for (DetalleHistoriaClinica d : hClinica.getDetallesHistoriaClinica()) {
                    detalleServicio.eliminar(d);
                }
            }

            // Marcamos la historia clínica como eliminada
            hClinica.setEliminado(true);

            // Guardamos la historia actualizada
            historiaClinicaServicio.guardar(hClinica);

            redirectAttributes.addFlashAttribute("successMessage", "Historia clínica eliminada correctamente.");
            return "redirect:/historiasClinicas";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar: " + e.getMessage());
            return "redirect:/historiasClinicas";
        }
    }

    // Clase auxiliar para que Thymeleaf mapee la lista
    public static class DetalleFormWrapper {
        private List<DetalleHistoriaClinica> detalles;

        public List<DetalleHistoriaClinica> getDetalles() {
            return detalles;
        }

        public void setDetalles(List<DetalleHistoriaClinica> detalles) {
            this.detalles = detalles;
        }
    }
}
        
    


