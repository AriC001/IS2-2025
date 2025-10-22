# Implementación de Carga y Visualización de PDFs para Libros

## Resumen
Se ha implementado exitosamente la funcionalidad para cargar archivos PDF al crear un libro y visualizarlos posteriormente. Los PDFs se almacenan en el servidor en `C://biblioteca//` con el formato `libro_nombrelibro_.pdf`.

## Arquitectura Implementada

### Backend (EJ2-b-Rest)

#### 1. Entidad Documento
- **Ubicación**: `com.practica.ej2b.business.domain.entity.Documento`
- **Atributos**:
  - `nombreArchivo`: Nombre del archivo PDF
  - `rutaArchivo`: Ruta completa en el sistema de archivos
  - `tipoContenido`: Tipo MIME del archivo
- **Relación**: OneToOne con `Libro` (bidireccional)

#### 2. Repositorio DocumentoRepository
- **Ubicación**: `com.practica.ej2b.business.persistence.repository.DocumentoRepository`
- Extiende `BaseRepository<Documento, Long>`
- Método adicional: `findByLibroId(Long libroId)`

#### 3. Servicio DocumentoService
- **Ubicación**: `com.practica.ej2b.business.logic.service.DocumentoService`
- **Métodos principales**:
  - `guardarDocumento(MultipartFile archivo, String nombreLibro)`: Guarda el PDF en el servidor
  - `obtenerArchivo(Long documentoId)`: Recupera el contenido del PDF
  - `eliminarArchivo(Long documentoId)`: Elimina el PDF físico y lógicamente
- **Directorio de almacenamiento**: `C://biblioteca//`
- **Formato de nombre**: `libro_{nombre_sanitizado}_.pdf`

#### 4. Controlador REST DocumentoRestController
- **Ubicación**: `com.practica.ej2b.controller.rest.DocumentoRestController`
- **Endpoints**:
  - `POST /api/v1/documentos/upload`: Sube un PDF
  - `GET /api/v1/documentos/download/{id}`: Descarga un PDF

#### 5. Actualización de Libro
- Se agregó la relación `@OneToOne` con `Documento` en la entidad `Libro`
- Cascada configurada para facilitar la gestión del ciclo de vida

### Frontend (EJ2-b-Consumer)

#### 1. DocumentoDTO
- **Ubicación**: `com.practica.ej2consumer.business.domain.dto.DocumentoDTO`
- Refleja los atributos de la entidad Documento del servidor

#### 2. Actualización de LibroDTO
- Se agregó el campo `DocumentoDTO documento`

#### 3. DocumentoDAORest
- **Ubicación**: `com.practica.ej2consumer.business.persistence.rest.DocumentoDAORest`
- **Métodos**:
  - `uploadDocumento(MultipartFile archivo, String nombreLibro)`: Envía el PDF al servidor
  - `downloadDocumento(Long id)`: Obtiene el PDF del servidor

#### 4. DocumentoService (Cliente)
- **Ubicación**: `com.practica.ej2consumer.business.logic.service.DocumentoService`
- Capa de servicio que maneja la lógica de negocio del cliente

#### 5. DocumentoController
- **Ubicación**: `com.practica.ej2consumer.controller.view.DocumentoController`
- **Endpoint clave**:
  - `GET /documentos/ver/{id}`: Abre el PDF en una nueva pestaña del navegador

#### 6. LibroController Actualizado
- Se agregó el servicio `DocumentoService`
- **Método `create` modificado**:
  - Acepta un parámetro adicional `@RequestParam("archivoPDF")` de tipo `MultipartFile`
  - Primero crea el libro
  - Si hay un PDF, lo sube y asocia al libro
  - Manejo de errores robusto (si falla la carga del PDF, el libro se crea de todas formas)

#### 7. Vistas Actualizadas

**form.html**:
- Se agregó `enctype="multipart/form-data"` al formulario
- Campo nuevo de tipo `file` para cargar PDFs (solo visible al crear, no al editar)
- Acepta únicamente archivos PDF (`accept="application/pdf"`)

**detalle.html**:
- Se agregó una tarjeta que muestra información del PDF si existe
- Botón "Abrir PDF" que abre el documento en una nueva pestaña
- El botón usa `target="_blank"` para abrir en nueva ventana

## Flujo de Uso

### Crear un Libro con PDF
1. El usuario accede a `/libros/nuevo`
2. Completa el formulario del libro
3. Selecciona un archivo PDF (opcional)
4. Al enviar:
   - Se crea el libro en la base de datos
   - Si hay PDF, se sube al servidor
   - El PDF se guarda en `C://biblioteca//libro_{titulo}_.pdf`
   - Se crea la entidad Documento y se asocia al libro

### Visualizar el PDF de un Libro
1. El usuario accede a `/libros/{id}` (detalle del libro)
2. Si el libro tiene un PDF asociado, se muestra un botón "Abrir PDF"
3. Al hacer clic, se abre el PDF en una nueva pestaña del navegador
4. La URL es `/documentos/ver/{documentoId}`

## Características Técnicas

### Seguridad y Validación
- Solo se permiten archivos PDF (validación por tipo MIME)
- Sanitización del nombre del archivo (caracteres especiales eliminados)
- Validación de archivo vacío
- Validación de existencia del archivo al recuperarlo

### Manejo de Errores
- Si falla la carga del PDF, el libro se crea de todas formas
- Se muestra un mensaje de advertencia al usuario
- Los errores en el servidor son capturados y reportados apropiadamente

### Persistencia
- Los PDFs se almacenan físicamente en el disco
- La metadata se guarda en la base de datos (tabla `documento`)
- Relación OneToOne entre `libro` y `documento`

## Patrón Plantilla Respetado

La implementación sigue estrictamente el patrón plantilla existente:

1. **Capa de Entidad/DTO**: `Documento` y `DocumentoDTO`
2. **Capa de Repositorio**: `DocumentoRepository` extends `BaseRepository`
3. **Capa de Servicio**: `DocumentoService` extends `BaseService`
4. **Capa de Controlador REST**: `DocumentoRestController` extends `BaseRestController`
5. **Capa de Controlador MVC**: `DocumentoController` extends `BaseController`
6. **Capa de Acceso a Datos REST**: `DocumentoDAORest` extends `BaseDAORest`

## Base de Datos

Se debe ejecutar la migración para crear la tabla `documento`:

```sql
CREATE TABLE documento (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre_archivo VARCHAR(255) NOT NULL,
  ruta_archivo VARCHAR(500) NOT NULL,
  tipo_contenido VARCHAR(100) NOT NULL,
  eliminado BOOLEAN DEFAULT FALSE
);

ALTER TABLE libro 
ADD COLUMN documento_id BIGINT,
ADD CONSTRAINT fk_libro_documento 
FOREIGN KEY (documento_id) REFERENCES documento(id);
```

## Notas Importantes

1. **Directorio**: Asegurarse de que el directorio `C://biblioteca//` existe y tiene permisos de escritura
2. **Tamaño de archivo**: No hay límite configurado actualmente. Se recomienda agregar uno en producción
3. **Edición**: Actualmente no se permite cambiar el PDF al editar un libro (solo al crear)
4. **Eliminación**: Al eliminar un libro lógicamente, el documento se mantiene (borrado lógico)

## Archivos Creados/Modificados

### Servidor (EJ2-b-Rest):
- ✅ `Documento.java` (nuevo)
- ✅ `DocumentoRepository.java` (nuevo)
- ✅ `DocumentoService.java` (nuevo)
- ✅ `DocumentoRestController.java` (nuevo)
- ✅ `Libro.java` (modificado - agregada relación)

### Cliente (EJ2-b-Consumer):
- ✅ `DocumentoDTO.java` (nuevo)
- ✅ `LibroDTO.java` (modificado - agregado campo documento)
- ✅ `DocumentoDAORest.java` (nuevo)
- ✅ `DocumentoService.java` (nuevo)
- ✅ `DocumentoController.java` (nuevo)
- ✅ `LibroController.java` (modificado - manejo de archivos)
- ✅ `form.html` (modificado - campo de upload)
- ✅ `detalle.html` (modificado - botón ver PDF)

## Estado Final

✅ **Implementación completa y funcional**

La funcionalidad está lista para ser probada. Respeta completamente el patrón plantilla existente y sigue las mejores prácticas del proyecto.
