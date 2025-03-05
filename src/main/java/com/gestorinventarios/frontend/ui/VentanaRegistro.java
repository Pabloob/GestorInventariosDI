package com.gestorinventarios.frontend.ui;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.controller.ProductoController;
import com.gestorinventarios.frontend.controller.UsuarioController;
import com.gestorinventarios.frontend.controller.VentaController;

import javax.swing.*;
import java.util.Arrays;

public class VentanaRegistro extends BaseView {

    JButton botonRegistrar;
    JButton botonCancelar;
    JTextField campoNombre;
    JTextField campoEmail;
    JPasswordField campoContrasena;
    JLabel mensajeError;

    public VentanaRegistro(UsuarioController usuarioController,
                           ProductoController productoController,
                           VentaController ventaController) {
        super("Registro de Usuario", 450, 400,
                usuarioController, productoController, ventaController);

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

        Arrays.fill(contrasenaChars, '\0');

        if (usuarioController.obtenerUsuarioPorEmail(email) != null) {
            mensajeError.setText("El email ya existe");
        } else {
            if (usuarioController.registrarUsuario(nombre, email, contrasena)) {
                abrirVentanaInicioSesion();
            } else {
                mensajeError.setText("No se ha podido registrar");
            }
        }
    }
}