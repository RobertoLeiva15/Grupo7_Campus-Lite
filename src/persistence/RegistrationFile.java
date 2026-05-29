package persistence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import domain.Course;
import domain.Registration;
import domain.Student;

public class RegistrationFile {

    private final String CSV_FILE = "registrations.csv";
    private final String TXT_FILE = "registrations.txt";

    // Guardar inscripciones
    public void guardarRegistrations(ArrayList<Registration> registrations) {

        guardarCSV(registrations);
        guardarTXT(registrations);

    }
    // Guardar CSV
    private void guardarCSV(ArrayList<Registration> registrations) {

        try (FileWriter writer = new FileWriter(CSV_FILE)) {
        	writer.write("Carnet,CodigoCurso\n");
            for (Registration registration : registrations) {

                writer.write(
                    registration.getStudent().getCarnet() + "," +
                    registration.getCourse().getCodigo() + "\n"
                );

            }

        } catch (IOException e) {

            System.out.println("Error al guardar CSV de inscripciones");

        }

    }
    // Guardar TXT
    private void guardarTXT(ArrayList<Registration> registrations) {

        try (FileWriter writer = new FileWriter(TXT_FILE)) {

            for (Registration registration : registrations) {

                writer.write(registration.toString() + "\n");

            }

        } catch (IOException e) {
            System.out.println("Error al guardar TXT de inscripciones");

        }

    }
    // Cargar inscripciones
    public ArrayList<Registration> cargarRegistrations() {

        ArrayList<Registration> registrations = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(CSV_FILE))) {
        	reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                Student student = new Student();
                student.setCarnet(data[0]);

                Course course = new Course();
                course.setCodigo(data[1]);

                Registration registration =
                        new Registration(student, course);

                registrations.add(registration);

            }

        } catch (IOException e) {

            System.out.println("Error al cargar inscripciones");

        }

        return registrations;
    }

}