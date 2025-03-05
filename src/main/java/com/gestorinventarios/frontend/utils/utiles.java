package com.gestorinventarios.frontend.utils;

import javax.swing.*;
import java.awt.*;

public class utiles {


    public static void mostrarMensaje(String titulo, String mensaje, Component ventana, int tipoVentana) {
        JOptionPane.showMessageDialog(ventana, titulo, mensaje, tipoVentana);
    }

}
