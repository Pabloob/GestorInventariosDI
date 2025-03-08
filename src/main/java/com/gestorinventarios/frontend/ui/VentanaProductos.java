package com.gestorinventarios.frontend.ui;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.Tabla.TablaCustom;

import javax.swing.*;


public class VentanaProductos extends BaseView {
    JButton botonBorrar;
    JButton botonActualizar;
    JButton botonAñadir;
    private final TablaCustom tabla;

    public VentanaProductos() {
        super("Ventana ventas", 1400, 700);
        tabla = new TablaCustom("Productos", new String[]{"Nombre", "Stock", "Precio"}, productoController);
        botonAñadir = crearBoton("Añadir");
        botonActualizar = crearBoton("Actualizar");
        botonBorrar = crearBoton("Borrar");

        crearTitulo("Ventana productos");
        crearPanelTablas(1, 0, tabla);


        crearPanelBotones(2, botonAñadir, botonActualizar, botonBorrar);

        botonAñadir.addActionListener(e -> abrirVentanaAñadirProducto(tabla));
        botonBorrar.addActionListener(e -> borrar());

    }

    private void borrar() {
        int row = tabla.getTable().getSelectedRow();
        String nombre = (String) tabla.getTable().getValueAt(row, 0);
        productoController.eliminarProducto(productoController.obtenerPorNombre(nombre).getId());
        tabla.actualizarTabla();
    }

}