package domain;

public abstract class Evaluation {

    // Atributos privados (Encapsulamiento)
    private String nombre;
    private double ponderacion;
    private double nota;

    // Constructor vacío
    public Evaluation() {

    }

    // Constructor con parámetros (Sobrecarga)
    public Evaluation(String nombre, double ponderacion, double nota) {

        setNombre(nombre);
        setPonderacion(ponderacion);
        setNota(nota);

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

    // Getter y Setter de ponderacion
    public double getPonderacion() {
        return ponderacion;
    }

    public void setPonderacion(double ponderacion) {

        if (ponderacion < 0 || ponderacion > 100) {
            throw new IllegalArgumentException("La ponderación debe estar entre 0 y 100");
        }

        this.ponderacion = ponderacion;
    }

    // Getter y Setter de nota
    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {

        if (nota < 0 || nota > 100) {
            throw new IllegalArgumentException("La nota debe estar entre 0 y 100");
        }

        this.nota = nota;
    }

    // Método abstracto (Polimorfismo)
    public abstract double calcularNotaFinal();

    // Método sobrescrito
    @Override
    public String toString() {

        return "Nombre: " + nombre +
               " | Ponderación: " + ponderacion +
               "% | Nota: " + nota;
    }
}