package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import service.StudentManager;
import service.CourseManager;
import service.EvaluationManager;
import service.RegistrationManager;
import persistence.StudentFile;
import persistence.CourseFile;

public class MainWindow extends JFrame {

    // Paleta de colores
    public static final Color AZUL_OSCURO   = new Color(10,  40,  90);
    public static final Color AZUL_MEDIO    = new Color(25,  75, 160);
    public static final Color AZUL_CLARO    = new Color(60, 120, 210);
    public static final Color DORADO        = new Color(212, 175,  55);
    public static final Color DORADO_CLARO  = new Color(240, 210, 100);
    public static final Color BLANCO        = new Color(255, 255, 255);
    public static final Color GRIS_SUAVE    = new Color(235, 240, 250);
    public static final Color TEXTO_OSCURO  = new Color(15,  30,  60);

    // Managers
    private StudentManager    studentManager;
    private CourseManager     courseManager;
    private EvaluationManager evaluationManager;
    private RegistrationManager registrationManager;

    // Archivos 
    private StudentFile studentFile;
    private CourseFile  courseFile;

    public MainWindow() {
        studentManager      = new StudentManager();
        courseManager       = new CourseManager();
        evaluationManager   = new EvaluationManager();
        registrationManager = new RegistrationManager();
        studentFile         = new StudentFile();
        courseFile          = new CourseFile();

        cargarDatos();
        construirUI();
    }

    // Cargar datos al iniciar
    private void cargarDatos() {
        try {
            for (domain.Student s : studentFile.cargarStudents()) {
                studentManager.agregarStudent(s);
            }
        } catch (Exception ignored) {}

        try {
            for (domain.Course c : courseFile.cargarCourses()) {
                courseManager.agregarCourse(c);
            }
        } catch (Exception ignored) {}
    }

    // Guardar datos al cerrar
    private void guardarDatos() {
        studentFile.guardarStudents(studentManager.mostrarStudents());
        courseFile.guardarCourses(courseManager.mostrarCourses());
    }

    // Construcción de la UI
    private void construirUI() {
        setTitle("Campus Lite — Sistema Académico");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        // Guardar al cerrar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                guardarDatos();
                dispose();
                System.exit(0);
            }
        });

        // Panel raíz
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(GRIS_SUAVE);
        setContentPane(root);

        root.add(crearHeader(),   BorderLayout.NORTH);
        root.add(crearCentro(),   BorderLayout.CENTER);
        root.add(crearFooter(),   BorderLayout.SOUTH);
    }

    // Header con logo y título
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                // Degradado azul oscuro → azul medio
                GradientPaint gp = new GradientPaint(
                    0, 0, AZUL_OSCURO, getWidth(), 0, AZUL_MEDIO);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Línea dorada inferior
                g2.setColor(DORADO);
                g2.setStroke(new BasicStroke(3f));
                g2.drawLine(0, getHeight() - 3, getWidth(), getHeight() - 3);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(900, 110));
        header.setBorder(new EmptyBorder(15, 30, 15, 30));

        // Icono circular decorativo
        JLabel icono = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(DORADO);
                g2.fillOval(0, 0, 70, 70);
                g2.setColor(AZUL_OSCURO);
                g2.setFont(new Font("Serif", Font.BOLD, 32));
                FontMetrics fm = g2.getFontMetrics();
                String letra = "C";
                int x = (70 - fm.stringWidth(letra)) / 2;
                int y = (70 - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(letra, x, y);
                g2.dispose();
            }
        };
        icono.setPreferredSize(new Dimension(70, 70));

        // Textos
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setBorder(new EmptyBorder(0, 18, 0, 0));

        JLabel titulo = new JLabel("Campus Lite");
        titulo.setFont(new Font("Serif", Font.BOLD, 32));
        titulo.setForeground(BLANCO);

        JLabel subtitulo = new JLabel("Sistema de Gestión Académica");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitulo.setForeground(DORADO_CLARO);

        textos.add(titulo);
        textos.add(subtitulo);

        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        izq.setOpaque(false);
        izq.add(icono);
        izq.add(textos);

        // Grupo 7 en la derecha
        JLabel grupo = new JLabel("Grupo 7");
        grupo.setFont(new Font("SansSerif", Font.BOLD, 13));
        grupo.setForeground(DORADO_CLARO);

        header.add(izq,    BorderLayout.WEST);
        header.add(grupo,  BorderLayout.EAST);

        return header;
    }

    // Centro con botones de módulos
    private JPanel crearCentro() {
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(GRIS_SUAVE);
        centro.setBorder(new EmptyBorder(30, 40, 20, 40));

        // Título bienvenida
        JLabel bienvenida = new JLabel("¿Qué deseas gestionar hoy?");
        bienvenida.setFont(new Font("Serif", Font.BOLD, 20));
        bienvenida.setForeground(AZUL_OSCURO);
        bienvenida.setBorder(new EmptyBorder(0, 0, 25, 0));

        // Grid de tarjetas
        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setOpaque(false);

        grid.add(crearTarjeta("👤", "Estudiantes",
                "Registrar, buscar y gestionar estudiantes",
                e -> abrirEstudiantes()));

        grid.add(crearTarjeta("📚", "Cursos",
                "Administrar cursos y sus evaluaciones",
                e -> abrirCursos()));

        grid.add(crearTarjeta("📋", "Inscripciones",
                "Inscribir estudiantes a cursos",
                e -> abrirInscripciones()));

        grid.add(crearTarjeta("📊", "Reportes",
                "Ver promedios y estado de aprobación",
                e -> abrirReportes()));

        centro.add(bienvenida, BorderLayout.NORTH);
        centro.add(grid,       BorderLayout.CENTER);

        return centro;
    }

    // Tarjeta de módulo
    private JPanel crearTarjeta(String emoji, String titulo,
                                String descripcion, ActionListener accion) {
        JPanel tarjeta = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BLANCO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                // Borde azul claro
                g2.setColor(AZUL_CLARO);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
                // Franja superior dorada
                g2.setColor(DORADO);
                g2.fillRoundRect(0, 0, getWidth(), 6, 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tarjeta.setBorder(new EmptyBorder(18, 20, 18, 20));

        // Emoji grande
        JLabel iconoLbl = new JLabel(emoji);
        iconoLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
        iconoLbl.setBorder(new EmptyBorder(8, 0, 8, 0));

        // Textos
        JLabel tituloLbl = new JLabel(titulo);
        tituloLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        tituloLbl.setForeground(AZUL_OSCURO);

        JLabel descLbl = new JLabel("<html>" + descripcion + "</html>");
        descLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descLbl.setForeground(new Color(80, 100, 130));
        descLbl.setBorder(new EmptyBorder(4, 0, 10, 0));

        // Botón
        JButton btn = crearBoton("Abrir módulo");
        btn.addActionListener(accion);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(tituloLbl);
        textos.add(descLbl);
        textos.add(btn);

        tarjeta.add(iconoLbl, BorderLayout.NORTH);
        tarjeta.add(textos,   BorderLayout.CENTER);

        // Efecto hover
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                tarjeta.setBorder(new EmptyBorder(16, 18, 16, 18));
                tarjeta.repaint();
            }
            @Override public void mouseExited(MouseEvent e) {
                tarjeta.setBorder(new EmptyBorder(18, 20, 18, 20));
                tarjeta.repaint();
            }
        });

        return tarjeta;
    }

    // Botón estilo Campus Lite
    public static JButton crearBoton(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(AZUL_OSCURO);
                } else if (getModel().isRollover()) {
                    g2.setColor(AZUL_CLARO);
                } else {
                    g2.setColor(AZUL_MEDIO);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(BLANCO);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 32));
        btn.setMaximumSize(new Dimension(160, 32));
        return btn;
    }

    // Botón dorado
    public static JButton crearBotonDorado(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(new Color(170, 135, 30));
                } else if (getModel().isRollover()) {
                    g2.setColor(DORADO_CLARO);
                } else {
                    g2.setColor(DORADO);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(AZUL_OSCURO);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 32));
        btn.setMaximumSize(new Dimension(160, 32));
        return btn;
    }

    // Footer
    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(AZUL_OSCURO);
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        footer.setOpaque(false);
        footer.setPreferredSize(new Dimension(900, 32));
        footer.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel info = new JLabel("Programación 1 — UMG Jutiapa  |  Grupo 7");
        info.setFont(new Font("SansSerif", Font.PLAIN, 11));
        info.setForeground(new Color(160, 180, 220));

        JLabel version = new JLabel("v1.0");
        version.setFont(new Font("SansSerif", Font.PLAIN, 11));
        version.setForeground(DORADO);

        footer.add(info,    BorderLayout.WEST);
        footer.add(version, BorderLayout.EAST);

        return footer;
    }

    // Abrir ventanas de módulos
    private void abrirEstudiantes() {
        new StudentWindow(studentManager, studentFile).setVisible(true);
    }

    private void abrirCursos() {
        new CourseWindow(courseManager, courseFile).setVisible(true);
    }

    private void abrirInscripciones() {
        new RegistrationWindow(studentManager, courseManager,
                               registrationManager).setVisible(true);
    }

    private void abrirReportes() {
        new ReportWindow(studentManager, courseManager,
                         registrationManager).setVisible(true);
    }
}