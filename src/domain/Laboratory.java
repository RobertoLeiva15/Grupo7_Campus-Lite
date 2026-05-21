package domain;

public class Laboratory extends Evaluation {

    private double notaPractica;

    // Constructor vacío
    public Laboratory() {

    }

    // Constructor completo (Sobrecarga)
    public Laboratory(String nombre, double ponderacion, double nota, double notaPractica) {

        super(nombre, ponderacion, nota);
        setNotaPractica(notaPractica);

    }

    // Getter y Setter de notaPractica
    public double getNotaPractica() {
        return notaPractica;
    }

    public void setNotaPractica(double notaPractica) {

        if (notaPractica < 0 || notaPractica > 100) {
            throw new IllegalArgumentException("La nota de práctica debe estar entre 0 y 100");
        }

        this.notaPractica = notaPractica;
    }

    // Implementación del método abstracto
    @Override
    public double calcularNotaFinal() {

        double promedio = (getNota() + notaPractica) / 2;

        return promedio * (getPonderacion() / 100);
    }

    // Método sobrescrito
    @Override
    public String toString() {

        return super.toString() +
               " | Nota Práctica: " + notaPractica +
               " | Tipo: Laboratorio";
    }
}