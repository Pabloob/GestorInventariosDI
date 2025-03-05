package com.gestorinventarios.frontend.ui;

import com.gestorinventarios.frontend.components.BaseView;

import com.gestorinventarios.frontend.controller.ProductoController;
import com.gestorinventarios.frontend.controller.UsuarioController;
import com.gestorinventarios.frontend.controller.VentaController;

import javax.swing.*;
import java.util.Arrays;

public class VentanaInicioSesion extends BaseView {

    JButton botonLogin;
    JButton botonRegistrar;
    JTextField campoEmail;
    JPasswordField campoContrasena;
    JLabel mensajeError;

    public VentanaInicioSesion(UsuarioController usuarioController,
                               ProductoController productoController,
                               VentaController ventaController) {
        super("Iniciar Sesión", 450, 300,
                usuarioController, productoController, ventaController);

        crearTitulo("Inicio de sesion");
        campoEmail = (JTextField) crearCampoTexto("Email", false, 1);
        campoContrasena = (JPasswordField) crearCampoTexto("Contraseña", true, 2);
        botonLogin = crearBoton("Iniciar sesion");
        botonRegistrar = crearBoton("Registrar");
        mensajeError = crearMensajeError(3);
        crearPanelBotones(4, botonLogin, botonRegistrar);

        botonLogin.addActionListener(e -> abrirVentanaPrincipal());
        botonRegistrar.addActionListener(e -> abrirVentanRegistro());
    }

    private void comprobarInicioSesion() {
        String email = campoEmail.getText().trim();
        char[] contrasenaChars = campoContrasena.getPassword();
        String contrasena = new String(contrasenaChars);

        Arrays.fill(contrasenaChars, '\0');

        if (usuarioController.obtenerUsuarioPorEmail(email) == null) {
            mensajeError.setText("El email no existe");
        } else if (!usuarioController.autenticarUsuario(email, contrasena)) {
            mensajeError.setText("La contraseña es incorrecta");
        } else {
            dispose();
            abrirVentanaPrincipal();
        }
    }
}