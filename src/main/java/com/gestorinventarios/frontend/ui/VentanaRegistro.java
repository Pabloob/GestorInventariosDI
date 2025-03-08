package com.gestorinventarios.frontend.ui;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.utils.Utiles; // Importar Utiles para validaciones

import javax.swing.*;
import java.util.Arrays;

public class VentanaRegistro extends BaseView {

    JButton botonRegistrar;
    JButton botonCancelar;
    JTextField campoNombre;
    JTextField campoEmail;
    JPasswordField campoContrasena;
    JLabel mensajeError;

    public VentanaRegistro() {
        super("Registro de Usuario", 450, 400);

        crearTitulo("Registro");
        campoNombre = (JTextField) crearCampoTexto("Nombre", false, 1);
        campoEmail = (JTextField) crearCampoTexto("Email", false, 2);
        campoContrasena = (JPasswordField) crearCampoTexto("Contraseña", true, 3);
        botonRegistrar = crearBoton("Registrar");
        botonCancelar = crearBoton("Cancelar");
        mensajeError = crearMensajeError(4);
        crearPanelBotones(5, botonRegistrar, botonCancelar);

        botonRegistrar.addActionListener(e -> registrar());
        botonCancelar.addActionListener(e -> dispose());
    }


    private void registrar() {
        String nombre = campoNombre.getText();
        String email = campoEmail.getText();
        char[] contrasenaChars = campoContrasena.getPassword();
        String contrasena = new String(contrasenaChars);

        Arrays.fill(contrasenaChars, '\0'); // Borrar la contraseña de memoria

        // Validar que el email tenga @ y un dominio válido
        if (!Utiles.validarEmail(email)) {
            mensajeError.setText("Email inválido. Debe contener '@' y un dominio válido.");
            return;
        }

        // Validar si el email ya está en uso
        if (usuarioController.obtenerUsuarioPorEmail(email) != null) {
            mensajeError.setText("El email ya está registrado.");
            return;
        }

        // Intentar registrar al usuario
        if (usuarioController.registrarUsuario(nombre, email, contrasena)) {
            abrirVentanaInicioSesion();
            dispose();
        } else {
            mensajeError.setText("No se ha podido registrar.");
        }
    }
}
