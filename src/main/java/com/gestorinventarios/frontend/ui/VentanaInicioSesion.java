package com.gestorinventarios.frontend.ui;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.BotonConEstilo;
import com.gestorinventarios.frontend.utils.Utiles;

import javax.swing.*;
import java.util.Arrays;

public class VentanaInicioSesion extends BaseView {
    BotonConEstilo botonLogin;
    BotonConEstilo botonRegistrar;
    JTextField campoEmail;
    JPasswordField campoContrasena;
    JLabel mensajeError;

    public VentanaInicioSesion() {
        super("Iniciar Sesión", 500, 300);

        crearTitulo("Inicio de sesión");
        campoEmail = (JTextField) crearCampoTexto("Email", false, 1);
        campoContrasena = (JPasswordField) crearCampoTexto("Contraseña", true, 2);
        botonLogin = new BotonConEstilo("Iniciar sesión", this::comprobarInicioSesion);
        botonRegistrar = new BotonConEstilo("Registrar", VentanaRegistro::new);
        mensajeError = crearMensajeError(3);
        crearPanelBotones(4, botonLogin, botonRegistrar);

        getRootPane().setDefaultButton(botonLogin);
        añadirMenu();
        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void comprobarInicioSesion() {
        String email = campoEmail.getText().trim();
        char[] contrasenaChars = campoContrasena.getPassword();
        String contrasena = new String(contrasenaChars);

        Arrays.fill(contrasenaChars, '\0');

        if (!Utiles.validarEmail(email)) {
            mensajeError.setText("Email inválido.");
            return;
        }

        if (usuarioController.obtenerUsuarioPorEmail(email) == null) {
            mensajeError.setText("El email no está registrado.");
            return;
        }

        if (!usuarioController.autenticarUsuario(email, contrasena)) {
            mensajeError.setText("La contraseña es incorrecta.");
            return;
        }

        dispose();
        new VentanaPrincipal();
    }

    private void añadirMenu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menu.add(añadirMenuAyuda());
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }
}
