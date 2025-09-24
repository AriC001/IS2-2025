package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Socio;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.enums.tipoDocumento;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.SocioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sport.proyecto.servicios.DireccionServicio;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SocioServicio {
    @Autowired
    private SocioRepositorio repositorioSocio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private SucursalServicio sucursalServicio;

    @Autowired
    private DireccionServicio direccionServicio;

    // Busqueda

    @Transactional
    public Socio nuevSocio(Usuario usuario) {
        Socio socio = new Socio();
        socio.setUsuario(usuario);
        return socio;
    }

    @Transactional
    public List<Socio> listarSocio() throws ErrorServicio {
        try {
            List<Socio> socios = repositorioSocio.findAll();
            if (socios.isEmpty()) {
                throw new ErrorServicio("No existen socios registrados");
            }
            return socios;
        } catch (Exception e) {
            throw new ErrorServicio("Error del sistema");
        }
    }

    @Transactional
    public List<Socio> listarSocioActivos() throws ErrorServicio {
        try {
            List<Socio> socios = repositorioSocio.findAllActiveSocios();
            if (socios.isEmpty()) {
                throw new ErrorServicio("No existen socios registrados");
            }
            return socios;
        } catch (Exception e) {
            throw new ErrorServicio("Error del sistema");
        }
    }

    @Transactional
    public Socio buscarSocio(Long nroSocio) throws ErrorServicio {
        try{
            Optional<Socio> opt = repositorioSocio.findByNumeroSocio(nroSocio);
            if (opt.isPresent()) {
                return opt.get();
            } else {
                throw new ErrorServicio("No se encontro el socio solicitado");
            }
        }catch (Exception e){
            throw new ErrorServicio("Error del sistema");
        }
    }

    // Alta

    @Transactional
    public void crearSocio(String nombre, String apellido, LocalDate fechaNacimiento, tipoDocumento tipoDocumento, String numeroDocumento
        , String telefono, String correoElectronico, String idSucursal, String idDireccion, Usuario usuario) throws ErrorServicio {
        validar(nombre, apellido, fechaNacimiento, tipoDocumento, numeroDocumento, telefono, correoElectronico, idSucursal, usuario, idDireccion);
        try{
            if (repositorioSocio.findByNumeroDocumentoYTipo(numeroDocumento, tipoDocumento.toString()) != null) {
                throw new ErrorServicio("Ya existe un socio con el tipo y número de documento ingresado");
            }
            if (sucursalServicio.buscarSucursal(idSucursal) == null) {
                throw new ErrorServicio("No se encontro la sucursal solicitada");
            }

            Socio socio = new Socio();
            socio.setNombre(nombre);
            socio.setApellido(apellido);
            socio.setFechaNacimiento(fechaNacimiento);
            socio.setTipoDocumento(tipoDocumento);
            socio.setNumeroDocumento(numeroDocumento);
            socio.setTelefono(telefono);
            socio.setEmail(correoElectronico);
            socio.setEliminado(false);
            socio.setNumeroSocio(generarNumeroSocio());
            socio.setSucursal(sucursalServicio.buscarSucursal(idSucursal));
            socio.setDireccion(direccionServicio.buscarDireccion(idDireccion));
            socio.setUsuario(usuario);
            repositorioSocio.save(socio);
        }catch (Exception e){
            throw new ErrorServicio("Error del sistema");
        }
    }

    // Modificacion

    @Transactional
    public void modificarSocio(Long id, String nombre, String apellido, LocalDate fechaNacimiento, tipoDocumento tipoDocumento, String numeroDocumento
        , String telefono, String correoElectronico, String idSucursal, String idDireccion, Usuario usuario) throws ErrorServicio {
        validar(nombre, apellido, fechaNacimiento, tipoDocumento, numeroDocumento, telefono, correoElectronico, idSucursal, usuario, idDireccion);
        try{
            Socio socio = buscarSocio(id);
            if (socio == null) {
                throw new ErrorServicio("No se encontro el socio solicitado");
            }
            if (!socio.getNumeroDocumento().equals(numeroDocumento) || !socio.getTipoDocumento().toString().equals(tipoDocumento.toString())) {
                if (repositorioSocio.findByNumeroDocumentoYTipo(numeroDocumento, tipoDocumento.toString()) != null) {
                    throw new ErrorServicio("Ya existe un socio con el tipo y número de documento ingresado");
                }
            }
            if (sucursalServicio.buscarSucursal(idSucursal) == null) {
                throw new ErrorServicio("No se encontro la sucursal solicitada");
            }

            socio.setNombre(nombre);
            socio.setApellido(apellido);
            socio.setFechaNacimiento(fechaNacimiento);
            socio.setTipoDocumento(tipoDocumento);
            socio.setNumeroDocumento(numeroDocumento);
            socio.setTelefono(telefono);
            socio.setEmail(correoElectronico);
            socio.setSucursal(sucursalServicio.buscarSucursal(idSucursal));
            socio.setDireccion(direccionServicio.buscarDireccion(idDireccion));
            socio.setUsuario(usuario);
            repositorioSocio.save(socio);
        }catch (Exception e){
            throw new ErrorServicio("Error del sistema");
        }
    }

    // Baja

    @Transactional
    public void eliminarSocio(Long id) throws ErrorServicio {
        try{
            Socio socio = buscarSocio(id);
            if (socio == null) {
                throw new ErrorServicio("No se encontro el socio solicitado");
            }
            socio.setEliminado(true);
            repositorioSocio.save(socio);
        }catch (Exception e){
            throw new ErrorServicio("Error del sistema");
        }
    }


    @Transactional
    public Long generarNumeroSocio() {
        Long ultimo = repositorioSocio.obtenerUltimoNumeroSocio();
        return ultimo + 1;
    }

    @Transactional
    public void asociarSocioUsuario(Long nroSocio, String idUsuario) throws ErrorServicio {
        try{
            Socio socio = buscarSocio(nroSocio);
            if (socio == null) {
                throw new ErrorServicio("No se encontro el socio solicitado");
            }
            socio.setUsuario(usuarioServicio.buscarUsuario(idUsuario));
            repositorioSocio.save(socio);
        }catch (Exception e){
            throw new ErrorServicio("Error del sistema");
        }
    }

    // Validacion

    private void validar(String nombre, String apellido, LocalDate fechaNacimiento, tipoDocumento tipoDocumento, String numeroDocumento, String telefono, String correoElectronico, String idSucursal, Usuario usuario, String idDireccion) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo o estar vacio");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido no puede ser nulo o estar vacio");
        }
        if (fechaNacimiento == null) {
            throw new ErrorServicio("La fecha de nacimiento no puede ser nula");
        }
        if (tipoDocumento == null) {
            throw new ErrorServicio("El tipo de documento no puede ser nulo");
        }
        if (numeroDocumento == null || numeroDocumento.isEmpty()) {
            throw new ErrorServicio("El numero de documento no puede ser nulo o estar vacio");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new ErrorServicio("El telefono no puede ser nulo o estar vacio");
        }
        if (correoElectronico == null || correoElectronico.isEmpty()) {
            throw new ErrorServicio("El correo electronico no puede ser nulo o estar vacio");
        }
        if (!UtilServicio.esEmailValido(correoElectronico)) {
            throw new ErrorServicio("El correo electrónico no es válido");
        }
        if (idSucursal == null || idSucursal.toString().isEmpty()) {
            throw new ErrorServicio("La sucursal no puede ser nula o estar vacia");
        }
        if (usuario == null) {
            throw new ErrorServicio("El usuario no puede ser nulo");
        }
        if (idDireccion == null) {
            throw new ErrorServicio("La direccion no puede ser nula");
        }
    }
    @Transactional
    public Optional<Socio> findByNumeroSocio(Long numeroSocio) {
        return this.repositorioSocio.findByNumeroSocio(numeroSocio);
    }
    @Transactional
    public Optional<Socio> buscarSocioPorIdUsuario(String idUsuario) {
        Long nroSocio = this.repositorioSocio.findNroSocioByIdUsuario(idUsuario);
        return this.repositorioSocio.findByNumeroSocio(nroSocio);
    }
    @Transactional
    public java.util.List<Socio> obtenerSociosActivos() {
        return this.repositorioSocio.findAllActiveSocios();
    }
    @Transactional
    public Socio buscarPorId(String id) {
        return this.repositorioSocio.findById(id).orElse(null);
    }
}
