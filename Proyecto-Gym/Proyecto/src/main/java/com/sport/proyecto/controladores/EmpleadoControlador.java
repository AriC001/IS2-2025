package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.Departamento;
import com.sport.proyecto.entidades.Direccion;
import com.sport.proyecto.entidades.Empleado;
import com.sport.proyecto.entidades.Sucursal;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.enums.tipoDocumento;
import com.sport.proyecto.enums.tipoEmpleado;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.servicios.DepartamentoServicio;
import com.sport.proyecto.servicios.DireccionServicio;
import com.sport.proyecto.servicios.EmpleadoServicio;
import com.sport.proyecto.servicios.LocalidadServicio;
import com.sport.proyecto.servicios.PaisServicio;
import com.sport.proyecto.servicios.ProvinciaServicio;
import com.sport.proyecto.servicios.SucursalServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sport.proyecto.servicios.UtilServicio;

import java.util.List;

@Controller
@RequestMapping("/empleados")
public class EmpleadoControlador {
  @Autowired
  private EmpleadoServicio empleadoServicio;

  @Autowired 
  private SucursalServicio sucursalServicio;

  @Autowired
  private DireccionServicio direccionServicio;

  @Autowired
  private PaisServicio paisServicio;
  
  @Autowired
  private ProvinciaServicio provinciaServicio;

  @Autowired
  private LocalidadServicio localidadServicio;

  @Autowired
  private DepartamentoServicio departamentoServicio;

  

  @GetMapping("")
  public String empleados(Model model) {
    try{
      List<Empleado> empleados = empleadoServicio.listaEmpleadosActivos();
      model.addAttribute("empleados", empleados);
      return "views/empleados";

    } catch (Exception e){
      model.addAttribute("error", e.getMessage());
      return "views/empleados";
    }
  }
  


  @GetMapping("/nuevo")
  public String nuevoEmpleado(Model model){
    model.addAttribute("empleado", new Empleado());
    model.addAttribute("nuevaDireccion", new Direccion());
    model.addAttribute("tiposEmpleado", tipoEmpleado.values());
    model.addAttribute("tiposDocumento", tipoDocumento.values());
    model.addAttribute("sucursales", sucursalServicio.listarSucursalActivas());
    model.addAttribute("direcciones", direccionServicio.listarDireccionActiva());
    model.addAttribute("paises", paisServicio.listarPaisActivo());
    model.addAttribute("provincias", provinciaServicio.listarProvinciaActiva());
    model.addAttribute("departamentos", departamentoServicio.listarDepartamentoActivo());
    model.addAttribute("localidades", localidadServicio.listarLocalidadActiva());
    return "views/alta_empleado";
  }

  @PostMapping("/crear")
  public String crear(@ModelAttribute("empleado") Empleado empleado,
                      @ModelAttribute("nuevaDireccion") Direccion nuevaDireccion,
                      @RequestParam(name = "crearNuevaDireccion", required = false) String crearNuevaDireccionFlag,
                      RedirectAttributes ra,
                      Model model) {


      try {
          Direccion direccionFinal = null;

          // 1. Si el usuario quiere CREAR NUEVA dirección
          if ("true".equalsIgnoreCase(crearNuevaDireccionFlag)) {
              if (nuevaDireccion.getCalle() == null || nuevaDireccion.getCalle().isBlank()) {
                  throw new Exception("Debe ingresar al menos calle y numeración para la nueva dirección.");
              }
              System.out.println(">>> Creando NUEVA dirección");
              direccionFinal = direccionServicio.guardarDireccion(nuevaDireccion);

          // 2. Si seleccionó una dirección existente
          } else if (empleado.getDireccion() != null && empleado.getDireccion().getId() != null) {
              System.out.println(">>> Usando dirección EXISTENTE ID=" + empleado.getDireccion().getId());
              direccionFinal = direccionServicio.buscarDireccion(empleado.getDireccion().getId());

          // 3. Si no eligió nada
          } else {
              throw new Exception("Debe seleccionar una dirección existente o crear una nueva.");
          }
          empleado.setDireccion(direccionFinal);
          // Crear la sucursal
          empleadoServicio.crearEmpleado(empleado);

          ra.addFlashAttribute("msg", "Sucursal creada exitosamente");
          return "redirect:/empleados";

      } catch (Exception e) {
          e.printStackTrace();
          ra.addFlashAttribute("error", e.getMessage());
          return "views/alta_empleado";
      }
  }
  /* 
  @PostMapping("/crear")
  public String crearEmpleado(@ModelAttribute("empleado") Empleado empleado, ModelMap model) {
   
    try {
      empleadoServicio.crearEmpleado(
              empleado
      );

      model.addAttribute("msg", "Empleado creado exitosamente");
      return "redirect:/empleados"; // redirige a la lista de empleados
    } catch (Exception e) {
      model.put("error", e.getMessage());
      model.addAttribute("empleado", empleado);
      model.addAttribute("tiposEmpleado", tipoEmpleado.values());
      model.addAttribute("tiposDocumento", tipoDocumento.values());
      model.addAttribute("sucursales",sucursalServicio.listarSucursalActivas());
      return "views/alta_empleado"; // vuelve al formulario con error
    }
  } */

  @GetMapping("/guardar")
  public String empleadoForm(Model model) {
    model.addAttribute("empleado", new Empleado());
    model.addAttribute("nuevaDireccion", new Direccion());
    model.addAttribute("tiposEmpleado", tipoEmpleado.values());
    model.addAttribute("tiposDocumento", tipoDocumento.values());
    model.addAttribute("sucursales", sucursalServicio.listarSucursalActivas());
    model.addAttribute("direcciones", direccionServicio.listarDireccionActiva());
    model.addAttribute("paises", paisServicio.listarPaisActivo());
    model.addAttribute("provincias", provinciaServicio.listarProvinciaActiva());
    model.addAttribute("departamentos", departamentoServicio.listarDepartamentoActivo());
    model.addAttribute("localidades", localidadServicio.listarLocalidadActiva());
    return "views/alta_empleado";
  }



/*
  @PostMapping("/guardar")
  public String guardarEmpleado(@RequestParam String nombre, @RequestParam String apellido,@RequestParam tipoDocumento tipoDocuemento,@RequestParam String numeroDocumento,@RequestParam String dia,
                                @RequestParam String mes,
                                @RequestParam String anio,@RequestParam String telefono,@RequestParam String correoElectronico,@RequestParam tipoEmpleado tipoEmpleado,@RequestParam String idSucursal, Usuario usuario, @ModelAttribute Empleado empleado, Model model) {
    try {
      empleadoServicio.guardarEmpleado(nombre,apellido,dia,mes,anio,tipoDocuemento,numeroDocumento,telefono, correoElectronico, tipoEmpleado, idSucursal, usuario);  // ajustá según tu servicio
      model.addAttribute("exito", "Empleado guardado con éxito");
      return "redirect:/empleados";
    } catch (Exception e) {
      model.addAttribute("errorMessage", e.getMessage());
      model.addAttribute("tiposDni", tipoDocumento.values());
      return "views/empleado-formulario";
    }
  }
*/
/*
  @GetMapping("/empleados/formulario")
  public String mostrarFormulario(@RequestParam(required = false) Long id, Model model) {
    if (id != null) {
      Empleado empleado = empleadoServicio.buscarPorId(id);
      model.addAttribute("empleado", empleado);
    } else {
      model.addAttribute("empleado", new Empleado()); // para el caso nuevo
    }
    model.addAttribute("tiposDni", TipoDni.values());
    return "views/empleado-formulario";
  }
*/
  @GetMapping("/editar/{id}")
  public String editar(@PathVariable String id, Model model) {
    try {
        Empleado empleado =empleadoServicio.buscarEmpleado(id).get();
        model.addAttribute("empleado", empleado);
        model.addAttribute("nuevaDireccion", new Direccion());
        model.addAttribute("tiposEmpleado", tipoEmpleado.values());
        model.addAttribute("tiposDocumento", tipoDocumento.values());
        model.addAttribute("sucursales", sucursalServicio.listarSucursalActivas());
        model.addAttribute("direcciones", direccionServicio.listarDireccionActiva());
        model.addAttribute("paises", paisServicio.listarPaisActivo());
        model.addAttribute("provincias", provinciaServicio.listarProvinciaActiva());
        model.addAttribute("departamentos", departamentoServicio.listarDepartamentoActivo());
        model.addAttribute("localidades", localidadServicio.listarLocalidadActiva());
        return "views/alta_empleado";

    } catch (ErrorServicio e) {
      model.addAttribute("error", e.getMessage());
      return "redirect:/empleados";
    }
  }
  @PostMapping("/actualizar/{id}")
  public String actualizar(@PathVariable String id, @ModelAttribute("empleado") Empleado empleado, Model model) {
      try {
          //System.out.println("CONTROLADOR");
          empleadoServicio.modificar(id, empleado.getNombre(), empleado.getApellido(), empleado.getFechaNacimiento(),
              empleado.getTipoDocumento(), empleado.getNumeroDocumento(), empleado.getEmail(), empleado.getTelefono(),
              empleado.getTipoEmpleado(), empleado.getUsuario(), empleado.getSucursal(),empleado.getDireccion().getId());
          return "redirect:/empleados";
      } catch (Exception e) {
         System.err.println(e.getMessage());
          return "redirect:/empleados/editar/" + id;}
  }

  @GetMapping("/eliminar/{id}")
  public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes) {
    try {
      empleadoServicio.eliminarEmpleado(id);
      redirectAttributes.addFlashAttribute("msg", "Empleado eliminada con exito");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al eliminar empleado");
    }
    return "redirect:/empleados";
  }

  
  // usuariosession provided by GlobalControllerAdvice


}

