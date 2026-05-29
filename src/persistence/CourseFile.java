package persistence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import domain.Course;

public class CourseFile {

    private final String CSV_FILE = "courses.csv";
    private final String TXT_FILE = "courses.txt";

    // Guardar cursos
    public void guardarCourses(ArrayList<Course> courses) {

        guardarCSV(courses);
        guardarTXT(courses);

    }
    // Guardar CSV
    private void guardarCSV(ArrayList<Course> courses) {

        try (FileWriter writer = new FileWriter(CSV_FILE)) {
        	writer.write("Codigo,Nombre,Creditos,Cupo\n");
            for (Course course : courses) {

                writer.write(
                    course.getCodigo() + "," +
                    course.getNombre() + "," +
                    course.getCreditos() + "," +
                    course.getCupo() + "\n"
                );

            }

        } catch (IOException e) {

            System.out.println("Error al guardar CSV de cursos");

        }

    }
    // Guardar TXT
    private void guardarTXT(ArrayList<Course> courses) {

        try (FileWriter writer = new FileWriter(TXT_FILE)) {

            for (Course course : courses) {

                writer.write(course.toString() + "\n");

            }

        } catch (IOException e) {

            System.out.println("Error al guardar TXT de cursos");

        }

    }

    // Cargar cursos
    public ArrayList<Course> cargarCourses() {

        ArrayList<Course> courses = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(CSV_FILE))) {
        	reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                Course course = new Course(
                        data[0],
                        data[1],
                        Integer.parseInt(data[2]),
                        Integer.parseInt(data[3])
                );

                courses.add(course);

            }

        } catch (IOException e) {

            System.out.println("Error al cargar cursos");

        }

        return courses;
    }
}