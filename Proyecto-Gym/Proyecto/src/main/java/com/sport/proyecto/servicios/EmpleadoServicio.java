import com.sport.proyecto.enums.tipoEmpleado;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.EmpleadoRepositorio;
import com.sport.proyecto.repositorios.SucursalRepositorio;
import com.sport.proyecto.entidades.Sucursal;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import com.sport.proyecto.servicios.UsuarioServicio;
import com.sport.proyecto.enums.Rol;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServicio {
  @Autowired
  private EmpleadoRepositorio empleadoRepositorio;

  @Autowired
  private SucursalRepositorio sucursalRepositorio;

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
  public void modificar(String id, String nombre, String apellido, LocalDate fechaNacimiento,
                        tipoDocumento tipoDocumento, String numeroDocumento, String email,
                        String telefono, tipoEmpleado tipoEmpleado, Usuario usuario,Sucursal sucursal) throws ErrorServicio {
        //validar(nombreMascota, sexo);
        System.out.println("servicio");
        Empleado empleado = empleadoRepositorio.findById(id).get();
        empleado.setNombre(nombre);
        empleado.setApellido(apellido);
        empleado.setFechaNacimiento(fechaNacimiento);
        empleado.setTipoDocumento(tipoDocumento);
        empleado.setNumeroDocumento(numeroDocumento);
        empleado.setEmail(email);
        empleado.setTelefono(telefono);
        empleado.setTipoEmpleado(tipoEmpleado);
        empleado.setUsuario(usuario);
        empleado.setSucursal(sucursal);
        
        empleadoRepositorio.save(empleado);
    }
  
  

  public void crearEmpleado(Empleado empleado) throws ErrorServicio {
    System.out.println("entra a servicio");
    if (empleado == null) {
      throw new ErrorServicio("El empleado no puede ser nulo");
    }
    //System.out.println("Creando empleado: " + empleado);
    validar(empleado.getNombre(), empleado.getApellido(), empleado.getFechaNacimiento(), empleado.getTipoDocumento(), empleado.getNumeroDocumento(), empleado.getTelefono(), empleado.getEmail(), empleado.getTipoEmpleado(), empleado.getSucursal());
    empleado.setEliminado(false);
    Usuario usuario= usuarioServicio.crearUsuario(empleado.getEmail(),empleado.getNumeroDocumento(), Rol.EMPLEADO);
    empleado.setUsuario(usuario);
    empleadoRepositorio.save(empleado);
  }


  public void validar(String nombre, String apellido, LocalDate fechaNacimiento, tipoDocumento tipoDocumento, String numeroDocumento, String telefono, String correoElectronico, tipoEmpleado tipoEmpleado, Sucursal sucursal) throws ErrorServicio {
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
    if (sucursal == null || sucursalRepositorio.findById(sucursal.getId()).get()==null) {
      throw new ErrorServicio("El ID de la sucursal no puede ser nulo o estar vacío");
    }
    //if (usuario == null) {
      //throw new ErrorServicio("El usuario no puede ser nulo");
    //}
  }

  @Transactional
  public Optional<Empleado> buscarEmpleado(String id){
    return empleadoRepositorio.findById(id);
  }

}
