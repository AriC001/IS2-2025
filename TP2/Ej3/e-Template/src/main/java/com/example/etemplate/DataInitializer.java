package com.example.etemplate;

import com.example.etemplate.entities.Proveedor;
import com.example.etemplate.repositories.ProveedorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDefaultProveedor(ProveedorRepository proveedorRepository) {
        return args -> {
            if (proveedorRepository.count() == 0) {
                Proveedor proveedor = new Proveedor();
                proveedor.setName("Proveedor Fijo");
                proveedor.setDeleted(false);
                proveedorRepository.save(proveedor);
                System.out.println("✅ Proveedor por defecto creado: " + proveedor.getName());
            } else {
                System.out.println("➡️ Ya existen proveedores en la base de datos");
            }
        };
    }
}
