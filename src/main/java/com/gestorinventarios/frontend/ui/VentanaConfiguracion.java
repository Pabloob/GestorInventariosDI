package com.gestorinventarios.frontend.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.gestorinventarios.frontend.components.BaseView;

import javax.swing.*;
import java.awt.*;

public class VentanaConfiguracion extends BaseView {
    private static VentanaConfiguracion instance;

    private final JCheckBox controlTema;
    private final JButton guardar;

    public VentanaConfiguracion() {
        super("Configuración", 500, 400);

        crearTitulo("Configuración");

        controlTema = new JCheckBox("Activar tema oscuro");
        controlTema.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        controlTema.setFocusable(false);
        controlTema.setOpaque(true);

        // Cargar el estado del tema (Opcional: guardar preferencia en archivo o base de datos)
        boolean esTemaOscuro = UIManager.getLookAndFeel().getClass().getName().equals(FlatDarkLaf.class.getName());
        controlTema.setSelected(esTemaOscuro);

        guardar = crearBoton("Guardar");

        // Acción al cambiar el estado del checkbox
        controlTema.addActionListener(e -> aplicarTemaOscuro());

        // Agregar componentes a la UI
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);
        mainPanel.add(controlTema, gbc);

        crearPanelBotones(2, guardar);
    }

    private void aplicarTemaOscuro() {
        try {
            aplicarTema(controlTema.isSelected());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static VentanaConfiguracion getInstance() {
        if (instance == null) {
            instance = new VentanaConfiguracion();
        }
        return instance;
    }
}
