package com.gestorinventarios.frontend.components;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.gestorinventarios.MainApp;
import com.gestorinventarios.frontend.components.Tabla.TablaCustom;
import com.gestorinventarios.frontend.controller.*;
import com.gestorinventarios.frontend.ui.*;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseView extends JFrame {
    protected JPanel mainPanel;
    protected GridBagConstraints gbc;
    protected final UsuarioController usuarioController;
    protected final ProductoController productoController;
    protected final VentaController ventaController;
    protected final DetallesVentaController detallesVentaController;

    protected static final Map<Class<? extends BaseView>, BaseView> ventanasAbiertas = new HashMap<>();
    private static boolean temaOscuro = false; // Estado del tema actual

    public BaseView(String titulo, int width, int height) {
        // Cerrar una ventana del mismo tipo si ya está abierta
        if (ventanasAbiertas.containsKey(this.getClass())) {
            ventanasAbiertas.get(this.getClass()).dispose();
        }

        // Obtener el contexto de Spring
        ApplicationContext context = MainApp.getApplicationContext();
        this.usuarioController = context.getBean(UsuarioController.class);
        this.productoController = context.getBean(ProductoController.class);
        this.ventaController = context.getBean(VentaController.class);
        this.detallesVentaController = context.getBean(DetallesVentaController.class);

        // Aplicar el tema actual antes de crear la ventana
        configurarUI();

        // Configuración de la ventana
        setTitle(titulo);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        configLayoutPrincipal();

        // Registrar la ventana en el mapa
        ventanasAbiertas.put(this.getClass(), this);
        setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        ventanasAbiertas.remove(this.getClass());
    }

    private void configurarUI() {
        try {
            if (temaOscuro) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            UIManager.put("Component.arc", 10);
            UIManager.put("Button.arc", 15);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("ScrollBar.width", 12);

            // Aplicar el LookAndFeel a la ventana actual
            SwingUtilities.updateComponentTreeUI(this);
            this.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void configLayoutPrincipal() {
        mainPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        setContentPane(mainPanel);
    }

    protected static void aplicarTema(boolean oscuro) {
        try {
            temaOscuro = oscuro; // Guardar el estado del tema

            if (oscuro) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }

            // Aplicar el tema a todas las ventanas abiertas
            for (BaseView ventana : ventanasAbiertas.values()) {
                SwingUtilities.updateComponentTreeUI(ventana);
                ventana.repaint();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    // ---------------- MÉTODOS DE NAVEGACIÓN ---------------- //
    protected void abrirVentanaInicioSesion() {
        new VentanaInicioSesion();
    }

    protected void abrirVentanRegistro() {
        new VentanaRegistro();
    }

    protected void abrirVentanaPrincipal() {
        new VentanaPrincipal();
    }

    protected VentanaVentas abrirVentanaVentas() {
        return new VentanaVentas();
    }

    protected VentanaProductos abrirVentanaProductos() {
        return new VentanaProductos();
    }

    protected void abrirVentanaConfiguracion() {
        new VentanaConfiguracion();
    }

    protected void abrirVentanaExportar() {
        new VentanaExportar();
    }

    protected void abrirVentanaAñadirProducto(TablaCustom tabla) {
        new VentanaAñadirProducto(tabla);
    }

    protected void abrirVentanaAñadirVenta(TablaCustom tabla) {
        new VentanaAñadirVenta(tabla);
    }

    // ---------------- COMPONENTES DE UI ---------------- //
    protected void crearTitulo(String texto) {
        JLabel tituloLabel = new JLabel(texto, SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        tituloLabel.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(tituloLabel, gbc);
    }

    protected JLabel crearMensajeError(int fila) {
        JLabel mensajeError = new JLabel("", SwingConstants.CENTER);
        mensajeError.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mensajeError.setForeground(Color.RED);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(mensajeError, gbc);
        return mensajeError;
    }

    protected JComponent crearCampoTexto(String texto, boolean esContrasena, int fila) {
        JLabel label = new JLabel(texto, SwingConstants.RIGHT);
        JComponent campoTexto = esContrasena ? new JPasswordField() : new JTextField();

        campoTexto.setPreferredSize(new Dimension(200, 25));
        label.setPreferredSize(new Dimension(100, 30));

        return aplicarEstiloTexto(fila, label, campoTexto);
    }

    private JComponent aplicarEstiloTexto(int fila, JLabel label, JComponent campoTexto) {
        label.setFont(new Font("Arial", Font.PLAIN, 14));

        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 0;
        gbcLabel.gridy = fila;
        gbcLabel.anchor = GridBagConstraints.EAST;
        gbcLabel.insets = new Insets(5, 5, 5, 10); // Espacio a la derecha del label
        mainPanel.add(label, gbcLabel);

        GridBagConstraints gbcCampoTexto = new GridBagConstraints();
        gbcCampoTexto.gridx = 1;
        gbcCampoTexto.gridy = fila;
        gbcCampoTexto.fill = GridBagConstraints.HORIZONTAL;
        gbcCampoTexto.insets = new Insets(5, 0, 5, 5); // Espacio general en los lados
        mainPanel.add(campoTexto, gbcCampoTexto);

        return campoTexto;
    }


    protected JComponent crearDesplegable(String texto, JComboBox<?> desplegable, int fila) {
        JLabel label = new JLabel(texto, SwingConstants.RIGHT);
        label.setPreferredSize(new Dimension(100, 30));
        desplegable.setPreferredSize(new Dimension(200, 25));

        return aplicarEstiloTexto(fila, label, desplegable);
    }

    protected void crearPanelBotones(int fila, JButton... botones) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        for (JButton boton : botones) {
            asignarEstiloBoton(boton);
            panel.add(boton);
        }

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(panel, gbc);
    }

    protected JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        asignarEstiloBoton(boton);
        return boton;
    }

    protected void asignarEstiloBoton(JButton boton) {
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setMargin(new Insets(5, 20, 5, 20));

        switch (boton.getText().toLowerCase()) {
            case "iniciar sesion":
            case "añadir":
                boton.setBackground(new Color(30, 144, 255));
                break;
            case "registrar":
                boton.setBackground(new Color(0x19FF00));
                break;
            case "borrar":
            case "cancelar":
                boton.setBackground(new Color(0xFF0000));
                break;
            default:
                boton.setBackground(new Color(0xFF9800));
                break;
        }
    }

    protected void crearPanelTablas(int fila, int columna, TablaCustom... tablas) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcTabla = new GridBagConstraints();
        gbcTabla.fill = GridBagConstraints.BOTH;
        gbcTabla.gridy = 0;
        gbcTabla.weighty = 1.0;

        double weightX = 1.0 / tablas.length;

        for (int i = 0; i < tablas.length; i++) {
            gbcTabla.gridx = i;
            gbcTabla.weightx = weightX;
            panel.add(tablas[i], gbcTabla);
        }

        gbc.gridx = columna;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0.9;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(panel, gbc);
    }


    protected void crearPanelLateral(int fila, int columna, String[] titulos, String[] textos) {
        if (titulos.length != textos.length) {
            throw new IllegalArgumentException("Los arrays titulos y textos deben tener la misma longitud.");
        }

        JPanel sidebarPanel = new JPanel(new GridLayout(textos.length, 1, 10, 10));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        for (int i = 0; i < textos.length; i++) {
            JPanel box = getJPanel(titulos, textos, i);

            sidebarPanel.add(box);
        }
        sidebarPanel.setPreferredSize(new Dimension(120, 0));
        gbc.gridx = columna;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0.1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(sidebarPanel, gbc);
    }

    private static JPanel getJPanel(String[] titulos, String[] textos, int i) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        box.setBackground(new Color(186, 186, 186));
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel tituloLabel = new JLabel(titulos[i], SwingConstants.LEFT);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tituloLabel.setForeground(Color.BLACK);

        JLabel contenidoLabel = new JLabel(textos[i], SwingConstants.CENTER);
        contenidoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        contenidoLabel.setForeground(Color.WHITE);

        box.add(tituloLabel, BorderLayout.NORTH);
        box.add(contenidoLabel, BorderLayout.CENTER);
        return box;
    }
}
