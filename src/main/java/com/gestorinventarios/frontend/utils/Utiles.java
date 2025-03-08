package com.gestorinventarios.frontend.utils;

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
            return (num > 0) ? num : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Validar que una cadena no esté vacía ni sea solo espacios
    public static boolean validarTexto(String txt) {
        return txt != null && !txt.trim().isEmpty();
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
}
