package domain;

public class Exam extends Evaluation {

    private double bonificacion;

    // Constructor vacío
    public Exam() {

    }

    // Constructor completo (Sobrecarga)
    public Exam(String nombre, double ponderacion, double nota, double bonificacion) {

        super(nombre, ponderacion, nota);
        setBonificacion(bonificacion);

    }

    public double getBonificacion() {
        return bonificacion;
    }

    public void setBonificacion(double bonificacion) {

        if (bonificacion < 0 || bonificacion > 10) {
            throw new IllegalArgumentException("La bonificación debe estar entre 0 y 10");
        }

        this.bonificacion = bonificacion;
    }

    // Implementación del método abstracto
    @Override
    public double calcularNotaFinal() {
        return (getNota() + bonificacion) * (getPonderacion() / 100);
    }

    @Override
    public String toString() {
        return super.toString() + " | Bonificación: " + bonificacion + " | Tipo: Examen";
    }
}