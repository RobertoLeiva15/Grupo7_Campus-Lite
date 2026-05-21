package service;

import java.util.ArrayList;

import domain.Student; 

public class StudentManager {
	 // Lista de estudiantes
    private ArrayList<Student> students;

    // Constructor
    public StudentManager() {

        students = new ArrayList<>();

    }

    // Agregar estudiante
    public void agregarStudent(Student student) {

        if (student == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo");
        }

        students.add(student);

    }

    // Mostrar lista de estudiantes
    public ArrayList<Student> mostrarStudents() {

        return students;

    }

    // Buscar estudiante por carnet
    public Student buscarStudent(String carnet) {

        for (Student student : students) {

            if (student.getCarnet().equalsIgnoreCase(carnet)) {
                return student;
            }

        }

        return null;
    }

    // Eliminar estudiante por carnet
    public boolean eliminarStudent(String carnet) {

        Student student = buscarStudent(carnet);

        if (student != null) {

            students.remove(student);
            return true;

        }
        
        return false;
    }
}
