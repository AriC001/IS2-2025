# Funcionalidad de Migración de Proveedores

## Descripción
Esta funcionalidad permite importar proveedores desde un archivo de texto plano (.txt) a la base de datos del sistema.

## Cómo usar

### 1. Preparar el archivo
- El archivo debe ser de texto plano con extensión `.txt`
- Cada línea representa un proveedor
- Los campos deben estar separados por punto y coma (;)
- Se recomienda nombrar el archivo como `migracion.txt`

### 2. Formato del archivo
Cada línea debe contener exactamente 12 campos en este orden:

```
NOMBRE;APELLIDO;TELÉFONO;CORREO ELECTRÓNICO;CUIT;CALLE;NÚMERO;LOCALIDAD;DEPARTAMENTO;PROVINCIA;PAÍS;
```

**Importante:** Notar el punto y coma final al terminar cada línea.

### 3. Ejemplo de archivo válido

```
Lionel;Escaloneta;2613654789;l.scalo@gmail.com;202934256790;Perú;345;Villa Hipodromo;Godoy Cruz;Mendoza;Argentina;
Pablo;Perez;26145622319;l.perez@gmail.com;20324326780;San Martín;1050;Capital;Mendoza;Mendoza;Argentina;
Esteban;Peralta;2615673490;l.estaban_p@gmail.com;20349874570;Benavente;121;Villa Nueva;Guaymallen;Mendoza;Argentina;
```

### 4. Acceder a la funcionalidad
1. Inicie sesión en el sistema
2. En el menú lateral, busque la sección **"Herramientas"**
3. Haga clic en **"Migración"**
4. Seleccione el archivo usando el botón "Elegir archivo"
5. Haga clic en **"Procesar Archivo"**

### 5. Resultados
Al finalizar el proceso, el sistema mostrará:
- Cantidad de proveedores importados exitosamente
- Lista de errores si los hubiera (con el número de línea)
- Un resumen completo de la operación

## Características importantes

### Creación automática de datos geográficos
Si el país, provincia, departamento o localidad no existen en la base de datos, el sistema los creará automáticamente.

### Validaciones
- No se permiten CUITs duplicados
- Los campos obligatorios son: NOMBRE, APELLIDO y CUIT
- Se valida el formato del archivo

### Errores comunes

| Error | Causa | Solución |
|-------|-------|----------|
| "Formato incorrecto" | Faltan campos en la línea | Verificar que la línea tenga los 12 campos |
| "Ya existe un proveedor con CUIT" | CUIT duplicado | Verificar el CUIT o eliminar el proveedor existente |
| "Campos obligatorios vacíos" | Nombre, apellido o CUIT vacío | Completar los campos obligatorios |

## Archivo de ejemplo
Se incluye un archivo de ejemplo llamado `migracion.txt` en la raíz del proyecto que puede usar como plantilla.

## Notas técnicas
- El archivo se procesa línea por línea
- Se usa transaccionalidad para garantizar la integridad de los datos
- Los proveedores importados quedan activos (no eliminados) por defecto
- El proceso registra logs detallados para auditoría
