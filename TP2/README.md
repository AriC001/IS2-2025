# TP2 - Resumen de ejercicios

Este README describe, de forma muy breve, los ejercicios y proyectos contenidos en este workspace.

## Estructura y breve descripción

- `Ej1/`
  - `1-b/Ej1b/`: Ejercicio 1b — proyecto Maven (Spring Boot) con implementación del enunciado 1b.
  - `Auto/`:
    - `SpringBootExcel/`: Generación/lectura de Excel con Spring Boot.
    - `SpringBootPDF/`: Generación de PDFs con Spring Boot.
    - `SpringBootSchedule/`: Ejemplo de tareas programadas (scheduling) en Spring Boot.
    - `SpringBootTxt/`: Manejo de ficheros TXT / ejemplo de carga de datos (`clientes.txt`).
  - `Direccion-Maps/`: Integración con APIs/servicios de mapas o manejo de direcciones.

- `Ej2/` y `Ej2-b/` / `Ej2-c/` (variantes y entregas):
  - Ejercicios de la práctica 2 con varias implementaciones:
    - `EJ2-b-Rest/`: Servicio REST (Maven + Spring Boot) que expone endpoints requeridos.
    - `EJ2-b-Consumer/`: Cliente/consumer que consume los servicios REST.
    - `2-c PDF` / `IMPLEMENTACION_PDF.md`: Implementaciones relacionadas con generación/consumo de PDF.
    - `MovieScrapper/`: Scraper para obtener listados de películas/series (archivos `topmovies.json`, `topseries.json`).
  - Nota: hay varias carpetas `EJ2-b-*` repetidas en distintas raíces; contienen distintas versiones/entregas.

- `Ej3/`
  - `e-Template/`: Implementación basada en patrón Template Method (plantilla y ejemplos).
  - `Ej3-c/` (subcarpetas): más variantes del ejercicio 3.

- `Ej4/`
  - `b-VideojuegosRest/`: API REST para videojuegos (JSON de ejemplo `allGames.json`, `fewGames.json`).
  - `c-biblioteca/`: Ejercicios vinculados al dominio biblioteca (consumer/rest separados en subcarpetas).
  - `ej4-a.txt`: Notas o enunciado para el subejercicio a.

- `Ej5/`
  - `Ejercicio_N°5_B_Spring_Security/` y `Ejercicio_N°5_C_Spring_Security/`: ejercicios de seguridad con Spring Security; incluyen `docker-compose.yml`, ejemplos de requests y configuración.
  - `5-d`: Aplicado a Gym-Sport, se encuentra en la carpeta Proyecto-Gym
  - `5-f`: Aplicado en Ejercicio_N°5_B_Spring_Security.
    
- `Ej6/`
  - `6-a`: Incluye Diagrama de clases, historias de usuarios, presupuesto, prototipos, acuerdo de confidencialidad y citaciones a 3 páginas del mismo rubro.
  - `6-c`: Proyecto 1 desplegado y dockerizado. Se encuentra en la carpeta Proyecto-Gym. 
  - `ej6-e`: Desarrollo de sofware para un sistema de gestión de contactos (Incluye 6-i).
    
- `Ej7/`
  - `B_Seguridad_Avanzada/`: Trabajo avanzado de seguridad (guías, Auth0, ejemplos de endpoints, JMX test plan).
  - `ej-d/`: Proyecto con `Dockerfile` y configuración — corresponde a uno de los ejercicios desplegados (7d).
  - `ej-e/`: Contiene dos subproyectos `EJ2-b-Consumer/` y `EJ2-b-Rest/` como ejemplos/adaptaciones.
  - Notas de despliegue: Los ejercicios del grupo 7 están desplegados en DonWeb (VPS). Hay dos entregas desplegadas:
    - 7d (ej-d) y 7c (proyecto integrador 1). URLs / IPs:
      - http://vps-5421101-x.dattaweb.com (puertos 9000 y 8081)
      - http://66.97.42.220 (puertos 9000 y 8081)
    - Es decir, hay dos servicios accesibles en esos hosts/puertos; 7c corresponde al "proyectoIntegrador 1" (no tiene carpeta independiente nombrada como `7c` en este repo), y 7d corresponde a la carpeta `ej-d`.

- `medicine/`:
  - Proyecto Maven (posiblemente de ejemplo o entrega adicional). Contiene `src/main` y `src/test` con código y `target/` tras builds previos.

## Cómo explorar / correr

- La mayoría de subproyectos son aplicaciones Maven/Spring Boot. Para ejecutarlos localmente:
  - Desde la carpeta del subproyecto ejecutar `mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`) o compilar con `./mvnww.cmd package` y ejecutar el JAR.
- Revisar `pom.xml` en cada subcarpeta para dependencias y puertos configurados.

## Observaciones
- Hay muchas carpetas que contienen versiones o entregas repetidas (por ejemplo varias `EJ2-b-*`). Cada una suele ser una variante/entrega diferente del mismo enunciado.
- Si querés, puedo:
  - 1) Generar un README más detallado por carpeta con instrucciones de run específicas (puertos, endpoints principales). 
  - 2) Verificar que los servicios mencionados en Ej7 están realmente reachables desde esta red.

---
*README generado automáticamente: resumen rápido por pedido.*
