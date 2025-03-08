package com.gestorinventarios;

import com.gestorinventarios.frontend.ui.VentanaInicioSesion;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class MainApp {
    private static ApplicationContext context;

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");

        context = SpringApplication.run(MainApp.class, args);

        SwingUtilities.invokeLater(VentanaInicioSesion::new);
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }
}