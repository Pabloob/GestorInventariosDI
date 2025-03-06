package com.gestorinventarios.frontend.ui;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.Tabla.TablaCustom;
import com.gestorinventarios.frontend.controller.ProductoController;
import com.gestorinventarios.frontend.controller.UsuarioController;
import com.gestorinventarios.frontend.controller.VentaController;

import javax.swing.*;


public class VentanaProductos extends BaseView {

    TablaCustom tablaProductos;
    JButton botonBorrar;
    JButton botonActualizar;
    JButton botonAñadir;

    public VentanaProductos(VentanaPrincipal ventanaPrincipal, UsuarioController usuarioController,
                         ProductoController productoController,
                         VentaController ventaController) {
        super("Ventana ventas", 1400, 700,
                usuarioController, productoController, ventaController);

        tablaProductos = ventanaPrincipal.getTablaProductos();
        botonAñadir = crearBoton("Añadir");
        botonActualizar = crearBoton("Actualizar");
        botonBorrar = crearBoton("Borrar");

        crearTitulo("Ventana productos");
        crearPanelTablas(1, 0, tablaProductos);


        crearPanelBotones(2, botonAñadir, botonActualizar, botonBorrar);

        botonAñadir.addActionListener(e -> abrirFormularioAnadir(tablaProductos,"Añadir producto",false));
        botonBorrar.addActionListener(e -> borrar());

    }

    private void borrar() {
        int row = tablaProductos.getTable().getSelectedRow();
        String nombre = (String) tablaProductos.getTable().getValueAt(row, 0);
        productoController.eliminarProducto(productoController.obtenerPorNombre(nombre).getId());
        productoController.actualizarProductos(tablaProductos);
    }
}