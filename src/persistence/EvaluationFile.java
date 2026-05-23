package persistence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import domain.Evaluation;
import domain.Exam;

public class EvaluationFile {

    private final String CSV_FILE = "evaluations.csv";
    private final String TXT_FILE = "evaluations.txt";

    // Guardar evaluaciones
    public void guardarEvaluations(ArrayList<Evaluation> evaluations) {

        guardarCSV(evaluations);
        guardarTXT(evaluations);

    }
    // Guardar CSV
    private void guardarCSV(ArrayList<Evaluation> evaluations) {

        try (FileWriter writer = new FileWriter(CSV_FILE)) {

            for (Evaluation evaluation : evaluations) {

                writer.write(
                    evaluation.getNombre() + "," +
                    evaluation.getPonderacion() + "," +
                    evaluation.getNota() + "\n"
                );

            }

        } catch (IOException e) {

            System.out.println("Error al guardar CSV de evaluaciones");

        }

    }
    // Guardar TXT
    private void guardarTXT(ArrayList<Evaluation> evaluations) {

        try (FileWriter writer = new FileWriter(TXT_FILE)) {

            for (Evaluation evaluation : evaluations) {

                writer.write(evaluation.toString() + "\n");

            }

        } catch (IOException e) {

            System.out.println("Error al guardar TXT de evaluaciones");

        }

    }
    // Cargar evaluaciones
    public ArrayList<Evaluation> cargarEvaluations() {

        ArrayList<Evaluation> evaluations = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(CSV_FILE))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                Evaluation evaluation = new Exam(
                        data[0],
                        Double.parseDouble(data[1]),
                        Double.parseDouble(data[2]),
                        0
                );

                evaluations.add(evaluation);

            }

        } catch (IOException e) {

            System.out.println("Error al cargar evaluaciones");

        }
        return evaluations;
    }
}