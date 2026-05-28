package domain;

import java.util.ArrayList;

public class Course {

    // Atributos privados (Encapsulamiento)
    private String codigo;
    private String nombre;
    private int creditos;
    private int cupo;

    // Lista de evaluaciones asociadas al curso
    private ArrayList<Evaluation> evaluations;

    // Constructor vacío (Sobrecarga)
    public Course() {
        evaluations = new ArrayList<>();
    }

    // Constructor sin créditos ni cupo (Sobrecarga)
    public Course(String codigo, String nombre) {
        setCodigo(codigo);
        setNombre(nombre);
        this.creditos = 0;
        this.cupo = 0;
        evaluations = new ArrayList<>();
    }

    // Constructor completo (Sobrecarga)
    public Course(String codigo, String nombre, int creditos, int cupo) {
        setCodigo(codigo);
        setNombre(nombre);
        setCreditos(creditos);
        setCupo(cupo);
        evaluations = new ArrayList<>();
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

    // Lista de evaluaciones del curso
    public ArrayList<Evaluation> getEvaluations() {
        return evaluations;
    }

    // Agregar evaluación al curso
    public void agregarEvaluation(Evaluation evaluation) {
        if (evaluation == null) {
            throw new IllegalArgumentException("La evaluación no puede ser nula");
        }
        evaluations.add(evaluation);
    }

    // Eliminar evaluación del curso por nombre
    public boolean eliminarEvaluation(String nombre) {
        for (Evaluation e : evaluations) {
            if (e.getNombre().equalsIgnoreCase(nombre)) {
                evaluations.remove(e);
                return true;
            }
        }
        return false;
    }

    // Calcular promedio ponderado de todas las evaluaciones del curso
    public double calcularPromedio() {
        double total = 0;
        for (Evaluation e : evaluations) {
            total += e.calcularNotaFinal();
        }
        return total;
    }

    // Verificar si el alumno aprobo
    public boolean estaAprobado() {
        return calcularPromedio() >= 61.0;
    }

    // Calcular suma total de ponderaciones registradas
    public double sumaPonderaciones() {
        double suma = 0;
        for (Evaluation e : evaluations) {
            suma += e.getPonderacion();
        }
        return suma;
    }

    // Método sobrescrito
    @Override
    public String toString() {
        return "Código: " + codigo +
               " | Nombre: " + nombre +
               " | Créditos: " + creditos +
               " | Cupo: " + cupo;
    }
}