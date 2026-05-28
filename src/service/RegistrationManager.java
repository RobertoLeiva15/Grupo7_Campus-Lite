package service;

import java.util.ArrayList;

import domain.Course;
import domain.Registration;
import domain.Student;

public class RegistrationManager {

    // Lista de inscripciones
    private ArrayList<Registration> registrations;

    // Constructor
    public RegistrationManager() {
        registrations = new ArrayList<>();
    }

    // Agregar inscripción
    public void agregarRegistration(Registration registration) {
        if (registration == null) {
            throw new IllegalArgumentException("La inscripción no puede ser nula");
        }
        registrations.add(registration);
    }

    // Mostrar inscripciones
    public ArrayList<Registration> mostrarRegistrations() {
        return registrations;
    }

    // Eliminar inscripción
    public boolean eliminarRegistration(Registration registration) {
        if (registration != null) {
            registrations.remove(registration);
            return true;
        }
        return false;
    }

    // Buscar todas las inscripciones de un estudiante por carnet
    public ArrayList<Registration> buscarPorEstudiante(String carnet) {
        ArrayList<Registration> resultado = new ArrayList<>();
        for (Registration r : registrations) {
            if (r.getStudent().getCarnet().equalsIgnoreCase(carnet)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    // Buscar todas las inscripciones de un curso por código
    public ArrayList<Registration> buscarPorCurso(String codigoCurso) {
        ArrayList<Registration> resultado = new ArrayList<>();
        for (Registration r : registrations) {
            if (r.getCourse().getCodigo().equalsIgnoreCase(codigoCurso)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    // Verificar si un estudiante ya está inscrito en un curso
    public boolean estaInscrito(String carnet, String codigoCurso) {
        for (Registration r : registrations) {
            if (r.getStudent().getCarnet().equalsIgnoreCase(carnet) &&
                r.getCourse().getCodigo().equalsIgnoreCase(codigoCurso)) {
                return true;
            }
        }
        return false;
    }
}