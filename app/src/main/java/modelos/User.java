package modelos;

public class User {
    private int id;
    private String nombre;
    private String contrasena;

    public User(int id, String nombre, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getContrasena() { return contrasena; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}