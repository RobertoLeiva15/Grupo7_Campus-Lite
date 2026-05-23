package persistence;

	import java.io.BufferedReader;
	import java.io.FileReader;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.util.ArrayList;

	import domain.Student;

	public class StudentFile {

	    private final String CSV_FILE = "students.csv";
	    private final String TXT_FILE = "students.txt";

	    // Guardar estudiantes
	    public void guardarStudents(ArrayList<Student> students) {

	        guardarCSV(students);
	        guardarTXT(students);

	    }

	    // Guardar CSV
	    private void guardarCSV(ArrayList<Student> students) {

	        try (FileWriter writer = new FileWriter(CSV_FILE)) {

	            for (Student student : students) {

	                writer.write(
	                    student.getCarnet() + "," +
	                    student.getNombre() + "," +
	                    student.getApellido() + "," +
	                    student.getCorreo() + "\n"
	                );

	            }

	        } catch (IOException e) {

	            System.out.println("Error al guardar CSV de estudiantes");

	        }

	    }

	    // Guardar TXT
	    private void guardarTXT(ArrayList<Student> students) {

	        try (FileWriter writer = new FileWriter(TXT_FILE)) {

	            for (Student student : students) {

	                writer.write(student.toString() + "\n");

	            }

	        } catch (IOException e) {

	            System.out.println("Error al guardar TXT de estudiantes");

	        }

	    }

	    // Cargar estudiantes
	    public ArrayList<Student> cargarStudents() {

	        ArrayList<Student> students = new ArrayList<>();

	        try (BufferedReader reader =
	                     new BufferedReader(new FileReader(CSV_FILE))) {

	            String line;

	            while ((line = reader.readLine()) != null) {

	                String[] data = line.split(",");

	                Student student = new Student(
	                        data[0],
	                        data[1],
	                        data[2],
	                        data[3]
	                );

	                students.add(student);
	            }

	        } catch (IOException e) {

	            System.out.println("Error al cargar estudiantes");
	        }

	        return students;
	    }
	}