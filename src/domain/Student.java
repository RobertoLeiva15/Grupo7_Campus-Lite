package domain;

public class Student {

    // Atributos privados (Encapsulamiento)
    private String carnet;
    private String nombre;
    private String apellido;
    private String correo;

    // Constructor vacío (Sobrecarga)
    public Student() {

    }

    // Constructor con parámetros (Sobrecarga)
    public Student(String carnet, String nombre, String apellido, String correo) {

        setCarnet(carnet);
        setNombre(nombre);
        setApellido(apellido);
        setCorreo(correo);

    }

    // Getter y Setter de carnet
    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {

        if (carnet == null || carnet.trim().isEmpty()) {
            throw new IllegalArgumentException("El carnet no puede estar vacío");
        }

        this.carnet = carnet;
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

    // Getter y Setter de apellido
    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {

        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }

        this.apellido = apellido;
    }

    // Getter y Setter de correo
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {

        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }

        if (!correo.contains("@")) {
            throw new IllegalArgumentException("Correo inválido");
        }

        this.correo = correo;
    }

    // Método sobrescrito
    @Override
    public String toString() {

        return "Carnet: " + carnet +
               " | Nombre: " + nombre + " " + apellido +
               " | Correo: " + correo;
    }
}