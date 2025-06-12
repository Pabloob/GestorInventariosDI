package com.gestorinventarios.frontend.ui.menus;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.model.Venta;
import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.BotonConEstilo;
import com.gestorinventarios.frontend.components.Tabla.SPTabla;
import com.gestorinventarios.frontend.ui.VentanaPrincipal;
import com.gestorinventarios.frontend.ui.menus.submenus.MenuVentanaVentas;

import javax.swing.*;
import java.util.List;

import static com.gestorinventarios.frontend.utils.Utiles.*;

public class VentanaVentas extends BaseView {
    private final SPTabla tablaVentas;
    private final BotonConEstilo botonBorrar, botonAnadir, botonActualizar;

    public VentanaVentas() {
        super("Gestion ventas", 1400, 700);
        tablaVentas = new SPTabla("Ventas", new String[]{"Producto", "Cantidad", "Precio", "Fecha"}, false, ventaController, detallesVentaController);

        botonAnadir = new BotonConEstilo("Añadir", () -> new MenuVentanaVentas().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                tablaVentas.actualizarTabla();
            }
        }));
        botonActualizar = new BotonConEstilo("Actualizar", this::abrirVentanaActualizarVenta);
        botonBorrar = new BotonConEstilo("Borrar", this::borrar);

        crearTitulo("Gestion ventas");
        crearPanelComponentes(1, 0, tablaVentas);
        crearPanelBotones(2, botonAnadir, botonActualizar, botonBorrar);
        crearMenu();
    }

    private void borrar() {
        int row = tablaVentas.getTable().getSelectedRow();
        if (row == -1) {
            mostrarMensajeAdvertencia(this, "Seleccione una venta para borrar.", "Selecciona una venta");
            return;
        }

        long idVenta = tablaVentas.getVentaId(row);

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea eliminar esta venta?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (!ventaController.eliminarVenta(idVenta)) {
                mostrarMensajeError(this, "No se pudo borrar la venta.", "No se pudo borrar");
                return;
            }
            mostrarMensajeInformacion(this, "Venta borrada correctamente.", "Venta borrada");
            tablaVentas.actualizarTabla();
        }
    }

    private void abrirVentanaActualizarVenta() {
        int row = tablaVentas.getTable().getSelectedRow();
        if (row == -1) {
            mostrarMensajeAdvertencia(this, "Seleccione una venta para actualizar.", "Advertencia");
            return;
        }

        // Obtener el ID de la venta seleccionada
        Long idVenta = tablaVentas.getVentaId(row);
        Venta venta = ventaController.obtenerVenta(idVenta);
        List<DetalleVenta> detalles = detallesVentaController.obtenerDetallesVenta(idVenta);

        if (venta == null) {
            mostrarMensajeError(this, "No se encontró la venta.", "Error");
            return;
        }

        new MenuVentanaVentas(venta, detalles).addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                tablaVentas.actualizarTabla();
            }
        });
    }

    private void crearMenu() {
        JMenu menu = new JMenu("Menu");
        JMenuItem ventanaPrincipal = new JMenuItem("Ventana principal");
        JMenuItem productos = new JMenuItem("Productos");
        JMenuItem configuracion = new JMenuItem("Configuracion");
        JMenuItem exportar = new JMenuItem("Exportar");
        menu.add(ventanaPrincipal);
        menu.add(productos);
        menu.add(configuracion);
        menu.add(exportar);
        menu.add(añadirMenuAyuda());
        menuBar.add(menu);

        ventanaPrincipal.addActionListener(e -> new VentanaPrincipal());
        productos.addActionListener(e -> new VentanaProductos());
        configuracion.addActionListener(e -> new VentanaConfiguracion());
        exportar.addActionListener(e -> new VentanaExportar());
    }
}