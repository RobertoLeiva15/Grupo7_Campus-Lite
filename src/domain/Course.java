package domain;

public class Course {

    // Atributos privados (Encapsulamiento)
    private String codigo;
    private String nombre;
    private int creditos;
    private int cupo;

    // Constructor vacío (Sobrecarga)
    public Course() {

    }

    // Constructor sin créditos ni cupo (Sobrecarga)
    public Course(String codigo, String nombre) {

        setCodigo(codigo);
        setNombre(nombre);
        this.creditos = 0;
        this.cupo = 0;

    }

    // Constructor completo (Sobrecarga)
    public Course(String codigo, String nombre, int creditos, int cupo) {

        setCodigo(codigo);
        setNombre(nombre);
        setCreditos(creditos);
        setCupo(cupo);

    }

    // Getter y Setter de codigo
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío");
        }

        this.codigo = codigo;
    }

    // Getter y Setter de nombre
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {

        if (nombre == null || nombre.trim().length() < 2) {
            throw new IllegalArgumentException("El nombre debe tener al menos 2 letras");
        }

        this.nombre = nombre;
    }

    // Getter y Setter de creditos
    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {

        if (creditos < 0) {
            throw new IllegalArgumentException("Los créditos no pueden ser negativos");
        }

        this.creditos = creditos;
    }

    // Getter y Setter de cupo
    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {

        if (cupo < 0) {
            throw new IllegalArgumentException("El cupo no puede ser negativo");
        }

        this.cupo = cupo;
    }

    // Método sobrescrito
    public String toString() {

        return "Código: " + codigo +
               " | Nombre: " + nombre +
               " | Créditos: " + creditos +
               " | Cupo: " + cupo;
    }
}