package com.gestorinventarios.frontend.ui;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.Tabla.TablaCustom;
import lombok.Getter;

import javax.swing.*;

public class VentanaPrincipal extends BaseView {
    @Getter
    TablaCustom tablaVentas;
    @Getter
    TablaCustom tablaProductos;
    String cantidadProductosStockBajo;
    String numVentas;
    String ingresos;

    public VentanaPrincipal() {
        super("Gestor de Inventarios", 1400, 700);

        // Crear el menú FlatLaf
        crearMenu();

        crearTitulo("Gestión de Inventarios");

        tablaVentas = new TablaCustom("Ventas", new String[]{"ID", "Producto", "Cantidad", "Precio", "Fecha"}, ventaController, detallesVentaController);
        tablaProductos = new TablaCustom("Productos", new String[]{"Nombre", "Stock", "Precio"}, productoController);

        crearPanelTablas(1, 0, tablaVentas, tablaProductos);
        actualizarPanelLateral();
        crearPanelLateral(1, 1, new String[]{"Bajo Stock", "Ventas hoy", "Ingresos"},
                new String[]{cantidadProductosStockBajo, numVentas, ingresos});

        actualizarTablas();
    }


    private void crearMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Menu");
        JMenuItem ventas = new JMenuItem("Ventas");
        JMenuItem productos = new JMenuItem("Productos");
        JMenuItem configuracion = new JMenuItem("Configuracion");
        JMenuItem exportar = new JMenuItem("Exportar");
        menu.add(ventas);
        menu.add(productos);
        menu.add(configuracion);
        menu.add(exportar);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        ventas.addActionListener(e -> {
            VentanaVentas ventana = abrirVentanaVentas();
            ventana.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    actualizarTablas();
                }
            });
        });

        productos.addActionListener(e -> {
            VentanaProductos ventana = abrirVentanaProductos();
            ventana.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    actualizarTablas();
                }
            });
        });


        configuracion.addActionListener(e -> abrirVentanaConfiguracion());
        exportar.addActionListener(e -> abrirVentanaExportar());
    }

    public void actualizarTablas() {
        tablaVentas.actualizarTabla();
        tablaProductos.actualizarTabla();
    }

    private void actualizarPanelLateral() {
        cantidadProductosStockBajo = String.valueOf(productoController.obtenerStockBajo());
        numVentas = String.valueOf(ventaController.obtenerNumVentas());
        ingresos = ventaController.obtenerTotalIngresos() + "€";
    }

}
