package com.gestorinventarios.frontend.ui.menus;

import com.gestorinventarios.backend.model.Producto;
import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.BotonConEstilo;
import com.gestorinventarios.frontend.components.Tabla.SPTabla;
import com.gestorinventarios.frontend.ui.VentanaPrincipal;
import com.gestorinventarios.frontend.ui.menus.submenus.VentanaActualizarProducto;
import com.gestorinventarios.frontend.ui.menus.submenus.VentanaAnadirProducto;

import javax.swing.*;

import static com.gestorinventarios.frontend.utils.Utiles.*;

public class VentanaProductos extends BaseView {
    private final SPTabla tablaProductos;
    private final BotonConEstilo botonAnadir, botonActualizar, botonBorrar;

    public VentanaProductos() {
        super("Gestión de Productos", 1400, 700);
        tablaProductos = new SPTabla("Productos", new String[]{"Nombre", "Stock", "Precio", "Activo"}, true, productoController);

        botonAnadir = new BotonConEstilo("Añadir", () -> new VentanaAnadirProducto().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                tablaProductos.actualizarTabla();
            }
        }));
        botonActualizar = new BotonConEstilo("Actualizar", this::abrirVentanaActualizarProducto);
        botonBorrar = new BotonConEstilo("Borrar", this::borrar);

        crearTitulo("Gestión de Productos");
        crearPanelComponentes(1, 0, tablaProductos);
        crearPanelBotones(2, botonAnadir, botonActualizar, botonBorrar);
        crearMenu();
    }

    private void borrar() {
        int row = tablaProductos.getTable().getSelectedRow();
        if (row == -1) {
            mostrarMensajeAdvertencia(this, "Seleccione un producto para borrar.", "Selecciona un producto");
            return;
        }

        String nombre = (String) tablaProductos.getTable().getValueAt(row, 0);
        Long idProducto = productoController.obtenerPorNombre(nombre) != null
                ? productoController.obtenerPorNombre(nombre).getId()
                : null;

        if (idProducto == null) {
            mostrarMensajeError(this, "No se encontró el producto.", "No existe");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea eliminar el producto \"" + nombre + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (!productoController.eliminarProducto(idProducto)) {
                mostrarMensajeError(this, "No se pudo borrar el producto.", "No se pudo borrar");
                return;
            }
            mostrarMensajeInformacion(this, "Producto borrado correctamente.", "Producto borrado");
            tablaProductos.actualizarTabla();
        }
    }


    private void abrirVentanaActualizarProducto() {
        int row = tablaProductos.getTable().getSelectedRow();
        if (row == -1) {
            mostrarMensajeAdvertencia(this, "Seleccione un producto para actualizar.", "Selecciona un producto");
            return;
        }

        // Obtener el nombre del producto seleccionado
        String nombreProducto = (String) tablaProductos.getTable().getValueAt(row, 0);
        Producto producto = productoController.obtenerPorNombre(nombreProducto);

        if (producto == null) {
            mostrarMensajeError(this, "No se encontró el producto.", "No existe");
            return;
        }
        new VentanaActualizarProducto(producto).addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                tablaProductos.actualizarTabla();
            }
        });
    }

    private void crearMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Menu");
        JMenuItem ventanaPrincipal = new JMenuItem("Ventana principal");
        JMenuItem ventas = new JMenuItem("Ventas");
        JMenuItem configuracion = new JMenuItem("Configuracion");
        JMenuItem exportar = new JMenuItem("Exportar");
        menu.add(ventanaPrincipal);
        menu.add(ventas);
        menu.add(configuracion);
        menu.add(exportar);
        menu.add(añadirMenuAyuda());
        menuBar.add(menu);
        setJMenuBar(menuBar);

        ventanaPrincipal.addActionListener(e -> new VentanaPrincipal());
        ventas.addActionListener(e -> new VentanaVentas());
        configuracion.addActionListener(e -> new VentanaConfiguracion());
        exportar.addActionListener(e -> new VentanaExportar());
    }

}
