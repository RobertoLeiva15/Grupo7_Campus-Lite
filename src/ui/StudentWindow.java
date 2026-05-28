package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

import domain.Student;
import service.StudentManager;
import persistence.StudentFile;

public class StudentWindow extends JFrame {

    private StudentManager studentManager;
    private StudentFile    studentFile;

    // Campos del formulario
    private JTextField txtCarnet;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtCorreo;
    private JTextField txtBuscar;

    // Tabla
    private JTable            tabla;
    private DefaultTableModel modeloTabla;

    public StudentWindow(StudentManager studentManager, StudentFile studentFile) {
        this.studentManager = studentManager;
        this.studentFile    = studentFile;
        construirUI();
        actualizarTabla();
    }

    private void construirUI() {
        setTitle("Campus Lite — Gestión de Estudiantes");
        setSize(820, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(MainWindow.GRIS_SUAVE);
        setContentPane(root);

        root.add(crearHeader(), BorderLayout.NORTH);
        root.add(crearCuerpo(), BorderLayout.CENTER);
    }

    // Header
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(
                    0, 0, MainWindow.AZUL_OSCURO,
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

        JLabel lbl = new JLabel("👤  Gestión de Estudiantes");
        lbl.setFont(new Font("Serif", Font.BOLD, 22));
        lbl.setForeground(MainWindow.BLANCO);

        header.add(lbl, BorderLayout.WEST);
        return header;
    }

    // Cuerpo dividido en formulario + tabla
    private JPanel crearCuerpo() {
        JPanel cuerpo = new JPanel(new BorderLayout(15, 0));
        cuerpo.setBackground(MainWindow.GRIS_SUAVE);
        cuerpo.setBorder(new EmptyBorder(18, 20, 18, 20));

        cuerpo.add(crearFormulario(), BorderLayout.WEST);
        cuerpo.add(crearPanelTabla(), BorderLayout.CENTER);

        return cuerpo;
    }

    // Formulario
    private JPanel crearFormulario() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainWindow.BLANCO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(MainWindow.AZUL_CLARO);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setPreferredSize(new Dimension(240, 0));

        JLabel titulo = new JLabel("Datos del Estudiante");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        titulo.setForeground(MainWindow.AZUL_OSCURO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(MainWindow.DORADO);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));

        txtCarnet   = crearCampo("Carnet");
        txtNombre   = crearCampo("Nombre");
        txtApellido = crearCampo("Apellido");
        txtCorreo   = crearCampo("Correo electrónico");

        JButton btnAgregar  = MainWindow.crearBoton("Agregar");
        JButton btnEliminar = MainWindow.crearBotonDorado("Eliminar");
        JButton btnLimpiar  = crearBotonSecundario("Limpiar");

        btnAgregar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEliminar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLimpiar.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnAgregar.addActionListener(e  -> agregarEstudiante());
        btnEliminar.addActionListener(e -> eliminarEstudiante());
        btnLimpiar.addActionListener(e  -> limpiarCampos());

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(6));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(14));
        panel.add(etiqueta("Carnet *"));
        panel.add(txtCarnet);
        panel.add(Box.createVerticalStrut(8));
        panel.add(etiqueta("Nombre *"));
        panel.add(txtNombre);
        panel.add(Box.createVerticalStrut(8));
        panel.add(etiqueta("Apellido *"));
        panel.add(txtApellido);
        panel.add(Box.createVerticalStrut(8));
        panel.add(etiqueta("Correo *"));
        panel.add(txtCorreo);
        panel.add(Box.createVerticalStrut(18));
        panel.add(btnAgregar);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnEliminar);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnLimpiar);

        return panel;
    }

    // Panel de tabla con búsqueda
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        // Barra de búsqueda
        JPanel barraBusqueda = new JPanel(new BorderLayout(8, 0));
        barraBusqueda.setOpaque(false);

        txtBuscar = crearCampo("Buscar por carnet o nombre...");
        txtBuscar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JButton btnBuscar = MainWindow.crearBoton("Buscar");
        JButton btnMostrarTodos = crearBotonSecundario("Ver todos");

        btnBuscar.addActionListener(e     -> buscarEstudiante());
        btnMostrarTodos.addActionListener(e -> actualizarTabla());

        barraBusqueda.add(txtBuscar,      BorderLayout.CENTER);
        barraBusqueda.add(btnBuscar,      BorderLayout.EAST);

        JPanel barraBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        barraBotones.setOpaque(false);
        barraBotones.add(btnMostrarTodos);

        JPanel topBar = new JPanel(new BorderLayout(0, 5));
        topBar.setOpaque(false);
        topBar.add(barraBusqueda, BorderLayout.CENTER);
        topBar.add(barraBotones,  BorderLayout.SOUTH);

        // Tabla
        String[] columnas = {"Carnet", "Nombre", "Apellido", "Correo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modeloTabla);
        estilizarTabla(tabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(MainWindow.AZUL_CLARO, 1));
        scroll.getViewport().setBackground(MainWindow.BLANCO);

        // Selección en tabla carga campos
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int fila = tabla.getSelectedRow();
                txtCarnet.setText((String) modeloTabla.getValueAt(fila, 0));
                txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
                txtApellido.setText((String) modeloTabla.getValueAt(fila, 2));
                txtCorreo.setText((String) modeloTabla.getValueAt(fila, 3));
            }
        });

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // Lógica de acciones
    private void agregarEstudiante() {
        try {
            String carnet   = txtCarnet.getText().trim();
            String nombre   = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String correo   = txtCorreo.getText().trim();

            if (carnet.isEmpty() || nombre.isEmpty() ||
                apellido.isEmpty() || correo.isEmpty()) {
                mostrarError("Todos los campos son obligatorios.");
                return;
            }

            if (studentManager.buscarStudent(carnet) != null) {
                mostrarError("Ya existe un estudiante con ese carnet.");
                return;
            }

            Student s = new Student(carnet, nombre, apellido, correo);
            studentManager.agregarStudent(s);
            studentFile.guardarStudents(studentManager.mostrarStudents());
            actualizarTabla();
            limpiarCampos();
            mostrarExito("Estudiante registrado correctamente.");

        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void eliminarEstudiante() {
        String carnet = txtCarnet.getText().trim();
        if (carnet.isEmpty()) {
            mostrarError("Selecciona o escribe el carnet del estudiante a eliminar.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Eliminar al estudiante con carnet: " + carnet + "?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (studentManager.eliminarStudent(carnet)) {
                studentFile.guardarStudents(studentManager.mostrarStudents());
                actualizarTabla();
                limpiarCampos();
                mostrarExito("Estudiante eliminado.");
            } else {
                mostrarError("No se encontró el estudiante con ese carnet.");
            }
        }
    }

    private void buscarEstudiante() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        modeloTabla.setRowCount(0);
        for (Student s : studentManager.mostrarStudents()) {
            if (s.getCarnet().toLowerCase().contains(texto) ||
                s.getNombre().toLowerCase().contains(texto) ||
                s.getApellido().toLowerCase().contains(texto)) {
                modeloTabla.addRow(new Object[]{
                    s.getCarnet(), s.getNombre(), s.getApellido(), s.getCorreo()
                });
            }
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Student s : studentManager.mostrarStudents()) {
            modeloTabla.addRow(new Object[]{
                s.getCarnet(), s.getNombre(), s.getApellido(), s.getCorreo()
            });
        }
    }

    private void limpiarCampos() {
        txtCarnet.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtCorreo.setText("");
        tabla.clearSelection();
    }

    // Utilidades de UI
    private JTextField crearCampo(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setForeground(MainWindow.TEXTO_OSCURO);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 230), 1),
            new EmptyBorder(5, 8, 5, 8)));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        return tf;
    }

    private JLabel etiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(new Color(60, 80, 120));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setForeground(MainWindow.AZUL_MEDIO);
        btn.setBackground(MainWindow.BLANCO);
        btn.setBorder(BorderFactory.createLineBorder(MainWindow.AZUL_CLARO, 1));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 30));
        return btn;
    }

    public static void estilizarTabla(JTable tabla) {
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setRowHeight(28);
        tabla.setGridColor(new Color(210, 220, 240));
        tabla.setSelectionBackground(new Color(60, 120, 210, 60));
        tabla.setSelectionForeground(MainWindow.TEXTO_OSCURO);
        tabla.setShowVerticalLines(false);

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(MainWindow.AZUL_OSCURO);
        header.setForeground(MainWindow.BLANCO);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBorder(BorderFactory.createEmptyBorder());

        // Filas alternadas
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(
                    t, v, sel, foc, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? MainWindow.BLANCO
                                                 : new Color(240, 245, 255));
                }
                ((JLabel) c).setBorder(new EmptyBorder(0, 8, 0, 8));
                return c;
            }
        });
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
    }
}