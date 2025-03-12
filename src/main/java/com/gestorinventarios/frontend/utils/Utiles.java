package com.gestorinventarios.frontend.utils;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class Utiles {

    // Validar un número entero positivo
    public static int validarInt(String txt) {
        if (txt == null || txt.trim().isEmpty()) {
            return -1;
        }

        try {
            int num = Integer.parseInt(txt.trim());
            return (num >= 0) ? num : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static int validarIntMayor0(String txt) {
        if (txt == null || txt.trim().isEmpty()) {
            return -1;
        }

        try {
            int num = Integer.parseInt(txt.trim());
            return (num > 0) ? num : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Validar un número decimal positivo
    public static double validarDouble(String txt) {
        if (txt == null || txt.trim().isEmpty()) {
            return -1;
        }

        try {
            double num = Double.parseDouble(txt.trim());
            return (num >= 0) ? num : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static double validarDoubleMayor0(String txt) {
        if (txt == null || txt.trim().isEmpty()) {
            return -1;
        }

        try {
            double num = Double.parseDouble(txt.trim());
            return (num > 0) ? num : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Validar que un correo electrónico tenga @ y termine en .com, .es, etc.
    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        return Pattern.matches(regex, email.trim());
    }

    // Validar una fecha en formato dd/MM/yyyy
    public static boolean validarFecha(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); // No permitir fechas incorrectas

        try {
            sdf.parse(fecha.trim());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Validar un precio (permite dos decimales)
    public static boolean validarPrecio(String precio) {
        if (precio == null || precio.trim().isEmpty()) {
            return false;
        }

        String regex = "^\\d+(\\.\\d{1,2})?$"; // Acepta números con hasta 2 decimales
        return Pattern.matches(regex, precio.trim());
    }

    // Validar un número de teléfono (9 dígitos en España, por ejemplo)
    public static boolean validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return false;
        }

        String regex = "^[0-9]{9}$";
        return Pattern.matches(regex, telefono.trim());
    }

    //Mostrar mensaje
    public static void mostrarMensajeError(Component ventana, String mensaje,String titulo) {
        JOptionPane.showMessageDialog(ventana, mensaje,titulo,JOptionPane.ERROR_MESSAGE);
    }
    public static void mostrarMensajeAdvertencia(Component ventana, String mensaje,String titulo) {
        JOptionPane.showMessageDialog(ventana, mensaje,titulo,JOptionPane.WARNING_MESSAGE);
    }

    public static int mostrarMensajeConfirmacion(Component ventana, String mensaje,String titulo) {
        return JOptionPane.showConfirmDialog(ventana, mensaje,titulo,JOptionPane.YES_NO_OPTION);
    }

    public static void mostrarMensajeInformacion(Component ventana, String mensaje,String titulo) {
        JOptionPane.showMessageDialog(ventana, mensaje,titulo,JOptionPane.INFORMATION_MESSAGE);
    }
}
