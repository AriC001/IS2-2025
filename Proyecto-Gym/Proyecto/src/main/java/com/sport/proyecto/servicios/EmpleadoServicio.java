package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Empleado;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.enums.tipoDocumento;
import com.sport.proyecto.enums.tipoEmpleado;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.EmpleadoRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import com.sport.proyecto.servicios.UsuarioServicio;
import com.sport.proyecto.enums.Rol;
import java.util.Date;
import java.util.List;

@Service
public class EmpleadoServicio {
  @Autowired
  private EmpleadoRepositorio empleadoRepositorio;

  @Autowired
  private UsuarioServicio usuarioServicio;

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

  @Transactional
  public void guardarEmpleado(String nombre, String apellido, LocalDate fechaNacimiento, tipoDocumento tipoDocumento, String numeroDocumento, String telefono, String correoElectronico, tipoEmpleado tipoEmpleado, String idSucursal, Usuario usuario) throws ErrorServicio {

    validar(nombre, apellido, fechaNacimiento, tipoDocumento, numeroDocumento, telefono, correoElectronico, tipoEmpleado, idSucursal);

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

  public void crearEmpleado(Empleado empleado) throws ErrorServicio {
    if (empleado == null) {
      throw new ErrorServicio("El empleado no puede ser nulo");
    }
    //System.out.println("Creando empleado: " + empleado);
    validar(empleado.getNombre(), empleado.getApellido(), empleado.getFechaNacimiento(), empleado.getTipoDocumento(), empleado.getNumeroDocumento(), empleado.getTelefono(), empleado.getEmail(), empleado.getTipoEmpleado(), "1");
    empleado.setEliminado(false);
    Usuario usuario= usuarioServicio.crearUsuario(empleado.getEmail(),empleado.getNumeroDocumento(), Rol.EMPLEADO);
    empleado.setUsuario(usuario);
    empleadoRepositorio.save(empleado);
  }

  public void validar(String nombre, String apellido, LocalDate fechaNacimiento, tipoDocumento tipoDocumento, String numeroDocumento, String telefono, String correoElectronico, tipoEmpleado tipoEmpleado, String idSucursal) throws ErrorServicio {
    if (nombre == null || nombre.isEmpty()) {
      throw new ErrorServicio("El nombre no puede ser nulo o estar vacío");
    }
    if (apellido == null || apellido.isEmpty()) {
      throw new ErrorServicio("El apellido no puede ser nulo o estar vacío");
    }
    if (fechaNacimiento == null) {
      throw new ErrorServicio("La fecha de nacimiento no puede ser nula");
    }
    if (tipoDocumento == null) {
      throw new ErrorServicio("El tipo de documento no puede ser nulo");
    }
    if (numeroDocumento == null || numeroDocumento.isEmpty()) {
      throw new ErrorServicio("El número de documento no puede ser nulo o estar vacío");
    }
    if (telefono == null || telefono.isEmpty()) {
      throw new ErrorServicio("El teléfono no puede ser nulo o estar vacío");
    }
    if (correoElectronico == null || correoElectronico.isEmpty()) {
      throw new ErrorServicio("El correo electrónico no puede ser nulo o estar vacío");
    }
    if (!UtilServicio.esEmailValido(correoElectronico)) {
      throw new ErrorServicio("El correo electrónico no es válido");
    }
    if (tipoEmpleado == null) {
      throw new ErrorServicio("El tipo de empleado no puede ser nulo");
    }
    if (idSucursal == null || idSucursal.isEmpty()) {
      throw new ErrorServicio("El ID de la sucursal no puede ser nulo o estar vacío");
    }
    //if (usuario == null) {
      //throw new ErrorServicio("El usuario no puede ser nulo");
    //}
  }

}