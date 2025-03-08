package com.gestorinventarios.frontend.ui;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.utils.Utiles;

import javax.swing.*;
import java.util.Arrays;

public class VentanaInicioSesion extends BaseView {
    JButton botonLogin;
    JButton botonRegistrar;
    JTextField campoEmail;
    JPasswordField campoContrasena;
    JLabel mensajeError;

    public VentanaInicioSesion() {
        super("Iniciar Sesión", 450, 300);

        crearTitulo("Inicio de sesión");
        campoEmail = (JTextField) crearCampoTexto("Email", false, 1);
        campoContrasena = (JPasswordField) crearCampoTexto("Contraseña", true, 2);
        botonLogin = crearBoton("Iniciar sesión");
        botonRegistrar = crearBoton("Registrar");
        mensajeError = crearMensajeError(3);
        crearPanelBotones(4, botonLogin, botonRegistrar);

        botonLogin.addActionListener(e -> comprobarInicioSesion());
        botonRegistrar.addActionListener(e -> abrirVentanRegistro());
        getRootPane().setDefaultButton(botonLogin);
    }

    private void comprobarInicioSesion() {
        String email = campoEmail.getText().trim();
        char[] contrasenaChars = campoContrasena.getPassword();
        String contrasena = new String(contrasenaChars);

        Arrays.fill(contrasenaChars, '\0'); // Borra la contraseña de memoria

        // Validar formato de email
        if (!Utiles.validarEmail(email)) {
            mensajeError.setText("Email inválido.");
            return;
        }

        // Validar si el email existe en la base de datos
        if (usuarioController.obtenerUsuarioPorEmail(email) == null) {
            mensajeError.setText("El email no está registrado.");
            return;
        }

        // Autenticar usuario
        if (!usuarioController.autenticarUsuario(email, contrasena)) {
            mensajeError.setText("La contraseña es incorrecta.");
            return;
        }

        // Inicio de sesión exitoso
        dispose();
        abrirVentanaPrincipal();
    }

}
