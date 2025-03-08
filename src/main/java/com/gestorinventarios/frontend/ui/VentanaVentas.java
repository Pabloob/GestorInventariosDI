package com.gestorinventarios.frontend.ui;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.Tabla.TablaCustom;

import javax.swing.*;

public class VentanaVentas extends BaseView {
    JButton botonBorrar;
    JButton botonActualizar;
    JButton botonAñadir;
    TablaCustom tabla;

    public VentanaVentas() {
        super("Ventana ventas", 1400, 700);
        tabla = new TablaCustom("Ventas", new String[]{"ID", "Producto", "Cantidad", "Precio", "Fecha"}, ventaController, detallesVentaController);
        botonAñadir = crearBoton("Añadir");
        botonActualizar = crearBoton("Actualizar");
        botonBorrar = crearBoton("Borrar");

        crearTitulo("Ventana ventas");
        crearPanelTablas(1, 0, tabla);


        crearPanelBotones(2, botonAñadir, botonActualizar, botonBorrar);

        botonAñadir.addActionListener(e -> abrirVentanaAñadirVenta(tabla));
        botonBorrar.addActionListener(e -> borrar());

    }

    private void borrar() {
        int row = tabla.getTable().getSelectedRow();
        long idVenta = (long) tabla.getTable().getValueAt(row, 0);
        ventaController.eliminarVenta(idVenta);
        tabla.actualizarTabla();
    }

}