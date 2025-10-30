package com.sport.proyecto;

import com.sport.proyecto.servicios.CuotaMensualServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@SpringBootApplication
public class ProyectoApplication {

    @Autowired
    private CuotaMensualServicio cuotaMensualServicio;

    public static void main(String[] args) {
        SpringApplication.run(ProyectoApplication.class, args);

    }

    @Scheduled(cron = "0 0 2 1 * ?") // cada 1 del mes a las 2 AM
    public void generarCuotasAutomaticamente() {
        cuotaMensualServicio.generarCuotasMensuales();
    }

}
