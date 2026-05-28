package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

import domain.Course;
import domain.Evaluation;
import domain.Exam;
import domain.Laboratory;
import domain.Project;
import service.CourseManager;
import persistence.CourseFile;

public class CourseWindow extends JFrame {

    private CourseManager courseManager;
    private CourseFile    courseFile;

    // Formulario curso
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtCreditos;
    private JTextField txtCupo;

    // Formulario evaluación
    private JTextField txtEvalNombre;
    private JTextField txtEvalPonderacion;
    private JTextField txtEvalNota;
    private JTextField txtEvalExtra;
    private JComboBox<String> cmbTipo;
    private JLabel lblExtra;

    // Tablas
    private JTable            tablaCursos;
    private DefaultTableModel modeloCursos;
    private JTable            tablaEvals;
    private DefaultTableModel modeloEvals;

    private Course cursoSeleccionado = null;

    public CourseWindow(CourseManager courseManager, CourseFile courseFile) {
        this.courseManager = courseManager;
        this.courseFile    = courseFile;
        construirUI();
        actualizarTablaCursos();
    }

    private void construirUI() {
        setTitle("Campus Lite — Gestión de Cursos");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(MainWindow.GRIS_SUAVE);
        setContentPane(root);

        root.add(crearHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.setBackground(MainWindow.GRIS_SUAVE);
        tabs.addTab("📚 Cursos",       crearPestañaCursos());
        tabs.addTab("📝 Evaluaciones", crearPestañaEvaluaciones());
        root.add(tabs, BorderLayout.CENTER);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
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
        header.setPreferredSize(new Dimension(950, 65));
        header.setBorder(new EmptyBorder(12, 24, 12, 24));

        JLabel lbl = new JLabel("📚  Gestión de Cursos y Evaluaciones");
        lbl.setFont(new Font("Serif", Font.BOLD, 22));
        lbl.setForeground(MainWindow.BLANCO);
        header.add(lbl, BorderLayout.WEST);
        return header;
    }

    // Pestaña de Cursos
    private JPanel crearPestañaCursos() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(MainWindow.GRIS_SUAVE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Formulario
        JPanel form = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainWindow.BLANCO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(MainWindow.AZUL_CLARO);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        form.setPreferredSize(new Dimension(230, 0));

        JLabel tit = new JLabel("Datos del Curso");
        tit.setFont(new Font("SansSerif", Font.BOLD, 14));
        tit.setForeground(MainWindow.AZUL_OSCURO);
        tit.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtCodigo   = campo();
        txtNombre   = campo();
        txtCreditos = campo();
        txtCupo     = campo();

        JButton btnAgregar  = MainWindow.crearBoton("Agregar curso");
        JButton btnEliminar = MainWindow.crearBotonDorado("Eliminar curso");
        btnAgregar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEliminar.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnAgregar.addActionListener(e  -> agregarCurso());
        btnEliminar.addActionListener(e -> eliminarCurso());

        form.add(tit);
        form.add(Box.createVerticalStrut(8));
        form.add(sep());
        form.add(Box.createVerticalStrut(12));
        form.add(lbl("Código *")); form.add(txtCodigo);   form.add(Box.createVerticalStrut(8));
        form.add(lbl("Nombre *")); form.add(txtNombre);   form.add(Box.createVerticalStrut(8));
        form.add(lbl("Créditos")); form.add(txtCreditos); form.add(Box.createVerticalStrut(8));
        form.add(lbl("Cupo"));     form.add(txtCupo);     form.add(Box.createVerticalStrut(18));
        form.add(btnAgregar);
        form.add(Box.createVerticalStrut(8));
        form.add(btnEliminar);

        // Tabla
        String[] cols = {"Código", "Nombre", "Créditos", "Cupo", "Evaluaciones"};
        modeloCursos = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaCursos = new JTable(modeloCursos);
        StudentWindow.estilizarTabla(tablaCursos);

        tablaCursos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaCursos.getSelectedRow() >= 0) {
                String cod = (String) modeloCursos.getValueAt(tablaCursos.getSelectedRow(), 0);
                cursoSeleccionado = courseManager.buscarCourse(cod);
                if (cursoSeleccionado != null) {
                    txtCodigo.setText(cursoSeleccionado.getCodigo());
                    txtNombre.setText(cursoSeleccionado.getNombre());
                    txtCreditos.setText(String.valueOf(cursoSeleccionado.getCreditos()));
                    txtCupo.setText(String.valueOf(cursoSeleccionado.getCupo()));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tablaCursos);
        scroll.setBorder(BorderFactory.createLineBorder(MainWindow.AZUL_CLARO));

        panel.add(form,   BorderLayout.WEST);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // Pestaña de Evaluaciones
    private JPanel crearPestañaEvaluaciones() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(MainWindow.GRIS_SUAVE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Formulario evaluación
        JPanel form = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainWindow.BLANCO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(MainWindow.AZUL_CLARO);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        form.setPreferredSize(new Dimension(250, 0));

        JLabel tit = new JLabel("Nueva Evaluación");
        tit.setFont(new Font("SansSerif", Font.BOLD, 14));
        tit.setForeground(MainWindow.AZUL_OSCURO);
        tit.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Selector de curso
        JLabel lblCurso = new JLabel("Selecciona un curso en la tabla →");
        lblCurso.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblCurso.setForeground(new Color(100, 120, 160));
        lblCurso.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Tipo
        cmbTipo = new JComboBox<>(new String[]{"Examen", "Laboratorio", "Proyecto"});
        cmbTipo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cmbTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cmbTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbTipo.addActionListener(e -> actualizarCampoExtra());

        txtEvalNombre      = campo();
        txtEvalPonderacion = campo();
        txtEvalNota        = campo();
        txtEvalExtra       = campo();

        lblExtra = lbl("Bonificación (0-10)");

        JButton btnAgregar  = MainWindow.crearBoton("Agregar eval.");
        JButton btnEliminar = MainWindow.crearBotonDorado("Eliminar eval.");
        btnAgregar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEliminar.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnAgregar.addActionListener(e  -> agregarEvaluacion());
        btnEliminar.addActionListener(e -> eliminarEvaluacion());

        form.add(tit);
        form.add(Box.createVerticalStrut(4));
        form.add(lblCurso);
        form.add(Box.createVerticalStrut(8));
        form.add(sep());
        form.add(Box.createVerticalStrut(10));
        form.add(lbl("Tipo"));              form.add(cmbTipo);             form.add(Box.createVerticalStrut(8));
        form.add(lbl("Nombre *"));          form.add(txtEvalNombre);       form.add(Box.createVerticalStrut(8));
        form.add(lbl("Ponderación (%) *")); form.add(txtEvalPonderacion);  form.add(Box.createVerticalStrut(8));
        form.add(lbl("Nota (0-100) *"));    form.add(txtEvalNota);         form.add(Box.createVerticalStrut(8));
        form.add(lblExtra);                 form.add(txtEvalExtra);        form.add(Box.createVerticalStrut(18));
        form.add(btnAgregar);
        form.add(Box.createVerticalStrut(8));
        form.add(btnEliminar);

        // Tabla evaluaciones
        String[] cols = {"Nombre", "Tipo", "Ponderación", "Nota", "Contribución"};
        modeloEvals = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaEvals = new JTable(modeloEvals);
        StudentWindow.estilizarTabla(tablaEvals);

        // Panel de tabla + selector de curso
        JPanel panelTabla = new JPanel(new BorderLayout(0, 8));
        panelTabla.setOpaque(false);

        // Selector de curso arriba
        JPanel selectorCurso = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        selectorCurso.setOpaque(false);
        JLabel lblSel = new JLabel("Curso activo: ");
        lblSel.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblSel.setForeground(MainWindow.AZUL_OSCURO);

        JComboBox<String> cmbCursos = new JComboBox<>();
        cmbCursos.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cmbCursos.setPreferredSize(new Dimension(220, 28));
        for (Course c : courseManager.mostrarCourses()) {
            cmbCursos.addItem(c.getCodigo() + " - " + c.getNombre());
        }

        JButton btnCargar = MainWindow.crearBoton("Cargar evals.");
        btnCargar.addActionListener(e -> {
            if (cmbCursos.getSelectedItem() != null) {
                String cod = cmbCursos.getSelectedItem().toString().split(" - ")[0];
                cursoSeleccionado = courseManager.buscarCourse(cod);
                actualizarTablaEvals();
            }
        });

        selectorCurso.add(lblSel);
        selectorCurso.add(cmbCursos);
        selectorCurso.add(btnCargar);

        JScrollPane scroll = new JScrollPane(tablaEvals);
        scroll.setBorder(BorderFactory.createLineBorder(MainWindow.AZUL_CLARO));

        panelTabla.add(selectorCurso, BorderLayout.NORTH);
        panelTabla.add(scroll,        BorderLayout.CENTER);

        // Info de ponderación
        JLabel lblInfo = new JLabel("Suma de ponderaciones: 0%");
        lblInfo.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblInfo.setForeground(new Color(100, 120, 160));
        panelTabla.add(lblInfo, BorderLayout.SOUTH);

        panel.add(form,       BorderLayout.WEST);
        panel.add(panelTabla, BorderLayout.CENTER);
        return panel;
    }

    // Lógica cursos
    private void agregarCurso() {
        try {
            String cod    = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            String credStr = txtCreditos.getText().trim();
            String cupoStr = txtCupo.getText().trim();

            if (cod.isEmpty() || nombre.isEmpty()) {
                error("Código y nombre son obligatorios."); return;
            }
            if (courseManager.buscarCourse(cod) != null) {
                error("Ya existe un curso con ese código."); return;
            }

            int cred = credStr.isEmpty() ? 0 : Integer.parseInt(credStr);
            int cupo = cupoStr.isEmpty() ? 0 : Integer.parseInt(cupoStr);

            courseManager.agregarCourse(new Course(cod, nombre, cred, cupo));
            courseFile.guardarCourses(courseManager.mostrarCourses());
            actualizarTablaCursos();
            limpiarFormCurso();
            exito("Curso agregado correctamente.");
        } catch (NumberFormatException ex) {
            error("Créditos y cupo deben ser números enteros.");
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        }
    }

    private void eliminarCurso() {
        String cod = txtCodigo.getText().trim();
        if (cod.isEmpty()) { error("Escribe o selecciona el código del curso."); return; }
        int c = JOptionPane.showConfirmDialog(this,
            "¿Eliminar el curso: " + cod + "?", "Confirmar",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            if (courseManager.eliminarCourse(cod)) {
                courseFile.guardarCourses(courseManager.mostrarCourses());
                actualizarTablaCursos();
                limpiarFormCurso();
                exito("Curso eliminado.");
            } else {
                error("No se encontró el curso.");
            }
        }
    }

    // Lógica evaluaciones
    private void agregarEvaluacion() {
        if (cursoSeleccionado == null) {
            error("Primero carga un curso en la pestaña de Evaluaciones.");
            return;
        }
        try {
            String nombre = txtEvalNombre.getText().trim();
            double pond   = Double.parseDouble(txtEvalPonderacion.getText().trim());
            double nota   = Double.parseDouble(txtEvalNota.getText().trim());
            double extra  = txtEvalExtra.getText().trim().isEmpty() ? 0
                            : Double.parseDouble(txtEvalExtra.getText().trim());

            if (nombre.isEmpty()) { error("El nombre de la evaluación es obligatorio."); return; }

            double sumaActual = cursoSeleccionado.sumaPonderaciones();
            if (sumaActual + pond > 100) {
                error("La suma de ponderaciones superaría el 100%.\n"
                    + "Disponible: " + (100 - sumaActual) + "%");
                return;
            }

            String tipo = (String) cmbTipo.getSelectedItem();
            Evaluation eval;
            switch (tipo) {
                case "Examen":      eval = new Exam(nombre, pond, nota, extra);       break;
                case "Laboratorio": eval = new Laboratory(nombre, pond, nota, extra); break;
                default:            eval = new Project(nombre, pond, nota, extra);    break;
            }

            cursoSeleccionado.agregarEvaluation(eval);
            actualizarTablaEvals();
            limpiarFormEval();
            exito("Evaluación agregada al curso " + cursoSeleccionado.getCodigo());
        } catch (NumberFormatException ex) {
            error("Ponderación, nota y el campo extra deben ser números.");
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        }
    }

    private void eliminarEvaluacion() {
        if (cursoSeleccionado == null) { error("Selecciona un curso primero."); return; }
        int fila = tablaEvals.getSelectedRow();
        if (fila < 0) { error("Selecciona una evaluación de la tabla."); return; }
        String nombre = (String) modeloEvals.getValueAt(fila, 0);
        int c = JOptionPane.showConfirmDialog(this,
            "¿Eliminar la evaluación: " + nombre + "?", "Confirmar",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            cursoSeleccionado.eliminarEvaluation(nombre);
            actualizarTablaEvals();
        }
    }

    private void actualizarTablaCursos() {
        modeloCursos.setRowCount(0);
        for (Course c : courseManager.mostrarCourses()) {
            modeloCursos.addRow(new Object[]{
                c.getCodigo(), c.getNombre(), c.getCreditos(),
                c.getCupo(), c.getEvaluations().size()
            });
        }
    }

    private void actualizarTablaEvals() {
        modeloEvals.setRowCount(0);
        if (cursoSeleccionado == null) return;
        for (Evaluation e : cursoSeleccionado.getEvaluations()) {
            String tipo = e.getClass().getSimpleName();
            modeloEvals.addRow(new Object[]{
                e.getNombre(), tipo,
                String.format("%.1f%%", e.getPonderacion()),
                String.format("%.1f", e.getNota()),
                String.format("%.2f", e.calcularNotaFinal())
            });
        }
    }

    private void actualizarCampoExtra() {
        String tipo = (String) cmbTipo.getSelectedItem();
        switch (tipo) {
            case "Examen":      lblExtra.setText("Bonificación (0-10)");  break;
            case "Laboratorio": lblExtra.setText("Nota práctica (0-100)"); break;
            default:            lblExtra.setText("Nota presentación (0-100)"); break;
        }
    }

    // Limpieza
    private void limpiarFormCurso() {
        txtCodigo.setText(""); txtNombre.setText("");
        txtCreditos.setText(""); txtCupo.setText("");
        tablaCursos.clearSelection();
    }

    private void limpiarFormEval() {
        txtEvalNombre.setText(""); txtEvalPonderacion.setText("");
        txtEvalNota.setText(""); txtEvalExtra.setText("");
    }

    // Helpers UI
    private JTextField campo() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 230)),
            new EmptyBorder(5, 8, 5, 8)));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        return tf;
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(new Color(60, 80, 120));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JSeparator sep() {
        JSeparator s = new JSeparator();
        s.setForeground(MainWindow.DORADO);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        return s;
    }

    private void error(String msg)  { JOptionPane.showMessageDialog(this, msg, "Error",  JOptionPane.ERROR_MESSAGE); }
    private void exito(String msg)  { JOptionPane.showMessageDialog(this, msg, "Éxito",  JOptionPane.INFORMATION_MESSAGE); }
}