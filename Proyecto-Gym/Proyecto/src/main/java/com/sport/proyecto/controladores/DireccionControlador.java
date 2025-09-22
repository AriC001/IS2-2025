package com.sport.proyecto.controladores;


import com.sport.proyecto.entidades.*;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.servicios.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/direccion")
public class DireccionControlador {

  @Autowired
  private DireccionServicio direccionServicio;
  @Autowired
  private PaisServicio paisServicio;
  @Autowired
  private ProvinciaServicio provinciaServicio;
  @Autowired
  private DepartamentoServicio departamentoServicio;
  @Autowired
  private LocalidadServicio localidadServicio;

  @GetMapping("")
  public String listar(Model model){
    try {
      model.addAttribute("direcciones", direccionServicio.listarDireccionActiva());
      return "views/direcciones";
    } catch (Exception e) {
      return "views/direcciones";
    }
  }

  @GetMapping("/nuevo")
  public String nuevo(Model model){
    Direccion direccion = new Direccion();
    direccion.setLocalidad(new Localidad()); // para que no sea null
    model.addAttribute("direccion", direccion);
    model.addAttribute("paises", paisServicio.listarPais());
    model.addAttribute("provincias", provinciaServicio.listarProvincia());       // todas
    model.addAttribute("departamentos", departamentoServicio.listarDepartamento()); // todas
    model.addAttribute("localidades", localidadServicio.listarLocalidad());     // todas
    return "views/direccion-form";
  }

  @PostMapping("/crear")
  public String crear(@ModelAttribute("direccion") Direccion direccion, Model model) {
    try {
      direccionServicio.crearDireccion(direccion.getCalle(), direccion.getNumeracion(), direccion.getBarrio(), direccion.getManzanaPiso(), direccion.getCasaDepartamento(),
        direccion.getReferencia(), direccion.getLocalidad().getId());
      return "redirect:/direccion";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      model.addAttribute("direccion", direccion);
      return "views/direccion-form";
    }
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable String id, Model model) {
    try {
      Direccion direccion = direccionServicio.buscarDireccion(id);
      model.addAttribute("direccion", direccion);
      model.addAttribute("paises", paisServicio.listarPais());
      model.addAttribute("provincias", provinciaServicio.listarProvincia());       // todas
      model.addAttribute("departamentos", departamentoServicio.listarDepartamento()); // todas
      model.addAttribute("localidades", localidadServicio.listarLocalidad());     // todas
      return "views/direccion-form";
    } catch (ErrorServicio e) {
      model.addAttribute("error", e.getMessage());
      return "redirect:/direccion";
    }
  }

  @PostMapping("/actualizar/{id}")
  public String actualizar(@PathVariable String id, @ModelAttribute("direccion") Direccion direccion, Model model) {
    try {
      direccionServicio.modificarDireccion(id, direccion.getCalle(), direccion.getNumeracion(), direccion.getBarrio(), direccion.getManzanaPiso(), direccion.getCasaDepartamento(),
        direccion.getReferencia(), direccion.getLocalidad().getId());
      return "redirect:/direccion";
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      model.addAttribute("paises", paisServicio.listarPais());
      model.addAttribute("provincias", provinciaServicio.listarProvincia());       // todas
      model.addAttribute("departamentos", departamentoServicio.listarDepartamento()); // todas
      model.addAttribute("localidades", localidadServicio.listarLocalidad());     // todas
      return "redirect:/direccion/editar/" + id;
    }
  }

  @GetMapping("/eliminar/{id}")
  public String eliminar(@PathVariable String id, Model model) {
    try {
      direccionServicio.eliminarDireccion(id);
      return "redirect:/direccion";
    } catch (ErrorServicio e) {
      model.addAttribute("error", e.getMessage());
      return "redirect:/direccion";
    }
  }

  /* ---------- endpoints JSON para selects dependientes ---------- */

  /*@GetMapping("/api/provincias/{paisId}")
  @ResponseBody
  public ResponseEntity<List<Map<String,Object>>> provinciasPorPais(@PathVariable Long paisId) {
    List<Provincia> list = provinciaServicio.buscarProvinciaPorPais(paisId);
    List<Map<String, ? extends Serializable>> out = list.stream()
      .map(p -> Map.of("id", p.getId(), "nombre", p.getNombre()))
      .collect(Collectors.toList());
    return (ResponseEntity<List<Map<String, Object>>>) ResponseEntity.ok();
  }

  @GetMapping("/api/departamentos/{provinciaId}")
  @ResponseBody
  public ResponseEntity<List<Map<String,Object>>> departamentosPorProvincia(@PathVariable Long provinciaId) {
    List<Departamento> list = departamentoServicio.buscarDepartamentoPorProvincia(provinciaId);
    List<Map<String, ? extends Serializable>> out = list.stream()
      .map(d -> Map.of("id", d.getId(), "nombre", d.getNombre()))
      .collect(Collectors.toList());
    return (ResponseEntity<List<Map<String, Object>>>) ResponseEntity.ok();
  }

  @GetMapping("/api/localidades/{departamentoId}")
  @ResponseBody
  public ResponseEntity<List<Map<String,Object>>> localidadesPorDepartamento(@PathVariable Long departamentoId) {
    List<Localidad> list = localidadServicio.buscarLocalidadPorDepartamento(departamentoId);
    List<Map<String, ? extends Serializable>> out = list.stream()
      .map(l -> Map.of("id", l.getId(), "nombre", l.getNombre()))
      .collect(Collectors.toList());
    return (ResponseEntity<List<Map<String, Object>>>) ResponseEntity.ok();
  }*/

  @ModelAttribute("usuariosession")
  public Usuario usuarioSession(HttpSession session) {
    return (Usuario) session.getAttribute("usuariosession");
  }

}
