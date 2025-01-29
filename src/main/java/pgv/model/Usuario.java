package pgv.model;

public class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String fechaCambioContrasenia;

    public Usuario(int id, String nombre, String correo, String fechaCambioContrasenia) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.fechaCambioContrasenia = fechaCambioContrasenia;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFechaCambioContrasenia() {
        return fechaCambioContrasenia;
    }

    public void setFechaCambioContrasenia(String fechaCambioContrasenia) {
        this.fechaCambioContrasenia = fechaCambioContrasenia;
    }
}