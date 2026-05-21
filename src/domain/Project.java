package domain;

public class Project extends Evaluation {

    private double notaPresentacion;

    // Constructor vacío
    public Project() {

    }

    // Constructor completo (Sobrecarga)
    public Project(String nombre, double ponderacion, double nota, double notaPresentacion) {

        super(nombre, ponderacion, nota);
        setNotaPresentacion(notaPresentacion);

    }

    public double getNotaPresentacion() {
        return notaPresentacion;
    }

    public void setNotaPresentacion(double notaPresentacion) {

        if (notaPresentacion < 0 || notaPresentacion > 100) {
            throw new IllegalArgumentException("La nota de presentación debe estar entre 0 y 100");
        }

        this.notaPresentacion = notaPresentacion;
    }

    // Implementación del método abstracto
    @Override
    public double calcularNotaFinal() {
        double notaFinal = (getNota() * 0.70) + (notaPresentacion * 0.30);
        return notaFinal * (getPonderacion() / 100);
    }

    @Override
    public String toString() {
        return super.toString() + " | Nota Presentación: " + notaPresentacion + " | Tipo: Proyecto";
    }
}