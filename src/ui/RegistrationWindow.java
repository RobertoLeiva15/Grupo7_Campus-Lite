package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

import domain.Course;
import domain.Registration;
import domain.Student;
import service.CourseManager;
import service.RegistrationManager;
import service.StudentManager;

public class RegistrationWindow extends JFrame {

    private StudentManager      studentManager;
    private CourseManager       courseManager;
    private RegistrationManager registrationManager;

    private JComboBox<String> cmbEstudiante;
    private JComboBox<String> cmbCurso;

    private JTable            tablaInscritos;
    private DefaultTableModel modeloInscritos;

    private JComboBox<String> cmbFiltro;

    public RegistrationWindow(StudentManager studentManager,
                              CourseManager courseManager,
                              RegistrationManager registrationManager) {
        this.studentManager      = studentManager;
        this.courseManager       = courseManager;
        this.registrationManager = registrationManager;
        construirUI();
        actualizarTabla();
    }

    private void construirUI() {
        setTitle("Campus Lite — Inscripciones");
        setSize(780, 520);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(MainWindow.GRIS_SUAVE);
        setContentPane(root);

        root.add(crearHeader(),  BorderLayout.NORTH);
        root.add(crearCuerpo(),  BorderLayout.CENTER);
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
        header.setPreferredSize(new Dimension(780, 65));
        header.setBorder(new EmptyBorder(12, 24, 12, 24));

        JLabel lbl = new JLabel("📋  Inscripciones");
        lbl.setFont(new Font("Serif", Font.BOLD, 22));
        lbl.setForeground(MainWindow.BLANCO);
        header.add(lbl, BorderLayout.WEST);
        return header;
    }

    private JPanel crearCuerpo() {
        JPanel cuerpo = new JPanel(new BorderLayout(15, 0));
        cuerpo.setBackground(MainWindow.GRIS_SUAVE);
        cuerpo.setBorder(new EmptyBorder(18, 20, 18, 20));

        cuerpo.add(crearFormulario(), BorderLayout.WEST);
        cuerpo.add(crearPanelTabla(), BorderLayout.CENTER);
        return cuerpo;
    }

    private JPanel crearFormulario() {
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

        JLabel tit = new JLabel("Nueva Inscripción");
        tit.setFont(new Font("SansSerif", Font.BOLD, 14));
        tit.setForeground(MainWindow.AZUL_OSCURO);
        tit.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Combo estudiantes
        cmbEstudiante = new JComboBox<>();
        cmbEstudiante.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cmbEstudiante.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cmbEstudiante.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (Student s : studentManager.mostrarStudents()) {
            cmbEstudiante.addItem(s.getCarnet() + " - " + s.getNombre() + " " + s.getApellido());
        }

        // Combo cursos
        cmbCurso = new JComboBox<>();
        cmbCurso.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cmbCurso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cmbCurso.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (Course c : courseManager.mostrarCourses()) {
            cmbCurso.addItem(c.getCodigo() + " - " + c.getNombre());
        }

        JButton btnInscribir = MainWindow.crearBoton("Inscribir");
        JButton btnEliminar  = MainWindow.crearBotonDorado("Desinscribir");
        btnInscribir.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEliminar.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnInscribir.addActionListener(e -> inscribir());
        btnEliminar.addActionListener(e  -> desinscribir());

        // Aviso si listas vacías
        JLabel aviso = new JLabel("<html><i>Registra estudiantes y cursos<br>antes de inscribir.</i></html>");
        aviso.setFont(new Font("SansSerif", Font.PLAIN, 11));
        aviso.setForeground(new Color(120, 140, 180));
        aviso.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(tit);
        form.add(Box.createVerticalStrut(8));
        form.add(sep());
        form.add(Box.createVerticalStrut(14));
        form.add(lbl("Estudiante")); form.add(cmbEstudiante); form.add(Box.createVerticalStrut(10));
        form.add(lbl("Curso"));     form.add(cmbCurso);       form.add(Box.createVerticalStrut(18));
        form.add(btnInscribir);
        form.add(Box.createVerticalStrut(8));
        form.add(btnEliminar);
        form.add(Box.createVerticalStrut(16));
        form.add(aviso);

        return form;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        // Filtro
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filtroPanel.setOpaque(false);

        JLabel lblFiltro = new JLabel("Filtrar por:");
        lblFiltro.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblFiltro.setForeground(MainWindow.AZUL_OSCURO);

        cmbFiltro = new JComboBox<>();
        cmbFiltro.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cmbFiltro.setPreferredSize(new Dimension(220, 28));
        cmbFiltro.addItem("-- Todos --");
        for (Course c : courseManager.mostrarCourses()) {
            cmbFiltro.addItem("Curso: " + c.getCodigo() + " - " + c.getNombre());
        }
        for (Student s : studentManager.mostrarStudents()) {
            cmbFiltro.addItem("Estudiante: " + s.getCarnet() + " - " + s.getNombre());
        }

        JButton btnFiltrar = MainWindow.crearBoton("Filtrar");
        btnFiltrar.addActionListener(e -> filtrarTabla());

        filtroPanel.add(lblFiltro);
        filtroPanel.add(cmbFiltro);
        filtroPanel.add(btnFiltrar);

        // Tabla
        String[] cols = {"Carnet", "Estudiante", "Código Curso", "Curso"};
        modeloInscritos = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaInscritos = new JTable(modeloInscritos);
        StudentWindow.estilizarTabla(tablaInscritos);

        JScrollPane scroll = new JScrollPane(tablaInscritos);
        scroll.setBorder(BorderFactory.createLineBorder(MainWindow.AZUL_CLARO));

        panel.add(filtroPanel, BorderLayout.NORTH);
        panel.add(scroll,      BorderLayout.CENTER);

        JLabel info = new JLabel("Selecciona una fila y presiona 'Desinscribir' para eliminar.");
        info.setFont(new Font("SansSerif", Font.ITALIC, 11));
        info.setForeground(new Color(120, 140, 180));
        panel.add(info, BorderLayout.SOUTH);

        return panel;
    }

    // Lógica
    private void inscribir() {
        if (cmbEstudiante.getItemCount() == 0 || cmbCurso.getItemCount() == 0) {
            error("No hay estudiantes o cursos disponibles."); return;
        }

        String itemEst  = (String) cmbEstudiante.getSelectedItem();
        String itemCurs = (String) cmbCurso.getSelectedItem();

        if (itemEst == null || itemCurs == null) return;

        String carnet = itemEst.split(" - ")[0].trim();
        String cod    = itemCurs.split(" - ")[0].trim();

        if (registrationManager.estaInscrito(carnet, cod)) {
            error("El estudiante ya está inscrito en ese curso."); return;
        }

        Student student = studentManager.buscarStudent(carnet);
        Course  course  = courseManager.buscarCourse(cod);

        if (student == null || course == null) {
            error("Estudiante o curso no encontrado."); return;
        }

        // Validar cupo del curso
        if (course.getCupo() > 0) {
            int inscritos = registrationManager.buscarPorCurso(cod).size();
            if (inscritos >= course.getCupo()) {
                error("El curso " + course.getNombre() + " ya alcanzó su cupo máximo de "
                    + course.getCupo() + " estudiantes.");
                return;
            }
        }

        registrationManager.agregarRegistration(new Registration(student, course));
        actualizarTabla();
        exito("Inscripción realizada correctamente.");
    }

    private void desinscribir() {
        int fila = tablaInscritos.getSelectedRow();
        if (fila < 0) { error("Selecciona una inscripción de la tabla."); return; }

        String carnet = (String) modeloInscritos.getValueAt(fila, 0);
        String cod    = (String) modeloInscritos.getValueAt(fila, 2);

        for (Registration r : registrationManager.mostrarRegistrations()) {
            if (r.getStudent().getCarnet().equals(carnet) &&
                r.getCourse().getCodigo().equals(cod)) {

                int c = JOptionPane.showConfirmDialog(this,
                    "¿Desinscribir a " + r.getStudent().getNombre() +
                    " del curso " + r.getCourse().getNombre() + "?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);

                if (c == JOptionPane.YES_OPTION) {
                    registrationManager.eliminarRegistration(r);
                    actualizarTabla();
                    exito("Inscripción eliminada.");
                }
                return;
            }
        }
    }

    private void filtrarTabla() {
        String sel = (String) cmbFiltro.getSelectedItem();
        if (sel == null || sel.equals("-- Todos --")) {
            actualizarTabla(); return;
        }

        modeloInscritos.setRowCount(0);

        if (sel.startsWith("Curso: ")) {
            String cod = sel.replace("Curso: ", "").split(" - ")[0].trim();
            for (Registration r : registrationManager.buscarPorCurso(cod)) {
                agregarFila(r);
            }
        } else if (sel.startsWith("Estudiante: ")) {
            String carnet = sel.replace("Estudiante: ", "").split(" - ")[0].trim();
            for (Registration r : registrationManager.buscarPorEstudiante(carnet)) {
                agregarFila(r);
            }
        }
    }

    private void actualizarTabla() {
        modeloInscritos.setRowCount(0);
        for (Registration r : registrationManager.mostrarRegistrations()) {
            agregarFila(r);
        }
    }

    private void agregarFila(Registration r) {
        modeloInscritos.addRow(new Object[]{
            r.getStudent().getCarnet(),
            r.getStudent().getNombre() + " " + r.getStudent().getApellido(),
            r.getCourse().getCodigo(),
            r.getCourse().getNombre()
        });
    }

    // Helpers
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

    private void error(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }
    private void exito(String msg) { JOptionPane.showMessageDialog(this, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE); }
}