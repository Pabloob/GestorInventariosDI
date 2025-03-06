package com.gestorinventarios.frontend.ui;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.Tabla.TablaCustom;
import com.gestorinventarios.frontend.controller.ProductoController;
import com.gestorinventarios.frontend.controller.UsuarioController;
import com.gestorinventarios.frontend.controller.VentaController;
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

    public VentanaPrincipal(UsuarioController usuarioController, ProductoController productoController, VentaController ventaController) {
        super("Gestor de Inventarios", 1400, 700,
                usuarioController, productoController, ventaController);

        // Crear el menú FlatLaf
        crearMenu();

        crearTitulo("Gestión de Inventarios");

        tablaVentas = new TablaCustom("Ventas", new String[]{"ID", "Producto", "Cantidad", "Precio", "Fecha"});
        tablaProductos = new TablaCustom("Productos", new String[]{"Nombre", "Stock", "Precio"});

        crearPanelTablas(1, 0, tablaVentas, tablaProductos);
        actualizarPanelLateral();
        crearPanelLateral(1, 1, new String[]{"Productos en bajo stock", "Numero de ventas", "Total de ingresos"},
                new String[]{cantidadProductosStockBajo, numVentas, ingresos});

        actualizarTablas();
    }

    private void crearMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Menu");
        JMenuItem ventas = new JMenuItem("Ventas");
        JMenuItem productos = new JMenuItem("Productos");
        JMenuItem configuracion = new JMenuItem("Configuracion");
        menu.add(ventas);
        menu.add(productos);
        menu.add(configuracion);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        ventas.addActionListener(e -> abrirVentanaVentas(this));
        productos.addActionListener(e -> abrirVentanaProductos(this));
        configuracion.addActionListener(e -> abrirVentanaConfiguracion(this));

    }

    private void actualizarTablas() {
        ventaController.actualizarVentas(tablaVentas);
        productoController.actualizarProductos(tablaProductos);
    }

    private void actualizarPanelLateral() {
        cantidadProductosStockBajo = String.valueOf(productoController.obtenerStockBajo());
        numVentas = String.valueOf(ventaController.obtenerNumVentas());
        ingresos = ventaController.obtenerTotalIngresos() + "€";
    }
}
