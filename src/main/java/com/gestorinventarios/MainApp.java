package com.gestorinventarios;

import com.gestorinventarios.frontend.controller.ProductoController;
import com.gestorinventarios.frontend.controller.UsuarioController;
import com.gestorinventarios.frontend.controller.VentaController;
import com.gestorinventarios.frontend.ui.VentanaInicioSesion;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class MainApp {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");

        // Iniciar Spring Boot y obtener el contexto
        ApplicationContext context = SpringApplication.run(MainApp.class, args);

        // Obtener controladores desde el contexto de Spring
        UsuarioController usuarioController = context.getBean(UsuarioController.class);
        ProductoController productoController = context.getBean(ProductoController.class);
        VentaController ventaController = context.getBean(VentaController.class);

        // Ejecutar la aplicación Swing en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> new VentanaInicioSesion(usuarioController, productoController, ventaController));
    }
}
