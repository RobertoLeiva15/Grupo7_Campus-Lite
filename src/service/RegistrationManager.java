package service;

import java.util.ArrayList;

import domain.Registration;

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

}