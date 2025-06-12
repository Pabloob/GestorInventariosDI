package com.gestorinventarios.frontend.ui;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.Tabla.SPTabla;
import com.gestorinventarios.frontend.ui.menus.VentanaConfiguracion;
import com.gestorinventarios.frontend.ui.menus.VentanaExportar;
import com.gestorinventarios.frontend.ui.menus.VentanaProductos;
import com.gestorinventarios.frontend.ui.menus.VentanaVentas;

import javax.swing.*;

public class VentanaPrincipal extends BaseView {
    SPTabla tablaVentas, tablaProductos;
    String cantidadProductosStockBajo, numVentas, ingresos;

    JPanel panelLateral;

    public VentanaPrincipal() {
        super("Gestor de Inventarios", 1400, 700);

        crearMenu();

        crearTitulo("Gestión de Inventarios");

        tablaVentas = new SPTabla("Ventas", new String[]{"Producto", "Cantidad", "Precio", "Fecha"}, false, ventaController, detallesVentaController);
        tablaProductos = new SPTabla("Productos", new String[]{"Nombre", "Stock", "Precio"}, productoController);

        crearPanelComponentes(1, 0, tablaVentas, tablaProductos);
        actualizarPanelLateral();
        panelLateral = crearPanelLateral(1, 1, new String[]{"Productos Bajo Stock", "Ventas", "Ingresos"},
                new String[]{cantidadProductosStockBajo, numVentas, ingresos});
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void crearMenu() {
        JMenu menu = new JMenu("Menu");
        JMenuItem ventas = new JMenuItem("Ventas");
        JMenuItem productos = new JMenuItem("Productos");
        JMenuItem configuracion = new JMenuItem("Configuracion");
        JMenuItem exportar = new JMenuItem("Exportar");
        menu.add(ventas);
        menu.add(productos);
        menu.add(configuracion);
        menu.add(exportar);
        menu.add(añadirMenuAyuda());
        menuBar.add(menu);

        ventas.addActionListener(e -> new VentanaVentas().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                tablaVentas.actualizarTabla();
                actualizarPanelLateral();
            }
        }));
        productos.addActionListener(e -> new VentanaProductos().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                tablaProductos.actualizarTabla();
                actualizarPanelLateral();
            }
        }));
        configuracion.addActionListener(e -> new VentanaConfiguracion());
        exportar.addActionListener(e -> new VentanaExportar());
    }

    private void actualizarPanelLateral() {
        cantidadProductosStockBajo = String.valueOf(productoController.obtenerStockBajo());
        numVentas = String.valueOf(ventaController.obtenerNumVentas());
        ingresos = ventaController.obtenerTotalIngresos() + "€";
        if (panelLateral == null) return;
        panelLateral.revalidate();
        panelLateral.repaint();
    }

}
