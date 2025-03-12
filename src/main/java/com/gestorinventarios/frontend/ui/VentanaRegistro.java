package com.gestorinventarios.frontend.ui;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.BotonConEstilo;
import com.gestorinventarios.frontend.utils.Utiles;

import javax.swing.*;
import java.util.Arrays;

public class VentanaRegistro extends BaseView {

    BotonConEstilo botonRegistrar;
    BotonConEstilo botonCancelar;
    JTextField campoNombre;
    JTextField campoEmail;
    JPasswordField campoContrasena;
    JLabel mensajeError;

    public VentanaRegistro() {
        super("Registro de Usuario", 500, 400);

        crearTitulo("Registro");
        campoNombre = (JTextField) crearCampoTexto("Nombre", false, 1);
        campoEmail = (JTextField) crearCampoTexto("Email", false, 2);
        campoContrasena = (JPasswordField) crearCampoTexto("Contraseña", true, 3);
        botonRegistrar = new BotonConEstilo("Registrar", this::registrar);
        botonCancelar = new BotonConEstilo("Cancelar", this::dispose);
        mensajeError = crearMensajeError(4);
        crearPanelBotones(5, botonRegistrar, botonCancelar);
        añadirMenu();
        getRootPane().setDefaultButton(botonRegistrar);
    }

    private void registrar() {
        String nombre = campoNombre.getText();
        String email = campoEmail.getText();
        char[] contrasenaChars = campoContrasena.getPassword();
        String contrasena = new String(contrasenaChars);

        Arrays.fill(contrasenaChars, '\0');

        if (!Utiles.validarEmail(email)) {
            mensajeError.setText("Email inválido. Debe contener '@' y un dominio válido.");
            return;
        }

        if (usuarioController.obtenerUsuarioPorEmail(email) != null) {
            mensajeError.setText("El email ya está registrado.");
            return;
        }

        if (usuarioController.registrarUsuario(nombre, email, contrasena)) {
            new VentanaInicioSesion();
            dispose();
        } else {
            mensajeError.setText("No se ha podido registrar.");
        }
    }
    private void añadirMenu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menu.add(añadirMenuAyuda());
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }
}
