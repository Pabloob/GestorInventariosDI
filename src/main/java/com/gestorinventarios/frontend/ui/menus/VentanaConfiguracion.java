package com.gestorinventarios.frontend.ui.menus;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.BotonConEstilo;
import com.gestorinventarios.frontend.ui.VentanaInicioSesion;
import com.gestorinventarios.frontend.ui.VentanaPrincipal;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class VentanaConfiguracion extends BaseView {
    private final JCheckBox controlTema;
    private final BotonConEstilo guardar,cerrarSesion;

    public VentanaConfiguracion() {
        super("Configuración", 500, 400);

        crearTitulo("Configuración");

        controlTema = new JCheckBox("Activar tema oscuro");
        controlTema.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        controlTema.setFocusable(false);
        controlTema.setOpaque(true);

        controlTema.setSelected(cargarPreferenciaTema());

        guardar = new BotonConEstilo("Guardar", this::aplicarTemaOscuro);
        cerrarSesion = new BotonConEstilo("Cerrar sesion", this::reiniciar);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);
        mainPanel.add(controlTema, gbc);

        crearPanelBotones(2, guardar,cerrarSesion);
        crearMenu();
    }

    private void aplicarTemaOscuro() {
        boolean activarOscuro = controlTema.isSelected();
        aplicarTema(activarOscuro);
        guardarPreferenciaTema(activarOscuro);
    }

    private boolean cargarPreferenciaTema() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("config.properties")) {
            props.load(in);
            return Boolean.parseBoolean(props.getProperty("temaOscuro", "false"));
        } catch (IOException e) {
            return false; // Si no se encuentra el archivo, usar el tema claro por defecto
        }
    }

    private void guardarPreferenciaTema(boolean oscuro) {
        Properties props = new Properties();
        props.setProperty("temaOscuro", String.valueOf(oscuro));
        try (FileOutputStream out = new FileOutputStream("config.properties")) {
            props.store(out, "Configuraciones de la aplicación");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void crearMenu() {
        JMenu menu = new JMenu("Menu");
        JMenuItem ventanaPrincipal = new JMenuItem("Ventana principal");
        JMenuItem ventas = new JMenuItem("Ventas");
        JMenuItem productos = new JMenuItem("Productos");
        JMenuItem exportar = new JMenuItem("Exportar");
        menu.add(ventanaPrincipal);
        menu.add(ventas);
        menu.add(exportar);
        menu.add(productos);
        menu.add(añadirMenuAyuda());
        menuBar.add(menu);

        ventanaPrincipal.addActionListener(e -> new VentanaPrincipal());
        ventas.addActionListener(e -> new VentanaVentas());
        exportar.addActionListener(e -> new VentanaExportar());
        productos.addActionListener(e -> new VentanaProductos());
    }

}
