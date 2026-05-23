package service;

import java.util.ArrayList;

import domain.Evaluation;

public class EvaluationManager {

    // Lista de evaluaciones
    private ArrayList<Evaluation> evaluations;

    // Constructor
    public EvaluationManager() {

        evaluations = new ArrayList<>();

    }

    // Agregar evaluación
    public void agregarEvaluation(Evaluation evaluation) {

        if (evaluation == null) {
            throw new IllegalArgumentException("La evaluación no puede ser nula");
        }

        evaluations.add(evaluation);

    }

    // Mostrar evaluaciones
    public ArrayList<Evaluation> mostrarEvaluations() {

        return evaluations;

    }

    // Buscar evaluación por nombre
    public Evaluation buscarEvaluation(String nombre) {

        for (Evaluation evaluation : evaluations) {

            if (evaluation.getNombre().equalsIgnoreCase(nombre)) {
                return evaluation;
            }

        }

        return null;
    }

    // Eliminar evaluación
    public boolean eliminarEvaluation(String nombre) {

        Evaluation evaluation = buscarEvaluation(nombre);

        if (evaluation != null) {

            evaluations.remove(evaluation);
            return true;

        }

        return false;
    }

}