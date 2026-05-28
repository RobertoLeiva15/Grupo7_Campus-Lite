package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

import domain.Course;
import domain.Evaluation;
import domain.Registration;
import domain.Student;
import service.CourseManager;
import service.RegistrationManager;
import service.StudentManager;

public class ReportWindow extends JFrame {

    private StudentManager      studentManager;
    private CourseManager       courseManager;
    private RegistrationManager registrationManager;

    private JTable            tablaReporte;
    private DefaultTableModel modeloReporte;

    private JComboBox<String> cmbModo;
    private JComboBox<String> cmbFiltro;

    public ReportWindow(StudentManager studentManager,
                        CourseManager courseManager,
                        RegistrationManager registrationManager) {
        this.studentManager      = studentManager;
        this.courseManager       = courseManager;
        this.registrationManager = registrationManager;
        construirUI();
    }

    private void construirUI() {
        setTitle("Campus Lite — Reportes");
        setSize(820, 540);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(MainWindow.GRIS_SUAVE);
        setContentPane(root);

        root.add(crearHeader(),   BorderLayout.NORTH);
        root.add(crearCuerpo(),   BorderLayout.CENTER);
        root.add(crearResumen(),  BorderLayout.SOUTH);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, MainWindow.AZUL_OSCURO,
                                              getWidth(), 0, MainWindow.AZUL_MEDIO));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(MainWindow.DORADO);
                g2.setStroke(new BasicStroke(3f));
                g2.drawLine(0, getHeight()-3, getWidth(), getHeight()-3);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(820, 65));
        header.setBorder(new EmptyBorder(12, 24, 12, 24));

        JLabel lbl = new JLabel("📊  Reportes Académicos");
        lbl.setFont(new Font("Serif", Font.BOLD, 22));
        lbl.setForeground(MainWindow.BLANCO);
        header.add(lbl, BorderLayout.WEST);
        return header;
    }

    private JPanel crearCuerpo() {
        JPanel cuerpo = new JPanel(new BorderLayout(0, 12));
        cuerpo.setBackground(MainWindow.GRIS_SUAVE);
        cuerpo.setBorder(new EmptyBorder(16, 20, 10, 20));

        // Barra de controles
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controles.setOpaque(false);

        JLabel lblModo = new JLabel("Ver por:");
        lblModo.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblModo.setForeground(MainWindow.AZUL_OSCURO);

        cmbModo = new JComboBox<>(new String[]{"Por Curso", "Por Estudiante"});
        cmbModo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cmbModo.setPreferredSize(new Dimension(160, 28));
        cmbModo.addActionListener(e -> actualizarCmbFiltro());

        JLabel lblFiltro = new JLabel("Filtro:");
        lblFiltro.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblFiltro.setForeground(MainWindow.AZUL_OSCURO);

        cmbFiltro = new JComboBox<>();
        cmbFiltro.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cmbFiltro.setPreferredSize(new Dimension(240, 28));

        JButton btnGenerar  = MainWindow.crearBoton("Generar reporte");
        JButton btnTodos    = new JButton("Ver todo");
        btnTodos.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnTodos.setForeground(MainWindow.AZUL_MEDIO);
        btnTodos.setBackground(MainWindow.BLANCO);
        btnTodos.setBorder(BorderFactory.createLineBorder(MainWindow.AZUL_CLARO));
        btnTodos.setFocusPainted(false);
        btnTodos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnTodos.setPreferredSize(new Dimension(80, 28));

        btnGenerar.addActionListener(e -> generarReporte());
        btnTodos.addActionListener(e   -> generarReporteTodos());

        controles.add(lblModo);
        controles.add(cmbModo);
        controles.add(lblFiltro);
        controles.add(cmbFiltro);
        controles.add(btnGenerar);
        controles.add(btnTodos);

        // Tabla reporte
        String[] cols = {"Estudiante", "Carnet", "Curso", "Promedio", "Estado"};
        modeloReporte = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaReporte = new JTable(modeloReporte);
        estilizarTablaReporte();

        JScrollPane scroll = new JScrollPane(tablaReporte);
        scroll.setBorder(BorderFactory.createLineBorder(MainWindow.AZUL_CLARO));

        cuerpo.add(controles, BorderLayout.NORTH);
        cuerpo.add(scroll,    BorderLayout.CENTER);

        // Cargar combos iniciales
        actualizarCmbFiltro();

        return cuerpo;
    }

    private JPanel crearResumen() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 8)) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(220, 228, 245));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);

        JLabel nota = new JLabel("Umbral de aprobación: 61 / 100 puntos");
        nota.setFont(new Font("SansSerif", Font.ITALIC, 12));
        nota.setForeground(new Color(60, 80, 130));

        JLabel leyenda = new JLabel("🟢 Aprobado   🔴 Reprobado");
        leyenda.setFont(new Font("SansSerif", Font.PLAIN, 12));
        leyenda.setForeground(MainWindow.AZUL_OSCURO);

        panel.add(nota);
        panel.add(leyenda);
        return panel;
    }

    // Lógica
    private void actualizarCmbFiltro() {
        cmbFiltro.removeAllItems();
        String modo = (String) cmbModo.getSelectedItem();

        if ("Por Curso".equals(modo)) {
            for (Course c : courseManager.mostrarCourses()) {
                cmbFiltro.addItem(c.getCodigo() + " - " + c.getNombre());
            }
        } else {
            for (Student s : studentManager.mostrarStudents()) {
                cmbFiltro.addItem(s.getCarnet() + " - " + s.getNombre() + " " + s.getApellido());
            }
        }
    }

    private void generarReporte() {
        modeloReporte.setRowCount(0);
        String modo = (String) cmbModo.getSelectedItem();
        String sel  = (String) cmbFiltro.getSelectedItem();
        if (sel == null) return;

        String clave = sel.split(" - ")[0].trim();

        if ("Por Curso".equals(modo)) {
            Course curso = courseManager.buscarCourse(clave);
            if (curso == null) return;
            for (Registration r : registrationManager.buscarPorCurso(clave)) {
                double promedio = curso.calcularPromedio();
                agregarFilaReporte(r.getStudent(), curso, promedio);
            }
        } else {
            Student est = studentManager.buscarStudent(clave);
            if (est == null) return;
            for (Registration r : registrationManager.buscarPorEstudiante(clave)) {
                Course curso = r.getCourse();
                double promedio = curso.calcularPromedio();
                agregarFilaReporte(est, curso, promedio);
            }
        }

        if (modeloReporte.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No hay inscripciones para mostrar con ese filtro.",
                "Sin datos", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void generarReporteTodos() {
        modeloReporte.setRowCount(0);
        for (Registration r : registrationManager.mostrarRegistrations()) {
            double promedio = r.getCourse().calcularPromedio();
            agregarFilaReporte(r.getStudent(), r.getCourse(), promedio);
        }
    }

    private void agregarFilaReporte(Student est, Course curso, double promedio) {
        String estado = promedio >= 61.0 ? "✅ Aprobado" : "❌ Reprobado";
        modeloReporte.addRow(new Object[]{
            est.getNombre() + " " + est.getApellido(),
            est.getCarnet(),
            curso.getNombre(),
            String.format("%.2f", promedio),
            estado
        });
    }

    // Estilo tabla con colores por estado
    private void estilizarTablaReporte() {
        tablaReporte.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaReporte.setRowHeight(28);
        tablaReporte.setGridColor(new Color(210, 220, 240));
        tablaReporte.setShowVerticalLines(false);

        JTableHeader header = tablaReporte.getTableHeader();
        header.setBackground(MainWindow.AZUL_OSCURO);
        header.setForeground(MainWindow.BLANCO);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Renderer con color por estado
        tablaReporte.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(
                    t, v, sel, foc, row, col);

                if (!sel) {
                    String estado = "";
                    if (modeloReporte.getRowCount() > row && modeloReporte.getColumnCount() > 4) {
                        Object val = modeloReporte.getValueAt(row, 4);
                        if (val != null) estado = val.toString();
                    }

                    if (estado.contains("Aprobado")) {
                        c.setBackground(new Color(230, 255, 235));
                    } else if (estado.contains("Reprobado")) {
                        c.setBackground(new Color(255, 232, 232));
                    } else {
                        c.setBackground(row % 2 == 0 ? MainWindow.BLANCO
                                                      : new Color(240, 245, 255));
                    }
                }
                ((JLabel) c).setBorder(new EmptyBorder(0, 8, 0, 8));
                return c;
            }
        });
    }
}