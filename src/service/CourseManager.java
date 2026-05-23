package service;

import java.util.ArrayList;

import domain.Course;

public class CourseManager {

    // Lista de cursos
    private ArrayList<Course> courses;

    // Constructor
    public CourseManager() {

        courses = new ArrayList<>();

    }

    // Agregar curso
    public void agregarCourse(Course course) {

        if (course == null) {
            throw new IllegalArgumentException("El curso no puede ser nulo");
        }

        courses.add(course);

    }

    // Mostrar cursos
    public ArrayList<Course> mostrarCourses() {

        return courses;

    }

    // Buscar curso por código
    public Course buscarCourse(String codigo) {

        for (Course course : courses) {

            if (course.getCodigo().equalsIgnoreCase(codigo)) {
                return course;
            }

        }

        return null;
    }

    // Eliminar curso
    public boolean eliminarCourse(String codigo) {

        Course course = buscarCourse(codigo);

        if (course != null) {

            courses.remove(course);
            return true;

        }

        return false;
    }

}