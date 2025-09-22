package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Empleado;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.enums.tipoDocumento;
import com.sport.proyecto.enums.tipoEmpleado;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.EmpleadoRepositorio;
import com.sport.proyecto.repositorios.UsuarioRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServicio {
  @Autowired
  private UsuarioRepositorio usuarioRepositorio;

  @Autowired
  private EmpleadoRepositorio empleadoRepositorio;

  @Autowired
  private PersonaServicio personaServicio;

    @Transactional
    public Empleado buscarEmpleadoPorIdUsuario(String idUsuario) {
        return empleadoRepositorio.findEmpleadoByIdUsuario(idUsuario);
    }
    @Transactional
    public List<Empleado> obtenerProfesores() {
        return empleadoRepositorio.findAllProfesores();
    }

  @Transactional
  public List<Empleado> listaEmpleadosActivos() throws ErrorServicio {
    List<Empleado> empleados = empleadoRepositorio.buscarEmpleadosActivos();
    if (empleados.isEmpty()) {
      throw new ErrorServicio("No hay empleados activos");
    }
    return empleados;
  }
  /*
  @Transactional
  public void guardarEmpleado(String nombre, String apellido, String dia,String mes, String anio, tipoDocumento tipoDocumento, String numeroDocumento, String telefono, String correoElectronico, tipoEmpleado tipoEmpleado, String idSucursal, Usuario usuario) throws ErrorServicio, ParseException {
    // Junta la fecha como string
    String fechaString = dia + "/" + mes + "/" + anio;
    // Parseo a Date
    Date fechaNacimiento = new SimpleDateFormat("dd/MM/yyyy").parse(fechaString);
    validar(nombre, apellido, tipoDocumento, numeroDocumento, fechaNacimiento, telefono, correoElectronico, tipoEmpleado);;

    Empleado empleado = new Empleado();
    empleado.setNombre(nombre);
    empleado.setApellido(apellido);
    empleado.setFechaNacimiento(fechaNacimiento);
    empleado.setTipoDocumento(tipoDocumento);
    empleado.setNumeroDocumento(numeroDocumento);
    empleado.setTelefono(telefono);
    empleado.setEmail(correoElectronico);
    empleado.setTipoEmpleado(tipoEmpleado);
    empleado.setUsuario(usuario);
    empleado.setEliminado(false);

    empleadoRepositorio.save(empleado);
  }
*/
    @Transactional
    public Empleado crearEmpleado(String nombre, String apellido, tipoDocumento tipoDocumento, String numeroDocumento, LocalDate fechaNacimiento,
                                  String telefono, String correoElectronico, tipoEmpleado tipoEmpleado) throws ErrorServicio{
        validar(nombre, apellido, tipoDocumento, numeroDocumento, fechaNacimiento, telefono, correoElectronico, tipoEmpleado);
        Optional<Empleado> empleadoExistente = empleadoRepositorio.findByNumeroDocumento(numeroDocumento);
        if (empleadoExistente.isPresent()) {
            throw new ErrorServicio("Ya existe un empleado con el mismo número de documento.");
        }
        Empleado empleado = new Empleado();
        empleado.setNombre(nombre);
        empleado.setApellido(apellido);
        empleado.setNumeroDocumento(numeroDocumento);
        empleado.setFechaNacimiento(fechaNacimiento);
        empleado.setTipoDocumento(tipoDocumento);
        empleado.setTelefono(telefono);
        empleado.setEmail(correoElectronico);
        empleado.setTipoEmpleado(tipoEmpleado);
        empleado.setEliminado(false);
        personaServicio.registro(nombre, apellido, correoElectronico, numeroDocumento, numeroDocumento, true, Optional.ofNullable(tipoEmpleado));
        return empleadoRepositorio.save(empleado);
    }


    @Transactional
    public Empleado modificarEmpleado(String id, String nombre, String apellido, tipoDocumento tipoDocumento, String numeroDocumento, LocalDate fechaNacimiento,
                                      String telefono, String correoElectronico, tipoEmpleado tipoEmp) throws ErrorServicio{
        validar(nombre, apellido, tipoDocumento, numeroDocumento, fechaNacimiento, telefono, correoElectronico, tipoEmp);
        Optional<Empleado> respuesta = empleadoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Empleado empleado = respuesta.get();
            empleado.setNombre(nombre);
            empleado.setApellido(apellido);
            empleado.setNumeroDocumento(numeroDocumento);
            empleado.setFechaNacimiento(fechaNacimiento);
            empleado.setTipoDocumento(tipoDocumento);
            empleado.setTelefono(telefono);
            empleado.setEmail(correoElectronico);
            empleado.setTipoEmpleado(tipoEmp);
            return empleadoRepositorio.save(empleado);
        } else {
            throw new ErrorServicio("No se encontró el empleado con el ID proporcionado.");
        }
    }
    @Transactional
    public void eliminarEmpleado(String id) throws ErrorServicio{
        Optional<Empleado> respuesta = empleadoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Empleado empleado = respuesta.get();
            empleado.setEliminado(true);
            empleado.getUsuario().setEliminado(true);
            usuarioRepositorio.save(empleado.getUsuario());
            empleadoRepositorio.save(empleado);
        } else {
            throw new ErrorServicio("No se encontró el empleado con el ID proporcionado.");
        }
    }

    @Transactional
    public void asociarEmpleadoUsuario(Empleado empleado, Usuario usuario) {
        empleado.setUsuario(usuario);
        empleadoRepositorio.save(empleado);
        usuarioRepositorio.save(usuario);
    }

    @Transactional
    public void validar(String nombre, String apellido, tipoDocumento tipoDocumento, String numeroDocumento, LocalDate fechaNacimiento,
                        String telefono, String correoElectronico, tipoEmpleado tipoEmpleado) throws ErrorServicio{
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido no puede ser nulo.");
        }
        if (tipoDocumento == null) {
            throw new ErrorServicio("El tipo de documento no puede ser nulo.");
        }
        if (numeroDocumento == null || numeroDocumento.isEmpty() ) {
            throw new ErrorServicio("El número de documento no puede ser nulo.");
        }
        if (numeroDocumento.length()!=8 ) {
            throw new ErrorServicio("El número de documento debe tener 8 caracteres.");
        }
        if (fechaNacimiento == null) {
            throw new ErrorServicio("La fecha de nacimiento no puede ser nula.");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new ErrorServicio("El teléfono no puede ser nulo");
        }
        if (telefono.length()!=12) {
            throw new ErrorServicio("El teléfono debe tener 12 caracteres.");
        }
        if (correoElectronico == null || correoElectronico.isEmpty()) {
            throw new ErrorServicio("El correo electrónico no puede ser nulo o vacío.");
        }
        if (!correoElectronico.contains("@") || !correoElectronico.contains(".") ||correoElectronico.contains(" ") ) {
            throw new ErrorServicio("El correo electrónico debe ser válido.");
        }
        if (tipoEmpleado == null) {
            throw new ErrorServicio("El tipo de empleado no puede ser nulo.");
        }
    }
}