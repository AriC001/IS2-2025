package nexora.proyectointegrador2.utils.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterRequestDTO {

  // Datos de acceso
  @NotBlank(message = "El nombre de usuario es obligatorio")
  @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
  private String nombreUsuario;

  @NotBlank(message = "La contraseña es obligatoria")
  @Size(min = 4, max = 100, message = "La contraseña debe tener al menos 4 caracteres")
  private String clave;

  // Datos personales
  @NotBlank(message = "El nombre es obligatorio")
  private String nombre;

  @NotBlank(message = "El apellido es obligatorio")
  private String apellido;

  @NotNull(message = "La fecha de nacimiento es obligatoria")
  private LocalDate fechaNacimiento;

  @NotBlank(message = "El email es obligatorio")
  @Email(message = "El email debe ser válido")
  private String email;

  @NotBlank(message = "El tipo de documento es obligatorio")
  private String tipoDocumento;

  @NotBlank(message = "El número de documento es obligatorio")
  private String numeroDocumento;

  @NotBlank(message = "El teléfono es obligatorio")
  private String telefono;

  @NotBlank(message = "La nacionalidad es obligatoria")
  private String nacionalidadId;

  // Dirección
  @NotBlank(message = "La calle es obligatoria")
  private String calle;

  @NotBlank(message = "El número es obligatorio")
  private String numero;

  private String barrio;

  @NotBlank(message = "La localidad es obligatoria")
  private String localidadId;

  private String manzanaPiso;
  private String casaDepartamento;
  private String referencia;
  private String direccionEstadia;

  // Constructor vacío
  public RegisterRequestDTO() {
  }

  // Constructor privado para el Builder
  private RegisterRequestDTO(Builder builder) {
    this.nombreUsuario = builder.nombreUsuario;
    this.clave = builder.clave;
    this.nombre = builder.nombre;
    this.apellido = builder.apellido;
    this.fechaNacimiento = builder.fechaNacimiento;
    this.email = builder.email;
    this.tipoDocumento = builder.tipoDocumento;
    this.numeroDocumento = builder.numeroDocumento;
    this.telefono = builder.telefono;
    this.nacionalidadId = builder.nacionalidadId;
    this.calle = builder.calle;
    this.numero = builder.numero;
    this.barrio = builder.barrio;
    this.localidadId = builder.localidadId;
    this.manzanaPiso = builder.manzanaPiso;
    this.casaDepartamento = builder.casaDepartamento;
    this.referencia = builder.referencia;
    this.direccionEstadia = builder.direccionEstadia;
  }

  // Builder estático
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String nombreUsuario;
    private String clave;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String email;
    private String tipoDocumento;
    private String numeroDocumento;
    private String telefono;
    private String nacionalidadId;
    private String calle;
    private String numero;
    private String barrio;
    private String localidadId;
    private String manzanaPiso;
    private String casaDepartamento;
    private String referencia;
    private String direccionEstadia;

    public Builder nombreUsuario(String nombreUsuario) {
      this.nombreUsuario = nombreUsuario;
      return this;
    }

    public Builder clave(String clave) {
      this.clave = clave;
      return this;
    }

    public Builder nombre(String nombre) {
      this.nombre = nombre;
      return this;
    }

    public Builder apellido(String apellido) {
      this.apellido = apellido;
      return this;
    }

    public Builder fechaNacimiento(LocalDate fechaNacimiento) {
      this.fechaNacimiento = fechaNacimiento;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder tipoDocumento(String tipoDocumento) {
      this.tipoDocumento = tipoDocumento;
      return this;
    }

    public Builder numeroDocumento(String numeroDocumento) {
      this.numeroDocumento = numeroDocumento;
      return this;
    }

    public Builder telefono(String telefono) {
      this.telefono = telefono;
      return this;
    }

    public Builder nacionalidadId(String nacionalidadId) {
      this.nacionalidadId = nacionalidadId;
      return this;
    }

    public Builder calle(String calle) {
      this.calle = calle;
      return this;
    }

    public Builder numero(String numero) {
      this.numero = numero;
      return this;
    }

    public Builder barrio(String barrio) {
      this.barrio = barrio;
      return this;
    }

    public Builder localidadId(String localidadId) {
      this.localidadId = localidadId;
      return this;
    }

    public Builder manzanaPiso(String manzanaPiso) {
      this.manzanaPiso = manzanaPiso;
      return this;
    }

    public Builder casaDepartamento(String casaDepartamento) {
      this.casaDepartamento = casaDepartamento;
      return this;
    }

    public Builder referencia(String referencia) {
      this.referencia = referencia;
      return this;
    }

    public Builder direccionEstadia(String direccionEstadia) {
      this.direccionEstadia = direccionEstadia;
      return this;
    }

    public RegisterRequestDTO build() {
      return new RegisterRequestDTO(this);
    }
  }

  // Getters y Setters
  public String getNombreUsuario() { return nombreUsuario; }
  public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

  public String getClave() { return clave; }
  public void setClave(String clave) { this.clave = clave; }

  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }

  public String getApellido() { return apellido; }
  public void setApellido(String apellido) { this.apellido = apellido; }

  public LocalDate getFechaNacimiento() { return fechaNacimiento; }
  public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getTipoDocumento() { return tipoDocumento; }
  public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

  public String getNumeroDocumento() { return numeroDocumento; }
  public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

  public String getTelefono() { return telefono; }
  public void setTelefono(String telefono) { this.telefono = telefono; }

  public String getNacionalidadId() { return nacionalidadId; }
  public void setNacionalidadId(String nacionalidadId) { this.nacionalidadId = nacionalidadId; }

  public String getCalle() { return calle; }
  public void setCalle(String calle) { this.calle = calle; }

  public String getNumero() { return numero; }
  public void setNumero(String numero) { this.numero = numero; }

  public String getBarrio() { return barrio; }
  public void setBarrio(String barrio) { this.barrio = barrio; }

  public String getLocalidadId() { return localidadId; }
  public void setLocalidadId(String localidadId) { this.localidadId = localidadId; }

  public String getManzanaPiso() { return manzanaPiso; }
  public void setManzanaPiso(String manzanaPiso) { this.manzanaPiso = manzanaPiso; }

  public String getCasaDepartamento() { return casaDepartamento; }
  public void setCasaDepartamento(String casaDepartamento) { this.casaDepartamento = casaDepartamento; }

  public String getReferencia() { return referencia; }
  public void setReferencia(String referencia) { this.referencia = referencia; }

  public String getDireccionEstadia() { return direccionEstadia; }
  public void setDireccionEstadia(String direccionEstadia) { this.direccionEstadia = direccionEstadia; }

  @Override
  public String toString() {
    return "RegisterRequestDTO{" +
           "nombreUsuario='" + nombreUsuario + '\'' +
           ", nombre='" + nombre + '\'' +
           ", apellido='" + apellido + '\'' +
           ", fechaNacimiento=" + fechaNacimiento +
           ", email='" + email + '\'' +
           ", tipoDocumento='" + tipoDocumento + '\'' +
           ", numeroDocumento='" + numeroDocumento + '\'' +
           ", telefono='" + telefono + '\'' +
           ", nacionalidadId='" + nacionalidadId + '\'' +
           ", calle='" + calle + '\'' +
           ", numero='" + numero + '\'' +
           ", localidadId='" + localidadId + '\'' +
           '}';
  }
}