package com.gestorinventarios.frontend.components;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.gestorinventarios.MainApp;
import com.gestorinventarios.frontend.controller.*;
import com.gestorinventarios.frontend.ui.VentanaInicioSesion;
import com.gestorinventarios.frontend.ui.menus.VentanaVentas;
import org.springframework.context.ApplicationContext;

import java.awt.*;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public abstract class BaseView extends JFrame {
    protected JPanel mainPanel;
    protected GridBagConstraints gbc;
    protected final UsuarioController usuarioController;
    protected final ProductoController productoController;
    protected final VentaController ventaController;
    protected final DetallesVentaController detallesVentaController;
    private HelpBroker hb;
    protected static final Map<Class<? extends BaseView>, BaseView> ventanasAbiertas = new HashMap<>();
    private static boolean temaOscuro = cargarPreferenciaTema();

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

        //Añadir javaHelp
        agregarJavaHelp();

        // Registrar la ventana en el mapa
        ventanasAbiertas.put(this.getClass(), this);
        setVisible(true);
    }

    private void configurarUI() {
        try {
            UIManager.setLookAndFeel(temaOscuro ? new FlatDarkLaf() : new FlatLightLaf());
            UIManager.put("Component.arc", 10);
            UIManager.put("Button.arc", 15);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("ScrollBar.width", 12);
            SwingUtilities.updateComponentTreeUI(this);
            this.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void agregarJavaHelp() {
        try {
            URL hsURL = getClass().getClassLoader().getResource("help/help_set.hs");

            if (hsURL != null) {
                HelpSet helpset = new HelpSet(null, hsURL);
                hb = helpset.createHelpBroker();

                hb.enableHelpKey(this.getContentPane(), "principal", helpset);
            } else {
                System.err.println("Error: No se encontró el archivo help_set.hs");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void aplicarTema(boolean oscuro) {
        if (temaOscuro == oscuro) return;

        temaOscuro = oscuro;
        guardarPreferenciaTema(oscuro);

        try {
            UIManager.setLookAndFeel(oscuro ? new FlatDarkLaf() : new FlatLightLaf());

            // Aplicar el tema a todas las ventanas abiertas
            for (BaseView ventana : ventanasAbiertas.values()) {
                SwingUtilities.updateComponentTreeUI(ventana);
                ventana.repaint();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static boolean cargarPreferenciaTema() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("config.properties")) {
            props.load(in);
            return Boolean.parseBoolean(props.getProperty("temaOscuro", "false"));
        } catch (IOException e) {
            return false; // Si no se encuentra el archivo, usar el tema claro por defecto
        }
    }

    private static void guardarPreferenciaTema(boolean oscuro) {
        Properties props = new Properties();
        props.setProperty("temaOscuro", String.valueOf(oscuro));
        try (FileOutputStream out = new FileOutputStream("config.properties")) {
            props.store(out, "Configuraciones de la aplicación");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void configLayoutPrincipal() {
        mainPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        setContentPane(mainPanel);
    }

    public void reiniciar() {
        List<BaseView> ventanas = new ArrayList<>(ventanasAbiertas.values());

        for (BaseView ventana : ventanas) {
            ventana.dispose();
        }
        ventanasAbiertas.clear();
        new VentanaInicioSesion();
    }


    @Override
    public void dispose() {
        super.dispose();
        ventanasAbiertas.remove(this.getClass());
    }

    // ---------------- COMPONENTES DE UI ---------------- //
    protected void crearTitulo(String texto) {
        JLabel tituloLabel = new JLabel(texto, SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));

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

    protected javax.swing.JComponent crearCampoTexto(String texto, boolean esContrasena, int fila) {
        JLabel label = new JLabel(texto, SwingConstants.RIGHT);
        javax.swing.JComponent campoTexto = esContrasena ? new JPasswordField() : new JTextField();

        campoTexto.setPreferredSize(new Dimension(200, 25));
        label.setPreferredSize(new Dimension(100, 30));

        return aplicarEstiloTexto(fila, label, campoTexto);
    }

    private javax.swing.JComponent aplicarEstiloTexto(int fila, JLabel label, javax.swing.JComponent campoTexto) {
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

    protected void crearPanelBotones(int fila, BotonConEstilo... botones) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        for (BotonConEstilo boton : botones) {
            panel.add(boton);
        }

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(panel, gbc);
    }

    protected void crearPanelComponentes(int fila, int columna, JComponent... componentes) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbcTabla = new GridBagConstraints();
        gbcTabla.fill = GridBagConstraints.BOTH;
        gbcTabla.gridy = 0;
        gbcTabla.weighty = 1.0;

        double weightX = 1.0 / componentes.length;

        for (int i = 0; i < componentes.length; i++) {
            gbcTabla.gridx = i;
            gbcTabla.weightx = weightX;
            JPanel container = new JPanel(new BorderLayout());
            container.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2, true));
            container.add(componentes[i], BorderLayout.CENTER);
            panel.add(container, gbcTabla);
        }

        gbc.gridx = columna;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0.9;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(panel, gbc);
    }

// -------------------------------

    protected JPanel crearPanelLateral(int fila, int columna, String[] titulos, String[] textos) {
        if (titulos.length != textos.length) {
            throw new IllegalArgumentException("Los arrays titulos y textos deben tener la misma longitud.");
        }

        JPanel sidebarPanel = new JPanel(new GridLayout(textos.length, 1, 10, 10));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for (int i = 0; i < textos.length; i++) {
            JPanel box = getJPanel(titulos, textos, i);
            sidebarPanel.add(box);
        }

        gbc.gridx = columna;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0.1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(sidebarPanel, gbc);
        return sidebarPanel;
    }

// -------------------------------

    private static JPanel getJPanel(String[] titulos, String[] textos, int i) {
        JPanel box = new JPanel(new BorderLayout());

        box.setBackground(new Color(80, 80, 80));
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120, 120, 120), 2, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel tituloLabel = new JLabel(titulos[i], SwingConstants.LEFT);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tituloLabel.setForeground(Color.WHITE);

        JLabel contenidoLabel = new JLabel(textos[i], SwingConstants.CENTER);
        contenidoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        contenidoLabel.setForeground(Color.WHITE);

        box.add(tituloLabel, BorderLayout.NORTH);
        box.add(contenidoLabel, BorderLayout.CENTER);
        return box;
    }

    protected JMenuItem añadirMenuAyuda() {
        JMenuItem menuAyuda = new JMenuItem("Ayuda");

        if (hb != null) {
            menuAyuda.addActionListener(e -> hb.setDisplayed(true));
        } else {
            System.err.println("Error: HelpBroker no está inicializado.");
        }

        return menuAyuda;
    }

}
