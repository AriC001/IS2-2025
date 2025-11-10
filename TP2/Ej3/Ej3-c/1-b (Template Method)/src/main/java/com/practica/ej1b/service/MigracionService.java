package com.practica.ej1b.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.practica.ej1b.business.domain.entity.Departamento;
import com.practica.ej1b.business.domain.entity.Direccion;
import com.practica.ej1b.business.domain.entity.Localidad;
import com.practica.ej1b.business.domain.entity.Pais;
import com.practica.ej1b.business.domain.entity.Proveedor;
import com.practica.ej1b.business.domain.entity.Provincia;
import com.practica.ej1b.business.persistence.repository.DepartamentoRepositorio;
import com.practica.ej1b.business.persistence.repository.DireccionRepositorio;
import com.practica.ej1b.business.persistence.repository.LocalidadRepositorio;
import com.practica.ej1b.business.persistence.repository.PaisRepositorio;
import com.practica.ej1b.business.persistence.repository.ProveedorRepositorio;
import com.practica.ej1b.business.persistence.repository.ProvinciaRepositorio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MigracionService {

    private final ProveedorRepositorio proveedorRepositorio;
    private final DireccionRepositorio direccionRepositorio;
    private final PaisRepositorio paisRepositorio;
    private final ProvinciaRepositorio provinciaRepositorio;
    private final DepartamentoRepositorio departamentoRepositorio;
    private final LocalidadRepositorio localidadRepositorio;

    public ResultadoMigracion procesarArchivoMigracion(MultipartFile archivo) throws IOException {
        ResultadoMigracion resultado = new ResultadoMigracion();
        
        try (InputStream is = archivo.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            
            String linea;
            int numeroLinea = 0;
            
            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                
                if (linea.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    procesarLineaTransaccional(linea, numeroLinea, resultado);
                } catch (Exception e) {
                    resultado.agregarError(numeroLinea, "Error al procesar línea: " + e.getMessage());
                    log.error("Error en línea {}: {}", numeroLinea, e.getMessage());
                }
            }
            
        }
        
        log.info("Migración completada: {} exitosos, {} errores", 
                resultado.getExitosos(), resultado.getErrores().size());
        
        return resultado;
    }
    
    @Transactional
    public void procesarLineaTransaccional(String linea, int numeroLinea, ResultadoMigracion resultado) {
        procesarLinea(linea, numeroLinea, resultado);
    }

    private void procesarLinea(String linea, int numeroLinea, ResultadoMigracion resultado) {
        String[] campos = linea.split(";");
        
        if (campos.length != 11) {
            resultado.agregarError(numeroLinea, 
                "Formato incorrecto. Se esperan 11 campos, se encontraron " + campos.length);
            return;
        }
        
        try {
            // Extraer campos
            String nombre = campos[0].trim();
            String apellido = campos[1].trim();
            String telefono = campos[2].trim();
            String correo = campos[3].trim();
            String cuit = campos[4].trim();
            String calle = campos[5].trim();
            String numero = campos[6].trim();
            String localidadNombre = campos[7].trim();
            String departamentoNombre = campos[8].trim();
            String provinciaNombre = campos[9].trim();
            String paisNombre = campos[10].trim();
            
            // Validaciones básicas
            if (nombre.isEmpty() || apellido.isEmpty() || cuit.isEmpty()) {
                resultado.agregarError(numeroLinea, "Campos obligatorios vacíos (nombre, apellido o CUIT)");
                return;
            }
            
            // Verificar si ya existe el proveedor por CUIT
            if (proveedorRepositorio.findByCuit(cuit) != null) {
                resultado.agregarError(numeroLinea, "Ya existe un proveedor con CUIT: " + cuit);
                return;
            }
            
            // Buscar o crear entidades geográficas
            Pais pais = buscarOCrearPais(paisNombre);
            Provincia provincia = buscarOCrearProvincia(provinciaNombre, pais);
            Departamento departamento = buscarOCrearDepartamento(departamentoNombre, provincia);
            Localidad localidad = buscarOCrearLocalidad(localidadNombre, departamento);
            
            // Crear dirección
            Direccion direccion = new Direccion();
            direccion.setCalle(calle);
            direccion.setNumeracion(numero);
            direccion.setBarrio("-"); // Campo obligatorio en BD
            direccion.setManzanaPiso("-");
            direccion.setCasaDepartamento("-");
            direccion.setReferencia("Importado desde migración");
            direccion.setLocalidad(localidad);
            direccion.setEliminado(false);
            direccion = direccionRepositorio.save(direccion);
            
            // Crear proveedor
            Proveedor proveedor = new Proveedor();
            proveedor.setNombre(nombre);
            proveedor.setApellido(apellido);
            proveedor.setTelefono(telefono);
            proveedor.setCorreoElectronico(correo);
            proveedor.setCuit(cuit);
            proveedor.setDireccion(direccion);
            proveedor.setEliminado(false);
            
            proveedorRepositorio.save(proveedor);
            resultado.incrementarExitosos();
            
        } catch (Exception e) {
            resultado.agregarError(numeroLinea, "Error al guardar: " + e.getMessage());
            log.error("Error guardando proveedor en línea {}: {}", numeroLinea, e.getMessage());
        }
    }

    private Pais buscarOCrearPais(String nombre) {
        Pais existente = paisRepositorio.findAllByEliminadoFalse().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
        
        if (existente != null) {
            return existente;
        }
        
        Pais pais = new Pais();
        pais.setNombre(nombre);
        pais.setEliminado(false);
        return paisRepositorio.save(pais);
    }

    private Provincia buscarOCrearProvincia(String nombre, Pais pais) {
        Provincia existente = provinciaRepositorio.findAllByEliminadoFalse().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre) && 
                           p.getPais().getId().equals(pais.getId()))
                .findFirst()
                .orElse(null);
        
        if (existente != null) {
            return existente;
        }
        
        Provincia provincia = new Provincia();
        provincia.setNombre(nombre);
        provincia.setPais(pais);
        provincia.setEliminado(false);
        return provinciaRepositorio.save(provincia);
    }

    private Departamento buscarOCrearDepartamento(String nombre, Provincia provincia) {
        Departamento existente = departamentoRepositorio.findAllByEliminadoFalse().stream()
                .filter(d -> d.getNombre().equalsIgnoreCase(nombre) && 
                           d.getProvincia().getId().equals(provincia.getId()))
                .findFirst()
                .orElse(null);
        
        if (existente != null) {
            return existente;
        }
        
        Departamento departamento = new Departamento();
        departamento.setNombre(nombre);
        departamento.setProvincia(provincia);
        departamento.setEliminado(false);
        return departamentoRepositorio.save(departamento);
    }

    private Localidad buscarOCrearLocalidad(String nombre, Departamento departamento) {
        // Buscar primero en la base de datos
        Localidad existente = localidadRepositorio.findAllByEliminadoFalse().stream()
                .filter(l -> l.getNombre().equalsIgnoreCase(nombre) && 
                           l.getDepartamento().getId().equals(departamento.getId()))
                .findFirst()
                .orElse(null);
        
        if (existente != null) {
            return existente;
        }
        
        // Si no existe, crear nueva
        Localidad localidad = new Localidad();
        localidad.setNombre(nombre);
        localidad.setDepartamento(departamento);
        localidad.setEliminado(false);
        return localidadRepositorio.save(localidad);
    }

    // Clase interna para el resultado
    public static class ResultadoMigracion {
        private int exitosos = 0;
        private final List<String> errores = new ArrayList<>();

        public void incrementarExitosos() {
            this.exitosos++;
        }

        public void agregarError(int linea, String mensaje) {
            this.errores.add("Línea " + linea + ": " + mensaje);
        }

        public int getExitosos() {
            return exitosos;
        }

        public List<String> getErrores() {
            return errores;
        }
    }
}
