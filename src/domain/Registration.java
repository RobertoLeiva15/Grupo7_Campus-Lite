package domain;

public class Registration {

    // Relación entre Student y Course
    private Student student;
    private Course course;

    // Constructor vacío
    public Registration() {

    }

    // Constructor con parámetros
    public Registration(Student student, Course course) {

        setStudent(student);
        setCourse(course);

    }

    // Getter y Setter de student
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {

        if (student == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo");
        }

        this.student = student;
    }

    // Getter y Setter de course
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {

        if (course == null) {
            throw new IllegalArgumentException("El curso no puede ser nulo");
        }

        this.course = course;
    }

    // Método sobrescrito
    @Override
    public String toString() {

        return "Estudiante: " +
               student.getNombre() + " " +
               student.getApellido() +
               " | Curso: " +
               course.getNombre();
    }
}